package com.nikola.simeonov.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

import com.nikola.simeonov.exception.CurrencyMismatchException;
import com.nikola.simeonov.exception.InsufficientBalanceException;
import com.nikola.simeonov.model.Account;
import com.nikola.simeonov.model.Transaction;
import com.nikola.simeonov.model.TransactionLog;
import com.nikola.simeonov.model.TransactionStatus;
import com.nikola.simeonov.persistence.AccountRepository;
import org.apache.commons.collections4.map.LinkedMap;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    private static final String BANK_ID_1 = "bankId01";
    private static final String BANK_ID_2 = "bankId02";

    private static final String ACCOUNT_ID_1 = BANK_ID_1 + "accountId1";
    private static final String ACCOUNT_ID_2 = BANK_ID_2 + "accountId2";

    private static final String TRANSACTION_LOG_ID_1 = "transactionLogId1";
    private static final String TRANSACTION_LOG_ID_2 = "transactionLogId2";
    private static final String TRANSACTION_ID_1 = "transactionId1";
    private static final String TRANSACTION_ID_2 = "transactionId2";
    private static final String TRANSACTION_ID_3 = "transactionId3";
    public static final BigDecimal ACCOUNT_1_INITIAL_BALANCE = new BigDecimal(1500);
    public static final BigDecimal ACCOUNT_2_INITIAL_BALANCE = new BigDecimal(2000);
    public static final BigDecimal DEPOSIT_AMOUNT = new BigDecimal(200);
    public static final BigDecimal WITHDRAW_AMOUNT = new BigDecimal(400);



    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    AccountService accountService;
    private Account account1;
    private Account account2;

    @Before
    public void setUp() {

        account1 = createAccount1();
        account2 = createAccount2();
        when(accountRepository.getAccountById(ACCOUNT_ID_1)).thenReturn(account1);
        when(accountRepository.getAccountById(ACCOUNT_ID_2)).thenReturn(account2);

    }

    @Test
    public void whenDepositTransactionThenUpdateLog() {
        Transaction deposit = new Transaction(TRANSACTION_ID_3, DEPOSIT_AMOUNT, LocalDateTime.now(), ACCOUNT_ID_2,
          ACCOUNT_ID_1, TransactionStatus.PENDING, Currency.getInstance("EUR"));
        accountService.deposit(deposit);
        verify(accountRepository, times(2)).getAccountById(ACCOUNT_ID_1);
        Assert.assertEquals(ACCOUNT_1_INITIAL_BALANCE.add(DEPOSIT_AMOUNT), account1.getBalance());
        Assert.assertEquals(BigDecimal.ZERO, account1.getReservedBalance());
        Assert.assertEquals(2, account1.getTransactionLogList().size());
    }

    @Test
    public void whenWithdrawTransactionThenUpdateLog() {
        Transaction withdraw = new Transaction(TRANSACTION_ID_3, WITHDRAW_AMOUNT, LocalDateTime.now(), ACCOUNT_ID_2,
          ACCOUNT_ID_1, TransactionStatus.PENDING, Currency.getInstance("EUR"));
        accountService.withdraw(withdraw);
        verify(accountRepository, times(1)).getAccountById(ACCOUNT_ID_2);
        Assert.assertEquals(ACCOUNT_2_INITIAL_BALANCE.subtract(withdraw.getAmount()), account2.getBalance());
        Assert.assertEquals(BigDecimal.ZERO.add(withdraw.getAmount()), account2.getReservedBalance());
        Assert.assertEquals(2, account2.getTransactionLogList().size());
    }

    @Test
    public void whenWithdrawMoreThanBalanceThenThrowException() {
        Transaction withdraw = new Transaction(TRANSACTION_ID_3, ACCOUNT_2_INITIAL_BALANCE.add(BigDecimal.ONE), LocalDateTime.now(), ACCOUNT_ID_2,
          ACCOUNT_ID_1, TransactionStatus.PENDING, Currency.getInstance("EUR"));
        assertThatThrownBy(() -> {
            accountService.withdraw(withdraw);
        }).isInstanceOf(InsufficientBalanceException.class);
        Assert.assertEquals(2,account2.getTransactionLogList().size());
        Assert.assertEquals(ACCOUNT_2_INITIAL_BALANCE,account2.getBalance());
        Assert.assertEquals(BigDecimal.ZERO,account2.getReservedBalance());


    }

    @Test
    public void whenWithdrawDifferentCurrencyThenThrowException() {
        Transaction withdraw = new Transaction(TRANSACTION_ID_3, ACCOUNT_2_INITIAL_BALANCE.add(BigDecimal.ONE), LocalDateTime.now(), ACCOUNT_ID_2,
          ACCOUNT_ID_1, TransactionStatus.PENDING, Currency.getInstance("GBP"));
        assertThatThrownBy(() -> {
            accountService.withdraw(withdraw);
        }).isInstanceOf(InsufficientBalanceException.class);
        Assert.assertEquals(2,account2.getTransactionLogList().size());
        Assert.assertEquals(ACCOUNT_2_INITIAL_BALANCE,account2.getBalance());
        Assert.assertEquals(BigDecimal.ZERO,account2.getReservedBalance());}

    private Account createAccount1() {
        Transaction transaction =
          new Transaction(TRANSACTION_ID_1, ACCOUNT_1_INITIAL_BALANCE, LocalDateTime.now(), ACCOUNT_ID_1,
            ACCOUNT_ID_2, TransactionStatus.EXECUTED, Currency.getInstance("EUR"));
        TransactionLog transactionLog = new TransactionLog(transaction, new BigDecimal(1500), BigDecimal.ZERO,
          Currency.getInstance("EUR"));
        LinkedMap<String, TransactionLog> transactionLogMap = new LinkedMap<>();
        transactionLogMap.put(TRANSACTION_LOG_ID_1, transactionLog);
        return new Account(ACCOUNT_ID_1, BANK_ID_1, transactionLogMap);
    }

    private Account createAccount2() {
        Transaction transaction =
          new Transaction(TRANSACTION_ID_2, ACCOUNT_2_INITIAL_BALANCE, LocalDateTime.now(), ACCOUNT_ID_1,
            ACCOUNT_ID_2, TransactionStatus.EXECUTED, Currency.getInstance("EUR"));
        TransactionLog transactionLog = new TransactionLog(transaction, new BigDecimal(2000), BigDecimal.ZERO,
          Currency.getInstance("EUR"));
        LinkedMap<String, TransactionLog> transactionLogMap = new LinkedMap<>();
        transactionLogMap.put(TRANSACTION_LOG_ID_2, transactionLog);
        return new Account(ACCOUNT_ID_2, BANK_ID_2, transactionLogMap);
    }


}
