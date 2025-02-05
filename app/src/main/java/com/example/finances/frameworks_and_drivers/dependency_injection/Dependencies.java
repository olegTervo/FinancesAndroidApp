package com.example.finances.frameworks_and_drivers.dependency_injection;

import android.content.Context;

import com.example.finances.frameworks_and_drivers.database.account.AccountDao;
import com.example.finances.frameworks_and_drivers.database.account.AccountDatabase;

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
    public static AccountDatabase provideAccountDatabase(@ApplicationContext Context context) {
        return AccountDatabase.getInstance(context);
    }

    @Provides
    @Singleton
    public static AccountDao provideAccountDao(AccountDatabase accountDatabase) {
        return new AccountDao(accountDatabase);
    }
}
