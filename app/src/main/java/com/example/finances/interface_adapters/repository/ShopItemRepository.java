package com.example.finances.interface_adapters.repository;

import com.example.finances.domain.interfaces.IShopItemRepository;
import com.example.finances.domain.models.ShopItem;
import com.example.finances.frameworks_and_drivers.database.shop.ShopItemDao;

import java.util.ArrayList;

import javax.inject.Inject;

public class ShopItemRepository implements IShopItemRepository {
    private final ShopItemDao shopItemDao;

    @Inject
    public ShopItemRepository(ShopItemDao shopItemDao) {
        this.shopItemDao = shopItemDao;
    }

    @Override
    public int GetShopItemNumber(String name) {
        return shopItemDao.GetShopItemNumber(name);
    }

    @Override
    public ArrayList<ShopItem> GetShopItems(long shopId) {
        return shopItemDao.GetShopItems(shopId);
    }

    @Override
    public boolean CreateItem(String name, long shopId) {
        return shopItemDao.CreateItem(name, shopId);
    }

    @Override
    public int GetItemAmount(int id) {
        return shopItemDao.GetItemAmount(id);
    }

    @Override
    public boolean AddItems(long id, int amount) {
        return shopItemDao.AddItems(id, amount);
    }

    @Override
    public boolean DeleteItem(int id) {
        return shopItemDao.DeleteItem(id);
    }
}
