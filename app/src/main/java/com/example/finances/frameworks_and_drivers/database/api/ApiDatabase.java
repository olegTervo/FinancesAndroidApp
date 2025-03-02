package com.example.finances.frameworks_and_drivers.database.api;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.finances.frameworks_and_drivers.database.common.DatabaseHelper;

public class ApiDatabase extends DatabaseHelper {
    public static final String API_TABLE_NAME = "Api";
    public static final String API_TABLE_ID_COLUMN_NAME = "Id";
    public static final String API_TABLE_NAME_COLUMN_NAME = "Name";
    public static final String API_TABLE_LINK_COLUMN_NAME = "Link";
    public static final String API_TABLE_KEY_COLUMN_NAME = "Key";

    private static volatile ApiDatabase INSTANCE;

    public ApiDatabase(Context context) {
        super(context);
    }

    public static synchronized ApiDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ApiDatabase(context.getApplicationContext());
        }
        return INSTANCE;
    }

    public static String CreateTableString() {
        String initialString = "CREATE TABLE "
                + API_TABLE_NAME
                + " (" + API_TABLE_ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + API_TABLE_NAME_COLUMN_NAME +" VARCHAR(1024) NOT NULL, "
                + API_TABLE_LINK_COLUMN_NAME +" VARCHAR(1024) NOT NULL, "
                + API_TABLE_KEY_COLUMN_NAME +" VARCHAR(1024) NOT NULL"
                + ");\n";

        return initialString;
    }

    public static String DropTableString() {
        String res = "DROP TABLE " + API_TABLE_NAME + ";\n";
        return res;
    }

}
