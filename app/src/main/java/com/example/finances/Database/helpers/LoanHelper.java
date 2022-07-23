package com.example.finances.Database.helpers;

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
                + LOAN_TABLE_OPEN_DATE_COLUMN_NAME +" TIMESTAMP DEFAULT CURRENT_TIMESTAMP );\n";

        return initialString;
    }

    public static boolean TakeLoan(DatabaseHelper connection, int i, int value){
        return true;
    }

}
