package com.example.finances.frameworks_and_drivers.database.value_date;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ValueDateDatabase extends SQLiteOpenHelper {

    public static final String VALUE_DATE_TABLE_NAME = "ValueDate";
    public static final String VALUE_DATE_ID_COLUMN_NAME = "Id";
    public static final String VALUE_DATE_TYPE_COLUMN_NAME = "Type";
    public static final String VALUE_DATE_VALUE_COLUMN_NAME = "Value";
    public static final String VALUE_DATE_DATE_COLUMN_NAME = "Date";

    private static volatile ValueDateDatabase INSTANCE;

    public ValueDateDatabase(Context context) {
        super(context, "finances.db", null, 17);
    }

    public static synchronized ValueDateDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ValueDateDatabase(context.getApplicationContext());
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
