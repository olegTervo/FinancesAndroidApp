package com.example.finances.domain.interfaces;

public interface IVariableRepository {
    boolean setVariable(int type, int value);
    int getVariable(int type);
}
