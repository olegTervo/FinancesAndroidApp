package com.example.finances.Database.helpers;

import static com.example.finances.Database.helpers.ShopHelper.SHOP_TABLE_NAME;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finances.enums.ItemType;
import com.example.finances.enums.ShopItemPriceType;

public class PriceHelper {

    public static final String PRICE_TABLE_NAME =               "Price";
    public static final String PRICE_TABLE_ITEM_COLUMN_NAME =       "ItemId";
    public static final String PRICE_TABLE_ITEM_TYPE_COLUMN_NAME =  "ItemType";
    public static final String PRICE_TABLE_TYPE_COLUMN_NAME =       "Type";
    public static final String PRICE_TABLE_PRICE_COLUMN_NAME =      "Price";

    public static String CreateTableString() {
        String initialString = "CREATE TABLE "
                + PRICE_TABLE_NAME
                + " (" + PRICE_TABLE_ITEM_COLUMN_NAME + " INTEGER NOT NULL, "
                + PRICE_TABLE_ITEM_TYPE_COLUMN_NAME + " INTEGER NOT NULL, "
                + PRICE_TABLE_TYPE_COLUMN_NAME + " INTEGER NOT NULL, "
                + PRICE_TABLE_PRICE_COLUMN_NAME +" FLOAT NOT NULL, "

                + "PRIMARY KEY (" + PRICE_TABLE_ITEM_COLUMN_NAME + ", " + PRICE_TABLE_ITEM_TYPE_COLUMN_NAME + ", " + PRICE_TABLE_TYPE_COLUMN_NAME + ")"

                + ");\n";

        return initialString;
    }

    public static String DropTableString() {
        String res = "DROP TABLE " + PRICE_TABLE_NAME + ";\n";
        return res;
    }

    public static double GetShopItemPrice(DatabaseHelper connection, int itemId, ShopItemPriceType type) {
        return GetPrice(connection, itemId, ItemType.ShopItem, ShopItemPriceType.toInt(type));
    }

    public static double GetPrice(DatabaseHelper connection, int itemId, ItemType itemType, int priceType) {
        double result = 0;
        SQLiteDatabase db = connection.getReadableDatabase();

        String getScript = String.format("SELECT " + PRICE_TABLE_PRICE_COLUMN_NAME + " FROM " + PRICE_TABLE_NAME +
                " WHERE " + PRICE_TABLE_ITEM_COLUMN_NAME + " = %s AND " +
                PRICE_TABLE_ITEM_TYPE_COLUMN_NAME + " = %s AND " +
                PRICE_TABLE_TYPE_COLUMN_NAME + " = %s " +
                "LIMIT 1", itemId, ItemType.toInt(itemType), priceType);
        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            result = reader.getInt(0);

        reader.close();
        return result;
    }

    public static boolean CreateShopItemPrice(DatabaseHelper connection, int itemId, double buyPrice, double sellPrice) {
        boolean res = false;

        res = CreateItemPrice(connection, itemId, ItemType.ShopItem, ShopItemPriceType.toInt(ShopItemPriceType.BuyPrice), buyPrice);

        if(res)
            res = CreateItemPrice(connection, itemId, ItemType.ShopItem, ShopItemPriceType.toInt(ShopItemPriceType.SellPrice), sellPrice);

        return res;
    }

    private static boolean CreateItemPrice(DatabaseHelper connection, int itemId, ItemType itemType, int priceType, double price) {
        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(PRICE_TABLE_ITEM_COLUMN_NAME, itemId);
        cv.put(PRICE_TABLE_ITEM_TYPE_COLUMN_NAME, ItemType.toInt(itemType));
        cv.put(PRICE_TABLE_TYPE_COLUMN_NAME, priceType);
        cv.put(PRICE_TABLE_PRICE_COLUMN_NAME, price);
        long res = db.insert(PRICE_TABLE_NAME, null, cv);

        if (res == -1) {
            return false;
        }
        return true;
    }

    public static boolean DeleteItemPrices(DatabaseHelper connection, int itemId, ItemType itemType){
        long res = -1;

        SQLiteDatabase db = connection.getWritableDatabase();

        res = db.delete(
                PRICE_TABLE_NAME
                , PRICE_TABLE_ITEM_COLUMN_NAME + "=? AND " + PRICE_TABLE_ITEM_TYPE_COLUMN_NAME + "=?"
                , new String[] { Integer.toString(itemId), Integer.toString(ItemType.toInt(itemType)) }
        );

        if (res == -1) {
            return false;
        }
        return true;
    }
}
