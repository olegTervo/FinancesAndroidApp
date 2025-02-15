package com.example.finances.domain.interfaces;

import com.example.finances.domain.models.Loan;
import com.example.finances.frameworks_and_drivers.database.loan.LoanDao;

import java.util.ArrayList;

public interface ILoanRepository {
    long CreateLoan(int amount);
    boolean ChangeLoanUnpaid(long id, int amount);
    ArrayList<Loan> GetLoans();
    ArrayList<Loan> GetUnpaidLoans();
    boolean CreateLoanOperation(long loanId, long operationId);

    /*boolean UpdateUnpaid(DatabaseHelper connection, long id, int value);
    int GetUnpaid(DatabaseHelper connection, long id);*/
}
