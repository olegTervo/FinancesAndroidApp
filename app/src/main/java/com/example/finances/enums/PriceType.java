package com.example.finances.enums;

public enum PriceType {
    Unknown,
    ShopItem,
    Investment;

    public static int toInt(PriceType type) {
        switch (type) {
            case ShopItem: return 1;
            case Investment: return 2;
            default: return 0;
        }
    }

    public static PriceType fromInt(int type) {
        switch (type) {
            case 1: return ShopItem;
            case 2: return Investment;
            default: return Unknown;
        }
    }
}
