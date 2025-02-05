package com.example.finances.interface_adapters.repository;

import com.example.finances.domain.interfaces.IAccountRepository;
import com.example.finances.frameworks_and_drivers.database.account.AccountDao;
import com.example.finances.frameworks_and_drivers.database.account.AccountDatabase;

public class AccountRepository implements IAccountRepository {
    private final AccountDao accountDao;

    public AccountRepository(AccountDatabase db) {
        accountDao = new AccountDao(db.getWritableDatabase());
    }

    @Override
    public boolean Initialize(){
        accountDao.Initialize();

        return true;
    }

    @Override
    public boolean Exists(int account) {
        return accountDao.AccountExists(account);
    }

    @Override
    public int GetAccountNumber(String name) {
        return accountDao.GetAccountNumber(name);
    }

    @Override
    public boolean OpenBankAccount(String name, int type) {
        return accountDao.OpenBankAccount(name, type);
    }

    @Override
    public int GetMoney(int account) {
        return accountDao.GetMoney(account);
    }

    @Override
    public long PutMoney(int account, int amount, String message) {
        return accountDao.PutMoney(account, amount, message);
    }
}
