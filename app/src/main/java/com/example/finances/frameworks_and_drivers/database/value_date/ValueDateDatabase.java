package com.example.finances.frameworks_and_drivers.database.value_date;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.finances.frameworks_and_drivers.database.common.DatabaseHelper;

public class ValueDateDatabase extends DatabaseHelper {

    public static final String VALUE_DATE_TABLE_NAME = "ValueDate";
    public static final String VALUE_DATE_ID_COLUMN_NAME = "Id";
    public static final String VALUE_DATE_TYPE_COLUMN_NAME = "Type";
    public static final String VALUE_DATE_VALUE_COLUMN_NAME = "Value";
    public static final String VALUE_DATE_DATE_COLUMN_NAME = "Date";

    private static volatile ValueDateDatabase INSTANCE;

    public ValueDateDatabase(Context context) {
        super(context);
    }

    public static synchronized ValueDateDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ValueDateDatabase(context.getApplicationContext());
        }
        return INSTANCE;
    }

    public static String CreateTableString() {
        String initialString = "CREATE TABLE "
                + VALUE_DATE_TABLE_NAME
                + " (" + VALUE_DATE_ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + VALUE_DATE_TYPE_COLUMN_NAME + " INTEGER NOT NULL, "
                + VALUE_DATE_VALUE_COLUMN_NAME +" FLOAT NOT NULL, "
                + VALUE_DATE_DATE_COLUMN_NAME +" TIMESTAMP DEFAULT CURRENT_TIMESTAMP );\n";

        return initialString;
    }

    public static String DropTableString() {
        String res = "DROP TABLE " + VALUE_DATE_TABLE_NAME + ";\n";
        return res;
    }
}
