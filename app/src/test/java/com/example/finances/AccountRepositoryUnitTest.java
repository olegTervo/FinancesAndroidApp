package com.example.finances;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import com.example.finances.frameworks_and_drivers.database.account.AccountDao;
import com.example.finances.interface_adapters.repository.AccountRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AccountRepositoryUnitTest {
    @Mock
    private AccountDao accountDao;

    private AccountRepository repository;

    @Before
    public void prepare() {
        MockitoAnnotations.openMocks(this);
        repository = new AccountRepository(accountDao);
    }

    @Test
    public void test_exists() {
        when(accountDao.AccountExists(1)).thenReturn(true);
        assertTrue(repository.Exists(1));
    }
}
