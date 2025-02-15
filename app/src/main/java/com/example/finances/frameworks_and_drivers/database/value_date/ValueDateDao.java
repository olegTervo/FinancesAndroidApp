package com.example.finances.frameworks_and_drivers.database.value_date;

import static com.example.finances.frameworks_and_drivers.database.value_date.ValueDateDatabase.*;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finances.domain.enums.ValueDateType;
import com.example.finances.domain.models.DailyGrowth;
import com.example.finances.domain.models.HistoryPrice;
import com.example.finances.domain.models.ValueDate;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.inject.Inject;

public class ValueDateDao {
    private final ValueDateDatabase valueDateDatabase;

    @Inject
    public ValueDateDao(ValueDateDatabase valueDateDatabase) {
        this.valueDateDatabase = valueDateDatabase;
    }

    public boolean sync(int daily) {
        DailyGrowth top = (DailyGrowth) getFirst(ValueDateType.DailyGrowth);

        if(top != null) {
            LocalDate last = top.date;

            while(LocalDate.now().isAfter(last)) {
                last = last.plusDays(1);
                create(daily, last, ValueDateType.DailyGrowth);
            }
        }
        else
            create(0, LocalDate.now(), ValueDateType.DailyGrowth);

        return true;
    }

    public ValueDate getFirst(ValueDateType type) {
        ValueDate result = null;
        SQLiteDatabase db = valueDateDatabase.getReadableDatabase();

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
                result = new DailyGrowth(
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

    private boolean hasAny(ValueDateType type) {
        SQLiteDatabase db = valueDateDatabase.getReadableDatabase();
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

    public boolean create(float number, LocalDate date, ValueDateType type) {
        SQLiteDatabase db = valueDateDatabase.getWritableDatabase();
        ContentValues cv = new ContentValues();
        float toPut = number;

        if (type.equals(ValueDateType.DailyGrowth)){
            DailyGrowth last = (DailyGrowth) getFirst(ValueDateType.DailyGrowth);
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

    public ArrayList<ValueDate> getValues(ValueDateType type) {
        SQLiteDatabase db = valueDateDatabase.getReadableDatabase();
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
                    result.add(new DailyGrowth(
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

    public boolean increaseTopValue(float value, ValueDateType type) {
        ValueDate last = getFirst(type);

        if(last == null)
            return false;

        SQLiteDatabase db = valueDateDatabase.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(VALUE_DATE_VALUE_COLUMN_NAME, last.value + value);

        long res = db.update(VALUE_DATE_TABLE_NAME, cv, "id=?", new String[] { Integer.toString(last.getId()) });

        if (res == -1) {
            return false;
        }
        return true;
    }

    public void delete(ValueDateType type) {
        SQLiteDatabase db = valueDateDatabase.getWritableDatabase();
        db.delete(VALUE_DATE_TABLE_NAME, "type=?", new String[] { Integer.toString(ValueDateType.toInt(type)) });
    }

    public boolean update(int id, int value) {
        SQLiteDatabase db = valueDateDatabase.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(VALUE_DATE_VALUE_COLUMN_NAME, value);

        long res = db.update(VALUE_DATE_TABLE_NAME, cv, "id=?", new String[] { Integer.toString(id) });

        if (res == -1) {
            return false;
        }
        return true;
    }
}
