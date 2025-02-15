package com.example.finances.interface_adapters.repository;

import com.example.finances.domain.interfaces.IVariableRepository;
import com.example.finances.frameworks_and_drivers.database.variables.VariableDao;

import javax.inject.Inject;

public class VariableRepository implements IVariableRepository {
    private final VariableDao variableDao;

    @Inject
    public VariableRepository(VariableDao variableDao) {
        this.variableDao = variableDao;
    }

    @Override
    public boolean setVariable(int type, int value) {
        return variableDao.setVariable(type, value);
    }

    @Override
    public int getVariable(int type) {
        return variableDao.getVariable(type);
    }
}
