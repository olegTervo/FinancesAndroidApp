package com.example.finances.frameworks_and_drivers.database.investment;

import static com.example.finances.frameworks_and_drivers.database.common.RelationsHelper.*;
import static com.example.finances.frameworks_and_drivers.database.investment.InvestmentDatabase.*;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finances.domain.enums.InvestmentType;
import com.example.finances.domain.models.ApiInvestment;
import com.example.finances.domain.models.Investment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class InvestmentDao{

    private final InvestmentDatabase investmentDatabase;

    @Inject
    public InvestmentDao(InvestmentDatabase db) {
        this.investmentDatabase = db;
    }

    public long CreateInvestment(String name, float amount){
        SQLiteDatabase db = investmentDatabase.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(INVESTMENT_TABLE_NAME_COLUMN_NAME, name);
        cv.put(INVESTMENT_TABLE_AMOUNT_COLUMN_NAME, amount);
        long id = db.insert(INVESTMENT_TABLE_NAME, null, cv);

        return id;
    }

    public Investment GetInvestment(long id){
        Investment result = null;
        SQLiteDatabase db = investmentDatabase.getReadableDatabase();

        String getScript = String.format("SELECT "
                + "I." + INVESTMENT_TABLE_NAME_COLUMN_NAME + ", "
                + "I." +  INVESTMENT_TABLE_AMOUNT_COLUMN_NAME + ", "
                + "I." +  INVESTMENT_TABLE_OPENDATE_COLUMN_NAME + ", "
                + "I." +  INVESTMENT_TABLE_MODIFIED_COLUMN_NAME + ", "
                + " IA." + INVESTMENT_API_INVESTMENT_ID_COLUMN_NAME + ", "
                + " IA." + INVESTMENT_API_NAME_COLUMN_NAME
                + " FROM " + INVESTMENT_TABLE_NAME + " I"
                + " LEFT JOIN " + INVESTMENT_API_TABLE_NAME + " IA "
                + " ON IA." + INVESTMENT_API_INVESTMENT_ID_COLUMN_NAME + " = I." + INVESTMENT_TABLE_ID_COLUMN_NAME
                + " WHERE " + INVESTMENT_TABLE_ID_COLUMN_NAME + " = %d", id);

        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst()) {
            String name = reader.getString(0);
            float amount = reader.getFloat(1);
            LocalDate openDate = LocalDate.parse(reader.getString(2).substring(0, 10));
            LocalDate modified = LocalDate.parse(reader.getString(3).substring(0, 10));
            boolean isManual = reader.getInt(4) > 0;

            InvestmentType type = InvestmentType.ApiLinked;
            if (isManual)
                type = InvestmentType.Manual;

            result = new Investment(id, name, amount, openDate, modified, type);
        }

        reader.close();
        return result;
    }

    public List<Investment> GetInvestments(){
        ArrayList<Investment> result = new ArrayList<>();
        SQLiteDatabase db = investmentDatabase.getReadableDatabase();

        String getScript = "SELECT "
                + INVESTMENT_TABLE_ID_COLUMN_NAME + ", "
                + INVESTMENT_TABLE_NAME_COLUMN_NAME + ", "
                + INVESTMENT_TABLE_AMOUNT_COLUMN_NAME + ", "
                + INVESTMENT_TABLE_OPENDATE_COLUMN_NAME + ", "
                + INVESTMENT_TABLE_MODIFIED_COLUMN_NAME + ", "
                + " IA." + INVESTMENT_API_INVESTMENT_ID_COLUMN_NAME + ", "
                + " IA." + INVESTMENT_API_NAME_COLUMN_NAME
                + " FROM " + INVESTMENT_TABLE_NAME
                + " LEFT JOIN " + INVESTMENT_API_TABLE_NAME + " IA "
                + " ON IA." + INVESTMENT_API_INVESTMENT_ID_COLUMN_NAME + " = I." + INVESTMENT_TABLE_ID_COLUMN_NAME
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
                boolean isManual = reader.isNull(5);

                if (isManual){
                    InvestmentType type = InvestmentType.ApiLinked;
                    Investment investment = new Investment(id, name, amount, openDate, modified, type);
                    result.add(investment);
                }
                else {
                    int apiId = reader.getInt(5);
                    String apiSpecificInvestmentName = reader.getString(6);
                    InvestmentType type = InvestmentType.ApiLinked;
                    Investment investment = new ApiInvestment(id, name, amount, openDate, modified, type, apiId, apiSpecificInvestmentName);
                    result.add(investment);
                }
            } while (reader.moveToNext());

        reader.close();

        //for(Loan loan : result)
        //    loan.addOperations(OperationHelper.GetLoanOperations(connection, loan.id));

        return result;
    }

    public List<Investment> SearchInvestments(InvestmentType type){
        switch (type) {
            case Manual:
                return getManualInvestments();
            case ApiLinked:
                return getApiInvestments();
            default:
                return new ArrayList<>();
        }
    }

    public boolean UpdateInvestment(Investment investment){
        SQLiteDatabase db = investmentDatabase.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(INVESTMENT_TABLE_NAME_COLUMN_NAME, investment.getName());
        cv.put(INVESTMENT_TABLE_AMOUNT_COLUMN_NAME, investment.getAmount());
        cv.put(INVESTMENT_TABLE_MODIFIED_COLUMN_NAME, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())));

        long res = db.update(INVESTMENT_TABLE_NAME, cv, "id=?", new String[] { Long.toString(investment.getId()) });

        if (res == -1) {
            return false;
        }
        return true;
    }

    public boolean DeleteInvestment(long id){
        long res = -1;

        SQLiteDatabase db = investmentDatabase.getWritableDatabase();
        res = db.delete(INVESTMENT_TABLE_NAME, INVESTMENT_TABLE_ID_COLUMN_NAME + "=?", new String[] { Long.toString(id) });

        if (res == -1) {
            return false;
        }

        return true;
    }

    private List<Investment> getManualInvestments() {
        ArrayList<Investment> result = new ArrayList<>();
        SQLiteDatabase db = investmentDatabase.getReadableDatabase();
        String getScript = "SELECT " + "I." + INVESTMENT_TABLE_ID_COLUMN_NAME + ", "
                + "I." + INVESTMENT_TABLE_NAME_COLUMN_NAME + ", "
                + "I." + INVESTMENT_TABLE_AMOUNT_COLUMN_NAME + ", "
                + "I." + INVESTMENT_TABLE_OPENDATE_COLUMN_NAME + ", "
                + "I." + INVESTMENT_TABLE_MODIFIED_COLUMN_NAME
                + " FROM " + INVESTMENT_TABLE_NAME + " I " +
                " LEFT JOIN " + INVESTMENT_API_TABLE_NAME + " IA " +
                " ON IA." + INVESTMENT_API_INVESTMENT_ID_COLUMN_NAME + " = I." + INVESTMENT_TABLE_ID_COLUMN_NAME +
                " WHERE IA." + INVESTMENT_API_INVESTMENT_ID_COLUMN_NAME + " IS NULL" +
                " ORDER BY I." + INVESTMENT_TABLE_ID_COLUMN_NAME + " DESC";

        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            do {
                int id = reader.getInt(0);
                String name = reader.getString(1);
                float amount = reader.getFloat(2);
                LocalDate openDate = LocalDate.parse(reader.getString(3).substring(0, 10));
                LocalDate modified = LocalDate.parse(reader.getString(4).substring(0, 10));

                Investment toAdd = new Investment(id, name, amount, openDate, modified, InvestmentType.Manual);
                result.add(toAdd);
            } while (reader.moveToNext());

        reader.close();
        return result;
    }

    private List<Investment> getApiInvestments() {
        ArrayList<Investment> result = new ArrayList<>();
        SQLiteDatabase db = investmentDatabase.getReadableDatabase();
        String getScript = "SELECT " + "I." + INVESTMENT_TABLE_ID_COLUMN_NAME + ", "
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

        Cursor reader = db.rawQuery(getScript, null);

        if (reader.moveToFirst())
            do {
                int id = reader.getInt(0);
                String name = reader.getString(1);
                float amount = reader.getFloat(2);
                LocalDate openDate = LocalDate.parse(reader.getString(3).substring(0, 10));
                LocalDate modified = LocalDate.parse(reader.getString(4).substring(0, 10));

                int apiId = reader.getInt(5);
                String apiName = reader.getString(6);

                ApiInvestment toAdd = new ApiInvestment(id, name, amount, openDate, modified, InvestmentType.ApiLinked, apiId, apiName);
                result.add(toAdd);
            } while (reader.moveToNext());

        reader.close();
        return result;
    }
}
