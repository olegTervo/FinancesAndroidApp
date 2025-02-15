package com.example.finances.domain.interfaces;

import com.example.finances.domain.enums.ValueDateType;
import com.example.finances.domain.models.ValueDate;

import java.time.LocalDate;
import java.util.ArrayList;

public interface IValueDateRepository {
    boolean sync(int daily);
    ValueDate getFirst(ValueDateType type);
    boolean create(float number, LocalDate date, ValueDateType type);
    ArrayList<ValueDate> getValues(ValueDateType type);
    boolean increaseTopValue(float value, ValueDateType type);
    void delete(ValueDateType type);
    boolean update(int id, int value);
}
