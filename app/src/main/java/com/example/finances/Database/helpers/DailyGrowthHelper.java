package com.example.finances.Database.helpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finances.Database.models.DailyGrowthDao;

import java.time.LocalDate;
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

    public static boolean sync(DatabaseHelper connection) {
        DailyGrowthDao top = getFirst(connection);

        if(top != null) {
            int daily = VariablesHelper.getVariable(connection, 1);
            LocalDate last = top.date;

            while(LocalDate.now().isAfter(last)) {
                last = last.plusDays(1);
                create(connection, daily, last);
            }
        }
        else
            create(connection, 0, LocalDate.now());

        return true;
    }

    private static DailyGrowthDao getFirst(DatabaseHelper connection) {
        DailyGrowthDao result = null;
        SQLiteDatabase db = connection.getReadableDatabase();

        String getScript = "SELECT "
                + ID_COLUMN_NAME + ", "
                + FINANCES_TABLE_VALUE_COLUMN_NAME + ", "
                + FINANCES_TABLE_DATE_COLUMN_NAME
                + " FROM " + FINANCES_TABLE_NAME
                + " ORDER BY " + ID_COLUMN_NAME
                + " DESC LIMIT 1";

        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            result = new DailyGrowthDao(
                    reader.getInt(0)
                    , reader.getInt(1)
                    , LocalDate.parse(reader.getString(2))
            );

        reader.close();
        db.close();

        return result;
    }

    private static boolean hasAny(DatabaseHelper connection) {
        SQLiteDatabase db = connection.getReadableDatabase();
        String getScript = "SELECT " + FINANCES_TABLE_VALUE_COLUMN_NAME + " FROM " + FINANCES_TABLE_NAME + " ORDER BY " + ID_COLUMN_NAME + " DESC LIMIT 1";
        Cursor reader = db.rawQuery(getScript, null);

        boolean result = reader.moveToFirst();
        reader.close();
        db.close();

        return result;
    }

    public static boolean create(DatabaseHelper connection, int number, LocalDate date) {
        int last = getTopValue(connection);
        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues cv = new ContentValues();

        if(date != null)
            cv.put(FINANCES_TABLE_DATE_COLUMN_NAME, date.toString());

        cv.put(FINANCES_TABLE_VALUE_COLUMN_NAME, number+last);
        long res = db.insert(FINANCES_TABLE_NAME, null, cv);

        if (res == -1) {
            return false;
        }
        return true;
    }

    public static ArrayList<DailyGrowthDao> getValues(DatabaseHelper connection) {
        SQLiteDatabase db = connection.getReadableDatabase();
        ArrayList<DailyGrowthDao> result = new ArrayList<>();

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
                        new DailyGrowthDao(
                                reader.getInt(0)
                                , reader.getInt(1)
                                , LocalDate.parse(reader.getString(2))
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

    public static boolean increaseTopValue(DatabaseHelper connection, int value) {
        int lastId = getTopId(connection);
        int lastValue = getTopValue(connection);

        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(FINANCES_TABLE_VALUE_COLUMN_NAME, lastValue + value);

        long res = db.update(FINANCES_TABLE_NAME, cv, "id=?", new String[] { Integer.toString(lastId) });

        if (res == -1) {
            return false;
        }
        return true;
    }

    public static int getTopId(DatabaseHelper connection) {
        int result = 0;
        SQLiteDatabase db = connection.getReadableDatabase();

        String getScript = "SELECT " + ID_COLUMN_NAME + " FROM " + FINANCES_TABLE_NAME + " ORDER BY " + ID_COLUMN_NAME + " DESC LIMIT 1";

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

    public static boolean update(DatabaseHelper connection, int id, int value) {
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
