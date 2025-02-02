package com.example.finances.infrastructure.Database.helpers;

import static com.example.finances.infrastructure.Database.helpers.RelationsHelper.ACCOUNT_OPERATIONS_ACCOUNT_ID_COLUMN_NAME;
import static com.example.finances.infrastructure.Database.helpers.RelationsHelper.ACCOUNT_OPERATIONS_OPERATION_ID_COLUMN_NAME;
import static com.example.finances.infrastructure.Database.helpers.RelationsHelper.ACCOUNT_OPERATIONS_TABLE_NAME;
import static com.example.finances.infrastructure.Database.helpers.RelationsHelper.LOAN_OPERATIONS_LOAN_ID_COLUMN_NAME;
import static com.example.finances.infrastructure.Database.helpers.RelationsHelper.LOAN_OPERATIONS_OPERATION_ID_COLUMN_NAME;
import static com.example.finances.infrastructure.Database.helpers.RelationsHelper.LOAN_OPERATIONS_TABLE_NAME;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finances.infrastructure.Database.models.OperationDao;

import java.time.LocalDate;
import java.util.ArrayList;

public class OperationHelper {
    public static final String OPERATION_TABLE_NAME = "Operation";
    public static final String OPERATION_TABLE_TYPE_COLUMN_NAME = "Type";
    public static final String OPERATION_TABLE_AMOUNT_COLUMN_NAME = "Amount";
    public static final String OPERATION_TABLE_CREATION_DATE_COLUMN_NAME = "CreationDate";
    public static final String OPERATION_TABLE_COMMENT_COLUMN_NAME = "Comment";
    public static final String OPERATION_ID_COLUMN_NAME = "Id";

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

    public static long CreateOperationForAccount(DatabaseHelper connection, int account, int type, int amount, String info) {
        long result = CreateOperation(connection, type, amount, info);

        if(result == -1)
            return result;

        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ACCOUNT_OPERATIONS_ACCOUNT_ID_COLUMN_NAME, account);
        cv.put(ACCOUNT_OPERATIONS_OPERATION_ID_COLUMN_NAME, result);

        db.insert(ACCOUNT_OPERATIONS_TABLE_NAME, null, cv);

        return result;
    }

    public static long CreateOperation(DatabaseHelper connection, int type, int amount, String info) {
        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(OPERATION_TABLE_TYPE_COLUMN_NAME, type);
        cv.put(OPERATION_TABLE_AMOUNT_COLUMN_NAME, amount);
        cv.put(OPERATION_TABLE_COMMENT_COLUMN_NAME, info);

        long res = db.insert(OPERATION_TABLE_NAME, null, cv);

        return res;
    }

    public static ArrayList<OperationDao> GetOperations(DatabaseHelper connection) {
        ArrayList<OperationDao> result = new ArrayList<>();

        SQLiteDatabase db = connection.getReadableDatabase();

        String getScript = "SELECT "
                + OPERATION_ID_COLUMN_NAME + ", "
                + OPERATION_TABLE_TYPE_COLUMN_NAME + ", "
                + OPERATION_TABLE_AMOUNT_COLUMN_NAME + ", "
                + OPERATION_TABLE_COMMENT_COLUMN_NAME + ", "
                + OPERATION_TABLE_CREATION_DATE_COLUMN_NAME
                + " FROM " + OPERATION_TABLE_NAME
                + " ORDER BY " + OPERATION_ID_COLUMN_NAME
                + " DESC LIMIT 100";

        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            do {
                result.add(
                        new OperationDao(
                                reader.getInt(0)
                                , reader.getInt(1)
                                , reader.getInt(2)
                                , reader.getString(3)
                                , LocalDate.parse(reader.getString(4).substring(0, 10))
                        )
                );
            } while (reader.moveToNext());

        reader.close();
        return result;
    }

    public static ArrayList<OperationDao> GetAccountOperations(DatabaseHelper connection, int account) {
        if(account == 0)
            return GetOperations(connection);

        ArrayList<OperationDao> result = new ArrayList<>();

        SQLiteDatabase db = connection.getReadableDatabase();

        String getScript = "SELECT "
                + OPERATION_TABLE_NAME + "." + OPERATION_ID_COLUMN_NAME + ", "
                + OPERATION_TABLE_TYPE_COLUMN_NAME + ", "
                + OPERATION_TABLE_AMOUNT_COLUMN_NAME + ", "
                + OPERATION_TABLE_COMMENT_COLUMN_NAME + ", "
                + OPERATION_TABLE_CREATION_DATE_COLUMN_NAME
                + " FROM " + OPERATION_TABLE_NAME
                + " INNER JOIN " + ACCOUNT_OPERATIONS_TABLE_NAME + " ON "
                    + ACCOUNT_OPERATIONS_TABLE_NAME + "." + ACCOUNT_OPERATIONS_OPERATION_ID_COLUMN_NAME + " = " + OPERATION_TABLE_NAME + "." + OPERATION_ID_COLUMN_NAME
                + " WHERE " + ACCOUNT_OPERATIONS_TABLE_NAME + "." + ACCOUNT_OPERATIONS_ACCOUNT_ID_COLUMN_NAME + " = " + account
                + " ORDER BY " + OPERATION_TABLE_NAME + "." + OPERATION_ID_COLUMN_NAME
                + " DESC LIMIT 100";

        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            do {
                result.add(
                        new OperationDao(
                                reader.getInt(0)
                                , reader.getInt(1)
                                , reader.getInt(2)
                                , reader.getString(3)
                                , LocalDate.parse(reader.getString(4).substring(0, 10))
                        )
                );
            } while (reader.moveToNext());

        reader.close();
        return result;
    }

    public static ArrayList<OperationDao> GetLoanOperations(DatabaseHelper connection, int id) {
        ArrayList<OperationDao> result = new ArrayList<>();

        SQLiteDatabase db = connection.getReadableDatabase();

        String getScript = "SELECT "
                + OPERATION_TABLE_NAME + "." + OPERATION_ID_COLUMN_NAME + ", "
                + OPERATION_TABLE_TYPE_COLUMN_NAME + ", "
                + OPERATION_TABLE_AMOUNT_COLUMN_NAME + ", "
                + OPERATION_TABLE_COMMENT_COLUMN_NAME + ", "
                + OPERATION_TABLE_CREATION_DATE_COLUMN_NAME
                + " FROM " + OPERATION_TABLE_NAME
                + " INNER JOIN " + LOAN_OPERATIONS_TABLE_NAME + " ON "
                + LOAN_OPERATIONS_TABLE_NAME + "." + LOAN_OPERATIONS_OPERATION_ID_COLUMN_NAME + " = " + OPERATION_TABLE_NAME + "." + OPERATION_ID_COLUMN_NAME
                + " WHERE " + LOAN_OPERATIONS_TABLE_NAME + "." + LOAN_OPERATIONS_LOAN_ID_COLUMN_NAME + " = " + id
                + " ORDER BY " + OPERATION_TABLE_NAME + "." + OPERATION_ID_COLUMN_NAME
                + " DESC LIMIT 100";

        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            do {
                result.add(
                        new OperationDao(
                                reader.getInt(0)
                                , reader.getInt(1)
                                , reader.getInt(2)
                                , reader.getString(3)
                                , LocalDate.parse(reader.getString(4).substring(0, 10))
                        )
                );
            } while (reader.moveToNext());

        reader.close();
        return result;
    }
}
