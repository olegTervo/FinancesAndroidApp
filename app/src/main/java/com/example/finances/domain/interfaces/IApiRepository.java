package com.example.finances.domain.interfaces;

import com.example.finances.domain.enums.ApiType;
import com.example.finances.domain.models.Api;

public interface IApiRepository {
    void InsertCoinMarketCapApi();
    Api GetApi(ApiType type);
    Api GetApi(int apiId);
    boolean CreateInvestmentApi(ApiType type, long investmentId, String name);
}
