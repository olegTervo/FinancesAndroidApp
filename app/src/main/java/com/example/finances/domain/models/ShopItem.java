package com.example.finances.domain.models;

import com.example.finances.domain.enums.ShopItemPriceType;

public class ShopItem {
    private int id;
    private String name;
    private int amount;
    private float buyPrice;
    private float sellPrice;

    public ShopItem(int id, String name, int amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.buyPrice = 0;
        this.sellPrice = 0;
    }

    public ShopItem(int id, String name, int amount, float buyPrice, float sellPrice) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public void SetPrice(ShopItemPriceType priceType, float price) {
        switch (priceType) {
            case BuyPrice:
                this.buyPrice = price;
                break;
            case SellPrice:
                this.sellPrice = price;
                break;
            default:
                break;
        }
    }

    @Override
    public String toString() {
        return name + " - " + buyPrice + "/" + sellPrice + " - " + amount;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public float getBuyPrice() {
        return this.buyPrice;
    }

    public float getSellPrice() {
        return this.sellPrice;
    }
}
