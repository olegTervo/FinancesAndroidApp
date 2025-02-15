package com.example.finances.frameworks_and_drivers.database.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.finances.domain.services.ShopService;
import com.example.finances.frameworks_and_drivers.database.account.AccountDao;
import com.example.finances.frameworks_and_drivers.database.account.AccountDatabase;
import com.example.finances.frameworks_and_drivers.database.api.ApiDao;
import com.example.finances.frameworks_and_drivers.database.api.ApiDatabase;
import com.example.finances.frameworks_and_drivers.database.investment.InvestmentDatabase;
import com.example.finances.frameworks_and_drivers.database.loan.LoanDatabase;
import com.example.finances.frameworks_and_drivers.database.operation.OperationDatabase;
import com.example.finances.frameworks_and_drivers.database.price.PriceDatabase;
import com.example.finances.frameworks_and_drivers.database.shop.ShopDatabase;
import com.example.finances.frameworks_and_drivers.database.shop.ShopItemDatabase;
import com.example.finances.frameworks_and_drivers.database.value_date.ValueDateDatabase;
import com.example.finances.frameworks_and_drivers.database.variables.VariableDatabase;

import javax.inject.Inject;

public class DatabaseHelper extends SQLiteOpenHelper {

    @Inject
    ApiDao apiDao;
    @Inject
    AccountDao accountDao;
    @Inject
    ShopService shopService;

    private boolean updating = false;
    private SQLiteDatabase updatingDB = null;

    private static volatile DatabaseHelper INSTANCE;

    public DatabaseHelper(Context context) {
        super(context, "finances.db", null, 17);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseHelper(context.getApplicationContext());
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.updating = true;
        this.updatingDB = db;
        db.execSQL(VariableDatabase.CreateTableString());

        db.execSQL(LoanDatabase.CreateTableString());
        db.execSQL(InvestmentDatabase.CreateTableString());
        db.execSQL(OperationDatabase.CreateTableString());
        db.execSQL(AccountDatabase.CreateTableString());

        db.execSQL(RelationsHelper.CreateTableString());

        accountDao.Initialize();
        this.updating = false;
        this.updatingDB = null;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 2) {
            this.updating = true;
            this.updatingDB = db;
            db.execSQL(LoanDatabase.CreateTableString());
            db.execSQL(InvestmentDatabase.CreateTableString());
            db.execSQL(OperationDatabase.CreateTableString());
            db.execSQL(AccountDatabase.CreateTableString());

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
            db.execSQL(OperationDatabase.DropTableString());
            db.execSQL(OperationDatabase.CreateTableString());
            this.updating = false;
            this.updatingDB = null;
        }

        else if ( newVersion == 7 && oldVersion != 7){
            this.updating = true;
            this.updatingDB = db;
            db.execSQL(RelationsHelper.DropLoanOperationsTableString());
            db.execSQL(RelationsHelper.DropInvestmentOperationsTableString());
            db.execSQL(RelationsHelper.DropAccountOperationsTableString());

            db.execSQL(AccountDatabase.DropTableString());
            db.execSQL(AccountDatabase.CreateTableString());
            db.execSQL(InvestmentDatabase.DropTableString());
            db.execSQL(InvestmentDatabase.CreateTableString());
            db.execSQL(LoanDatabase.DropTableString());
            db.execSQL(LoanDatabase.CreateTableString());
            db.execSQL(OperationDatabase.DropTableString());
            db.execSQL(OperationDatabase.CreateTableString());

            db.execSQL(RelationsHelper.CreateAccountOperationsTableString());
            db.execSQL(RelationsHelper.CreateLoanOperationsTableString());
            db.execSQL(RelationsHelper.CreateInvestmentOperationsTableString());
            this.updating = false;
            this.updatingDB = null;
        }

        else if ( newVersion == 8 && oldVersion != 8){
            this.updating = true;
            this.updatingDB = db;
            db.execSQL(ShopDatabase.CreateTableString());
            db.execSQL(ShopItemDatabase.CreateTableString());
            db.execSQL(PriceDatabase.CreateTableString());
            shopService.Initialize();
            this.updating = false;
            this.updatingDB = null;
        }

        else if ( newVersion == 9 && oldVersion != 9){
            this.updating = true;
            this.updatingDB = db;
            db.execSQL(PriceDatabase.DropTableString());
            db.execSQL(PriceDatabase.CreateTableString());
            this.updating = false;
            this.updatingDB = null;
        }

        else if ( newVersion == 10 && oldVersion != 10){
            this.updating = true;
            this.updatingDB = db;
            db.execSQL(InvestmentDatabase.DropTableString());
            db.execSQL(InvestmentDatabase.CreateTableString());

            db.execSQL(ShopItemDatabase.DropTableString());
            db.execSQL(ShopItemDatabase.CreateTableString());

            db.execSQL(PriceDatabase.DropTableString());
            db.execSQL(PriceDatabase.CreateTableString());

            db.execSQL(RelationsHelper.CreateShopItemPriceTableString());
            db.execSQL(RelationsHelper.CreateInvestmentPriceTableString());

            this.updating = false;
            this.updatingDB = null;
        }

        else if ( newVersion == 11 && oldVersion != 11){
            this.updating = true;
            this.updatingDB = db;

            db.execSQL(ApiDatabase.CreateTableString());
            db.execSQL(RelationsHelper.CreateInvestmentApiTableString());

            apiDao.InsertCoinMarketCapApi();

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

            db.execSQL(InvestmentDatabase.DropTableString());
            db.execSQL(PriceDatabase.DropTableString());
            db.execSQL(InvestmentDatabase.CreateTableString());
            db.execSQL(PriceDatabase.CreateTableString());

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

            db.execSQL(ValueDateDatabase.DropTableString());
            db.execSQL(ValueDateDatabase.CreateTableString());

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
