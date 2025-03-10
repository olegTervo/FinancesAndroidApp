package com.example.finances.frameworks_and_drivers.database.price;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.finances.frameworks_and_drivers.database.common.DatabaseHelper;

public class PriceDatabase extends DatabaseHelper {
    public static final String PRICE_TABLE_NAME = "Price";
    public static final String PRICE_TABLE_ID_COLUMN_NAME = "Id";
    public static final String PRICE_TABLE_PRICE_COLUMN_NAME = "Price";
    public static final String PRICE_TABLE_CREATED_COLUMN_NAME = "Created";
    public static final String PRICE_TABLE_MODIFIED_COLUMN_NAME = "Modified";

    private static volatile PriceDatabase INSTANCE;

    public PriceDatabase(Context context) {
        super(context);
    }

    public static synchronized PriceDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new PriceDatabase(context.getApplicationContext());
        }
        return INSTANCE;
    }

    public static String CreateTableString() {
        String initialString = "CREATE TABLE "
                + PRICE_TABLE_NAME
                + " (" + PRICE_TABLE_ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PRICE_TABLE_PRICE_COLUMN_NAME +" FLOAT NOT NULL, "
                + PRICE_TABLE_CREATED_COLUMN_NAME +" TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + PRICE_TABLE_MODIFIED_COLUMN_NAME +" TIMESTAMP DEFAULT CURRENT_TIMESTAMP"

                + ");\n";

        return initialString;
    }

    public static String DropTableString() {
        String res = "DROP TABLE " + PRICE_TABLE_NAME + ";\n";
        return res;
    }
}
