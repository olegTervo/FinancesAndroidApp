package com.example.finances.domain.services;

import com.example.finances.domain.interfaces.IAccountRepository;

import javax.inject.Inject;

public class BankService {

    IAccountRepository accountRepository;

    @Inject
    public BankService(IAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public int GetBankMoney() {
        if(!accountRepository.Exists(1))
            accountRepository.Initialize();

        return accountRepository.GetMoney(1);
    }

    public long AddMoneyToBank(int amount) {
        return accountRepository.PutMoney(1, amount, "Extra money");
    }
}
