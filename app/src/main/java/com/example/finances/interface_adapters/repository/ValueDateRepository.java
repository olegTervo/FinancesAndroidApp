package com.example.finances.interface_adapters.repository;

import com.example.finances.domain.enums.ValueDateType;
import com.example.finances.domain.interfaces.IValueDateRepository;
import com.example.finances.domain.models.ValueDate;
import com.example.finances.frameworks_and_drivers.database.value_date.ValueDateDao;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.inject.Inject;

public class ValueDateRepository implements IValueDateRepository {
    private final ValueDateDao valueDateDao;

    @Inject
    public ValueDateRepository(ValueDateDao valueDateDao) {
        this.valueDateDao = valueDateDao;
    }

    @Override
    public boolean sync(int daily) {
        return valueDateDao.sync(daily);
    }

    @Override
    public ValueDate getFirst(ValueDateType type) {
        return valueDateDao.getFirst(type);
    }

    @Override
    public boolean create(float number, LocalDate date, ValueDateType type) {
        return valueDateDao.create(number, date, type);
    }

    @Override
    public ArrayList<ValueDate> getValues(ValueDateType type) {
        return valueDateDao.getValues(type);
    }

    @Override
    public boolean increaseTopValue(float value, ValueDateType type) {
        return valueDateDao.increaseTopValue(value, type);
    }

    @Override
    public void delete(ValueDateType type) {
        valueDateDao.delete(type);
    }

    @Override
    public boolean update(int id, int value) {
        return valueDateDao.update(id, value);
    }
}
