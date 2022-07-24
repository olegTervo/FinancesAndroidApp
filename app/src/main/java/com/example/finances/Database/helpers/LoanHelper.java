package com.example.finances.Database.helpers;

import static com.example.finances.Database.helpers.RelationsHelper.LOAN_OPERATIONS_LOAN_ID_COLUMN_NAME;
import static com.example.finances.Database.helpers.RelationsHelper.LOAN_OPERATIONS_OPERATION_ID_COLUMN_NAME;
import static com.example.finances.Database.helpers.RelationsHelper.LOAN_OPERATIONS_TABLE_NAME;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finances.Database.models.LoanDao;
import com.example.finances.models.Loan;

import java.time.LocalDate;
import java.util.ArrayList;

public class LoanHelper {

    public static final String LOAN_TABLE_NAME = "Loan";
    public static final String LOAN_TABLE_UNPAID_COLUMN_NAME = "Unpaid";
    public static final String LOAN_TABLE_RATE_COLUMN_NAME = "Rate";
    public static final String LOAN_TABLE_OPEN_DATE_COLUMN_NAME = "OpenDate";
    public static final String LOAN_ID_COLUMN_NAME = "Id";

    public static String CreateTableString() {
        String initialString = "CREATE TABLE "
                + LOAN_TABLE_NAME
                + " (" + LOAN_ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LOAN_TABLE_UNPAID_COLUMN_NAME + " INTEGER NOT NULL, "
                + LOAN_TABLE_RATE_COLUMN_NAME +" INTEGER NOT NULL, "
                + LOAN_TABLE_OPEN_DATE_COLUMN_NAME +" TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ");\n";

        return initialString;
    }

    public static String DropTableString() {
        String res = "DROP TABLE " + LOAN_TABLE_NAME + ";\n";
        return res;
    }

    public static boolean TakeLoan(DatabaseHelper connection, int amount){
        long loanId = CreateLoan(connection, amount);
        if (loanId == -1) {
            return false;
        }
        else {
            int interest = amount / 100 > 1 ? Math.round(amount / 100) : 1;
            long openOperation = OperationHelper.CreateOperation(connection, 1, interest*(-1), "Loan open interest");
            ChangeLoanUnpaid(connection, loanId, interest*(-1));

            if (loanId != -1 && openOperation != -1) {
                CreateLoanOperation(connection, loanId, openOperation);
            }
        }

        long operationId = AccountHelper.PutMoney(connection, 1, amount*(-1), "Loan payment");
        if (operationId == -1) {
            return false;
        }

        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(LOAN_OPERATIONS_LOAN_ID_COLUMN_NAME, loanId);
        cv.put(LOAN_OPERATIONS_OPERATION_ID_COLUMN_NAME, operationId);

        long res = db.insert(LOAN_OPERATIONS_TABLE_NAME, null, cv);

        return res != -1;
    }

    public static boolean PayLoan(DatabaseHelper connection, long id, int amount) {
        int current = GetUnpaid(connection, id);
        UpdateUnpaid(connection, id, current-amount);

        long operationId = AccountHelper.PutMoney(connection, 1, amount, "Loan return");

        if(operationId == -1)
            return false;

        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(LOAN_OPERATIONS_LOAN_ID_COLUMN_NAME, id);
        cv.put(LOAN_OPERATIONS_OPERATION_ID_COLUMN_NAME, operationId);

        long res = db.insert(LOAN_OPERATIONS_TABLE_NAME, null, cv);

        return res != -1;
    }

    public static boolean ChangeLoanUnpaid(DatabaseHelper connection, long id, int amount) {
        int current = GetUnpaid(connection, id);
        return UpdateUnpaid(connection, id, current-amount);
    }

    public static ArrayList<Loan> GetLoans(DatabaseHelper connection) {
        ArrayList<Loan> result = new ArrayList<>();
        SQLiteDatabase db = connection.getReadableDatabase();

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
        db.close();

        for(Loan loan : result)
            loan.addOperations(OperationHelper.GetLoanOperations(connection, loan.id));

        return result;
    }

    public static ArrayList<LoanDao> GetUnpaidLoans(DatabaseHelper connection) {
        ArrayList<LoanDao> result = new ArrayList<>();
        SQLiteDatabase db = connection.getReadableDatabase();

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
                        new LoanDao(
                                reader.getInt(0)
                                , reader.getInt(1)
                                , reader.getInt(2)
                                , LocalDate.parse(reader.getString(3).substring(0, 10))
                        )
                );
            } while (reader.moveToNext());

        reader.close();
        db.close();

        return result;
    }

    private static boolean UpdateUnpaid(DatabaseHelper connection, long id, int value) {
        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(LOAN_TABLE_UNPAID_COLUMN_NAME, value);

        long res = db.update(LOAN_TABLE_NAME, cv, "id=?", new String[] { Long.toString(id) });

        if (res == -1) {
            return false;
        }
        return true;
    }

    private static int GetUnpaid(DatabaseHelper connection, long id) {
        int result = 0;
        SQLiteDatabase db = connection.getReadableDatabase();

        String getScript = String.format("SELECT " + LOAN_TABLE_UNPAID_COLUMN_NAME + " FROM " + LOAN_TABLE_NAME + " WHERE " + LOAN_ID_COLUMN_NAME + " = %s", id);
        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            result = reader.getInt(0);

        reader.close();
        db.close();

        return result;
    }

    private static long CreateLoan(DatabaseHelper connection, int amount) {
        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(LOAN_TABLE_UNPAID_COLUMN_NAME, amount);
        cv.put(LOAN_TABLE_RATE_COLUMN_NAME, 1);
        long loanId = db.insert(LOAN_TABLE_NAME, null, cv);

        return loanId;
    }

    private static boolean CreateLoanOperation(DatabaseHelper connection, long loanId, long operationId) {
        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(LOAN_OPERATIONS_LOAN_ID_COLUMN_NAME, loanId);
        cv.put(LOAN_OPERATIONS_OPERATION_ID_COLUMN_NAME, operationId);

        return db.insert(LOAN_OPERATIONS_TABLE_NAME, null, cv) != -1;
    }
}
