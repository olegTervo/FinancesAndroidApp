package com.example.finances.domain.interfaces;

import com.example.finances.domain.enums.PriceType;
import com.example.finances.domain.enums.ShopItemPriceType;
import com.example.finances.domain.models.Price;

import java.util.List;

public interface IPriceRepository {
    float GetPrice(long itemId, PriceType priceType, int additionalType);
    List<Price> GetPrices(long itemId, PriceType priceType, int additionalType);
    Price CreatePrice(long itemId, PriceType priceType, int additionalType, float price);
    boolean DeletePrices(int itemId, PriceType priceType);
}
