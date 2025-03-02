package com.example.finances.domain.interfaces.api;

import com.example.finances.domain.models.ApiInvestment;
import com.example.finances.domain.models.Investment;

import java.util.List;

public interface ICoinMarketCapApi {
    void syncPricesAsync(List<ApiInvestment> toSync, IApiCallback externalCallback);
}
