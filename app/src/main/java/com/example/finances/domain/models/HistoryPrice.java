package com.example.finances.domain.models;

import java.text.DecimalFormat;
import java.time.LocalDate;

public class HistoryPrice extends ValueDate{
    public int id;

    public HistoryPrice(int id, float price, LocalDate date) {
        super(price, date);
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void add(float val) {
        this.value = this.value + val;
    }

    @Override
    public String toString() {
        String val = new DecimalFormat("#0.000€").format(value);
        String idTubs = "\t\t";

        if (id < 10) {
            idTubs += "\t";
        }

        return "id:" + id + idTubs + "|" + val + "\t\t|" + date.toString();
    }
}
