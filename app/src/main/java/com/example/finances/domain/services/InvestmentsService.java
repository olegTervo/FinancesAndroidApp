package com.example.finances.domain.services;

import com.example.finances.domain.interfaces.IApiRepository;
import com.example.finances.domain.interfaces.IInvestmentRepository;
import com.example.finances.domain.interfaces.IPriceRepository;
import com.example.finances.domain.interfaces.IValueDateRepository;
import com.example.finances.domain.models.Api;
import com.example.finances.domain.models.Price;
import com.example.finances.frameworks_and_drivers.database.common.DatabaseHelper;
import com.example.finances.interface_adapters.api.ApiClient;
import com.example.finances.interface_adapters.api.ApiInterface;
import com.example.finances.interface_adapters.api.models.CoinListDto;
import com.example.finances.interface_adapters.api.models.CoinMarketCap.Datum;
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

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvestmentsService {

    private IInvestmentRepository investmentRepository;
    private IApiRepository apiRepository;
    private IPriceRepository priceRepository;
    private IValueDateRepository valueDateRepository;
    private DatabaseHelper db;

    @Inject
    public InvestmentsService(
            IInvestmentRepository investmentRepository,
            IApiRepository apiRepository,
            IPriceRepository priceRepository,
            IValueDateRepository valueDateRepository,
            DatabaseHelper db)
    {
        this.investmentRepository = investmentRepository;
        this.apiRepository = apiRepository;
        this.priceRepository = priceRepository;
        this.valueDateRepository = valueDateRepository;
        this.db = db;
    }

    public void sync(IApiCallback callback) {
        updateInvestments(callback);
    }

    public Investment getInvestment(long id) {
        Investment result = investmentRepository.GetInvestment(id);
        List<Price> history = priceRepository.GetPrices(result.getId(), PriceType.Investment, 0);
        result.setHistory(history);

        return result;
    }

    public Investment createInvestment(String name, float amount, float price) {
        Investment newInvestment = investmentRepository.CreateInvestment(name, amount);
        boolean passed = priceRepository.CreatePrice(newInvestment.getId(), PriceType.Investment, 0, price);

        return newInvestment;
    }

    public Investment updateInvestment(Investment updated) {
        return investmentRepository.UpdateInvestment(updated);
    }

    public boolean deleteInvestment(long id) {
        return investmentRepository.DeleteInvestment(id);
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
        HistoryPrice last = (HistoryPrice) valueDateRepository.getFirst(ValueDateType.Investments);

        if (last == null
                || last.date.getDayOfYear() < LocalDate.now().getDayOfYear()
                || last.date.getYear() < LocalDate.now().getYear()) {
            float toAdd = 0;

            for (Investment investment : list) {
                if (investment.getLastPrice() != null)
                    toAdd += investment.getLastPrice().GetPrice()*investment.getAmount();
            }

            valueDateRepository.create(toAdd, LocalDate.now(), ValueDateType.Investments);
        }
        else {
            float toAdd = 0;

            for (Investment investment : list) {
                if (investment.getLastPrice() != null)
                    toAdd += investment.getLastPrice().GetPrice()*investment.getAmount();
            }
            toAdd -= last.value;

            valueDateRepository.increaseTopValue(toAdd, ValueDateType.Investments);
        }
    }

    public List<Investment> getInvestments() {
        List<Investment> investments = new ArrayList<>();
        List<Investment> apiInvestments = new ArrayList<>();

        investments.addAll(investmentRepository.SearchInvestments(InvestmentType.Manual));
        apiInvestments.addAll(investmentRepository.SearchInvestments(InvestmentType.ApiLinked));

        for (Investment investment : apiInvestments) {
            if (investment instanceof ApiInvestment) {
                Api api = apiRepository.GetApi(((ApiInvestment) investment).getApiId());
                ApiInvestment toAdd = new ApiInvestment(investment, api, ((ApiInvestment) investment).getApiSpecificInvestmentName());
                investments.add(toAdd);
            }
        }

        for (Investment investment : investments) {
            investment.setHistory(priceRepository.GetPrices(investment.getId(), PriceType.Investment, 0));
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
            Api coiMarketApi = apiRepository.GetApi(ApiType.CoinMarketCap);
            ApiInterface currenciesApi = ApiClient.getClient(coiMarketApi.getLink()).create(ApiInterface.class);
            String key = coiMarketApi.getKey();
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
                                priceRepository.CreatePrice(inv.get(0).getId(), PriceType.Investment, 0, coin.getQuote().getEUR().getPrice().floatValue());
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
