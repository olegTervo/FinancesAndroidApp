package com.example.finances.domain.models;

import com.example.finances.domain.enums.PriceType;
import com.example.finances.domain.enums.ShopItemPriceType;

import java.time.LocalDate;

public class ShopItemPrice extends Price {
    private ShopItemPriceType additionalType;

    public ShopItemPrice(int id, float price, LocalDate created, LocalDate modified, PriceType type, ShopItemPriceType additionalType) {
        super(id, price, created, modified, type);

        this.additionalType = additionalType;
    }
}
