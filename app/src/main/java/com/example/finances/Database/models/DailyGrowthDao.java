package com.example.finances.Database.models;

import java.time.LocalDate;

public class DailyGrowthDao {
    public int id;
    public int value;
    public LocalDate date;

    public DailyGrowthDao(int id, int value, LocalDate date) {
        this.id = id;
        this.value = value;
        this.date = date;
    }

    @Override
    public String toString() {
        return "id:" + id + "|" + value + "€|" + date.getDayOfMonth() + "th";
    }
}
