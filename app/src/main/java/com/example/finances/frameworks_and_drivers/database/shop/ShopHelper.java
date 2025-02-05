package com.example.finances.frameworks_and_drivers.database.shop;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finances.frameworks_and_drivers.database.account.AccountHelper;
import com.example.finances.frameworks_and_drivers.database.common.DatabaseHelper;

public class ShopHelper {

    public static final String SHOP_TABLE_NAME =                   "Shop";
    public static final String SHOP_ID_COLUMN_NAME =                   "Id";
    public static final String SHOP_TABLE_NAME_COLUMN_NAME =           "Name";
    public static final String SHOP_TABLE_BANK_ACCOUNT_COLUMN_NAME =   "AccountNumber";

    public static String CreateTableString() {
        String initialString = "CREATE TABLE "
                + SHOP_TABLE_NAME
                + " (" + SHOP_ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SHOP_TABLE_NAME_COLUMN_NAME +" VARCHAR(1024) NOT NULL, "
                + SHOP_TABLE_BANK_ACCOUNT_COLUMN_NAME +" INTEGER NOT NULL, "

                + "CONSTRAINT shop_bankAccount_fk FOREIGN KEY (" + SHOP_TABLE_BANK_ACCOUNT_COLUMN_NAME + ") "
                + "REFERENCES " + AccountHelper.ACCOUNT_TABLE_NAME + " (" + AccountHelper.ACCOUNT_ID_COLUMN_NAME + ")"

                + ");\n";

        return initialString;
    }

    public static String DropTableString() {
        String res = "DROP TABLE " + SHOP_TABLE_NAME + ";\n";
        return res;
    }

    public static void Initialize(DatabaseHelper connection) {
        OpenShop(connection, "FullPriceShop");
    }

    public static int GetShopId(DatabaseHelper connection, String name) {
        int res = -1;
        SQLiteDatabase db = connection.getReadableDatabase();

        String getScript = String.format("SELECT " + SHOP_ID_COLUMN_NAME + " FROM " + SHOP_TABLE_NAME + " WHERE " + SHOP_TABLE_NAME_COLUMN_NAME + " = '%s'", name);
        Cursor reader = db.rawQuery(getScript, null);

        if(reader.moveToFirst())
            res = reader.getInt(0);

        reader.close();

        return res;
    }

    public static boolean OpenShop(DatabaseHelper connection, String name) {
        long res = -1;
        String accountName = name + "_BankAccount";

        if(AccountHelper.OpenBankAccount(connection, accountName, 2)
                && AccountHelper.GetAccountNumber(connection, accountName) != -1){
            SQLiteDatabase db = connection.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put(SHOP_TABLE_NAME_COLUMN_NAME, name);
            cv.put(SHOP_TABLE_BANK_ACCOUNT_COLUMN_NAME, AccountHelper.GetAccountNumber(connection, accountName));
            res = db.insert(SHOP_TABLE_NAME, null, cv);
        }

        if (res == -1) {
            return false;
        }
        return true;
    }

    public static int GetShopAccountNumber(DatabaseHelper connection, String name) {
        String accountName = name + "_BankAccount";
        return AccountHelper.GetAccountNumber(connection, accountName);
    }
}
