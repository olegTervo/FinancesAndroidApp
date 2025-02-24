package com.example.finances.domain.enums;

public enum CurrencyType {
    Unknown,
    EUR;


    public static int toInt(CurrencyType type) {
        switch (type) {
            case EUR: return 1;
            default: return 0;
        }
    }

    public static CurrencyType fromInt(int type) {
        switch (type) {
            case 1: return EUR;
            default: return Unknown;
        }
    }

}
