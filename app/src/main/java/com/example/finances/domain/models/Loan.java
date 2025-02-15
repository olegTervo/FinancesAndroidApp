package com.example.finances.domain.models;

import com.example.finances.frameworks_and_drivers.database.operation.OperationDao;

import java.time.LocalDate;
import java.util.ArrayList;

public class Loan {
    public int id;
    private int unpaid;
    private int rate;
    private LocalDate openDate;

    private ArrayList<Operation> operations;

    public Loan(int id, int unpaid, int rate, LocalDate openDate) {
        this.id = id;
        this.unpaid = unpaid;
        this.rate = rate;
        this.openDate = openDate;

        this.operations = new ArrayList<>();
    }

    public void addOperations(ArrayList<Operation> operationsToAdd) {
        this.operations.addAll(operationsToAdd);
    }

    @Override
    public String toString() {
        String res = "";
        res += "id:" + id + "|" + unpaid + "€|" + openDate.toString();

        for(Operation operation : operations)
            res += "\n" + operation.toString();

        return res;
    }

    public int getId(){
        return this.id;
    }

    public int getUnpaid(){
        return this.unpaid;
    }

    public int getRate(){
        return this.rate;
    }

    public LocalDate getOpenDate(){
        return this.openDate;
    }
}
