package com.example.finances.frameworks_and_drivers.database.loan;

import static com.example.finances.frameworks_and_drivers.database.common.RelationsHelper.LOAN_OPERATIONS_LOAN_ID_COLUMN_NAME;
import static com.example.finances.frameworks_and_drivers.database.common.RelationsHelper.LOAN_OPERATIONS_OPERATION_ID_COLUMN_NAME;
import static com.example.finances.frameworks_and_drivers.database.common.RelationsHelper.LOAN_OPERATIONS_TABLE_NAME;
import static com.example.finances.frameworks_and_drivers.database.loan.LoanDatabase.LOAN_ID_COLUMN_NAME;
import static com.example.finances.frameworks_and_drivers.database.loan.LoanDatabase.LOAN_TABLE_NAME;
import static com.example.finances.frameworks_and_drivers.database.loan.LoanDatabase.LOAN_TABLE_OPEN_DATE_COLUMN_NAME;
import static com.example.finances.frameworks_and_drivers.database.loan.LoanDatabase.LOAN_TABLE_RATE_COLUMN_NAME;
import static com.example.finances.frameworks_and_drivers.database.loan.LoanDatabase.LOAN_TABLE_UNPAID_COLUMN_NAME;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finances.domain.models.Loan;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.inject.Inject;

public class LoanDao {
    private final LoanDatabase loanDatabase;

    @Inject
    public LoanDao(LoanDatabase db) {
        this.loanDatabase = db;
    }

    public long CreateLoan(int amount) {
        SQLiteDatabase db = loanDatabase.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(LOAN_TABLE_UNPAID_COLUMN_NAME, amount);
        cv.put(LOAN_TABLE_RATE_COLUMN_NAME, 1);
        long loanId = db.insert(LOAN_TABLE_NAME, null, cv);

        return loanId;
    }

    public boolean ChangeLoanUnpaid(long id, int amount) {
        int current = GetUnpaid(id);
        return UpdateUnpaid(id, current-amount);
    }

    public ArrayList<Loan> GetLoans() {
        ArrayList<Loan> result = new ArrayList<>();
        SQLiteDatabase db = loanDatabase.getReadableDatabase();

        String getScript = "SELECT "
                + LOAN_ID_COLUMN_NAME + ", "
                + LOAN_TABLE_UNPAID_COLUMN_NAME + ", "
                + LOAN_TABLE_RATE_COLUMN_NAME + ", "
                + LOAN_TABLE_OPEN_DATE_COLUMN_NAME
                + " FROM " + LOAN_TABLE_NAME
                + " ORDER BY " + LOAN_ID_COLUMN_NAME
                + " DESC LIMIT 100";

        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            do {
                Loan loan = new Loan(
                        reader.getInt(0)
                        , reader.getInt(1)
                        , reader.getInt(2)
                        , LocalDate.parse(reader.getString(3).substring(0, 10))
                );
                result.add(loan);
            } while (reader.moveToNext());

        reader.close();
        return result;
    }

    public ArrayList<Loan> GetUnpaidLoans() {
        ArrayList<Loan> result = new ArrayList<>();
        SQLiteDatabase db = loanDatabase.getReadableDatabase();

        String getScript = "SELECT "
                + LOAN_ID_COLUMN_NAME + ", "
                + LOAN_TABLE_UNPAID_COLUMN_NAME + ", "
                + LOAN_TABLE_RATE_COLUMN_NAME + ", "
                + LOAN_TABLE_OPEN_DATE_COLUMN_NAME
                + " FROM " + LOAN_TABLE_NAME
                + " WHERE " + LOAN_TABLE_UNPAID_COLUMN_NAME + " > 0"
                + " ORDER BY " + LOAN_ID_COLUMN_NAME
                + " DESC LIMIT 30";

        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            do {
                result.add(
                        new Loan(
                                reader.getInt(0)
                                , reader.getInt(1)
                                , reader.getInt(2)
                                , LocalDate.parse(reader.getString(3).substring(0, 10))
                        )
                );
            } while (reader.moveToNext());

        reader.close();
        return result;
    }

    public boolean CreateLoanOperation(long loanId, long operationId) {
        SQLiteDatabase db = loanDatabase.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(LOAN_OPERATIONS_LOAN_ID_COLUMN_NAME, loanId);
        cv.put(LOAN_OPERATIONS_OPERATION_ID_COLUMN_NAME, operationId);

        return db.insert(LOAN_OPERATIONS_TABLE_NAME, null, cv) != -1;
    }

    private boolean UpdateUnpaid(long id, int value) {
        SQLiteDatabase db = loanDatabase.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(LOAN_TABLE_UNPAID_COLUMN_NAME, value);

        long res = db.update(LOAN_TABLE_NAME, cv, "id=?", new String[] { Long.toString(id) });

        if (res == -1) {
            return false;
        }
        return true;
    }

    private int GetUnpaid(long id) {
        int result = 0;
        SQLiteDatabase db = loanDatabase.getReadableDatabase();

        String getScript = String.format("SELECT " + LOAN_TABLE_UNPAID_COLUMN_NAME + " FROM " + LOAN_TABLE_NAME + " WHERE " + LOAN_ID_COLUMN_NAME + " = %s", id);
        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            result = reader.getInt(0);

        reader.close();
        return result;
    }
}
