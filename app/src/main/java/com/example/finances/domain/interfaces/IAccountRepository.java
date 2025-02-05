package com.example.finances.domain.interfaces;

public interface IAccountRepository {
    boolean Initialize();
    boolean Exists(int account);
    int GetAccountNumber(String name);
    boolean OpenBankAccount(String name, int type);
    int GetMoney(int account);
    long PutMoney(int account, int amount, String message);
}
