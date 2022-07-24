package com.example.finances.Database.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context) {
        super(context, "finances.db", null, 7);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DailyGrowthHelper.CreateTableString());
        db.execSQL(VariablesHelper.CreateTableString());

        db.execSQL(LoanHelper.CreateTableString());
        db.execSQL(InvestmentHelper.CreateTableString());
        db.execSQL(OperationHelper.CreateTableString());
        db.execSQL(AccountHelper.CreateTableString());

        db.execSQL(RelationsHelper.CreateTableString());

        AccountHelper.Initialize(this);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 2) {
            db.execSQL(LoanHelper.CreateTableString());
            db.execSQL(InvestmentHelper.CreateTableString());
            db.execSQL(OperationHelper.CreateTableString());
            db.execSQL(AccountHelper.CreateTableString());

            db.execSQL(RelationsHelper.CreateTableString());
        }

        else if (newVersion == 3) {
            db.execSQL(RelationsHelper.CreateInvestmentOperationsTableString());
            db.execSQL(RelationsHelper.CreateAccountOperationsTableString());
        }

        else if (newVersion == 4 && oldVersion != 4) {
            db.execSQL(OperationHelper.DropTableString());
            db.execSQL(OperationHelper.CreateTableString());
        }

        else if ( newVersion == 7 && oldVersion != 7){
            db.execSQL(RelationsHelper.DropLoanOperationsTableString());
            db.execSQL(RelationsHelper.DropInvestmentOperationsTableString());
            db.execSQL(RelationsHelper.DropAccountOperationsTableString());

            db.execSQL(AccountHelper.DropTableString());
            db.execSQL(AccountHelper.CreateTableString());
            db.execSQL(InvestmentHelper.DropTableString());
            db.execSQL(InvestmentHelper.CreateTableString());
            db.execSQL(LoanHelper.DropTableString());
            db.execSQL(LoanHelper.CreateTableString());
            db.execSQL(OperationHelper.DropTableString());
            db.execSQL(OperationHelper.CreateTableString());

            db.execSQL(RelationsHelper.CreateAccountOperationsTableString());
            db.execSQL(RelationsHelper.CreateLoanOperationsTableString());
            db.execSQL(RelationsHelper.CreateInvestmentOperationsTableString());
        }
    }
}
