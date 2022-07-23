package com.example.finances.Database.helpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AccountHelper {

    public static final String ACCOUNT_TABLE_NAME = "Account";
    public static final String ACCOUNT_TABLE_MONEY_COLUMN_NAME = "Money";
    public static final String ACCOUNT_TABLE_NAME_COLUMN_NAME = "Name";
    public static final String ACCOUNT_TABLE_TYPE_COLUMN_NAME = "Type";
    public static final String ACCOUNT_ID_COLUMN_NAME = "Id";

    public static String CreateTableString() {
        String initialString = "CREATE TABLE "
                + ACCOUNT_TABLE_NAME
                + " (" + ACCOUNT_ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ACCOUNT_TABLE_TYPE_COLUMN_NAME + " INTEGER NOT NULL, "
                + ACCOUNT_TABLE_NAME_COLUMN_NAME +" VARCHAR(1024) NOT NULL, "
                + ACCOUNT_TABLE_MONEY_COLUMN_NAME +" INTEGER NOT NULL );\n";

        return initialString;
    }

    public static void Initialize(DatabaseHelper connection) {
        OpenBankAccount(connection, "MainBankAccount", 1);
    }

    public static boolean AccountExists(DatabaseHelper connection, int account) {
        SQLiteDatabase db = connection.getReadableDatabase();

        String getScript = String.format("SELECT " + ACCOUNT_ID_COLUMN_NAME + " FROM " + ACCOUNT_TABLE_NAME + " WHERE " + ACCOUNT_ID_COLUMN_NAME + " = %s", account);
        Cursor reader = db.rawQuery(getScript, null);

        return reader.moveToFirst();
    }

    public static boolean OpenBankAccount(DatabaseHelper connection, String name, int type) {
        SQLiteDatabase db = connection.getWritableDatabase();
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

    public static int GetMoney(DatabaseHelper connection, int account) {
        int result = 0;
        SQLiteDatabase db = connection.getReadableDatabase();

        String getScript = String.format("SELECT " + ACCOUNT_TABLE_MONEY_COLUMN_NAME + " FROM " + ACCOUNT_TABLE_NAME + " WHERE " + ACCOUNT_ID_COLUMN_NAME + " = %s", account);
        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            result = reader.getInt(0);

        reader.close();
        db.close();

        return result;
    }

    public static boolean PutMoney(DatabaseHelper connection, int account, int amount) {
        int current = GetMoney(connection, account);

        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ACCOUNT_TABLE_MONEY_COLUMN_NAME, current+amount);

        long res = db.update(ACCOUNT_TABLE_NAME, cv, "id=?", new String[] { Integer.toString(account) });

        if (res == -1) {
            return false;
        }

        return OperationHelper.CreateOperationForAccount(connection, account, 1, amount, "PutMoney autoinsert");
    }
}
