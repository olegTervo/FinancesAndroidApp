package com.example.finances.domain.interfaces.api;

import com.example.finances.domain.models.ApiInvestment;

import java.util.List;

public interface ICoinMarkerCapApi {
    void syncPricesAsync(List<ApiInvestment> toSync, IApiCallback externalCallback);
}
