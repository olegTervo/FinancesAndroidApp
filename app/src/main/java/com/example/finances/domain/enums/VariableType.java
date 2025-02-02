package com.example.finances.domain.enums;

public enum VariableType {
    Unknown,
    DailyGrowth,
    Target,
    Balance,
    Actives;


    public static int toInt(VariableType type) {
        switch (type) {
            case DailyGrowth: return 1;
            case Target: return 2;
            case Balance: return 3;
            case Actives: return 4;
            default: return 0;
        }
    }

    public static VariableType fromInt(int type) {
        switch (type) {
            case 1: return DailyGrowth;
            case 2: return Target;
            case 3: return Balance;
            case 4: return Actives;
            default: return Unknown;
        }
    }
}