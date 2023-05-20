package com.example.finances.Database.helpers;

import static com.example.finances.Database.helpers.PriceHelper.CreateShopItemPrice;
import static com.example.finances.Database.helpers.PriceHelper.GetShopItemPrice;
import static com.example.finances.Database.helpers.ShopHelper.SHOP_ID_COLUMN_NAME;
import static com.example.finances.Database.helpers.ShopHelper.SHOP_TABLE_NAME;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finances.Database.models.ShopItemDao;
import com.example.finances.enums.ShopItemPriceType;
import com.example.finances.models.Loan;

import java.time.LocalDate;
import java.util.ArrayList;

public class ShopItemHelper {

    public static final String SHOP_ITEM_TABLE_NAME =                  "ShopItem";
    public static final String SHOP_ITEM_ID_COLUMN_NAME =                  "Id";
    public static final String SHOP_ITEM_TABLE_NAME_COLUMN_NAME =          "Name";
    public static final String SHOP_ITEM_TABLE_AMOUNT_COLUMN_NAME =        "Amount";
    public static final String SHOP_ITEM_TABLE_SHOP_COLUMN_NAME =          "ShopId";

    public static String CreateTableString() {
        String initialString = "CREATE TABLE "
                + SHOP_ITEM_TABLE_NAME
                + " (" + SHOP_ITEM_ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SHOP_ITEM_TABLE_NAME_COLUMN_NAME +" VARCHAR(1024) NOT NULL, "
                + SHOP_ITEM_TABLE_AMOUNT_COLUMN_NAME +" INTEGER NOT NULL DEFAULT 0, "
                + SHOP_ITEM_TABLE_SHOP_COLUMN_NAME +" INTEGER NOT NULL, "

                + "CONSTRAINT shopItem_shop_fk FOREIGN KEY (" + SHOP_ITEM_TABLE_SHOP_COLUMN_NAME + ") "
                + "REFERENCES " + SHOP_TABLE_NAME + " (" + SHOP_ID_COLUMN_NAME + ")"

                + ");\n";

        return initialString;
    }

    public static String DropTableString() {
        String res = "DROP TABLE " + SHOP_ITEM_TABLE_NAME + ";\n";
        return res;
    }

    public static int GetShopItemNumber(DatabaseHelper connection, String name) {
        int res = -1;
        SQLiteDatabase db = connection.getReadableDatabase();

        String getScript = String.format("SELECT " + SHOP_ITEM_ID_COLUMN_NAME + " FROM " + SHOP_ITEM_TABLE_NAME + " WHERE " + SHOP_ITEM_TABLE_NAME_COLUMN_NAME + " = %s", name);
        Cursor reader = db.rawQuery(getScript, null);

        if(reader.moveToFirst())
            res = reader.getInt(0);

        reader.close();
        return res;
    }

    public static ArrayList<ShopItemDao> GetShopItems(DatabaseHelper connection, int shopId) {
        ArrayList<ShopItemDao> result = new ArrayList<>();
        SQLiteDatabase db = connection.getReadableDatabase();

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
                ShopItemDao item = new ShopItemDao(
                        reader.getInt(0)
                        , reader.getString(1)
                        , reader.getInt(2)
                );
                result.add(item);
            } while (reader.moveToNext());

        reader.close();

        for(ShopItemDao item : result) {
            item.SetPrice(ShopItemPriceType.BuyPrice, GetShopItemPrice(connection, item.id, ShopItemPriceType.BuyPrice));
            item.SetPrice(ShopItemPriceType.SellPrice, GetShopItemPrice(connection, item.id, ShopItemPriceType.SellPrice));
        }

        return result;
    }

    public static boolean CreateItem(DatabaseHelper connection, String name, int shopId, double buyPrice, double sellPrice) {
        long res = -1;

        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(SHOP_ITEM_TABLE_NAME_COLUMN_NAME, name);
        cv.put(SHOP_ITEM_TABLE_SHOP_COLUMN_NAME, shopId);
        res = db.insert(SHOP_TABLE_NAME, null, cv);

        if (res == -1) {
            return false;
        }
        return CreateShopItemPrice(connection, GetShopItemNumber(connection, name), buyPrice, sellPrice);
    }

    public static int GetItemAmount(DatabaseHelper connection, int id) {
        int result = 0;
        SQLiteDatabase db = connection.getReadableDatabase();

        String getScript = String.format("SELECT " + SHOP_ITEM_TABLE_AMOUNT_COLUMN_NAME + " FROM " + SHOP_TABLE_NAME + " WHERE " + SHOP_ITEM_ID_COLUMN_NAME + " = %s", id);
        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            result = reader.getInt(0);

        reader.close();
        return result;
    }

    public static boolean AddItems(DatabaseHelper connection, int id, int amount) {
        long res = -1;
        int current = GetItemAmount(connection, id);

        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(SHOP_ITEM_TABLE_AMOUNT_COLUMN_NAME, current+amount);
        res = db.update(SHOP_TABLE_NAME, cv, "id=?", new String[] { Integer.toString(id) });

        if (res == -1) {
            return false;
        }
        return true;
    }
}
