package com.example.finances.frameworks_and_drivers.dependency_injection;

import android.content.Context;

import com.example.finances.domain.enums.ApiType;
import com.example.finances.domain.models.Api;
import com.example.finances.frameworks_and_drivers.api_gateway.ApiClient;
import com.example.finances.frameworks_and_drivers.api_gateway.CoinMarketCapApiGateway;
import com.example.finances.frameworks_and_drivers.api_gateway.ApiInterface;
import com.example.finances.frameworks_and_drivers.database.account.AccountDao;
import com.example.finances.frameworks_and_drivers.database.account.AccountDatabase;
import com.example.finances.frameworks_and_drivers.database.api.ApiDao;
import com.example.finances.frameworks_and_drivers.database.api.ApiDatabase;
import com.example.finances.frameworks_and_drivers.database.common.DatabaseHelper;
import com.example.finances.frameworks_and_drivers.database.investment.InvestmentDao;
import com.example.finances.frameworks_and_drivers.database.investment.InvestmentDatabase;
import com.example.finances.frameworks_and_drivers.database.loan.LoanDao;
import com.example.finances.frameworks_and_drivers.database.loan.LoanDatabase;
import com.example.finances.frameworks_and_drivers.database.operation.OperationDao;
import com.example.finances.frameworks_and_drivers.database.operation.OperationDatabase;
import com.example.finances.frameworks_and_drivers.database.price.PriceDao;
import com.example.finances.frameworks_and_drivers.database.price.PriceDatabase;
import com.example.finances.frameworks_and_drivers.database.shop.ShopDao;
import com.example.finances.frameworks_and_drivers.database.shop.ShopDatabase;
import com.example.finances.frameworks_and_drivers.database.shop.ShopItemDao;
import com.example.finances.frameworks_and_drivers.database.shop.ShopItemDatabase;
import com.example.finances.frameworks_and_drivers.database.value_date.ValueDateDao;
import com.example.finances.frameworks_and_drivers.database.value_date.ValueDateDatabase;
import com.example.finances.frameworks_and_drivers.database.variables.VariableDao;
import com.example.finances.frameworks_and_drivers.database.variables.VariableDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class Dependencies {

    @Provides
    @Singleton
    public static DatabaseHelper provideDefaultDB(@ApplicationContext Context context) {
        return DatabaseHelper.getInstance(context);
    }

    @Provides
    @Singleton
    public static AccountDatabase provideAccountDatabase(@ApplicationContext Context context) {
        return AccountDatabase.getInstance(context);
    }

    @Provides
    @Singleton
    public static AccountDao provideAccountDao(AccountDatabase accountDatabase) {
        return new AccountDao(accountDatabase);
    }

    @Provides
    @Singleton
    public static ApiDatabase provideApiDatabase(@ApplicationContext Context context) {
        return ApiDatabase.getInstance(context);
    }

    @Provides
    @Singleton
    public static ApiDao provideApiDao(ApiDatabase apiDatabase) {
        return new ApiDao(apiDatabase);
    }

    @Provides
    @Singleton
    public static InvestmentDatabase provideInvestmentDatabase(@ApplicationContext Context context) {
        return InvestmentDatabase.getInstance(context);
    }

    @Provides
    @Singleton
    public static InvestmentDao provideInvestmentDao(InvestmentDatabase investmentDatabase) {
        return new InvestmentDao(investmentDatabase);
    }

    @Provides
    @Singleton
    public static LoanDatabase provideLoanDatabase(@ApplicationContext Context context) {
        return LoanDatabase.getInstance(context);
    }

    @Provides
    @Singleton
    public static LoanDao provideLoanDao(LoanDatabase loanDatabase) {
        return new LoanDao(loanDatabase);
    }

    @Provides
    @Singleton
    public static OperationDatabase provideOperationDatabase(@ApplicationContext Context context) {
        return OperationDatabase.getInstance(context);
    }

    @Provides
    @Singleton
    public static OperationDao provideOperationDao(OperationDatabase operationDatabase) {
        return new OperationDao(operationDatabase);
    }

    @Provides
    @Singleton
    public static PriceDatabase providePriceDatabase(@ApplicationContext Context context) {
        return PriceDatabase.getInstance(context);
    }

    @Provides
    @Singleton
    public static PriceDao providePriceDao(PriceDatabase priceDatabase) {
        return new PriceDao(priceDatabase);
    }

    @Provides
    @Singleton
    public static ShopDatabase provideShopDatabase(@ApplicationContext Context context) {
        return ShopDatabase.getInstance(context);
    }

    @Provides
    @Singleton
    public static ShopDao provideShopDao(ShopDatabase shopDatabase) {
        return new ShopDao(shopDatabase);
    }

    @Provides
    @Singleton
    public static ShopItemDatabase provideShopItemDatabase(@ApplicationContext Context context) {
        return ShopItemDatabase.getInstance(context);
    }

    @Provides
    @Singleton
    public static ShopItemDao provideShopItemDao(ShopItemDatabase shopItemDatabase) {
        return new ShopItemDao(shopItemDatabase);
    }

    @Provides
    @Singleton
    public static ValueDateDatabase provideValueDateDatabase(@ApplicationContext Context context) {
        return ValueDateDatabase.getInstance(context);
    }

    @Provides
    @Singleton
    public static ValueDateDao provideValueDateDao(ValueDateDatabase valueDateDatabase) {
        return new ValueDateDao(valueDateDatabase);
    }

    @Provides
    @Singleton
    public static VariableDatabase provideVariableDatabase(@ApplicationContext Context context) {
        return VariableDatabase.getInstance(context);
    }

    @Provides
    @Singleton
    public static VariableDao provideVariableDao(VariableDatabase variableDatabase) {
        return new VariableDao(variableDatabase);
    }

    @Provides
    @Singleton
    public static Api provideCoinMarketCapApiInstance(ApiDao dao) {
        return dao.GetApi(ApiType.CoinMarketCap);
    }

    @Provides
    @Singleton
    public static ApiInterface provideApiInterface(Api coinMarketCap) {
        return ApiClient
                .getClient(coinMarketCap.getLink())
                .create(ApiInterface.class);
    }

    @Provides
    @Singleton
    public static CoinMarketCapApiGateway provideCoinMarketCapApiGateway(Api api, ApiInterface apiInterface) {
        return new CoinMarketCapApiGateway(api, apiInterface);
    }

}
