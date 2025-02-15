package com.example.finances.frameworks_and_drivers.database.shop;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ShopItemDatabase extends SQLiteOpenHelper {

    public static final String SHOP_ITEM_TABLE_NAME = "ShopItem";
    public static final String SHOP_ITEM_ID_COLUMN_NAME = "Id";
    public static final String SHOP_ITEM_TABLE_NAME_COLUMN_NAME = "Name";
    public static final String SHOP_ITEM_TABLE_AMOUNT_COLUMN_NAME = "Amount";
    public static final String SHOP_ITEM_TABLE_SHOP_COLUMN_NAME = "ShopId";

    private static volatile ShopItemDatabase INSTANCE;

    public ShopItemDatabase(Context context) {
        super(context, "finances.db", null, 17);
    }

    public static synchronized ShopItemDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ShopItemDatabase(context.getApplicationContext());
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreateTableString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 17)
            return;

        if (oldVersion == 17 && newVersion == 18) {
            //do update
        }
    }

    public static String CreateTableString() {
        String initialString = "CREATE TABLE "
                + SHOP_ITEM_TABLE_NAME
                + " (" + SHOP_ITEM_ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SHOP_ITEM_TABLE_NAME_COLUMN_NAME +" VARCHAR(1024) NOT NULL, "
                + SHOP_ITEM_TABLE_AMOUNT_COLUMN_NAME +" INTEGER NOT NULL DEFAULT 0, "
                + SHOP_ITEM_TABLE_SHOP_COLUMN_NAME +" INTEGER NOT NULL, "

                + "CONSTRAINT shopItem_shop_fk FOREIGN KEY (" + SHOP_ITEM_TABLE_SHOP_COLUMN_NAME + ") "
                + "REFERENCES " + ShopDatabase.SHOP_TABLE_NAME + " (" + ShopDatabase.SHOP_ID_COLUMN_NAME + ")"

                + ");\n";

        return initialString;
    }

    public static String DropTableString() {
        String res = "DROP TABLE " + SHOP_ITEM_TABLE_NAME + ";\n";
        return res;
    }
}
