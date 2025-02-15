package com.example.finances.frameworks_and_drivers.database.shop;

import static com.example.finances.frameworks_and_drivers.database.shop.ShopDatabase.*;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import javax.inject.Inject;

public class ShopDao {
    private final ShopDatabase shopDatabase;

    @Inject
    public ShopDao(ShopDatabase shopDatabase) {
        this.shopDatabase = shopDatabase;
    }

    public long GetShopId(String name) {
        long res = -1;
        SQLiteDatabase db = shopDatabase.getReadableDatabase();

        String getScript = String.format("SELECT " + SHOP_ID_COLUMN_NAME + " FROM " + SHOP_TABLE_NAME + " WHERE " + SHOP_TABLE_NAME_COLUMN_NAME + " = '%s'", name);
        Cursor reader = db.rawQuery(getScript, null);

        if(reader.moveToFirst())
            res = reader.getLong(0);

        reader.close();

        return res;
    }

    public long OpenShop(String name, int accountNumber) {
        long res = -1;
        SQLiteDatabase db = shopDatabase.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(SHOP_TABLE_NAME_COLUMN_NAME, name);
        cv.put(SHOP_TABLE_BANK_ACCOUNT_COLUMN_NAME, accountNumber);
        res = db.insert(SHOP_TABLE_NAME, null, cv);

        return res;
    }

}
