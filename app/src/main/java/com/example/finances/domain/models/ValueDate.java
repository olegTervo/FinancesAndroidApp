package com.example.finances.domain.models;

import java.time.LocalDate;

public abstract class ValueDate implements Cloneable  {
    public float value;
    public LocalDate date;

    public ValueDate(float value, LocalDate date) {
        this.value = value;
        this.date = date;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public abstract int getId();
}
