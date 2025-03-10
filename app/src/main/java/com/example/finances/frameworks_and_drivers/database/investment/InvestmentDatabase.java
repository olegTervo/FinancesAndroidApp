package com.example.finances.frameworks_and_drivers.database.investment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.finances.frameworks_and_drivers.database.common.DatabaseHelper;

public class InvestmentDatabase extends DatabaseHelper {

    public static final String INVESTMENT_TABLE_NAME = "Investment";
    public static final String INVESTMENT_TABLE_ID_COLUMN_NAME = "Id";
    public static final String INVESTMENT_TABLE_NAME_COLUMN_NAME = "Name";
    public static final String INVESTMENT_TABLE_AMOUNT_COLUMN_NAME = "Amount";
    public static final String INVESTMENT_TABLE_OPENDATE_COLUMN_NAME = "OpenDate";
    public static final String INVESTMENT_TABLE_MODIFIED_COLUMN_NAME = "Modified";

    private static volatile InvestmentDatabase INSTANCE;

    public InvestmentDatabase(Context context) {
        super(context);
    }

    public static synchronized InvestmentDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new InvestmentDatabase(context.getApplicationContext());
        }
        return INSTANCE;
    }

    public static String CreateTableString() {
        String initialString = "CREATE TABLE "
                + INVESTMENT_TABLE_NAME
                + " (" + INVESTMENT_TABLE_ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + INVESTMENT_TABLE_NAME_COLUMN_NAME +" VARCHAR(1024) NOT NULL, "
                + INVESTMENT_TABLE_AMOUNT_COLUMN_NAME + " FLOAT NOT NULL, "
                + INVESTMENT_TABLE_OPENDATE_COLUMN_NAME +" TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + INVESTMENT_TABLE_MODIFIED_COLUMN_NAME +" TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ");\n";

        return initialString;
    }

    public static String DropTableString() {
        String res = "DROP TABLE " + INVESTMENT_TABLE_NAME + ";\n";
        return res;
    }


}
