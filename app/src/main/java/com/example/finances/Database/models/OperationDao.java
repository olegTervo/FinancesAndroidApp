package com.example.finances.Database.models;

import java.time.LocalDate;

public class OperationDao {
    public int id;
    public int type;
    public int amount;
    public String comment;
    public LocalDate creationDate;

    public OperationDao(int id, int type, int amount, String comment, LocalDate creationDate) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.comment = comment;
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "id:" + id + "|" + amount + "€|" + creationDate.getDayOfMonth() + "th";
    }
}
