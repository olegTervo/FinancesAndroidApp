package com.example.finances.frameworks_and_drivers.database.loan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.finances.frameworks_and_drivers.database.investment.InvestmentDatabase;

public class LoanDatabase extends SQLiteOpenHelper {
    public static final String LOAN_TABLE_NAME = "Loan";
    public static final String LOAN_TABLE_UNPAID_COLUMN_NAME = "Unpaid";
    public static final String LOAN_TABLE_RATE_COLUMN_NAME = "Rate";
    public static final String LOAN_TABLE_OPEN_DATE_COLUMN_NAME = "OpenDate";
    public static final String LOAN_ID_COLUMN_NAME = "Id";

    private static volatile LoanDatabase INSTANCE;

    public LoanDatabase(Context context) {
        super(context, "finances.db", null, 17);
    }

    public static synchronized LoanDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LoanDatabase(context.getApplicationContext());
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
}
