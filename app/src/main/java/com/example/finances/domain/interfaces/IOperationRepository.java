package com.example.finances.domain.interfaces;

import com.example.finances.domain.models.Operation;

import java.util.ArrayList;

public interface IOperationRepository {
    long CreateOperationForAccount(int account, int type, int amount, String info);
    long CreateOperation(int type, int amount, String info);
    ArrayList<Operation> GetOperations();
    ArrayList<Operation> GetAccountOperations(int account);
    ArrayList<Operation> GetLoanOperations(int id);

}
