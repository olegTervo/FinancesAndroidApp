package com.example.finances.infrastructure.Database.helpers;

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

    public static final String SHOP_ITEM_PRICE_TABLE_NAME = "ShopItemPrice";
    public static final String SHOP_ITEM_PRICE_SHOP_ITEM_ID_COLUMN_NAME = "ShopItemId";
    public static final String SHOP_ITEM_PRICE_PRICE_ID_COLUMN_NAME = "PriceId";
    public static final String SHOP_ITEM_PRICE_TYPE_COLUMN_NAME = "Type";

    public static final String INVESTMENT_PRICE_TABLE_NAME = "InvestmentPrice";
    public static final String INVESTMENT_PRICE_INVESTMENT_ID_COLUMN_NAME = "InvestmentId";
    public static final String INVESTMENT_PRICE_PRICE_ID_COLUMN_NAME = "PriceId";

    public static final String INVESTMENT_API_TABLE_NAME = "InvestmentApi";
    public static final String INVESTMENT_API_INVESTMENT_ID_COLUMN_NAME = "InvestmentId";
    public static final String INVESTMENT_API_API_ID_COLUMN_NAME = "ApiId";
    public static final String INVESTMENT_API_NAME_COLUMN_NAME = "Name";

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
                + LOAN_OPERATIONS_OPERATION_ID_COLUMN_NAME +" INTEGER NOT NULL, "

                + "CONSTRAINT loan_operations_loan_fk FOREIGN KEY (" + LOAN_OPERATIONS_LOAN_ID_COLUMN_NAME + ") "
                + "REFERENCES " + LoanHelper.LOAN_TABLE_NAME + "(" + LoanHelper.LOAN_ID_COLUMN_NAME + "), "

                + "CONSTRAINT loan_operations_operation_fk FOREIGN KEY (" + LOAN_OPERATIONS_OPERATION_ID_COLUMN_NAME + ") "
                + "REFERENCES " + OperationHelper.OPERATION_TABLE_NAME + "(" + OperationHelper.OPERATION_ID_COLUMN_NAME + ")"
                + ");\n";

        return initialString;
    }

    public static String CreateInvestmentOperationsTableString() {
        String initialString = "CREATE TABLE "
                + INVESTMENT_OPERATIONS_TABLE_NAME
                + " (" + INVESTMENT_OPERATIONS_ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + INVESTMENT_OPERATIONS_INVESTMENT_ID_COLUMN_NAME + " INTEGER NOT NULL, "
                + INVESTMENT_OPERATIONS_OPERATION_ID_COLUMN_NAME +" INTEGER NOT NULL, "

                + "CONSTRAINT investment_operations_investment_fk FOREIGN KEY (" + INVESTMENT_OPERATIONS_INVESTMENT_ID_COLUMN_NAME + ") "
                + "REFERENCES " + InvestmentHelper.INVESTMENT_TABLE_NAME + "(" + InvestmentHelper.INVESTMENT_TABLE_ID_COLUMN_NAME + "), "

                + "CONSTRAINT investment_operations_operation_fk FOREIGN KEY (" + INVESTMENT_OPERATIONS_OPERATION_ID_COLUMN_NAME + ")"
                + "REFERENCES " + OperationHelper.OPERATION_TABLE_NAME + "(" + OperationHelper.OPERATION_ID_COLUMN_NAME + ")"
                + ");\n";

        return initialString;
    }

    public static String CreateAccountOperationsTableString() {
        String initialString = "CREATE TABLE "
                + ACCOUNT_OPERATIONS_TABLE_NAME
                + " (" + ACCOUNT_OPERATIONS_ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ACCOUNT_OPERATIONS_ACCOUNT_ID_COLUMN_NAME + " INTEGER NOT NULL, "
                + ACCOUNT_OPERATIONS_OPERATION_ID_COLUMN_NAME +" INTEGER NOT NULL, "

                + "CONSTRAINT account_operations_account_fk FOREIGN KEY (" + ACCOUNT_OPERATIONS_ACCOUNT_ID_COLUMN_NAME + ") "
                + "REFERENCES " + AccountHelper.ACCOUNT_TABLE_NAME + "(" + AccountHelper.ACCOUNT_ID_COLUMN_NAME + "), "

                + "CONSTRAINT account_operations_operations_fk FOREIGN KEY (" + ACCOUNT_OPERATIONS_OPERATION_ID_COLUMN_NAME + ") "
                + "REFERENCES " + OperationHelper.OPERATION_TABLE_NAME + "(" + OperationHelper.OPERATION_ID_COLUMN_NAME + ")"
                + ");\n";

        return initialString;
    }

    public static String CreateShopItemPriceTableString() {
        String initialString = "CREATE TABLE "
                + SHOP_ITEM_PRICE_TABLE_NAME
                + " (" + SHOP_ITEM_PRICE_SHOP_ITEM_ID_COLUMN_NAME + " INTEGER NOT NULL, "
                + SHOP_ITEM_PRICE_PRICE_ID_COLUMN_NAME +" INTEGER NOT NULL, "
                + SHOP_ITEM_PRICE_TYPE_COLUMN_NAME +" INTEGER NOT NULL, "

                + "CONSTRAINT shop_item_price_shop_item_fk FOREIGN KEY (" + SHOP_ITEM_PRICE_SHOP_ITEM_ID_COLUMN_NAME + ") "
                + "REFERENCES " + ShopItemHelper.SHOP_ITEM_TABLE_NAME + "(" + ShopItemHelper.SHOP_ITEM_ID_COLUMN_NAME + "), "

                + "CONSTRAINT shop_item_price_price_fk FOREIGN KEY (" + SHOP_ITEM_PRICE_PRICE_ID_COLUMN_NAME + ") "
                + "REFERENCES " + PriceHelper.PRICE_TABLE_NAME + "(" + PriceHelper.PRICE_TABLE_ID_COLUMN_NAME + ")"
                + ");\n";

        return initialString;
    }

    public static String CreateInvestmentPriceTableString() {
        String initialString = "CREATE TABLE "
                + INVESTMENT_PRICE_TABLE_NAME
                + " (" + INVESTMENT_PRICE_INVESTMENT_ID_COLUMN_NAME + " INTEGER NOT NULL, "
                + INVESTMENT_PRICE_PRICE_ID_COLUMN_NAME +" INTEGER NOT NULL, "

                + "CONSTRAINT investment_price_investment_fk FOREIGN KEY (" + INVESTMENT_PRICE_INVESTMENT_ID_COLUMN_NAME + ") "
                + "REFERENCES " + InvestmentHelper.INVESTMENT_TABLE_NAME + "(" + InvestmentHelper.INVESTMENT_TABLE_ID_COLUMN_NAME + "), "

                + "CONSTRAINT investment_price_price_fk FOREIGN KEY (" + INVESTMENT_PRICE_PRICE_ID_COLUMN_NAME + ") "
                + "REFERENCES " + PriceHelper.PRICE_TABLE_NAME + "(" + PriceHelper.PRICE_TABLE_ID_COLUMN_NAME + ")"
                + ");\n";

        return initialString;
    }

    public static String CreateInvestmentApiTableString() {
        String initialString = "CREATE TABLE "
                + INVESTMENT_API_TABLE_NAME
                + " (" + INVESTMENT_API_INVESTMENT_ID_COLUMN_NAME + " INTEGER NOT NULL, "
                + INVESTMENT_API_API_ID_COLUMN_NAME +" INTEGER NOT NULL, "
                + INVESTMENT_API_NAME_COLUMN_NAME +" VARCHAR(1024) NOT NULL, "

                + "CONSTRAINT investment_api_investment_fk FOREIGN KEY (" + INVESTMENT_API_INVESTMENT_ID_COLUMN_NAME + ") "
                + "REFERENCES " + InvestmentHelper.INVESTMENT_TABLE_NAME + "(" + InvestmentHelper.INVESTMENT_TABLE_ID_COLUMN_NAME + "), "

                + "CONSTRAINT investment_api_api_fk FOREIGN KEY (" + INVESTMENT_API_API_ID_COLUMN_NAME + ") "
                + "REFERENCES " + ApiHelper.API_TABLE_NAME + "(" + ApiHelper.API_TABLE_ID_COLUMN_NAME + ")"
                + ");\n";

        return initialString;
    }

    public static String DropLoanOperationsTableString() {
        String res = "DROP TABLE " + LOAN_OPERATIONS_TABLE_NAME + ";\n";
        return res;
    }

    public static String DropInvestmentOperationsTableString() {
        String res = "DROP TABLE " + INVESTMENT_OPERATIONS_TABLE_NAME + ";\n";
        return res;
    }

    public static String DropAccountOperationsTableString() {
        String res = "DROP TABLE " + ACCOUNT_OPERATIONS_TABLE_NAME + ";\n";
        return res;
    }

    public static String DropShopItemPriceTableString() {
        String res = "DROP TABLE " + SHOP_ITEM_PRICE_TABLE_NAME + ";\n";
        return res;
    }

    public static String DropInvestmentPriceTableString() {
        String res = "DROP TABLE " + INVESTMENT_PRICE_TABLE_NAME + ";\n";
        return res;
    }

    public static String DropInvestmentApiTableString() {
        String res = "DROP TABLE " + INVESTMENT_API_TABLE_NAME + ";\n";
        return res;
    }
}
