package com.example.finances.services;

import com.example.finances.Api.ApiClient;
import com.example.finances.Api.ApiInterface;
import com.example.finances.Api.models.CoinListDto;
import com.example.finances.Api.models.CoinMarketCap.Datum;
import com.example.finances.Database.helpers.ApiHelper;
import com.example.finances.Database.helpers.DatabaseHelper;
import com.example.finances.Database.helpers.InvestmentHelper;
import com.example.finances.Database.helpers.PriceHelper;
import com.example.finances.Database.helpers.ValueDateHelper;
import com.example.finances.Database.models.ApiDao;
import com.example.finances.common.services.EventService;
import com.example.finances.enums.ApiType;
import com.example.finances.enums.InvestmentType;
import com.example.finances.enums.PriceType;
import com.example.finances.enums.ValueDateType;
import com.example.finances.models.ApiInvestment;
import com.example.finances.models.HistoryPrice;
import com.example.finances.models.Investment;
import com.example.finances.models.Price;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvestmentsService {
    private DatabaseHelper db;
    private EventService eventService;

    public InvestmentsService(DatabaseHelper db, EventService eventService) {
        this.db = db;
        this.eventService = eventService;
    }

    public void sync() {
        List<Investment> list = getInvestments(true);
        HistoryPrice last = (HistoryPrice) ValueDateHelper.getFirst(db, ValueDateType.Investments);

        if (last == null
                || last.date.getDayOfYear() < LocalDate.now().getDayOfYear()
                || last.date.getYear() < LocalDate.now().getYear()) {
            float toAdd = 0;

            for (Investment investment : list) {
                if (investment.getLastPrice() != null)
                    toAdd += investment.getLastPrice().GetPrice()*investment.getAmount();
            }

            ValueDateHelper.create(db, toAdd, LocalDate.now(), ValueDateType.Investments);
        }
        else {
            float toAdd = 0;

            for (Investment investment : list) {
                if (investment.getLastPrice() != null)
                    toAdd += investment.getLastPrice().GetPrice()*investment.getAmount();
            }
            toAdd -= last.value;

            ValueDateHelper.increaseTopValue(db, toAdd, ValueDateType.Investments);
        }

        eventService.fireEvent();
    }

    public List<Investment> getInvestments(boolean sync) {
        if (sync) {
            syncPricesAsync();
        }

        List<Investment> investments = new ArrayList<>();
        investments.addAll(InvestmentHelper.SearchInvestments(db, InvestmentType.Manual));
        investments.addAll(InvestmentHelper.SearchInvestments(db, InvestmentType.ApiLinked));

        for (Investment investment : investments) {
            investment.setHistory(PriceHelper.GetPrices(db, investment.getId(), PriceType.Investment, 0));
        }

        return investments;
    }

    public List<HistoryPrice> getSumHistory() {
        syncPricesAsync();
        List<HistoryPrice> res = new ArrayList<>();
        List<Investment> investments = getInvestments(true);

        for (Investment investment : investments) {
            List<Price> prices = investment.getHistory().stream()
                    .filter(p -> p.GetModified().compareTo(LocalDate.now().minusDays(300)) == 1)
                    .collect(Collectors.toList());

            if (res.isEmpty())
                res = prices.stream()
                        .map(p -> new HistoryPrice(p.GetId(), p.GetPrice(), p.GetModified()))
                        .collect(Collectors.toList());

            else {
                for (int i = 0; i < prices.size(); i++) {
                    for (int j = 0 ; j < res.size(); j++) {
                        if (prices.get(i).GetModified().equals(res.get(j).getDate())) {
                            res.get(j).add(prices.get(i).GetPrice());
                            break;
                        }

                        if (j == res.size()-1) {
                            res.add(new HistoryPrice(prices.get(i).GetId(), prices.get(i).GetPrice(), prices.get(i).GetModified()));
                        }
                    }
                }
            }
        }

        return res;
    }

    public void syncPricesAsync() {
        List<ApiInvestment> toSync = getInvestments(false)
                .stream()
                .filter(i -> i instanceof ApiInvestment
                        //&& (i.getLastPrice() == null
                        //    || i.getLastPrice().GetModified().getDayOfYear() < LocalDate.now().getDayOfYear()
                        //    || i.getLastPrice().GetModified().getYear() < LocalDate.now().getYear())
                )
                .map(i -> (ApiInvestment) i).collect(Collectors.toList());

        if(!toSync.isEmpty()) {
            System.out.println("will sync");
            List<ApiInvestment> currencies = toSync.stream()
                    .filter(i -> i.getApi().getType().equals(ApiType.CoinMarketCap))
                    .collect(Collectors.toList());
            ApiDao coiMarketApi = ApiHelper.GetApi(db, ApiType.CoinMarketCap);
            ApiInterface currenciesApi = ApiClient.getClient(coiMarketApi.Link).create(ApiInterface.class);
            String key = coiMarketApi.Key;
            Call<CoinListDto> call = currenciesApi.getCoins(key, "1", "300", "EUR");

            call.enqueue(new Callback<CoinListDto>() {
                @Override
                public void onResponse(Call<CoinListDto> call, Response<CoinListDto> response) {
                    System.out.println("got resp");
                    try {
                        CoinListDto resp = response.body();
                        List<Datum> coins = resp.getData().stream()
                                .filter(c -> currencies.stream().anyMatch(cur -> cur.getApiSpecificInvestmentName().equals(c.getName())))
                                .collect(Collectors.toList());

                        for (Datum coin : coins) {
                            List<ApiInvestment> inv = currencies.stream().filter(c -> c.getApiSpecificInvestmentName().equals(coin.getName())).collect(Collectors.toList());
                            System.out.println("SIZE " + inv.size());
                            if (inv.size() == 1)
                                PriceHelper.CreatePrice(db, inv.get(0).getId(), PriceType.Investment, 0, coin.getQuote().getEUR().getPrice().floatValue());
                        }
                    }
                    catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<CoinListDto> call, Throwable t) {
                    call.cancel();
                }
            });

            while (!call.isExecuted())
                try {
                    System.out.println("sleeping...");
                    Thread.sleep(100);
                }
                catch (Exception e){
                }
        }
    }

    public void syncPricesSync() {
        List<ApiInvestment> toSync = getInvestments(false)
                .stream()
                .filter(i -> i instanceof ApiInvestment
                        && (i.getLastPrice() == null
                        || i.getLastPrice().GetModified().getDayOfYear() < LocalDate.now().getDayOfYear()
                        || i.getLastPrice().GetModified().getYear() < LocalDate.now().getYear()))
                .map(i -> (ApiInvestment) i).collect(Collectors.toList());

        if(!toSync.isEmpty()) {
            System.out.println("will sync");
            List<ApiInvestment> currencies = toSync.stream()
                    .filter(i -> i.getApi().getType().equals(ApiType.CoinMarketCap))
                    .collect(Collectors.toList());
            ApiDao coiMarketApi = ApiHelper.GetApi(db, ApiType.CoinMarketCap);
            ApiInterface currenciesApi = ApiClient.getClient(coiMarketApi.Link).create(ApiInterface.class);
            String key = coiMarketApi.Key;
            Call<CoinListDto> call = currenciesApi.getCoins(key, "1", "100", "EUR");

            try {
                // Execute the call synchronously
                Response<CoinListDto> response = call.execute();

                // Check if the response is successful
                if (response.isSuccessful()) {
                    System.out.println("got resp");
                    CoinListDto resp = response.body();
                    List<Datum> coins = resp.getData().stream()
                            .filter(c -> currencies.stream().anyMatch(cur -> cur.getApiSpecificInvestmentName().equals(c.getName())))
                            .collect(Collectors.toList());

                    for (Datum coin : coins) {
                        List<ApiInvestment> inv = currencies.stream().filter(c -> c.getApiSpecificInvestmentName().equals(coin.getName())).collect(Collectors.toList());

                        if (inv.size() == 1)
                            PriceHelper.CreatePrice(db, inv.get(0).getId(), PriceType.Investment, 0, coin.getQuote().getEUR().getPrice().floatValue());
                    }
                } else {
                    // Handle the error response
                    System.out.println("Error Response Code: " + response.code());
                }
            } catch (Exception e) {
                System.out.println(e.toString());
                e.printStackTrace();
            }
        }
    }

    private void awaitSync() {
        int timesToTry = 5;

        while (timesToTry-- > 0){
            System.out.println(timesToTry);
            boolean synced = false;

            for (Investment i : InvestmentHelper.SearchInvestments(db, InvestmentType.ApiLinked)){
                List<Price> prices = PriceHelper.GetPrices(db, i.getId(), PriceType.Investment, 0);

                if (prices.isEmpty() || prices.get(prices.size()-1).GetModified().getDayOfYear() < LocalDate.now().getDayOfYear()) {
                    synced = false;
                    break;
                }
                else {
                    synced = true;
                }
            }

            if (synced)
                break;
        }
    }
}
