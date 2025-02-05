package com.example.finances.domain.services;

import com.example.finances.interface_adapters.api.ApiClient;
import com.example.finances.interface_adapters.api.ApiInterface;
import com.example.finances.interface_adapters.api.models.CoinListDto;
import com.example.finances.interface_adapters.api.models.CoinMarketCap.Datum;
import com.example.finances.frameworks_and_drivers.database.api.ApiHelper;
import com.example.finances.frameworks_and_drivers.database.common.DatabaseHelper;
import com.example.finances.frameworks_and_drivers.database.investment.InvestmentHelper;
import com.example.finances.frameworks_and_drivers.database.price.PriceHelper;
import com.example.finances.frameworks_and_drivers.database.value_date.ValueDateHelper;
import com.example.finances.frameworks_and_drivers.database.api.ApiDao;
import com.example.finances.interface_adapters.common.interfaces.IApiCallback;
import com.example.finances.domain.enums.ApiType;
import com.example.finances.domain.enums.InvestmentType;
import com.example.finances.domain.enums.PriceType;
import com.example.finances.domain.enums.ValueDateType;
import com.example.finances.domain.models.ApiInvestment;
import com.example.finances.domain.models.HistoryPrice;
import com.example.finances.domain.models.Investment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvestmentsService {
    private DatabaseHelper db;

    public InvestmentsService(DatabaseHelper db) {
        this.db = db;
    }

    public void sync(IApiCallback callback) {
        updateInvestments(callback);
    }

    public void updateInvestments(IApiCallback callback) {
        syncPricesAsync(new IApiCallback() {
            @Override
            public void onSuccess() {
                processInvestments();
                callback.onSuccess();
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public void processInvestments() {
        List<Investment> list = getInvestments();
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
    }

    public List<Investment> getInvestments() {
        List<Investment> investments = new ArrayList<>();
        investments.addAll(InvestmentHelper.SearchInvestments(db, InvestmentType.Manual));
        investments.addAll(InvestmentHelper.SearchInvestments(db, InvestmentType.ApiLinked));

        for (Investment investment : investments) {
            investment.setHistory(PriceHelper.GetPrices(db, investment.getId(), PriceType.Investment, 0));
        }

        return investments;
    }

    public void syncPricesAsync(IApiCallback syncCallback) {
        List<ApiInvestment> toSync = getInvestments()
                .stream()
                .filter(i -> i instanceof ApiInvestment
                        //&& (i.getLastPrice() == null
                        //    || i.getLastPrice().GetModified().getDayOfYear() < LocalDate.now().getDayOfYear()
                        //    || i.getLastPrice().GetModified().getYear() < LocalDate.now().getYear())
                )
                .map(i -> (ApiInvestment) i).collect(Collectors.toList());

        if(!toSync.isEmpty()) {
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
                        syncCallback.onFailure(e);
                    }

                    syncCallback.onSuccess();
                }

                @Override
                public void onFailure(Call<CoinListDto> call, Throwable t) {
                    call.cancel();
                    syncCallback.onFailure(t);
                }
            });
        }
    }
}
