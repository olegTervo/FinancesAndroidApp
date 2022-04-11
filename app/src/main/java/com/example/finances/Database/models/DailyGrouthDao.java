package com.example.finances.Database.models;

import java.time.LocalDate;

public class DailyGrouthDao {
    public int id;
    public int value;
    public LocalDate date;

    public DailyGrouthDao(int id, int value, LocalDate date) {
        this.id = id;
        this.value = value;
        this.date = date;
    }


}
