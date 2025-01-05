package com.example.finances.enums;

public enum ApiType {
    Unknown,
    CoinMarketCap,
    CoinMarketCapTest;

    public static int toInt(ApiType type) {
        switch (type) {
            case CoinMarketCap: return 1;
            case CoinMarketCapTest: return 2;
            default: return 0;
        }
    }

    public static ApiType fromInt(int type) {
        switch (type) {
            case 1: return CoinMarketCap;
            case 2: return CoinMarketCapTest;
            default: return Unknown;
        }
    }
}
