package com.example.finances.domain.interfaces;

import com.example.finances.domain.models.ShopItem;

import java.util.ArrayList;

public interface IShopItemRepository {
    int GetShopItemNumber(String name);
    ArrayList<ShopItem> GetShopItems(long shopId);
    boolean CreateItem(String name, long shopId);
    int GetItemAmount(int id);
    boolean AddItems(long id, int amount);
    boolean DeleteItem(int id);
}
