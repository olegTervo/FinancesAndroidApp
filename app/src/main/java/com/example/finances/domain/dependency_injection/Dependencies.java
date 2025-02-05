package com.example.finances.domain.dependency_injection;

import com.example.finances.domain.interfaces.IAccountRepository;
import com.example.finances.domain.services.BankService;
import com.example.finances.interface_adapters.repository.AccountRepository;

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
    public static BankService provideBankService(IAccountRepository repository) {
        return new BankService(repository);
    }
}
