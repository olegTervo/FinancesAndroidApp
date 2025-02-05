package com.example.finances.interface_adapters.dependency_injection;

import com.example.finances.frameworks_and_drivers.database.account.AccountDao;
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
    public static AccountRepository provideAccountRepository(AccountDao dao) {
        return new AccountRepository(dao);
    }
}
