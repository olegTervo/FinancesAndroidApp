package com.example.finances.frameworks_and_drivers.database.account;

import static com.example.finances.frameworks_and_drivers.database.account.AccountDatabase.*;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import javax.inject.Inject;

public class AccountDao {
    private final AccountDatabase accountDatabase;

    @Inject
    public AccountDao(AccountDatabase db) {
        this.accountDatabase = db;
    }

    public void Initialize() {
        OpenBankAccount("MainBankAccount", 1);
    }

    public boolean AccountExists(int account) {
        SQLiteDatabase db = accountDatabase.getReadableDatabase();
        boolean res = false;

        String getScript = String.format("SELECT " + ACCOUNT_ID_COLUMN_NAME + " FROM " + ACCOUNT_TABLE_NAME + " WHERE " + ACCOUNT_ID_COLUMN_NAME + " = %s", account);
        Cursor reader = db.rawQuery(getScript, null);
        res = reader.moveToFirst();

        reader.close();
        return res;
    }

    public int GetAccountNumber(String name) {
        SQLiteDatabase db = accountDatabase.getReadableDatabase();
        int res = -1;

        String getScript = String.format("SELECT " + ACCOUNT_ID_COLUMN_NAME + " FROM " + ACCOUNT_TABLE_NAME + " WHERE " + ACCOUNT_TABLE_NAME_COLUMN_NAME + " = '%s'", name);
        Cursor reader = db.rawQuery(getScript, null);

        if(reader.moveToFirst())
            res = reader.getInt(0);

        reader.close();
        return res;
    }

    public boolean OpenBankAccount(String name, int type) {
        SQLiteDatabase db = accountDatabase.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ACCOUNT_TABLE_TYPE_COLUMN_NAME, type);
        cv.put(ACCOUNT_TABLE_NAME_COLUMN_NAME, name);
        cv.put(ACCOUNT_TABLE_MONEY_COLUMN_NAME, 0);
        long res = db.insert(ACCOUNT_TABLE_NAME, null, cv);

        if (res == -1) {
            return false;
        }
        return true;
    }

    public int GetMoney(int account) {
        SQLiteDatabase db = accountDatabase.getReadableDatabase();
        int result = 0;

        String getScript = String.format("SELECT " + ACCOUNT_TABLE_MONEY_COLUMN_NAME + " FROM " + ACCOUNT_TABLE_NAME + " WHERE " + ACCOUNT_ID_COLUMN_NAME + " = %s", account);
        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            result = reader.getInt(0);

        reader.close();
        return result;
    }

    public long PutMoney(int account, int amount, String message) {
        SQLiteDatabase db = accountDatabase.getWritableDatabase();
        int current = GetMoney(account);

        ContentValues cv = new ContentValues();

        cv.put(ACCOUNT_TABLE_MONEY_COLUMN_NAME, current+amount);

        long res = db.update(ACCOUNT_TABLE_NAME, cv, "id=?", new String[] { Integer.toString(account) });

        if (res == -1) {
            return res;
        }
        return res;
        //return OperationHelper.CreateOperationForAccount(connection, account, 1, amount, message);
    }
}
