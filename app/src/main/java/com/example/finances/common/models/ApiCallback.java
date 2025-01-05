package com.example.finances.common.models;

import com.example.finances.Api.models.CoinListDto;
import retrofit2.Response;

public interface ApiCallback {
    void onSuccess(Response<CoinListDto> response);
    void onFailure(Throwable t);
}
