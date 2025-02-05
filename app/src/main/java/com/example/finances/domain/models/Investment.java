package com.example.finances.domain.models;

import com.example.finances.frameworks_and_drivers.database.investment.InvestmentDao;
import com.example.finances.domain.enums.InvestmentType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Investment {
    private int id;
    private String name;
    private float amount;
    private LocalDate openDate;
    private LocalDate modified;
    private InvestmentType type;

    private List<Price> priceHistory;

    public Investment(int id, String name, float amount, LocalDate openDate, LocalDate modified, InvestmentType type) {
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

    public Investment(InvestmentDao dto, InvestmentType type, List<Price> hist) {
        this.id = dto.Id;
        this.name = dto.Name;
        this.amount = dto.Amount;
        this.openDate = dto.OpenDate;
        this.modified = dto.Modified;
        this.type = type;

        this.priceHistory = hist;
    }

    public int getId() {
        return this.id;
    }

    public List<Price> getHistory() {
        return this.priceHistory;
    }

    public float getAmount() {
        return this.amount;
    }

    public Price getLastPrice() {
        if (this.priceHistory.isEmpty())
            return null;

        this.priceHistory.sort((a, b) -> a.GetModified().compareTo(b.GetModified()));
        return this.priceHistory.get(this.priceHistory.size()-1);
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
