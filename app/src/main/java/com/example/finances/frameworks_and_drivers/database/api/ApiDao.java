package com.example.finances.frameworks_and_drivers.database.api;

import static com.example.finances.frameworks_and_drivers.database.api.ApiDatabase.*;
import static com.example.finances.frameworks_and_drivers.database.common.RelationsHelper.*;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finances.domain.enums.ApiType;
import com.example.finances.domain.models.Api;

import javax.inject.Inject;

public class ApiDao {

    private final ApiDatabase apiDatabase;

    @Inject
    public ApiDao(ApiDatabase db) {
        this.apiDatabase = db;
    }

    public void InsertCoinMarketCapApi() {
        SQLiteDatabase db = apiDatabase.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(API_TABLE_NAME_COLUMN_NAME, "CoinMarketCap");
        cv.put(API_TABLE_LINK_COLUMN_NAME, "https://pro-api.coinmarketcap.com");
        cv.put(API_TABLE_KEY_COLUMN_NAME, "e3c851c7-d823-4530-8ee0-184174e4b847");
        db.insert(API_TABLE_NAME, null, cv);

        cv = new ContentValues();

        cv.put(API_TABLE_NAME_COLUMN_NAME, "CoinMarketCapTest");
        cv.put(API_TABLE_LINK_COLUMN_NAME, "https://sandbox-api.coinmarketcap.com");
        cv.put(API_TABLE_KEY_COLUMN_NAME, "b54bcf4d-1bca-4e8e-9a24-22ff2c3d462c");
        db.insert(API_TABLE_NAME, null, cv);
    }

    public Api GetApi(ApiType type) {
        Api result = null;
        SQLiteDatabase db = apiDatabase.getReadableDatabase();

        String getScript = String.format("SELECT "
                + API_TABLE_ID_COLUMN_NAME + ", "
                + API_TABLE_NAME_COLUMN_NAME + ", "
                + API_TABLE_LINK_COLUMN_NAME + ", "
                + API_TABLE_KEY_COLUMN_NAME
                + " FROM " + API_TABLE_NAME
                + " WHERE " + API_TABLE_ID_COLUMN_NAME + " = %s", ApiType.toInt(type));
        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst()) {
            int id = reader.getInt(0);
            String name = reader.getString(1);
            String link = reader.getString(2);
            String key = reader.getString(3);

            result = new Api(id, name, link, key);
        }

        reader.close();
        return result;
    }

    public Api GetApi(int apiId) {
        Api result = null;
        SQLiteDatabase db = apiDatabase.getReadableDatabase();

        String getScript = String.format("SELECT "
                + API_TABLE_ID_COLUMN_NAME + ", "
                + API_TABLE_NAME_COLUMN_NAME + ", "
                + API_TABLE_LINK_COLUMN_NAME + ", "
                + API_TABLE_KEY_COLUMN_NAME
                + " FROM " + API_TABLE_NAME
                + " WHERE " + API_TABLE_ID_COLUMN_NAME + " = %d", apiId);
        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst()) {
            int id = reader.getInt(0);
            String name = reader.getString(1);
            String link = reader.getString(2);
            String key = reader.getString(3);

            result = new Api(id, name, link, key);
        }

        reader.close();
        return result;
    }

    public boolean CreateInvestmentApi(ApiType type, long investmentId, String name) {
        SQLiteDatabase db = apiDatabase.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(INVESTMENT_API_INVESTMENT_ID_COLUMN_NAME, investmentId);
        cv.put(INVESTMENT_API_API_ID_COLUMN_NAME, ApiType.toInt(type));
        cv.put(INVESTMENT_API_NAME_COLUMN_NAME, name);
        long res = db.insert(INVESTMENT_API_TABLE_NAME, null, cv);

        if (res == -1) {
            return false;
        }
        return true;
    }

}
