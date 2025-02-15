package com.example.finances.domain.models;

import java.time.LocalDate;

public class Operation {
    private int id;
    private int type;
    private int amount;
    private String comment;
    private LocalDate creationDate;

    public Operation(int id, int type, int amount, String comment, LocalDate creationDate) {
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

    public int getType() {
        return this.type;
    }

    public int getAmount() {
        return this.amount;
    }
}
