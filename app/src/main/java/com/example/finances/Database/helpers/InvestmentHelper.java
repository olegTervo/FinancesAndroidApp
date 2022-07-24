package com.example.finances.Database.helpers;

public class InvestmentHelper {
    public static final String INVESTMENT_TABLE_NAME = "Investment";
    public static final String INVESTMENT_TABLE_PURE_INVESTMENT_COLUMN_NAME = "PureInvestment";
    public static final String INVESTMENT_TABLE_CURRENT_PRICE_COLUMN_NAME = "CurrentPrice";
    public static final String INVESTMENT_TABLE_OPENDATE_COLUMN_NAME = "OpenDate";
    public static final String INVESTMENT_ID_COLUMN_NAME = "Id";

    public static String CreateTableString() {
        String initialString = "CREATE TABLE "
                + INVESTMENT_TABLE_NAME
                + " (" + INVESTMENT_ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + INVESTMENT_TABLE_PURE_INVESTMENT_COLUMN_NAME + " INTEGER NOT NULL, "
                + INVESTMENT_TABLE_CURRENT_PRICE_COLUMN_NAME +" INTEGER NOT NULL, "
                + INVESTMENT_TABLE_OPENDATE_COLUMN_NAME +" TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ");\n";

        return initialString;
    }

    public static String DropTableString() {
        String res = "DROP TABLE " + INVESTMENT_TABLE_NAME + ";\n";
        return res;
    }
}
