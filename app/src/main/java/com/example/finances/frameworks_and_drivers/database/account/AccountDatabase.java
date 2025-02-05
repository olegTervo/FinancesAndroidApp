package com.example.finances.frameworks_and_drivers.database.account;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class AccountDatabase extends SQLiteOpenHelper {

    public static final String ACCOUNT_TABLE_NAME = "Account";
    public static final String ACCOUNT_TABLE_MONEY_COLUMN_NAME = "Money";
    public static final String ACCOUNT_TABLE_NAME_COLUMN_NAME = "Name";
    public static final String ACCOUNT_TABLE_TYPE_COLUMN_NAME = "Type";
    public static final String ACCOUNT_ID_COLUMN_NAME = "Id";

    private static volatile AccountDatabase INSTANCE;

    public AccountDatabase(Context context) {
        super(context, "finances.db", null, 17);
    }

    public static synchronized AccountDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AccountDatabase(context.getApplicationContext());
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreateTableString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 17)
            return;

        if (oldVersion == 17 && newVersion == 18) {
            //do update
        }
    }

    public static String CreateTableString() {
        String initialString = "CREATE TABLE "
                + ACCOUNT_TABLE_NAME
                + " (" + ACCOUNT_ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ACCOUNT_TABLE_TYPE_COLUMN_NAME + " INTEGER NOT NULL, "
                + ACCOUNT_TABLE_NAME_COLUMN_NAME +" VARCHAR(1024) NOT NULL, "
                + ACCOUNT_TABLE_MONEY_COLUMN_NAME +" INTEGER NOT NULL"
                + ");\n";

        return initialString;
    }

    public static String DropTableString() {
        String res = "DROP TABLE IF EXISTS " + ACCOUNT_TABLE_NAME + ";\n";
        return res;
    }
}
