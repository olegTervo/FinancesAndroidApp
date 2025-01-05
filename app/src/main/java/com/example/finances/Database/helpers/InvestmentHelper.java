package com.example.finances.Database.helpers;

import static com.example.finances.Database.helpers.PriceHelper.DeletePrices;
import static com.example.finances.Database.helpers.RelationsHelper.INVESTMENT_API_API_ID_COLUMN_NAME;
import static com.example.finances.Database.helpers.RelationsHelper.INVESTMENT_API_INVESTMENT_ID_COLUMN_NAME;
import static com.example.finances.Database.helpers.RelationsHelper.INVESTMENT_API_NAME_COLUMN_NAME;
import static com.example.finances.Database.helpers.RelationsHelper.INVESTMENT_API_TABLE_NAME;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finances.Database.models.ApiDao;
import com.example.finances.Database.models.InvestmentDao;
import com.example.finances.enums.ApiType;
import com.example.finances.enums.InvestmentType;
import com.example.finances.enums.PriceType;
import com.example.finances.models.Api;
import com.example.finances.models.ApiInvestment;
import com.example.finances.models.Investment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class InvestmentHelper {
    public static final String INVESTMENT_TABLE_NAME = "Investment";
    public static final String INVESTMENT_TABLE_ID_COLUMN_NAME = "Id";
    public static final String INVESTMENT_TABLE_NAME_COLUMN_NAME = "Name";
    public static final String INVESTMENT_TABLE_AMOUNT_COLUMN_NAME = "Amount";
    public static final String INVESTMENT_TABLE_OPENDATE_COLUMN_NAME = "OpenDate";
    public static final String INVESTMENT_TABLE_MODIFIED_COLUMN_NAME = "Modified";

    public static String CreateTableString() {
        String initialString = "CREATE TABLE "
                + INVESTMENT_TABLE_NAME
                + " (" + INVESTMENT_TABLE_ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + INVESTMENT_TABLE_NAME_COLUMN_NAME +" VARCHAR(1024) NOT NULL, "
                + INVESTMENT_TABLE_AMOUNT_COLUMN_NAME + " FLOAT NOT NULL, "
                + INVESTMENT_TABLE_OPENDATE_COLUMN_NAME +" TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + INVESTMENT_TABLE_MODIFIED_COLUMN_NAME +" TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ");\n";

        return initialString;
    }

    public static String DropTableString() {
        String res = "DROP TABLE " + INVESTMENT_TABLE_NAME + ";\n";
        return res;
    }

    public static long CreateInvestment(DatabaseHelper connection, InvestmentDao investment){
        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(INVESTMENT_TABLE_NAME_COLUMN_NAME, investment.Name);
        cv.put(INVESTMENT_TABLE_AMOUNT_COLUMN_NAME, investment.Amount);
        long id = db.insert(INVESTMENT_TABLE_NAME, null, cv);

        return id;
    }

    public static InvestmentDao GetInvestment(DatabaseHelper connection, int id){
        InvestmentDao result = null;
        SQLiteDatabase db = connection.getReadableDatabase();

        String getScript = String.format("SELECT "
                + INVESTMENT_TABLE_NAME_COLUMN_NAME + ", "
                + INVESTMENT_TABLE_AMOUNT_COLUMN_NAME + ", "
                + INVESTMENT_TABLE_OPENDATE_COLUMN_NAME + ", "
                + INVESTMENT_TABLE_MODIFIED_COLUMN_NAME
                + " FROM " + INVESTMENT_TABLE_NAME
                + " WHERE " + INVESTMENT_TABLE_ID_COLUMN_NAME + " = %s", id);
        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst()) {
            String name = reader.getString(0);
            float amount = reader.getFloat(1);
            LocalDate openDate = LocalDate.parse(reader.getString(2).substring(0, 10));
            LocalDate modified = LocalDate.parse(reader.getString(3).substring(0, 10));

            result = new InvestmentDao(id, name, amount, openDate, modified);
        }

        reader.close();
        return result;
    }

    public static ArrayList<InvestmentDao> GetInvestments(DatabaseHelper connection){
        ArrayList<InvestmentDao> result = new ArrayList<>();
        SQLiteDatabase db = connection.getReadableDatabase();

        String getScript = "SELECT "
                + INVESTMENT_TABLE_ID_COLUMN_NAME + ", "
                + INVESTMENT_TABLE_NAME_COLUMN_NAME + ", "
                + INVESTMENT_TABLE_AMOUNT_COLUMN_NAME + ", "
                + INVESTMENT_TABLE_OPENDATE_COLUMN_NAME + ", "
                + INVESTMENT_TABLE_MODIFIED_COLUMN_NAME
                + " FROM " + INVESTMENT_TABLE_NAME
                + " ORDER BY " + INVESTMENT_TABLE_ID_COLUMN_NAME
                + " DESC LIMIT 100";

        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            do {
                int id = reader.getInt(0);
                String name = reader.getString(1);
                float amount = reader.getFloat(2);
                LocalDate openDate = LocalDate.parse(reader.getString(3).substring(0, 10));
                LocalDate modified = LocalDate.parse(reader.getString(4).substring(0, 10));

                InvestmentDao investment = new InvestmentDao(id, name, amount, openDate, modified);
                result.add(investment);
            } while (reader.moveToNext());

        reader.close();

        //for(Loan loan : result)
        //    loan.addOperations(OperationHelper.GetLoanOperations(connection, loan.id));

        return result;
    }

    public static ArrayList<Investment> SearchInvestments(DatabaseHelper connection, InvestmentType type){
        ArrayList<Investment> result = new ArrayList<>();
        SQLiteDatabase db = connection.getReadableDatabase();
        String getScript = "";

        if (type.equals(InvestmentType.Manual)) {
            getScript = "SELECT " + "I." + INVESTMENT_TABLE_ID_COLUMN_NAME + ", "
                        + "I." + INVESTMENT_TABLE_NAME_COLUMN_NAME + ", "
                        + "I." + INVESTMENT_TABLE_AMOUNT_COLUMN_NAME + ", "
                        + "I." + INVESTMENT_TABLE_OPENDATE_COLUMN_NAME + ", "
                        + "I." + INVESTMENT_TABLE_MODIFIED_COLUMN_NAME
                    + " FROM " + INVESTMENT_TABLE_NAME + " I " +
                    " LEFT JOIN " + INVESTMENT_API_TABLE_NAME + " IA " +
                        " ON IA." + INVESTMENT_API_INVESTMENT_ID_COLUMN_NAME + " = I." + INVESTMENT_TABLE_ID_COLUMN_NAME +
                    " WHERE IA." + INVESTMENT_API_INVESTMENT_ID_COLUMN_NAME + " IS NULL" +
                    " ORDER BY I." + INVESTMENT_TABLE_ID_COLUMN_NAME + " DESC";
        }
        else if (type.equals(InvestmentType.ApiLinked)) {
            getScript = "SELECT " + "I." + INVESTMENT_TABLE_ID_COLUMN_NAME + ", "
                        + "I." + INVESTMENT_TABLE_NAME_COLUMN_NAME + ", "
                        + "I." + INVESTMENT_TABLE_AMOUNT_COLUMN_NAME + ", "
                        + "I." + INVESTMENT_TABLE_OPENDATE_COLUMN_NAME + ", "
                        + "I." + INVESTMENT_TABLE_MODIFIED_COLUMN_NAME + ", "

                        + "IA." + INVESTMENT_API_API_ID_COLUMN_NAME + ", "
                        + "IA." + INVESTMENT_API_NAME_COLUMN_NAME
                    + " FROM " + INVESTMENT_TABLE_NAME + " I " +
                    " INNER JOIN " + INVESTMENT_API_TABLE_NAME + " IA " +
                        " ON IA." + INVESTMENT_API_INVESTMENT_ID_COLUMN_NAME + " = I." + INVESTMENT_TABLE_ID_COLUMN_NAME +
                    " ORDER BY I." + INVESTMENT_TABLE_ID_COLUMN_NAME + " DESC";
        }

        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            do {
                int id = reader.getInt(0);
                String name = reader.getString(1);
                float amount = reader.getFloat(2);
                LocalDate openDate = LocalDate.parse(reader.getString(3).substring(0, 10));
                LocalDate modified = LocalDate.parse(reader.getString(4).substring(0, 10));

                Investment toAdd = new Investment(id, name, amount, openDate, modified, type);

                if (type.equals(InvestmentType.ApiLinked)) {
                    int apiId = reader.getInt(5);
                    String apiSpecificName = reader.getString(6);

                    ApiDao apiDetails = ApiHelper.GetApi(connection, ApiType.fromInt(apiId));

                    toAdd = new ApiInvestment(toAdd, new Api(apiDetails), apiSpecificName);
                }

                result.add(toAdd);
            } while (reader.moveToNext());

        reader.close();
        return result;
    }

    public static boolean UpdateInvestment(DatabaseHelper connection, InvestmentDao investment){
        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(INVESTMENT_TABLE_NAME_COLUMN_NAME, investment.Name);
        cv.put(INVESTMENT_TABLE_AMOUNT_COLUMN_NAME, investment.Amount);
        cv.put(INVESTMENT_TABLE_MODIFIED_COLUMN_NAME, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())));

        long res = db.update(INVESTMENT_TABLE_NAME, cv, "id=?", new String[] { Long.toString(investment.Id) });

        if (res == -1) {
            return false;
        }
        return true;
    }

    public static boolean DeleteInvestment(DatabaseHelper connection, int id){
        long res = -1;

        if(DeletePrices(connection, id, PriceType.Investment)) {
            SQLiteDatabase db = connection.getWritableDatabase();
            res = db.delete(INVESTMENT_TABLE_NAME, INVESTMENT_TABLE_ID_COLUMN_NAME + "=?", new String[] { Integer.toString(id) });
        }

        if (res == -1) {
            return false;
        }
        return true;
    }
}
