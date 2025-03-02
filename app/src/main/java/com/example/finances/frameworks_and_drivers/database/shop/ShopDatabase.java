package com.example.finances.frameworks_and_drivers.database.shop;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.finances.frameworks_and_drivers.database.account.AccountDatabase;
import com.example.finances.frameworks_and_drivers.database.common.DatabaseHelper;

public class ShopDatabase extends DatabaseHelper {

    public static final String SHOP_TABLE_NAME = "Shop";
    public static final String SHOP_ID_COLUMN_NAME = "Id";
    public static final String SHOP_TABLE_NAME_COLUMN_NAME = "Name";
    public static final String SHOP_TABLE_BANK_ACCOUNT_COLUMN_NAME = "AccountNumber";

    private static volatile ShopDatabase INSTANCE;

    public ShopDatabase(Context context) {
        super(context);
    }

    public static synchronized ShopDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ShopDatabase(context.getApplicationContext());
        }
        return INSTANCE;
    }

    public static String CreateTableString() {
        String initialString = "CREATE TABLE "
                + SHOP_TABLE_NAME
                + " (" + SHOP_ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SHOP_TABLE_NAME_COLUMN_NAME +" VARCHAR(1024) NOT NULL, "
                + SHOP_TABLE_BANK_ACCOUNT_COLUMN_NAME +" INTEGER NOT NULL, "

                + "CONSTRAINT shop_bankAccount_fk FOREIGN KEY (" + SHOP_TABLE_BANK_ACCOUNT_COLUMN_NAME + ") "
                + "REFERENCES " + AccountDatabase.ACCOUNT_TABLE_NAME + " (" + AccountDatabase.ACCOUNT_ID_COLUMN_NAME + ")"

                + ");\n";

        return initialString;
    }

    public static String DropTableString() {
        String res = "DROP TABLE " + SHOP_TABLE_NAME + ";\n";
        return res;
    }
}
