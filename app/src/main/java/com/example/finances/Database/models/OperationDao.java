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
        String idTabs = "";
        for(int i = String.valueOf(id).length(); i < 4; i++ ) {
            idTabs += "\t";
        }
        String typeTabs = "";
        for(int i = String.valueOf(type).length(); i < 2; i++ ) {
            typeTabs += "\t";
        }
        String amountTabs = "";
        for(int i = String.valueOf(amount).length(); i < 5; i++ ) {
            amountTabs += "\t";
        }

        return "id:" + id + idTabs + "|"
                + "type:" + type + typeTabs + "|"
                + amount + "€" + amountTabs + "|"
                + creationDate.toString() + "\t|" + comment;
    }
}
