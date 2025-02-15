package com.example.finances.frameworks_and_drivers.database.operation;

import static com.example.finances.frameworks_and_drivers.database.common.RelationsHelper.*;
import static com.example.finances.frameworks_and_drivers.database.operation.OperationDatabase.*;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finances.domain.models.Operation;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.inject.Inject;

public class OperationDao {
    private final OperationDatabase operationDatabase;

    @Inject
    public OperationDao(OperationDatabase db) {
        this.operationDatabase = db;
    }

    public long CreateOperationForAccount(int account, int type, int amount, String info) {
        long result = CreateOperation(type, amount, info);

        if(result == -1)
            return result;

        SQLiteDatabase db = operationDatabase.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ACCOUNT_OPERATIONS_ACCOUNT_ID_COLUMN_NAME, account);
        cv.put(ACCOUNT_OPERATIONS_OPERATION_ID_COLUMN_NAME, result);

        db.insert(ACCOUNT_OPERATIONS_TABLE_NAME, null, cv);

        return result;
    }

    public long CreateOperation(int type, int amount, String info) {
        SQLiteDatabase db = operationDatabase.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(OPERATION_TABLE_TYPE_COLUMN_NAME, type);
        cv.put(OPERATION_TABLE_AMOUNT_COLUMN_NAME, amount);
        cv.put(OPERATION_TABLE_COMMENT_COLUMN_NAME, info);

        long res = db.insert(OPERATION_TABLE_NAME, null, cv);

        return res;
    }

    public ArrayList<Operation> GetOperations() {
        ArrayList<Operation> result = new ArrayList<>();

        SQLiteDatabase db = operationDatabase.getReadableDatabase();

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
                        new Operation(
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

    public ArrayList<Operation> GetAccountOperations(int account) {
        if(account == 0)
            return GetOperations();

        ArrayList<Operation> result = new ArrayList<>();

        SQLiteDatabase db = operationDatabase.getReadableDatabase();

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
                        new Operation(
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

    public ArrayList<Operation> GetLoanOperations(int id) {
        ArrayList<Operation> result = new ArrayList<>();

        SQLiteDatabase db = operationDatabase.getReadableDatabase();

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
                        new Operation(
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
