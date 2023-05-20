package com.example.finances.enums;

public enum ShopItemPriceType {
    Unknown,
    BuyPrice,
    SellPrice;

    public static int toInt(ShopItemPriceType type) {
        switch (type) {
            case BuyPrice: return 1;
            case SellPrice: return 2;
            default: return 0;
        }
    }

    public static ShopItemPriceType fromInt(int type) {
        switch (type) {
            case 1: return BuyPrice;
            case 2: return SellPrice;
            default: return Unknown;
        }
    }
}
