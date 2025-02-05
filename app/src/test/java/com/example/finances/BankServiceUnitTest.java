package com.example.finances;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import com.example.finances.domain.interfaces.IAccountRepository;
import com.example.finances.domain.services.BankService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BankServiceUnitTest {
    @Mock
    private IAccountRepository repository;

    private BankService bankService;

    @Before
    public void prepare() {
        MockitoAnnotations.openMocks(this);
        bankService = new BankService(repository);
    }

    @Test
    public void test_getMoney() {
        when(repository.GetMoney(1)).thenReturn(10);

        int money = bankService.GetBankMoney();
        assertEquals(10, money);
    }
}
