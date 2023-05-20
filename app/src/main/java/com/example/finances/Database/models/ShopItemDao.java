package com.example.finances.Database.models;

import com.example.finances.enums.ShopItemPriceType;

public class ShopItemDao {
    public int id;
    public String name;
    public int amount;
    public double buyPrice;
    public double sellPrice;

    public ShopItemDao(int id, String name, int amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.buyPrice = 0;
        this.sellPrice = 0;
    }

    public ShopItemDao(int id, String name, int amount, double buyPrice, double sellPrice) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public void SetPrice(ShopItemPriceType priceType, double price) {
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
}
