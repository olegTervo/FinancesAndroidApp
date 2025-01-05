package com.example.finances.models;

import com.example.finances.enums.PriceType;
import com.example.finances.enums.ShopItemPriceType;

import java.time.LocalDate;

public class ShopItemPrice extends Price {
    private ShopItemPriceType additionalType;

    public ShopItemPrice(int id, float price, LocalDate created, LocalDate modified, PriceType type, ShopItemPriceType additionalType) {
        super(id, price, created, modified, type);

        this.additionalType = additionalType;
    }
}
