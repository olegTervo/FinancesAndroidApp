package com.example.finances.frameworks_and_drivers.database.variables;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.finances.frameworks_and_drivers.database.common.DatabaseHelper;

public class VariableDatabase extends DatabaseHelper {

    public static final String VARIABLE_TABLE_NAME = "Variable";
    public static final String VARIABLE_TABLE_VALUE_COLUMN_NAME = "Value";
    public static final String VARIABLE_TABLE_TYPE_COLUMN_NAME = "Type";
    public static final String VARIABLE_ID_COLUMN_NAME = "Id";

    private static volatile VariableDatabase INSTANCE;

    public VariableDatabase(Context context) {
        super(context);
    }

    public static synchronized VariableDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new VariableDatabase(context.getApplicationContext());
        }
        return INSTANCE;
    }

    public static String CreateTableString() {
        String initialString = "CREATE TABLE "
                + VARIABLE_TABLE_NAME
                + " (" + VARIABLE_ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + VARIABLE_TABLE_TYPE_COLUMN_NAME + " INTEGER NOT NULL, "
                + VARIABLE_TABLE_VALUE_COLUMN_NAME +" INTEGER NOT NULL );\n";

        return initialString;
    }

}
