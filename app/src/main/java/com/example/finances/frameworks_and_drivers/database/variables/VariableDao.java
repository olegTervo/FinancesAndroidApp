package com.example.finances.frameworks_and_drivers.database.variables;

import static com.example.finances.frameworks_and_drivers.database.variables.VariableDatabase.*;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import javax.inject.Inject;

public class VariableDao {
    private final VariableDatabase variableDatabase;

    @Inject
    public VariableDao(VariableDatabase variableDatabase) {
        this.variableDatabase = variableDatabase;
    }

    public boolean setVariable(int type, int value) {
        SQLiteDatabase db = variableDatabase.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(VARIABLE_TABLE_VALUE_COLUMN_NAME, value);

        long res = db.update(VARIABLE_TABLE_NAME, cv, VARIABLE_TABLE_TYPE_COLUMN_NAME + "=?", new String[] { Integer.toString(type) });

        if (res == -1) {
            return false;
        }
        return true;
    }

    public int getVariable(int type) {
        int result = 0;
        SQLiteDatabase db = variableDatabase.getReadableDatabase();

        String getScript = String.format("SELECT " + VARIABLE_TABLE_VALUE_COLUMN_NAME + " FROM " + VARIABLE_TABLE_NAME + " WHERE " + VARIABLE_TABLE_TYPE_COLUMN_NAME + " = %s ORDER BY " + VARIABLE_ID_COLUMN_NAME + " DESC LIMIT 1", type);
        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            result = reader.getInt(0);
        else
            createVariable(type);

        reader.close();
        return result;
    }

    private boolean createVariable(int type) {
        SQLiteDatabase db = variableDatabase.getWritableDatabase();
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
