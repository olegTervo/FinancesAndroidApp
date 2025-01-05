package com.example.finances.Database.helpers;

import static com.example.finances.Database.helpers.RelationsHelper.INVESTMENT_PRICE_INVESTMENT_ID_COLUMN_NAME;
import static com.example.finances.Database.helpers.RelationsHelper.INVESTMENT_PRICE_PRICE_ID_COLUMN_NAME;
import static com.example.finances.Database.helpers.RelationsHelper.INVESTMENT_PRICE_TABLE_NAME;
import static com.example.finances.Database.helpers.RelationsHelper.SHOP_ITEM_PRICE_PRICE_ID_COLUMN_NAME;
import static com.example.finances.Database.helpers.RelationsHelper.SHOP_ITEM_PRICE_SHOP_ITEM_ID_COLUMN_NAME;
import static com.example.finances.Database.helpers.RelationsHelper.SHOP_ITEM_PRICE_TABLE_NAME;
import static com.example.finances.Database.helpers.RelationsHelper.SHOP_ITEM_PRICE_TYPE_COLUMN_NAME;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finances.enums.PriceType;
import com.example.finances.enums.ShopItemPriceType;
import com.example.finances.models.Price;
import com.example.finances.models.ShopItemPrice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PriceHelper {

    public static final String PRICE_TABLE_NAME =               "Price";
    public static final String PRICE_TABLE_ID_COLUMN_NAME =       "Id";
    public static final String PRICE_TABLE_PRICE_COLUMN_NAME =      "Price";
    public static final String PRICE_TABLE_CREATED_COLUMN_NAME = "Created";
    public static final String PRICE_TABLE_MODIFIED_COLUMN_NAME = "Modified";

    public static String CreateTableString() {
        String initialString = "CREATE TABLE "
                + PRICE_TABLE_NAME
                + " (" + PRICE_TABLE_ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PRICE_TABLE_PRICE_COLUMN_NAME +" FLOAT NOT NULL, "
                + PRICE_TABLE_CREATED_COLUMN_NAME +" TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + PRICE_TABLE_MODIFIED_COLUMN_NAME +" TIMESTAMP DEFAULT CURRENT_TIMESTAMP"

                + ");\n";

        return initialString;
    }

    public static String DropTableString() {
        String res = "DROP TABLE " + PRICE_TABLE_NAME + ";\n";
        return res;
    }

    public static float GetPrice(DatabaseHelper connection, int itemId, PriceType priceType, int additionalType) {
        switch (priceType) {
            case ShopItem:
                return GetShopItemPrice(connection, itemId, ShopItemPriceType.fromInt(additionalType));
            case Investment:
                return GetInvestmentPrice(connection, itemId);
            default:
                break;
        }

        return -1;
    }

    public static List<Price> GetPrices(DatabaseHelper connection, int itemId, PriceType priceType, int additionalType) {
        switch (priceType) {
            case ShopItem:
                return GetShopItemPrices(connection, itemId);
            case Investment:
                return GetInvestmentPrices(connection, itemId);
            default:
                break;
        }

        return new ArrayList<>();
    }

    public static boolean CreatePrice(DatabaseHelper connection, long itemId, PriceType priceType, int additionalType, float price) {
        switch (priceType) {
            case ShopItem:
                return CreateShopItemPrice(connection, itemId, ShopItemPriceType.fromInt(additionalType), price);
            case Investment:
                return CreateInvestmentPrice(connection, itemId, price);
            default:
                break;
        }

        return false;
    }

    public static boolean DeletePrices(DatabaseHelper connection, int itemId, PriceType priceType){
        switch (priceType) {
            case ShopItem:
                return DeleteShopItemPrices(connection, itemId);
            case Investment:
                return DeleteInvestmentPrices(connection, itemId);
            default:
                break;
        }

        return false;
    }

    private static float GetShopItemPrice(DatabaseHelper connection, int shopItemId, ShopItemPriceType type) {
        float result = 0;
        SQLiteDatabase db = connection.getReadableDatabase();

        String getScript = String.format("SELECT " + PRICE_TABLE_PRICE_COLUMN_NAME + " FROM " + PRICE_TABLE_NAME + " P " +
                " INNER JOIN " + SHOP_ITEM_PRICE_TABLE_NAME + " SIP " +
                    " ON SIP." + SHOP_ITEM_PRICE_PRICE_ID_COLUMN_NAME + " = P." + PRICE_TABLE_ID_COLUMN_NAME +
                        " AND SIP." + SHOP_ITEM_PRICE_SHOP_ITEM_ID_COLUMN_NAME + " = %s" +
                        " AND SIP." + SHOP_ITEM_PRICE_TYPE_COLUMN_NAME + " = %s" +
                " LIMIT 1", shopItemId, ShopItemPriceType.toInt(type));
        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            result = reader.getFloat(0);

        reader.close();
        return result;
    }

    private static float GetInvestmentPrice(DatabaseHelper connection, int investmentId) {
        float result = 0;
        SQLiteDatabase db = connection.getReadableDatabase();

        String getScript = String.format("SELECT " + PRICE_TABLE_PRICE_COLUMN_NAME + " FROM " + PRICE_TABLE_NAME  + " P " +
                " INNER JOIN " + INVESTMENT_PRICE_TABLE_NAME + " IP " +
                    " ON IP." + INVESTMENT_PRICE_PRICE_ID_COLUMN_NAME + " = P." + PRICE_TABLE_ID_COLUMN_NAME +
                        " AND IP." + INVESTMENT_PRICE_INVESTMENT_ID_COLUMN_NAME + " = %s" +
                " ORDER BY P." + PRICE_TABLE_MODIFIED_COLUMN_NAME + " DESC" +
                " LIMIT 1", investmentId);
        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            result = reader.getFloat(0);

        reader.close();
        return result;
    }

    private static boolean CreateShopItemPrice(DatabaseHelper connection, long shopItemId, ShopItemPriceType type, double price) {
        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(PRICE_TABLE_PRICE_COLUMN_NAME, price);
        long res = db.insert(PRICE_TABLE_NAME, null, cv);

        if (res == -1) {
            return false;
        }

        cv = new ContentValues();
        cv.put(SHOP_ITEM_PRICE_SHOP_ITEM_ID_COLUMN_NAME, shopItemId);
        cv.put(SHOP_ITEM_PRICE_PRICE_ID_COLUMN_NAME, res);
        cv.put(SHOP_ITEM_PRICE_TYPE_COLUMN_NAME, ShopItemPriceType.toInt(type));

        res = db.insert(SHOP_ITEM_PRICE_TABLE_NAME, null, cv);

        if (res == -1) {
            return false;
        }

        return true;
    }

    private static boolean CreateInvestmentPrice(DatabaseHelper connection, long investmentId, double price) {
        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(PRICE_TABLE_PRICE_COLUMN_NAME, price);
        long res = db.insert(PRICE_TABLE_NAME, null, cv);

        if (res == -1) {
            return false;
        }

        cv = new ContentValues();
        cv.put(INVESTMENT_PRICE_INVESTMENT_ID_COLUMN_NAME, investmentId);
        cv.put(INVESTMENT_PRICE_PRICE_ID_COLUMN_NAME, res);

        res = db.insert(INVESTMENT_PRICE_TABLE_NAME, null, cv);

        if (res == -1) {
            return false;
        }

        return true;
    }

    private static boolean DeleteShopItemPrices(DatabaseHelper connection, int itemId) {
        long res = -1;
        int[] pricesToDelete = GetShopItemPrices(connection, itemId).stream().mapToInt(p -> p.GetId()).toArray();

        SQLiteDatabase db = connection.getWritableDatabase();

        res = db.delete(
                SHOP_ITEM_PRICE_TABLE_NAME
                , SHOP_ITEM_PRICE_SHOP_ITEM_ID_COLUMN_NAME + "=?"
                , new String[] { Integer.toString(itemId) }
        );

        if (res == -1) {
            return false;
        }

        for (int priceId : pricesToDelete) {
            res = db.delete(
                    PRICE_TABLE_NAME
                    , PRICE_TABLE_ID_COLUMN_NAME + "=?"
                    , new String[] { Integer.toString(priceId) }
            );

            if (res == -1) {
                return false;
            }
        }

        return true;
    }

    private static boolean DeleteInvestmentPrices(DatabaseHelper connection, int itemId) {
        long res = -1;
        int[] pricesToDelete = GetShopItemPrices(connection, itemId).stream().mapToInt(p -> p.GetId()).toArray();

        SQLiteDatabase db = connection.getWritableDatabase();

        res = db.delete(
                SHOP_ITEM_PRICE_TABLE_NAME
                , SHOP_ITEM_PRICE_SHOP_ITEM_ID_COLUMN_NAME + "=?"
                , new String[] { Integer.toString(itemId) }
        );

        if (res == -1) {
            return false;
        }

        for (int priceId : pricesToDelete) {
            res = db.delete(
                    PRICE_TABLE_NAME
                    , PRICE_TABLE_ID_COLUMN_NAME + "=?"
                    , new String[] { Integer.toString(priceId) }
            );

            if (res == -1) {
                return false;
            }
        }

        return true;
    }

    private static List<Price> GetShopItemPrices(DatabaseHelper connection, int shopItemId) {
        ArrayList<Price> result = new ArrayList<>();
        SQLiteDatabase db = connection.getReadableDatabase();

        String getScript = String.format("SELECT " +
                PRICE_TABLE_ID_COLUMN_NAME + ", " +
                PRICE_TABLE_PRICE_COLUMN_NAME + ", " +
                PRICE_TABLE_CREATED_COLUMN_NAME + ", " +
                PRICE_TABLE_MODIFIED_COLUMN_NAME + ", " +
                SHOP_ITEM_PRICE_TYPE_COLUMN_NAME +
                " FROM " + PRICE_TABLE_NAME  + " P " +
                " INNER JOIN " + SHOP_ITEM_PRICE_TABLE_NAME + " SIP " +
                    " ON SIP." + SHOP_ITEM_PRICE_PRICE_ID_COLUMN_NAME + " = P." + PRICE_TABLE_ID_COLUMN_NAME +
                        " AND SIP." + SHOP_ITEM_PRICE_SHOP_ITEM_ID_COLUMN_NAME + " = %s", shopItemId);

        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            do {
                int id = reader.getInt(0);
                float price = reader.getFloat(1);
                LocalDate created = LocalDate.parse(reader.getString(2).substring(0, 10));
                LocalDate modified = LocalDate.parse(reader.getString(3).substring(0, 10));
                ShopItemPriceType type = ShopItemPriceType.fromInt(reader.getInt(4));

                Price toAdd = new ShopItemPrice(id, price, created, modified, PriceType.ShopItem, type);

                result.add(toAdd);
            } while (reader.moveToNext());

        reader.close();
        return result;
    }

    private static List<Price> GetInvestmentPrices(DatabaseHelper connection, int investmentId) {
        ArrayList<Price> result = new ArrayList<>();
        SQLiteDatabase db = connection.getReadableDatabase();

        String getScript = String.format("SELECT " +
                    PRICE_TABLE_ID_COLUMN_NAME + ", " +
                    PRICE_TABLE_PRICE_COLUMN_NAME + ", " +
                    PRICE_TABLE_CREATED_COLUMN_NAME + ", " +
                    PRICE_TABLE_MODIFIED_COLUMN_NAME +
                " FROM " + PRICE_TABLE_NAME  + " P " +
                " INNER JOIN " + INVESTMENT_PRICE_TABLE_NAME + " IP " +
                    " ON IP." + INVESTMENT_PRICE_PRICE_ID_COLUMN_NAME + " = P." + PRICE_TABLE_ID_COLUMN_NAME +
                        " AND IP." + INVESTMENT_PRICE_INVESTMENT_ID_COLUMN_NAME + " = %s", investmentId);

        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            do {
                int id = reader.getInt(0);
                float price = reader.getFloat(1);
                LocalDate created = LocalDate.parse(reader.getString(2).substring(0, 10));
                LocalDate modified = LocalDate.parse(reader.getString(3).substring(0, 10));

                Price toAdd = new Price(id, price, created, modified, PriceType.Investment);

                result.add(toAdd);
            } while (reader.moveToNext());

        reader.close();
        return result;
    }

}
