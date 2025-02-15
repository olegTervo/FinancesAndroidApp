package com.example.finances.domain.services;

import static com.example.finances.presentation.activities.FullPriceShopActivity.FullPriceShopName;

import com.example.finances.domain.enums.ValueDateType;
import com.example.finances.domain.interfaces.IAccountRepository;
import com.example.finances.domain.interfaces.ILoanRepository;
import com.example.finances.domain.interfaces.IOperationRepository;
import com.example.finances.domain.interfaces.IValueDateRepository;
import com.example.finances.domain.models.Loan;
import com.example.finances.domain.models.Operation;
import com.example.finances.frameworks_and_drivers.database.common.DatabaseHelper;

import java.util.ArrayList;

import javax.inject.Inject;

public class BankService {
    private DatabaseHelper db;

    private IAccountRepository accountRepository;
    private ILoanRepository loanRepository;
    private IOperationRepository operationRepository;
    private IValueDateRepository valueDateRepository;

    @Inject
    public BankService(
            IAccountRepository accountRepository,
            ILoanRepository loanRepository,
            IOperationRepository operationRepository,
            IValueDateRepository valueDateRepository,
            DatabaseHelper db) {
        this.accountRepository = accountRepository;
        this.loanRepository = loanRepository;
        this.operationRepository = operationRepository;
        this.valueDateRepository = valueDateRepository;
        this.db = db;
    }

    public int GetBankMoney() {
        if(!accountRepository.Exists(1))
            accountRepository.Initialize();

        return accountRepository.GetMoney(1);
    }

    public int GetShopMoney() {
        return accountRepository.GetMoney(GetShopAccountNumber(FullPriceShopName));
    }

    public long AddMoneyToBank(int amount) {
        return accountRepository.PutMoney(1, amount, "Extra money");
    }

    public long AddMoneyToShop(int amount, String message) {
        return accountRepository.PutMoney(GetShopAccountNumber(FullPriceShopName), amount, message);
    }

    public boolean OpenBankAccount(String name, int type) {
        return accountRepository.OpenBankAccount(name, type);
    }

    public int GetShopAccountNumber(String name) {
        String accountName = name + "_BankAccount";
        return GetAccountNumber(accountName);
    }

    public int GetAccountNumber(String name) {
        return accountRepository.GetAccountNumber(name);
    }

    public ArrayList<Loan> GetLoans() {
        ArrayList<Loan> result = loanRepository.GetLoans();

        for(Loan loan : result)
            loan.addOperations(operationRepository.GetLoanOperations(loan.id));

        return result;
    }

    public ArrayList<Loan> GetUnpaidLoans() {
        return loanRepository.GetUnpaidLoans();
    }

    public boolean TakeLoan(int amount) {
        int loanSum = amount;
        int interest = amount / 100 > 1 ? Math.round(amount / 100) : 1;

        long loanId = loanRepository.CreateLoan(amount);

        if (loanId == -1) {
            return false;
        }
        else {
            long openOperation = operationRepository.CreateOperation(1, interest*(-1), "Loan open interest");
            loanRepository.ChangeLoanUnpaid(loanId, interest*(-1));

            if (loanId != -1 && openOperation != -1) {
                loanRepository.CreateLoanOperation(loanId, openOperation); // TODO think
            }
        }

        long operationId = accountRepository.PutMoney(1, amount*(-1), "Loan payment");
        if (operationId == -1) {
            return false;
        }

        return loanRepository.CreateLoanOperation(loanId, operationId);
    }

    public boolean PayLoan(int id, int toPay) {
        long operationId = accountRepository.PutMoney(1, toPay, "Loan return"); // TODO think

        if(operationId == -1)
            return false;
        else
            loanRepository.ChangeLoanUnpaid(id, toPay);

        valueDateRepository.increaseTopValue(toPay*(-1), ValueDateType.DailyGrowth);

        return loanRepository.CreateLoanOperation(id, operationId);
    }

    public ArrayList<Operation> GetAccountOperations(int id) {
        return operationRepository.GetAccountOperations(id);
    }
}
