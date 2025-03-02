package com.example.finances.domain.services;

import com.example.finances.domain.enums.ValueDateType;
import com.example.finances.domain.interfaces.IValueDateRepository;
import com.example.finances.domain.interfaces.IVariableRepository;
import com.example.finances.domain.models.ValueDate;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class VariablesService {

    private IValueDateRepository valueDateRepository;
    private IVariableRepository variableRepository;

    @Inject
    public VariablesService(IValueDateRepository valueDateRepository, IVariableRepository variableRepository) {
        this.valueDateRepository = valueDateRepository;
        this.variableRepository = variableRepository;
    }

    public void sync() {
        int daily = variableRepository.getVariable(1);
        valueDateRepository.sync(daily);
    }

    public boolean increaseTopValue(float value, ValueDateType type) {
        return valueDateRepository.increaseTopValue(value, type);
    }

    public ArrayList<ValueDate> getValues(ValueDateType type) {
        return valueDateRepository.getValues(type);
    }

    public ValueDate getFirst(ValueDateType type) {
        return valueDateRepository.getFirst(type);
    }

    public int getVariable(int type) {
        return variableRepository.getVariable(type);
    }

    public boolean setVariable(int type, int value) {
        return variableRepository.setVariable(type, value);
    }
}
