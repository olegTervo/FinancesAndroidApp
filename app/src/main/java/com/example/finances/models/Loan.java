package com.example.finances.models;

import com.example.finances.Database.models.OperationDao;

import java.time.LocalDate;
import java.util.ArrayList;

public class Loan {
    public int id;
    private int unpaid;
    private int rate;
    private LocalDate openDate;

    private ArrayList<OperationDao> operations;

    public Loan(int id, int unpaid, int rate, LocalDate openDate) {
        this.id = id;
        this.unpaid = unpaid;
        this.rate = rate;
        this.openDate = openDate;

        this.operations = new ArrayList<>();
    }

    public void addOperations(ArrayList<OperationDao> operationsToAdd) {
        this.operations.addAll(operationsToAdd);
    }

    @Override
    public String toString() {
        String res = "";
        res += "id:" + id + "|" + unpaid + "€|" + openDate.toString();

        for(OperationDao operation : operations)
            res += "\n" + operation.toString();

        return res;
    }
}
