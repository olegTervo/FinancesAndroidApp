package com.example.finances.interface_adapters.repository;

import com.example.finances.domain.interfaces.IShopRepository;
import com.example.finances.frameworks_and_drivers.database.shop.ShopDao;

import javax.inject.Inject;

public class ShopRepository implements IShopRepository {
    private final ShopDao shopDao;

    @Inject
    public ShopRepository(ShopDao dao) {
        shopDao = dao;
    }


    @Override
    public long GetShopId(String name) {
        return shopDao.GetShopId(name);
    }

    @Override
    public long OpenShop(String name, int accountNumber) {
        return shopDao.OpenShop(name, accountNumber);
    }
}
