package com.example.finances.domain.dependency_injection;

import com.example.finances.domain.interfaces.IAccountRepository;
import com.example.finances.domain.interfaces.IApiRepository;
import com.example.finances.domain.interfaces.IInvestmentRepository;
import com.example.finances.domain.interfaces.ILoanRepository;
import com.example.finances.domain.interfaces.IOperationRepository;
import com.example.finances.domain.interfaces.IPriceRepository;
import com.example.finances.domain.interfaces.IShopItemRepository;
import com.example.finances.domain.interfaces.IShopRepository;
import com.example.finances.domain.interfaces.IValueDateRepository;
import com.example.finances.domain.interfaces.IVariableRepository;
import com.example.finances.domain.services.BankService;
import com.example.finances.domain.services.InvestmentsService;
import com.example.finances.domain.services.ShopService;
import com.example.finances.domain.services.VariablesService;
import com.example.finances.frameworks_and_drivers.database.common.DatabaseHelper;
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
    public static IAccountRepository provideAccountRepository(AccountRepository repository) {
        return repository;
    }

    @Provides
    @Singleton
    public static IApiRepository provideApiRepository(ApiRepository repository) {
        return repository;
    }

    @Provides
    @Singleton
    public static IInvestmentRepository provideInvestmentRepository(InvestmentRepository repository) {
        return repository;
    }

    @Provides
    @Singleton
    public static ILoanRepository provideLoanRepository(LoanRepository repository) {
        return repository;
    }

    @Provides
    @Singleton
    public static IOperationRepository provideOperationRepository(OperationRepository repository) {
        return repository;
    }

    @Provides
    @Singleton
    public static IPriceRepository providePriceRepository(PriceRepository repository) {
        return repository;
    }

    @Provides
    @Singleton
    public static IShopItemRepository provideShopItemRepository(ShopItemRepository repository) {
        return repository;
    }

    @Provides
    @Singleton
    public static IShopRepository provideShopRepository(ShopRepository repository) {
        return repository;
    }

    @Provides
    @Singleton
    public static IValueDateRepository provideValueDateRepository(ValueDateRepository repository) {
        return repository;
    }

    @Provides
    @Singleton
    public static IVariableRepository provideVariableRepository(VariableRepository repository) {
        return repository;
    }

    @Provides
    @Singleton
    public static BankService provideBankService(
            IAccountRepository accountRepository,
            ILoanRepository loanRepository,
            IOperationRepository operationRepository,
            IValueDateRepository valueDateRepository,
            DatabaseHelper db) {
        return new BankService(accountRepository, loanRepository, operationRepository, valueDateRepository, db);
    }

    @Provides
    @Singleton
    public static InvestmentsService provideInvestmentService(
            IInvestmentRepository investmentRepository,
            IApiRepository apiRepository,
            IPriceRepository priceRepository,
            IValueDateRepository valueDateRepository,
            DatabaseHelper helper)
    {
        return new InvestmentsService(investmentRepository, apiRepository, priceRepository, valueDateRepository, helper);
    }

    @Provides
    @Singleton
    public static ShopService provideShopService(
            IAccountRepository accountRepository,
            IShopRepository shopRepository,
            IShopItemRepository shopItemRepository,
            IPriceRepository priceRepository,
            DatabaseHelper helper)
    {
        return new ShopService(accountRepository, shopRepository, shopItemRepository, priceRepository, helper);
    }

    @Provides
    @Singleton
    public static VariablesService provideVariablesService(
            IValueDateRepository valueDateRepository,
            IVariableRepository variableRepository
    )
    {
        return new VariablesService(valueDateRepository, variableRepository);
    }
}
