package com.example.finances.frameworks_and_drivers.database.value_date;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finances.frameworks_and_drivers.database.common.DatabaseHelper;
import com.example.finances.frameworks_and_drivers.database.daily_growth.DailyGrowthHelper;
import com.example.finances.frameworks_and_drivers.database.variables.VariablesHelper;
import com.example.finances.frameworks_and_drivers.database.daily_growth.DailyGrowthDao;
import com.example.finances.domain.enums.ValueDateType;
import com.example.finances.domain.models.HistoryPrice;
import com.example.finances.domain.models.ValueDate;

import java.time.LocalDate;
import java.util.ArrayList;

public class ValueDateHelper {


    public static final String VALUE_DATE_TABLE_NAME = "ValueDate";
    public static final String VALUE_DATE_ID_COLUMN_NAME = "Id";
    public static final String VALUE_DATE_TYPE_COLUMN_NAME = "Type";
    public static final String VALUE_DATE_VALUE_COLUMN_NAME = "Value";
    public static final String VALUE_DATE_DATE_COLUMN_NAME = "Date";

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

    public static void MigrateDailyGrowthTable(SQLiteDatabase db) {
        String res = "INSERT INTO " + VALUE_DATE_TABLE_NAME +
                " (" + VALUE_DATE_TYPE_COLUMN_NAME + ", " +
                    VALUE_DATE_VALUE_COLUMN_NAME + ", " +
                    VALUE_DATE_DATE_COLUMN_NAME + ")" +
                " SELECT " + ValueDateType.toInt(ValueDateType.DailyGrowth) + ", " +
                    DailyGrowthHelper.FINANCES_TABLE_VALUE_COLUMN_NAME + ", " +
                    DailyGrowthHelper.FINANCES_TABLE_DATE_COLUMN_NAME +
                " FROM " + DailyGrowthHelper.FINANCES_TABLE_NAME + ";";
        db.execSQL(res);
    }

    public static boolean sync(DatabaseHelper connection) {
        DailyGrowthDao top = (DailyGrowthDao) getFirst(connection, ValueDateType.DailyGrowth);

        if(top != null) {
            int daily = VariablesHelper.getVariable(connection, 1);
            LocalDate last = top.date;

            while(LocalDate.now().isAfter(last)) {
                last = last.plusDays(1);
                create(connection, daily, last, ValueDateType.DailyGrowth);
            }
        }
        else
            create(connection, 0, LocalDate.now(), ValueDateType.DailyGrowth);

        return true;
    }

    public static ValueDate getFirst(DatabaseHelper connection, ValueDateType type) {
        ValueDate result = null;
        SQLiteDatabase db = connection.getReadableDatabase();

        String getScript = "SELECT "
                + VALUE_DATE_ID_COLUMN_NAME + ", "
                + VALUE_DATE_VALUE_COLUMN_NAME + ", "
                + VALUE_DATE_DATE_COLUMN_NAME
                + " FROM " + VALUE_DATE_TABLE_NAME
                + " WHERE " + VALUE_DATE_TYPE_COLUMN_NAME + " = " + ValueDateType.toInt(type)
                + " ORDER BY " + VALUE_DATE_ID_COLUMN_NAME
                + " DESC LIMIT 1";

        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            if (type.equals(ValueDateType.DailyGrowth))
                result = new DailyGrowthDao(
                        reader.getInt(0)
                        , reader.getFloat(1)
                        , LocalDate.parse(reader.getString(2))
                );
            else
                result = new HistoryPrice(
                        reader.getInt(0)
                        , reader.getFloat(1)
                        , LocalDate.parse(reader.getString(2))
                );

        reader.close();
        return result;
    }

    private static boolean hasAny(DatabaseHelper connection, ValueDateType type) {
        SQLiteDatabase db = connection.getReadableDatabase();
        String getScript = "SELECT " + VALUE_DATE_VALUE_COLUMN_NAME +
                " FROM " + VALUE_DATE_TABLE_NAME +
                " WHERE " + VALUE_DATE_TYPE_COLUMN_NAME + " = " + ValueDateType.toInt(type) +
                " ORDER BY " + VALUE_DATE_ID_COLUMN_NAME +
                " DESC LIMIT 1";
        Cursor reader = db.rawQuery(getScript, null);

        boolean result = reader.moveToFirst();

        reader.close();
        return result;
    }

    public static boolean create(DatabaseHelper connection, float number, LocalDate date, ValueDateType type) {
        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues cv = new ContentValues();
        float toPut = number;

        if (type.equals(ValueDateType.DailyGrowth)){
            DailyGrowthDao last = (DailyGrowthDao) ValueDateHelper.getFirst(connection, ValueDateType.DailyGrowth);
            float lastVal = 0;
            if (last != null) lastVal = last.value;

            toPut = number+lastVal;
        }

        if(date != null)
            cv.put(VALUE_DATE_DATE_COLUMN_NAME, date.toString());
        cv.put(VALUE_DATE_VALUE_COLUMN_NAME, toPut);
        cv.put(VALUE_DATE_TYPE_COLUMN_NAME, ValueDateType.toInt(type));
        long res = db.insert(VALUE_DATE_TABLE_NAME, null, cv);

        if (res == -1) {
            return false;
        }
        return true;
    }

    public static ArrayList<ValueDate> getValues(DatabaseHelper connection, ValueDateType type) {
        SQLiteDatabase db = connection.getReadableDatabase();
        ArrayList<ValueDate> result = new ArrayList<>();

        String getScript = "SELECT "
                + VALUE_DATE_ID_COLUMN_NAME + ", "
                + VALUE_DATE_VALUE_COLUMN_NAME + ", "
                + VALUE_DATE_DATE_COLUMN_NAME
                + " FROM " + VALUE_DATE_TABLE_NAME
                + " WHERE " + VALUE_DATE_TYPE_COLUMN_NAME + " = " + ValueDateType.toInt(type)
                + " ORDER BY " + VALUE_DATE_ID_COLUMN_NAME
                + " DESC LIMIT 1000";

        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            do {
                if (type.equals(ValueDateType.DailyGrowth))
                    result.add(new DailyGrowthDao(
                            reader.getInt(0)
                            , reader.getFloat(1)
                            , LocalDate.parse(reader.getString(2))
                    ));
                else
                    result.add(new HistoryPrice(
                            reader.getInt(0)
                            , reader.getFloat(1)
                            , LocalDate.parse(reader.getString(2))
                    ));
            } while (reader.moveToNext());

        reader.close();
        return result;
    }

    public static boolean increaseTopValue(DatabaseHelper connection, float value, ValueDateType type) {
        ValueDate last = getFirst(connection, type);

        if(last == null)
            return false;

        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(VALUE_DATE_VALUE_COLUMN_NAME, last.value + value);

        long res = db.update(VALUE_DATE_TABLE_NAME, cv, "id=?", new String[] { Integer.toString(last.getId()) });

        if (res == -1) {
            return false;
        }
        return true;
    }

    public static void delete(DatabaseHelper connection, ValueDateType type) {
        SQLiteDatabase db = connection.getWritableDatabase();
        db.delete(VALUE_DATE_TABLE_NAME, "type=?", new String[] { Integer.toString(ValueDateType.toInt(type)) });
    }

    public static boolean update(DatabaseHelper connection, int id, int value) {
        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(VALUE_DATE_VALUE_COLUMN_NAME, value);

        long res = db.update(VALUE_DATE_TABLE_NAME, cv, "id=?", new String[] { Integer.toString(id) });

        if (res == -1) {
            return false;
        }
        return true;
    }

}
