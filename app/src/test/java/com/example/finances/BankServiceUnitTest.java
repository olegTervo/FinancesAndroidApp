package com.example.finances;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import com.example.finances.domain.interfaces.IAccountRepository;
import com.example.finances.domain.interfaces.ILoanRepository;
import com.example.finances.domain.interfaces.IOperationRepository;
import com.example.finances.domain.interfaces.IValueDateRepository;
import com.example.finances.domain.services.BankService;
import com.example.finances.frameworks_and_drivers.database.common.DatabaseHelper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BankServiceUnitTest {
    @Mock
    private IAccountRepository accountRepository;
    @Mock
    private ILoanRepository loanRepository;
    @Mock
    private IOperationRepository operationRepository;
    @Mock
    private IValueDateRepository valueDateRepository;
    @Mock
    private DatabaseHelper db;

    private BankService bankService;

    @Before
    public void prepare() {
        MockitoAnnotations.openMocks(this);
        bankService = new BankService(accountRepository, loanRepository, operationRepository, valueDateRepository, db);
    }

    @Test
    public void test_getMoney() {
        when(accountRepository.GetMoney(1)).thenReturn(10);

        int money = bankService.GetBankMoney();
        assertEquals(10, money);
    }
}
