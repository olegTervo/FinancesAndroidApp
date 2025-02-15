package com.example.finances.domain.models;

import android.os.LocaleList;

import com.example.finances.domain.enums.InvestmentType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Investment {
    private long id;
    private String name;
    private float amount;
    private LocalDate openDate;
    private LocalDate modified;
    private InvestmentType type;

    private List<Price> priceHistory;

    public Investment(long id, String name, float amount, LocalDate openDate, LocalDate modified, InvestmentType type) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.openDate = openDate;
        this.modified = modified;
        this.type = type;

        this.priceHistory = new ArrayList<>();
    }

    public Investment(Investment other) {
        this.id = other.id;
        this.name = other.name;
        this.amount = other.amount;
        this.openDate = other.openDate;
        this.modified = other.modified;
        this.type = other.type;

        this.priceHistory = new ArrayList<>();
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public float getAmount() {
        return this.amount;
    }

    public LocalDate getOpenDate() {
        return this.openDate;
    }

    public LocalDate getModified() {
        return this.modified;
    }

    public InvestmentType getType() {
        return this.type;
    }

    public List<Price> getHistory() {
        return this.priceHistory;
    }

    public Price getLastPrice() {
        if (this.priceHistory.isEmpty())
            return null;

        this.priceHistory.sort((a, b) -> a.GetModified().compareTo(b.GetModified()));
        return this.priceHistory.get(this.priceHistory.size()-1);
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setHistory(List<Price> history) {
        this.priceHistory = history;
    }

    @Override
    public String toString() {
        if (getLastPrice() != null)
            return "id:" + id + "|" + name + "|" + getLastPrice().GetPrice() + "€|" + amount + "|" + openDate.toString() + "|" + (this instanceof ApiInvestment);

        return "id:" + id + "|" + name + "|" + (-1) + "€|" + amount + "|" + openDate.toString() + "|" + (this instanceof ApiInvestment);
    }
}
