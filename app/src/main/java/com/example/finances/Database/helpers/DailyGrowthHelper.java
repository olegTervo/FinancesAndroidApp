package com.example.finances.Database.helpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finances.Database.models.DailyGrouthDao;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DailyGrowthHelper{
    public static final String FINANCES_TABLE_NAME = "DailyGrowth";
    public static final String FINANCES_TABLE_VALUE_COLUMN_NAME = "Value";
    public static final String FINANCES_TABLE_DATE_COLUMN_NAME = "Date";
    public static final String ID_COLUMN_NAME = "Id";

    public static String CreateTableString() {
        String initialString = "CREATE TABLE "
                + FINANCES_TABLE_NAME
                + " (" + ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FINANCES_TABLE_VALUE_COLUMN_NAME + " INTEGER NOT NULL, "
                + FINANCES_TABLE_DATE_COLUMN_NAME +" TIMESTAMP DEFAULT CURRENT_TIMESTAMP );\n";

        return initialString;
    }

    public static boolean add(DatabaseHelper connection, int number) {
        int last = getTopValue(connection);
        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(FINANCES_TABLE_VALUE_COLUMN_NAME, number+last);
        long res = db.insert(FINANCES_TABLE_NAME, null, cv);

        if (res == -1) {
            return false;
        }
        return true;
    }

    public static ArrayList<DailyGrouthDao> getValues(DatabaseHelper connection) {
        SQLiteDatabase db = connection.getReadableDatabase();
        ArrayList<DailyGrouthDao> result = new ArrayList<>();

        String getScript = "SELECT "
                + ID_COLUMN_NAME + ", "
                + FINANCES_TABLE_VALUE_COLUMN_NAME + ", "
                + FINANCES_TABLE_DATE_COLUMN_NAME
                + " FROM " + FINANCES_TABLE_NAME
                + " ORDER BY " + ID_COLUMN_NAME
                + " DESC LIMIT 30";

        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            do {
                result.add(
                        new DailyGrouthDao(
                                reader.getInt(0)
                                , reader.getInt(1)
                                , LocalDate.parse(reader.getString(2), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        )
                );
            } while (reader.moveToNext());

        reader.close();
        db.close();

        return  result;
    }

    public static int getTopValue(DatabaseHelper connection) {
        int result = 0;
        SQLiteDatabase db = connection.getReadableDatabase();

        String getScript = "SELECT " + FINANCES_TABLE_VALUE_COLUMN_NAME + " FROM " + FINANCES_TABLE_NAME + " ORDER BY " + ID_COLUMN_NAME + " DESC LIMIT 1";

        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            result = reader.getInt(0);

        reader.close();
        db.close();

        return result;
    }

    public static void delete(DatabaseHelper connection) {
        SQLiteDatabase db = connection.getWritableDatabase();
        db.delete(FINANCES_TABLE_NAME, "", null);
    }

    public static boolean set(DatabaseHelper connection, int id, int value) {
        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(FINANCES_TABLE_VALUE_COLUMN_NAME, value);

        long res = db.update(FINANCES_TABLE_NAME, cv, "id=?", new String[] { Integer.toString(id) });

        if (res == -1) {
            return false;
        }
        return true;
    }
}
