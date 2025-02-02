package com.example.finances.domain.enums;

public enum ValueDateType {
    Unknown,
    DailyGrowth,
    Investments;

    public static int toInt(ValueDateType type) {
        switch (type) {
            case DailyGrowth: return 1;
            case Investments: return 2;
            default: return 0;
        }
    }

    public static ValueDateType fromInt(int type) {
        switch (type) {
            case 1: return DailyGrowth;
            case 2: return Investments;
            default: return Unknown;
        }
    }}
