package com.example.finances.infrastructure.Database.helpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class VariablesHelper {

    public static final String VARIABLE_TABLE_NAME = "Variable";
    public static final String VARIABLE_TABLE_VALUE_COLUMN_NAME = "Value";
    public static final String VARIABLE_TABLE_TYPE_COLUMN_NAME = "Type";
    public static final String VARIABLE_ID_COLUMN_NAME = "Id";

    public static String CreateTableString() {
        String initialString = "CREATE TABLE "
                + VARIABLE_TABLE_NAME
                + " (" + VARIABLE_ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + VARIABLE_TABLE_TYPE_COLUMN_NAME + " INTEGER NOT NULL, "
                + VARIABLE_TABLE_VALUE_COLUMN_NAME +" INTEGER NOT NULL );\n";

        return initialString;
    }

    public static boolean setVariable(DatabaseHelper connection, int type, int value) {
        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(VARIABLE_TABLE_VALUE_COLUMN_NAME, value);

        long res = db.update(VARIABLE_TABLE_NAME, cv, VARIABLE_TABLE_TYPE_COLUMN_NAME + "=?", new String[] { Integer.toString(type) });

        if (res == -1) {
            return false;
        }
        return true;
    }

    public static int getVariable(DatabaseHelper connection, int type) {
        int result = 0;
        SQLiteDatabase db = connection.getReadableDatabase();

        String getScript = String.format("SELECT " + VARIABLE_TABLE_VALUE_COLUMN_NAME + " FROM " + VARIABLE_TABLE_NAME + " WHERE " + VARIABLE_TABLE_TYPE_COLUMN_NAME + " = %s ORDER BY " + VARIABLE_ID_COLUMN_NAME + " DESC LIMIT 1", type);
        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            result = reader.getInt(0);
        else
            createVariable(connection, type);

        reader.close();
        return result;
    }

    private static boolean createVariable(DatabaseHelper connection, int type) {
        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(VARIABLE_TABLE_TYPE_COLUMN_NAME, type);
        cv.put(VARIABLE_TABLE_VALUE_COLUMN_NAME, 0);
        long res = db.insert(VARIABLE_TABLE_NAME, null, cv);

        if (res == -1) {
            return false;
        }
        return true;
    }
}
