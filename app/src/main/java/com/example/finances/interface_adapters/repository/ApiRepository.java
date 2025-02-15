package com.example.finances.interface_adapters.repository;

import com.example.finances.domain.enums.ApiType;
import com.example.finances.domain.interfaces.IApiRepository;
import com.example.finances.domain.models.Api;
import com.example.finances.frameworks_and_drivers.database.api.ApiDao;

import javax.inject.Inject;

public class ApiRepository implements IApiRepository {
    private final ApiDao apiDao;

    @Inject
    public ApiRepository(ApiDao dao) {
        apiDao = dao;
    }

    @Override
    public void InsertCoinMarketCapApi() {
        apiDao.InsertCoinMarketCapApi();
    }

    @Override
    public Api GetApi(ApiType type) {
        return apiDao.GetApi(type);
    }

    @Override
    public Api GetApi(int apiId) {
        return apiDao.GetApi(apiId);
    }

    @Override
    public boolean CreateInvestmentApi(ApiType type, long investmentId, String name) {
        return apiDao.CreateInvestmentApi(type, investmentId, name);
    }
}
