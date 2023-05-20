package com.example.finances.enums;

public enum ItemType {
    Unknown,
    ShopItem;

    public static int toInt(ItemType type) {
        switch (type) {
            case ShopItem: return 1;
            default: return 0;
        }
    }

    public static ItemType fromInt(int type) {
        switch (type) {
            case 1: return ShopItem;
            default: return Unknown;
        }
    }
}
