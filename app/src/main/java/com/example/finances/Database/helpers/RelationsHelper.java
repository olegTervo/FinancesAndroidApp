package com.example.finances.Database.helpers;

public class RelationsHelper {
    public static final String LOAN_OPERATIONS_TABLE_NAME = "LoanOperations";
    public static final String LOAN_OPERATIONS_ID_COLUMN_NAME = "Id";
    public static final String LOAN_OPERATIONS_LOAN_ID_COLUMN_NAME = "LoanId";
    public static final String LOAN_OPERATIONS_OPERATION_ID_COLUMN_NAME = "OperationId";

    public static final String INVESTMENT_OPERATIONS_TABLE_NAME = "InvestmentOperations";
    public static final String INVESTMENT_OPERATIONS_ID_COLUMN_NAME = "Id";
    public static final String INVESTMENT_OPERATIONS_INVESTMENT_ID_COLUMN_NAME = "InvestmentId";
    public static final String INVESTMENT_OPERATIONS_OPERATION_ID_COLUMN_NAME = "OperationId";

    public static final String ACCOUNT_OPERATIONS_TABLE_NAME = "AccountOperations";
    public static final String ACCOUNT_OPERATIONS_ID_COLUMN_NAME = "Id";
    public static final String ACCOUNT_OPERATIONS_ACCOUNT_ID_COLUMN_NAME = "AccountId";
    public static final String ACCOUNT_OPERATIONS_OPERATION_ID_COLUMN_NAME = "OperationId";

    public static String CreateTableString() {
        String initialString = "";

        initialString += CreateLoanOperationsTableString();
        initialString += CreateInvestmentOperationsTableString();
        initialString += CreateAccountOperationsTableString();

        return initialString;
    }

    public static String CreateLoanOperationsTableString() {
        String initialString = "CREATE TABLE "
                + LOAN_OPERATIONS_TABLE_NAME
                + " (" + LOAN_OPERATIONS_ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LOAN_OPERATIONS_LOAN_ID_COLUMN_NAME + " INTEGER NOT NULL, "
                + LOAN_OPERATIONS_OPERATION_ID_COLUMN_NAME +" INTEGER NOT NULL );\n";

        return initialString;
    }

    public static String CreateInvestmentOperationsTableString() {
        String initialString = "CREATE TABLE "
                + INVESTMENT_OPERATIONS_TABLE_NAME
                + " (" + INVESTMENT_OPERATIONS_ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + INVESTMENT_OPERATIONS_INVESTMENT_ID_COLUMN_NAME + " INTEGER NOT NULL, "
                + INVESTMENT_OPERATIONS_OPERATION_ID_COLUMN_NAME +" INTEGER NOT NULL );\n";

        return initialString;
    }

    public static String CreateAccountOperationsTableString() {
        String initialString = "CREATE TABLE "
                + ACCOUNT_OPERATIONS_TABLE_NAME
                + " (" + ACCOUNT_OPERATIONS_ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ACCOUNT_OPERATIONS_ACCOUNT_ID_COLUMN_NAME + " INTEGER NOT NULL, "
                + ACCOUNT_OPERATIONS_OPERATION_ID_COLUMN_NAME +" INTEGER NOT NULL );\n";

        return initialString;
    }
}
