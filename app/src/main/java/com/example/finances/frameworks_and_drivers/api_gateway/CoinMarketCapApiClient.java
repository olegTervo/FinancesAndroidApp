package com.example.finances.frameworks_and_drivers.api_gateway;

import com.example.finances.domain.enums.CurrencyType;
import com.example.finances.domain.models.Api;
import com.example.finances.domain.models.api.CoinListDto;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;

public class CoinMarketCapApiClient {
    private Api coinMarketCap;
    private ApiInterface currenciesApi;

    @Inject
    public CoinMarketCapApiClient(Api coinMarketCap, ApiInterface coinMarketCapInterface) {
        this.coinMarketCap = coinMarketCap;
        this.currenciesApi = coinMarketCapInterface;
        //currenciesApi = ApiClient.getClient("https://sandbox-api.coinmarketcap.com").create(ApiInterface.class);
    }

    public void getCoins(Callback<CoinListDto> syncCallback, CurrencyType currency) {
        Call<CoinListDto> call = getCoinsCall(getApiKey());
        call.enqueue(syncCallback);
    }

    private Call<CoinListDto> getCoinsCall(String apiKey) {
        Call<CoinListDto> call = currenciesApi.getCoins(apiKey, "1", "300", "EUR");
        return call;
    }

    private String getApiKey() {
        return coinMarketCap.getKey();
    }
}
