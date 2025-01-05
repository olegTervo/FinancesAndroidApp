package com.example.finances.enums;

public enum InvestmentType {
    Unknown,
    Manual,
    ApiLinked;


    public static int toInt(InvestmentType type) {
        switch (type) {
            case Manual: return 1;
            case ApiLinked: return 2;
            default: return 0;
        }
    }

    public static InvestmentType fromInt(int type) {
        switch (type) {
            case 1: return Manual;
            case 2: return ApiLinked;
            default: return Unknown;
        }
    }

}
