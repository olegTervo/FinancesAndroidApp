package com.example.finances.frameworks_and_drivers.database.daily_growth;

import com.example.finances.domain.models.ValueDate;

import java.time.LocalDate;

public class DailyGrowthDao extends ValueDate implements Cloneable {
    public int id;

    public DailyGrowthDao(int id, float value, LocalDate date) {
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
