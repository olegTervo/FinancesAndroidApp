package com.example.finances;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.finances.models.Grouth;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String FINANCES_TABLE_NAME = "DailyGrowth";
    public static final String FINANCES_TABLE_VALUE_COLUMN_NAME = "Value";
    public static final String ID_COLUMN_NAME = "Id";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "finances.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String inicialString = "CREATE TABLE "
                + FINANCES_TABLE_NAME
                + " (" + ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FINANCES_TABLE_VALUE_COLUMN_NAME + " INTEGER NOT NULL)";

        db.execSQL(inicialString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean add(int number) {
        int last = getTopValue();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(FINANCES_TABLE_VALUE_COLUMN_NAME, number+last);
        long res = db.insert(FINANCES_TABLE_NAME, null, cv);

        if (res == -1) {
            return false;
        }
        return true;
    }

    public ArrayList<Grouth> getValues() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Grouth> result = new ArrayList<>();

        String getScript = "SELECT " + ID_COLUMN_NAME + ", " + FINANCES_TABLE_VALUE_COLUMN_NAME + " FROM " + FINANCES_TABLE_NAME + " ORDER BY " + ID_COLUMN_NAME + " DESC LIMIT 30";

        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            do {
            result.add(
                    new Grouth(
                            reader.getInt(0)
                            , reader.getInt(1)
                    )
            );
            } while (reader.moveToNext());

        reader.close();
        db.close();

        return  result;
    }

    public int getTopValue() {
        int result = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String getScript = "SELECT " + FINANCES_TABLE_VALUE_COLUMN_NAME + " FROM " + FINANCES_TABLE_NAME + " ORDER BY " + ID_COLUMN_NAME + " DESC LIMIT 1";

        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            result = reader.getInt(0);

        reader.close();
        db.close();

        return result;
    }

    public void delete() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FINANCES_TABLE_NAME, "", null);
    }

    public boolean set(int id, int value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(FINANCES_TABLE_VALUE_COLUMN_NAME, value);

        long res = db.update(FINANCES_TABLE_NAME, cv, "id=?", new String[] { Integer.toString(id) });

        if (res == -1) {
            return false;
        }
        return true;
    }
}
