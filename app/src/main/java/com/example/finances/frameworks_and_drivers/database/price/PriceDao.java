package com.example.finances.frameworks_and_drivers.database.price;

import static com.example.finances.frameworks_and_drivers.database.common.RelationsHelper.*;
import static com.example.finances.frameworks_and_drivers.database.price.PriceDatabase.*;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finances.domain.enums.PriceType;
import com.example.finances.domain.enums.ShopItemPriceType;
import com.example.finances.domain.models.Price;
import com.example.finances.domain.models.ShopItemPrice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PriceDao {
    private final PriceDatabase priceDatabase;

    @Inject
    public PriceDao(PriceDatabase db) {
        this.priceDatabase = db;
    }

    public float GetPrice(long itemId, PriceType priceType, int additionalType) {
        switch (priceType) {
            case ShopItem:
                return GetShopItemPrice(itemId, ShopItemPriceType.fromInt(additionalType));
            case Investment:
                return GetInvestmentPrice(itemId);
            default:
                break;
        }

        return -1;
    }

    public List<Price> GetPrices(long itemId, PriceType priceType, int additionalType) {
        switch (priceType) {
            case ShopItem:
                return GetShopItemPrices(itemId);
            case Investment:
                return GetInvestmentPrices(itemId);
            default:
                break;
        }

        return new ArrayList<>();
    }

    public boolean CreatePrice(long itemId, PriceType priceType, int additionalType, float price) {
        switch (priceType) {
            case ShopItem:
                return CreateShopItemPrice(itemId, ShopItemPriceType.fromInt(additionalType), price);
            case Investment:
                return CreateInvestmentPrice(itemId, price);
            default:
                break;
        }

        return false;
    }

    public boolean DeletePrices(int itemId, PriceType priceType) {
        switch (priceType) {
            case ShopItem:
                return DeleteShopItemPrices(itemId);
            case Investment:
                return DeleteInvestmentPrices(itemId);
            default:
                break;
        }

        return false;
    }

    private float GetShopItemPrice(long shopItemId, ShopItemPriceType type) {
        float result = 0;
        SQLiteDatabase db = priceDatabase.getReadableDatabase();

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

    private float GetInvestmentPrice(long investmentId) {
        float result = 0;
        SQLiteDatabase db = priceDatabase.getReadableDatabase();

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

    private boolean CreateShopItemPrice(long shopItemId, ShopItemPriceType type, double price) {
        SQLiteDatabase db = priceDatabase.getWritableDatabase();
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

    private boolean CreateInvestmentPrice(long investmentId, double price) {
        SQLiteDatabase db = priceDatabase.getWritableDatabase();
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

    private boolean DeleteShopItemPrices(int itemId) {
        long res = -1;
        int[] pricesToDelete = GetShopItemPrices(itemId).stream().mapToInt(p -> p.GetId()).toArray();

        SQLiteDatabase db = priceDatabase.getWritableDatabase();

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

    private boolean DeleteInvestmentPrices(int itemId) {
        long res = -1;
        int[] pricesToDelete = GetShopItemPrices(itemId).stream().mapToInt(p -> p.GetId()).toArray();

        SQLiteDatabase db = priceDatabase.getWritableDatabase();

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

    private List<Price> GetShopItemPrices(long shopItemId) {
        ArrayList<Price> result = new ArrayList<>();
        SQLiteDatabase db = priceDatabase.getReadableDatabase();

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

    private List<Price> GetInvestmentPrices(long investmentId) {
        ArrayList<Price> result = new ArrayList<>();
        SQLiteDatabase db = priceDatabase.getReadableDatabase();

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
