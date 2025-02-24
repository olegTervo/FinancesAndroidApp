package com.example.finances.interface_adapters.dependency_injection;

import com.example.finances.frameworks_and_drivers.api_gateway.CoinMarketCapApiGateway;
import com.example.finances.frameworks_and_drivers.database.account.AccountDao;
import com.example.finances.frameworks_and_drivers.database.api.ApiDao;
import com.example.finances.frameworks_and_drivers.database.investment.InvestmentDao;
import com.example.finances.frameworks_and_drivers.database.loan.LoanDao;
import com.example.finances.frameworks_and_drivers.database.operation.OperationDao;
import com.example.finances.frameworks_and_drivers.database.price.PriceDao;
import com.example.finances.frameworks_and_drivers.database.shop.ShopDao;
import com.example.finances.frameworks_and_drivers.database.shop.ShopItemDao;
import com.example.finances.frameworks_and_drivers.database.value_date.ValueDateDao;
import com.example.finances.frameworks_and_drivers.database.variables.VariableDao;
import com.example.finances.interface_adapters.api.CoinMarketCapApi;
import com.example.finances.interface_adapters.repository.AccountRepository;
import com.example.finances.interface_adapters.repository.ApiRepository;
import com.example.finances.interface_adapters.repository.InvestmentRepository;
import com.example.finances.interface_adapters.repository.LoanRepository;
import com.example.finances.interface_adapters.repository.OperationRepository;
import com.example.finances.interface_adapters.repository.PriceRepository;
import com.example.finances.interface_adapters.repository.ShopItemRepository;
import com.example.finances.interface_adapters.repository.ShopRepository;
import com.example.finances.interface_adapters.repository.ValueDateRepository;
import com.example.finances.interface_adapters.repository.VariableRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class Dependencies {

    @Provides
    @Singleton
    public static AccountRepository provideAccountRepository(AccountDao dao) {
        return new AccountRepository(dao);
    }

    @Provides
    @Singleton
    public static ApiRepository provideApiRepository(ApiDao dao) {
        return new ApiRepository(dao);
    }

    @Provides
    @Singleton
    public static InvestmentRepository provideInvestmentRepository(InvestmentDao dao) {
        return new InvestmentRepository(dao);
    }

    @Provides
    @Singleton
    public static LoanRepository provideLoanRepository(LoanDao dao) {
        return new LoanRepository(dao);
    }

    @Provides
    @Singleton
    public static OperationRepository provideOperationRepository(OperationDao dao) {
        return new OperationRepository(dao);
    }

    @Provides
    @Singleton
    public static PriceRepository providePriceRepository(PriceDao dao) {
        return new PriceRepository(dao);
    }

    @Provides
    @Singleton
    public static ShopRepository provideShopRepository(ShopDao dao) {
        return new ShopRepository(dao);
    }

    @Provides
    @Singleton
    public static ShopItemRepository provideShopItemRepository(ShopItemDao dao) {
        return new ShopItemRepository(dao);
    }

    @Provides
    @Singleton
    public static ValueDateRepository provideValueDateRepository(ValueDateDao dao) {
        return new ValueDateRepository(dao);
    }

    @Provides
    @Singleton
    public static VariableRepository provideVariableRepository(VariableDao dao) {
        return new VariableRepository(dao);
    }

    @Provides
    @Singleton
    public static CoinMarketCapApi provideCoinMarketCupApi(CoinMarketCapApiGateway api, PriceDao priceDao) {
        return new CoinMarketCapApi(api, priceDao);
    }
}
