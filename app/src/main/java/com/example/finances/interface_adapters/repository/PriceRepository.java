package com.example.finances.interface_adapters.repository;

import com.example.finances.domain.enums.PriceType;
import com.example.finances.domain.interfaces.IPriceRepository;
import com.example.finances.domain.models.Price;
import com.example.finances.frameworks_and_drivers.database.price.PriceDao;

import java.util.List;

import javax.inject.Inject;

public class PriceRepository implements IPriceRepository {
    private final PriceDao priceDao;

    @Inject
    public PriceRepository(PriceDao dao) {
        priceDao = dao;
    }

    @Override
    public float GetPrice(long itemId, PriceType priceType, int additionalType) {
        return priceDao.GetPrice(itemId, priceType, additionalType);
    }

    @Override
    public List<Price> GetPrices(long itemId, PriceType priceType, int additionalType) {
        return priceDao.GetPrices(itemId, priceType, additionalType);
    }

    @Override
    public boolean CreatePrice(long itemId, PriceType priceType, int additionalType, float price) {
        return priceDao.CreatePrice(itemId, priceType, additionalType, price);
    }

    @Override
    public boolean DeletePrices(int itemId, PriceType priceType) {
        return priceDao.DeletePrices(itemId, priceType);
    }
}
