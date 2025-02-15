package com.example.finances.frameworks_and_drivers.database.shop;

import static com.example.finances.frameworks_and_drivers.database.shop.ShopItemDatabase.*;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finances.domain.enums.PriceType;
import com.example.finances.domain.enums.ShopItemPriceType;
import com.example.finances.domain.models.ShopItem;

import java.util.ArrayList;

import javax.inject.Inject;

public class ShopItemDao {
    private final ShopItemDatabase shopItemDatabase;

    @Inject
    public ShopItemDao(ShopItemDatabase shopItemDatabase) {
        this.shopItemDatabase = shopItemDatabase;
    }

    public int GetShopItemNumber(String name) {
        int res = -1;
        SQLiteDatabase db = shopItemDatabase.getReadableDatabase();

        String getScript = String.format("SELECT " + SHOP_ITEM_ID_COLUMN_NAME + " FROM " + SHOP_ITEM_TABLE_NAME + " WHERE " + SHOP_ITEM_TABLE_NAME_COLUMN_NAME + " = '%s'", name);
        Cursor reader = db.rawQuery(getScript, null);

        if(reader.moveToFirst())
            res = reader.getInt(0);

        reader.close();
        return res;
    }

    public ArrayList<ShopItem> GetShopItems(long shopId) {
        ArrayList<ShopItem> result = new ArrayList<>();
        SQLiteDatabase db = shopItemDatabase.getReadableDatabase();

        String getScript = "SELECT "
                + SHOP_ITEM_ID_COLUMN_NAME + ", "
                + SHOP_ITEM_TABLE_NAME_COLUMN_NAME + ", "
                + SHOP_ITEM_TABLE_AMOUNT_COLUMN_NAME
                + " FROM " + SHOP_ITEM_TABLE_NAME
                + " ORDER BY " + SHOP_ITEM_ID_COLUMN_NAME
                + " DESC";

        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            do {
                ShopItem item = new ShopItem(
                        reader.getInt(0)
                        , reader.getString(1)
                        , reader.getInt(2)
                );
                result.add(item);
            } while (reader.moveToNext());

        reader.close();

        return result;
    }

    public boolean CreateItem(String name, long shopId) {
        long res = -1;

        SQLiteDatabase db = shopItemDatabase.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(SHOP_ITEM_TABLE_NAME_COLUMN_NAME, name);
        cv.put(SHOP_ITEM_TABLE_SHOP_COLUMN_NAME, shopId);
        res = db.insert(SHOP_ITEM_TABLE_NAME, null, cv);

        if (res == -1) {
            return false;
        }

        return true;
    }

    public int GetItemAmount(long id) {
        int result = 0;
        SQLiteDatabase db = shopItemDatabase.getReadableDatabase();

        String getScript = String.format("SELECT " + SHOP_ITEM_TABLE_AMOUNT_COLUMN_NAME + " FROM " + SHOP_ITEM_TABLE_NAME + " WHERE " + SHOP_ITEM_ID_COLUMN_NAME + " = %s", id);
        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            result = reader.getInt(0);

        reader.close();
        return result;
    }

    public boolean AddItems(long id, int amount) {
        long res = -1;
        int current = GetItemAmount(id);

        SQLiteDatabase db = shopItemDatabase.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(SHOP_ITEM_TABLE_AMOUNT_COLUMN_NAME, current+amount);
        res = db.update(SHOP_ITEM_TABLE_NAME, cv, SHOP_ITEM_ID_COLUMN_NAME + "=?", new String[] { Long.toString(id) });

        if (res == -1) {
            return false;
        }
        return true;
    }

    public boolean DeleteItem(int id) {
        long res = -1;

        SQLiteDatabase db = shopItemDatabase.getWritableDatabase();
        res = db.delete(SHOP_ITEM_TABLE_NAME, SHOP_ITEM_ID_COLUMN_NAME + "=?", new String[]{Integer.toString(id)});

        if (res == -1) {
            return false;
        }
        return true;
    }
}
