package com.example.finances.Database.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private boolean updating = false;
    private SQLiteDatabase updatingDB = null;

    public DatabaseHelper(@Nullable Context context) {
        super(context, "finances.db", null, 17);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.updating = true;
        this.updatingDB = db;
        db.execSQL(DailyGrowthHelper.CreateTableString());
        db.execSQL(VariablesHelper.CreateTableString());

        db.execSQL(LoanHelper.CreateTableString());
        db.execSQL(InvestmentHelper.CreateTableString());
        db.execSQL(OperationHelper.CreateTableString());
        db.execSQL(AccountHelper.CreateTableString());

        db.execSQL(RelationsHelper.CreateTableString());

        AccountHelper.Initialize(this);
        this.updating = false;
        this.updatingDB = null;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 2) {
            this.updating = true;
            this.updatingDB = db;
            db.execSQL(LoanHelper.CreateTableString());
            db.execSQL(InvestmentHelper.CreateTableString());
            db.execSQL(OperationHelper.CreateTableString());
            db.execSQL(AccountHelper.CreateTableString());

            db.execSQL(RelationsHelper.CreateTableString());
            this.updating = false;
            this.updatingDB = null;
        }

        else if (newVersion == 3) {
            this.updating = true;
            this.updatingDB = db;
            db.execSQL(RelationsHelper.CreateInvestmentOperationsTableString());
            db.execSQL(RelationsHelper.CreateAccountOperationsTableString());
            this.updating = false;
            this.updatingDB = null;
        }

        else if (newVersion == 4 && oldVersion != 4) {
            this.updating = true;
            this.updatingDB = db;
            db.execSQL(OperationHelper.DropTableString());
            db.execSQL(OperationHelper.CreateTableString());
            this.updating = false;
            this.updatingDB = null;
        }

        else if ( newVersion == 7 && oldVersion != 7){
            this.updating = true;
            this.updatingDB = db;
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
            this.updating = false;
            this.updatingDB = null;
        }

        else if ( newVersion == 8 && oldVersion != 8){
            this.updating = true;
            this.updatingDB = db;
            db.execSQL(ShopHelper.CreateTableString());
            db.execSQL(ShopItemHelper.CreateTableString());
            db.execSQL(PriceHelper.CreateTableString());

            ShopHelper.Initialize(this);
            this.updating = false;
            this.updatingDB = null;
        }

        else if ( newVersion == 9 && oldVersion != 9){
            this.updating = true;
            this.updatingDB = db;
            db.execSQL(PriceHelper.DropTableString());
            db.execSQL(PriceHelper.CreateTableString());
            this.updating = false;
            this.updatingDB = null;
        }

        else if ( newVersion == 10 && oldVersion != 10){
            this.updating = true;
            this.updatingDB = db;
            db.execSQL(InvestmentHelper.DropTableString());
            db.execSQL(InvestmentHelper.CreateTableString());

            db.execSQL(ShopItemHelper.DropTableString());
            db.execSQL(ShopItemHelper.CreateTableString());

            db.execSQL(PriceHelper.DropTableString());
            db.execSQL(PriceHelper.CreateTableString());

            db.execSQL(RelationsHelper.CreateShopItemPriceTableString());
            db.execSQL(RelationsHelper.CreateInvestmentPriceTableString());

            this.updating = false;
            this.updatingDB = null;
        }

        else if ( newVersion == 11 && oldVersion != 11){
            this.updating = true;
            this.updatingDB = db;

            db.execSQL(ApiHelper.CreateTableString());
            db.execSQL(RelationsHelper.CreateInvestmentApiTableString());

            ApiHelper.InsertCoinMarketCapApi(this);

            this.updating = false;
            this.updatingDB = null;
        }

        else if ( newVersion == 14 && oldVersion != 14){
            this.updating = true;
            this.updatingDB = db;

            db.execSQL(RelationsHelper.DropInvestmentApiTableString());
            db.execSQL(RelationsHelper.DropInvestmentPriceTableString());
            db.execSQL(RelationsHelper.DropInvestmentOperationsTableString());
            db.execSQL(RelationsHelper.DropShopItemPriceTableString());

            db.execSQL(InvestmentHelper.DropTableString());
            db.execSQL(PriceHelper.DropTableString());
            db.execSQL(InvestmentHelper.CreateTableString());
            db.execSQL(PriceHelper.CreateTableString());

            db.execSQL(RelationsHelper.CreateInvestmentApiTableString());
            db.execSQL(RelationsHelper.CreateInvestmentPriceTableString());
            db.execSQL(RelationsHelper.CreateInvestmentOperationsTableString());
            db.execSQL(RelationsHelper.CreateShopItemPriceTableString());

            this.updating = false;
            this.updatingDB = null;
        }

        else if ( newVersion == 17 && oldVersion != 17){
            this.updating = true;
            this.updatingDB = db;

            db.execSQL(ValueDateHelper.DropTableString());
            db.execSQL(ValueDateHelper.CreateTableString());
            ValueDateHelper.MigrateDailyGrowthTable(db);

            this.updating = false;
            this.updatingDB = null;
        }
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        if(updating && updatingDB != null)
            return updatingDB;
        return super.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        if(updating && updatingDB != null)
            return updatingDB;
        return super.getReadableDatabase();
    }
}
