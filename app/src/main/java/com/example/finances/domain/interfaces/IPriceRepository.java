package com.example.finances.domain.interfaces;

import com.example.finances.domain.enums.PriceType;
import com.example.finances.domain.enums.ShopItemPriceType;
import com.example.finances.domain.models.Price;

import java.util.List;

public interface IPriceRepository {
    float GetPrice(long itemId, PriceType priceType, int additionalType);
    List<Price> GetPrices(long itemId, PriceType priceType, int additionalType);
    boolean CreatePrice(long itemId, PriceType priceType, int additionalType, float price);
    boolean DeletePrices(int itemId, PriceType priceType);
    //float GetShopItemPrice(long shopItemId, ShopItemPriceType type);
    //float GetInvestmentPrice(long investmentId);
    //boolean CreateShopItemPrice(long shopItemId, ShopItemPriceType type, double price);
    //boolean CreateInvestmentPrice(long investmentId, double price);
    //boolean DeleteShopItemPrices(int itemId);
    //boolean DeleteInvestmentPrices(int itemId);
    //List<Price> GetShopItemPrices(long shopItemId);
    //List<Price> GetInvestmentPrices(long investmentId);
}
