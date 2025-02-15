package com.example.finances.interface_adapters.repository;

import com.example.finances.domain.interfaces.ILoanRepository;
import com.example.finances.domain.models.Loan;
import com.example.finances.frameworks_and_drivers.database.loan.LoanDao;

import java.util.ArrayList;

import javax.inject.Inject;

public class LoanRepository implements ILoanRepository {
    private final LoanDao loanDao;

    @Inject
    public LoanRepository(LoanDao dao) {
        loanDao = dao;
    }

    @Override
    public long CreateLoan(int amount) {
        return loanDao.CreateLoan(amount);
    }

    @Override
    public boolean ChangeLoanUnpaid(long id, int amount) {
        return loanDao.ChangeLoanUnpaid(id, amount);
    }

    @Override
    public ArrayList<Loan> GetLoans() {
        return loanDao.GetLoans();
    }

    @Override
    public ArrayList<Loan> GetUnpaidLoans() {
        return loanDao.GetUnpaidLoans();
    }

    @Override
    public boolean CreateLoanOperation(long loanId, long operationId) {
        return loanDao.CreateLoanOperation(loanId, operationId);
    }
}
