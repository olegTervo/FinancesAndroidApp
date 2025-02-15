package com.example.finances.domain.models;

import java.time.LocalDate;

public class DailyGrowth extends ValueDate implements Cloneable {
    public int id;

    public DailyGrowth(int id, float value, LocalDate date) {
        super(value, date);
        this.id = id;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "id:" + id + "|" + value + "€|" + date.getDayOfMonth() + "th";
    }
}
