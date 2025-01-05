package com.example.finances.Database.models;

import java.time.LocalDate;

public class InvestmentDao{
    public int Id;
    public String Name;
    public float Amount;
    public LocalDate OpenDate;
    public LocalDate Modified;

    public InvestmentDao(int id, String name, float amount, LocalDate openDate, LocalDate modified) {
        this.Id = id;
        this.Name = name;
        this.Amount = amount;
        this.OpenDate = openDate;
        this.Modified = modified;
    }

    public InvestmentDao(String name, float amount) {
        this.Id = 0;
        this.Name = name;
        this.Amount = amount;
        this.OpenDate = LocalDate.now();
        this.Modified = LocalDate.now();
    }

    @Override
    public String toString() {
        return Id + " - " + Name + "/" + Amount + " - " + OpenDate.toString();
    }
}
