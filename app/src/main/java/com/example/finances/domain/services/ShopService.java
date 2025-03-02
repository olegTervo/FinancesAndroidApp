package com.example.finances.domain.services;

import com.example.finances.domain.enums.PriceType;
import com.example.finances.domain.enums.ShopItemPriceType;
import com.example.finances.domain.interfaces.IAccountRepository;
import com.example.finances.domain.interfaces.IPriceRepository;
import com.example.finances.domain.interfaces.IShopItemRepository;
import com.example.finances.domain.interfaces.IShopRepository;
import com.example.finances.domain.models.ShopItem;
import com.example.finances.frameworks_and_drivers.database.common.DatabaseHelper;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ShopService {
    private DatabaseHelper db;
    private IAccountRepository accountRepository;
    private IShopRepository shopRepository;
    private IShopItemRepository shopItemRepository;
    private IPriceRepository priceRepository;

    @Inject
    public ShopService(
            IAccountRepository accountRepository,
            IShopRepository shopRepository,
            IShopItemRepository shopItemRepository,
            IPriceRepository priceRepository,
            DatabaseHelper db)
    {
        this.accountRepository = accountRepository;
        this.shopRepository = shopRepository;
        this.shopItemRepository = shopItemRepository;
        this.priceRepository = priceRepository;
        this.db = db;
    }

    public void Initialize() {
        String shopName = "FullPriceShop";
        String accountName = shopName + "_BankAccount";

        OpenShop(shopName, accountName);
    }

    public boolean OpenShop(String name, String accountName) {
        long res = -1;

        if (accountRepository.OpenBankAccount(accountName, 2)) {
            int account = accountRepository.GetAccountNumber(accountName);
            res = shopRepository.OpenShop(name, account);
        }

        if (res == -1) {
            return false;
        }

        return true;
    }

    public long GetShopId(String name) {
        return shopRepository.GetShopId(name);
    }

    public ArrayList<ShopItem> GetShopItems(long shopId) {
        ArrayList<ShopItem> result = shopItemRepository.GetShopItems(shopId);

        for(ShopItem item : result) {
            float buyPrice = priceRepository.GetPrice(item.getId(), PriceType.ShopItem, ShopItemPriceType.toInt(ShopItemPriceType.BuyPrice));
            float sellPrice = priceRepository.GetPrice(item.getId(), PriceType.ShopItem, ShopItemPriceType.toInt(ShopItemPriceType.SellPrice));

            item.SetPrice(ShopItemPriceType.BuyPrice, buyPrice);
            item.SetPrice(ShopItemPriceType.SellPrice, sellPrice);
        }

        return result;
    }

    public boolean CreateShopItem(String name, long shopId, float buyPrice, float sellPrice) {
        boolean result = shopItemRepository.CreateItem(name, shopId);

        result = result && priceRepository.CreatePrice(shopItemRepository.GetShopItemNumber(name), PriceType.ShopItem, ShopItemPriceType.toInt(ShopItemPriceType.BuyPrice), buyPrice) != null;
        result = result && priceRepository.CreatePrice(shopItemRepository.GetShopItemNumber(name), PriceType.ShopItem, ShopItemPriceType.toInt(ShopItemPriceType.SellPrice), sellPrice) != null;

        return result;
    }

    public boolean DeleteShopItem(int itemId) {
        boolean res = false;

        if(priceRepository.DeletePrices(itemId, PriceType.ShopItem)) {
            res = shopItemRepository.DeleteItem(itemId);
        }

        return res;
    }

    public boolean ChangeShopItemAmount(long itemId, int amount) {
        return shopItemRepository.AddItems(itemId, amount);
    }
}
