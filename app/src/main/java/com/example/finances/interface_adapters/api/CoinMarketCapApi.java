package com.example.finances.interface_adapters.api;

import com.example.finances.domain.enums.ApiType;
import com.example.finances.domain.enums.CurrencyType;
import com.example.finances.domain.enums.PriceType;
import com.example.finances.domain.interfaces.api.IApiCallback;
import com.example.finances.domain.interfaces.api.ICoinMarketCapApi;
import com.example.finances.domain.models.ApiInvestment;
import com.example.finances.domain.models.Investment;
import com.example.finances.domain.models.Price;
import com.example.finances.domain.models.api.CoinListDto;
import com.example.finances.domain.models.api.CoinMarketCap.Datum;
import com.example.finances.frameworks_and_drivers.api_gateway.CoinMarketCapApiClient;
import com.example.finances.frameworks_and_drivers.database.price.PriceDao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinMarketCapApi implements ICoinMarketCapApi {
    private final CoinMarketCapApiClient api;
    private final PriceDao priceDao;

    @Inject
    public CoinMarketCapApi(CoinMarketCapApiClient api, PriceDao priceDao){
        this.api = api;
        this.priceDao = priceDao;
    }

    @Override
    public void syncPricesAsync(List<ApiInvestment> toSync, IApiCallback externalCallback) {

        if(!toSync.isEmpty()) {
            List<ApiInvestment> currencies = toSync.stream()
                    .filter(i -> i.getApi().getType().equals(ApiType.CoinMarketCap))
                    .collect(Collectors.toList());

            Callback<CoinListDto> callback = new Callback<CoinListDto>() {
                @Override
                public void onResponse(Call<CoinListDto> call, Response<CoinListDto> response) {
                    ArrayList<Investment> res = new ArrayList<>();

                    try {
                        CoinListDto resp = response.body();
                        List<Datum> coins = resp.getData().stream()
                                .filter(c -> currencies.stream().anyMatch(cur -> cur.getApiSpecificInvestmentName().equals(c.getName())))
                                .collect(Collectors.toList());

                        for (Datum coin : coins) {
                            List<ApiInvestment> inv = currencies.stream().filter(c -> c.getApiSpecificInvestmentName().equals(coin.getName())).collect(Collectors.toList());

                            if (inv.size() == 1){
                                ApiInvestment toUpdate = inv.get(0);
                                Price newPrice = priceDao.CreatePrice(toUpdate.getId(), PriceType.Investment, 0, coin.getQuote().getEUR().getPrice().floatValue());
                            }
                        }
                    } catch (Exception e) {
                        externalCallback.onFailure(e);
                    }

                    externalCallback.onSuccess();
                }

                @Override
                public void onFailure(Call<CoinListDto> call, Throwable t) {
                    call.cancel();
                    externalCallback.onFailure(t);
                }
            };

            api.getCoins(callback, CurrencyType.EUR);
        }
    }
}
