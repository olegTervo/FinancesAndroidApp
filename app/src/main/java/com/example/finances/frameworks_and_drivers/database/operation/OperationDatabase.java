package com.example.finances.frameworks_and_drivers.database.operation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OperationDatabase extends SQLiteOpenHelper {
    public static final String OPERATION_TABLE_NAME = "Operation";
    public static final String OPERATION_TABLE_TYPE_COLUMN_NAME = "Type";
    public static final String OPERATION_TABLE_AMOUNT_COLUMN_NAME = "Amount";
    public static final String OPERATION_TABLE_CREATION_DATE_COLUMN_NAME = "CreationDate";
    public static final String OPERATION_TABLE_COMMENT_COLUMN_NAME = "Comment";
    public static final String OPERATION_ID_COLUMN_NAME = "Id";

    private static volatile OperationDatabase INSTANCE;

    public OperationDatabase(Context context) {
        super(context, "finances.db", null, 17);
    }

    public static synchronized OperationDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new OperationDatabase(context.getApplicationContext());
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
                + OPERATION_TABLE_NAME
                + " (" + OPERATION_ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + OPERATION_TABLE_TYPE_COLUMN_NAME + " INTEGER NOT NULL, "
                + OPERATION_TABLE_AMOUNT_COLUMN_NAME +" INTEGER NOT NULL, "
                + OPERATION_TABLE_COMMENT_COLUMN_NAME +" VARCHAR(65536) NOT NULL, "
                + OPERATION_TABLE_CREATION_DATE_COLUMN_NAME +" TIMESTAMP DEFAULT CURRENT_TIMESTAMP );\n";

        return initialString;
    }

    public static String DropTableString() {
        String res = "DROP TABLE " + OPERATION_TABLE_NAME + ";\n";
        return res;
    }
}
