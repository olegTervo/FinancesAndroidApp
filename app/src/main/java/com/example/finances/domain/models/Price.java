package com.example.finances.domain.models;

import com.example.finances.domain.enums.PriceType;

import java.time.LocalDate;

public class Price {
    private long id;
    private float price;
    private LocalDate created;
    private LocalDate modified;
    private PriceType priceType;

    public Price(long id, float price, LocalDate created, LocalDate modified, PriceType type) {
        this.id = id;
        this.price = price;
        this.created = created;
        this.modified = modified;
        this.priceType = type;
    }

    public long GetId(){
        return this.id;
    }

    public float GetPrice() {
        return this.price;
    }

    public LocalDate GetModified() {
        return this.modified;
    }
}
