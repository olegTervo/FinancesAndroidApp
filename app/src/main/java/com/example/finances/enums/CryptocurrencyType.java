package com.example.finances.enums;

public enum CryptocurrencyType {
    Unknown,
    Bitcoin,
    Ton;

    public static int toInt(CryptocurrencyType type) {
        switch (type) {
            case Bitcoin: return 1;
            case Ton: return 2;

            default: return 0;
        }
    }

    public static String toString(CryptocurrencyType type) {
        switch (type) {
            case Bitcoin: return "Bitcoin";
            case Ton: return "Toncoin";

            default: return "";
        }
    }

    public static CryptocurrencyType fromInt(int type) {
        switch (type) {
            case 1: return Bitcoin;
            case 2: return Ton;

            default: return Unknown;
        }
    }

    @Override
    public String toString() {
        return toString(this);
    }
}
