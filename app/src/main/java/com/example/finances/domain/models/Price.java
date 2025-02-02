package com.example.finances.domain.models;

import com.example.finances.domain.enums.PriceType;

import java.time.LocalDate;

public class Price {
    private int id;
    private float price;
    private LocalDate created;
    private LocalDate modified;
    private PriceType priceType;

    public Price(int id, float price, LocalDate created, LocalDate modified, PriceType type) {
        this.id = id;
        this.price = price;
        this.created = created;
        this.modified = modified;
        this.priceType = type;
    }

    public int GetId(){
        return this.id;
    }

    public float GetPrice() {
        return this.price;
    }

    public LocalDate GetModified() {
        return this.modified;
    }
}
