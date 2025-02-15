package com.example.finances.interface_adapters.repository;

import com.example.finances.domain.interfaces.IOperationRepository;
import com.example.finances.domain.models.Operation;
import com.example.finances.frameworks_and_drivers.database.operation.OperationDao;

import java.util.ArrayList;

import javax.inject.Inject;

public class OperationRepository implements IOperationRepository {
    private final OperationDao operationDao;

    @Inject
    public OperationRepository(OperationDao dao) {
        operationDao = dao;
    }

    @Override
    public long CreateOperationForAccount(int account, int type, int amount, String info) {
        return operationDao.CreateOperationForAccount(account, type, amount, info);
    }

    @Override
    public long CreateOperation(int type, int amount, String info) {
        return operationDao.CreateOperation(type, amount, info);
    }

    @Override
    public ArrayList<Operation> GetOperations() {
        return operationDao.GetOperations();
    }

    @Override
    public ArrayList<Operation> GetAccountOperations(int account) {
        return operationDao.GetAccountOperations(account);
    }

    @Override
    public ArrayList<Operation> GetLoanOperations(int id) {
        return operationDao.GetLoanOperations(id);
    }
}
