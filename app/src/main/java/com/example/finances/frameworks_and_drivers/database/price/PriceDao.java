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
import java.time.format.DateTimeFormatter;
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

    public Price CreatePrice(long itemId, PriceType priceType, int additionalType, float price) {
        long newId = -1;
        Price res = null;

        switch (priceType) {
            case ShopItem:
                newId = CreateShopItemPrice(itemId, ShopItemPriceType.fromInt(additionalType), price);
            case Investment:
                newId = CreateInvestmentPrice(itemId, price);
            default:
                break;
        }

        if (newId != -1)
            res = GetPrice(newId);

        return res;
    }

    public boolean DeletePrices(long itemId, PriceType priceType) {
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

    private Price GetPrice(long id) {
        Price result = null;
        SQLiteDatabase db = priceDatabase.getReadableDatabase();

        String getScript = String.format("SELECT " +
                        "P." + PRICE_TABLE_ID_COLUMN_NAME +
                        ", P." + PRICE_TABLE_PRICE_COLUMN_NAME +
                        ", P." + PRICE_TABLE_CREATED_COLUMN_NAME +
                        ", P." + PRICE_TABLE_MODIFIED_COLUMN_NAME +
                        ", SIP." + SHOP_ITEM_PRICE_SHOP_ITEM_ID_COLUMN_NAME +
                        ", IP." + INVESTMENT_PRICE_INVESTMENT_ID_COLUMN_NAME +
                        " FROM " + PRICE_TABLE_NAME + " P " +

                " LEFT JOIN " + SHOP_ITEM_PRICE_TABLE_NAME + " SIP " +
                " ON SIP." + SHOP_ITEM_PRICE_PRICE_ID_COLUMN_NAME + " = P." + PRICE_TABLE_ID_COLUMN_NAME +

                " LEFT JOIN " + INVESTMENT_PRICE_TABLE_NAME + " IP " +
                " ON IP." + INVESTMENT_PRICE_PRICE_ID_COLUMN_NAME + " = P." + PRICE_TABLE_ID_COLUMN_NAME +

                " WHERE " + PRICE_TABLE_ID_COLUMN_NAME + " = %s "
                , id);
        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst()) {
            int col = 0;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            long priceId = reader.getLong(col++);
            float price = reader.getFloat(col++);
            LocalDate created = LocalDate.parse(reader.getString(col++), formatter);
            LocalDate modified = LocalDate.parse(reader.getString(col++), formatter);
            PriceType type = PriceType.Unknown;

            if (!reader.isNull(col++)) {
                type = PriceType.ShopItem;
            }
            else if (!reader.isNull(col)) {
                type = PriceType.Investment;
            }

            result = new Price(priceId, price, created, modified, type);
        }

        reader.close();
        return result;
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

    private long CreateShopItemPrice(long shopItemId, ShopItemPriceType type, double price) {
        SQLiteDatabase db = priceDatabase.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(PRICE_TABLE_PRICE_COLUMN_NAME, price);
        long newPriceId = db.insert(PRICE_TABLE_NAME, null, cv);

        if (newPriceId == -1) {
            return -1;
        }

        cv = new ContentValues();
        cv.put(SHOP_ITEM_PRICE_SHOP_ITEM_ID_COLUMN_NAME, shopItemId);
        cv.put(SHOP_ITEM_PRICE_PRICE_ID_COLUMN_NAME, newPriceId);
        cv.put(SHOP_ITEM_PRICE_TYPE_COLUMN_NAME, ShopItemPriceType.toInt(type));

        long res = db.insert(SHOP_ITEM_PRICE_TABLE_NAME, null, cv);

        if (res != -1)
            return newPriceId;

        else return -1;
    }

    private long CreateInvestmentPrice(long investmentId, double price) {
        SQLiteDatabase db = priceDatabase.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(PRICE_TABLE_PRICE_COLUMN_NAME, price);
        long newPriceId = db.insert(PRICE_TABLE_NAME, null, cv);

        if (newPriceId == -1) {
            return -1;
        }

        cv = new ContentValues();
        cv.put(INVESTMENT_PRICE_INVESTMENT_ID_COLUMN_NAME, investmentId);
        cv.put(INVESTMENT_PRICE_PRICE_ID_COLUMN_NAME, newPriceId);

        long res = db.insert(INVESTMENT_PRICE_TABLE_NAME, null, cv);

        if (res != -1) {
            return newPriceId;
        }

        return -1;
    }

    private boolean DeleteShopItemPrices(long itemId) {
        long res = -1;
        long[] pricesToDelete = GetShopItemPrices(itemId).stream().mapToLong(p -> p.GetId()).toArray();

        SQLiteDatabase db = priceDatabase.getWritableDatabase();

        res = db.delete(
                SHOP_ITEM_PRICE_TABLE_NAME
                , SHOP_ITEM_PRICE_SHOP_ITEM_ID_COLUMN_NAME + "=?"
                , new String[] { Long.toString(itemId) }
        );

        if (res == -1) {
            return false;
        }

        for (long priceId : pricesToDelete) {
            res = db.delete(
                    PRICE_TABLE_NAME
                    , PRICE_TABLE_ID_COLUMN_NAME + "=?"
                    , new String[] { Long.toString(priceId) }
            );

            if (res == -1) {
                return false;
            }
        }

        return true;
    }

    private boolean DeleteInvestmentPrices(long itemId) {
        long res = -1;
        long[] pricesToDelete = GetInvestmentPrices(itemId).stream().mapToLong(p -> p.GetId()).toArray();

        SQLiteDatabase db = priceDatabase.getWritableDatabase();

        res = db.delete(
                INVESTMENT_PRICE_TABLE_NAME
                , INVESTMENT_PRICE_INVESTMENT_ID_COLUMN_NAME + "=?"
                , new String[] { Long.toString(itemId) }
        );

        if (res == -1) {
            return false;
        }

        for (long priceId : pricesToDelete) {
            res = db.delete(
                    PRICE_TABLE_NAME
                    , PRICE_TABLE_ID_COLUMN_NAME + "=?"
                    , new String[] { Long.toString(priceId) }
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
                "P." + PRICE_TABLE_ID_COLUMN_NAME + ", " +
                "P." + PRICE_TABLE_PRICE_COLUMN_NAME + ", " +
                "P." + PRICE_TABLE_CREATED_COLUMN_NAME + ", " +
                "P." + PRICE_TABLE_MODIFIED_COLUMN_NAME + ", " +
                "SIP." + SHOP_ITEM_PRICE_TYPE_COLUMN_NAME +
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
                "P." + PRICE_TABLE_ID_COLUMN_NAME + ", " +
                "P." + PRICE_TABLE_PRICE_COLUMN_NAME + ", " +
                "P." + PRICE_TABLE_CREATED_COLUMN_NAME + ", " +
                "P." + PRICE_TABLE_MODIFIED_COLUMN_NAME +
                " FROM " + PRICE_TABLE_NAME  + " P " +
                " INNER JOIN " + INVESTMENT_PRICE_TABLE_NAME + " IP " +
                " ON IP." + INVESTMENT_PRICE_PRICE_ID_COLUMN_NAME + " = P." + PRICE_TABLE_ID_COLUMN_NAME +
                " AND IP." + INVESTMENT_PRICE_INVESTMENT_ID_COLUMN_NAME + " = %s", investmentId);

        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            do {
                long id = reader.getLong(0);
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
