package com.example.finances.Api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

import com.example.finances.Api.models.CoinListDto;

public interface ApiInterface {

    @Headers("X-CMC_PRO_API_KEY: e3c851c7-d823-4530-8ee0-184174e4b847")
    // test
    //@Headers("X-CMC_PRO_API_KEY: b54bcf4d-1bca-4e8e-9a24-22ff2c3d462c")
    @GET("/v1/cryptocurrency/listings/latest?")
    Call<CoinListDto> getCoins(
            @Query("start") String start,
            @Query("limit") String limit,
            @Query("convert") String convert
            );
}
