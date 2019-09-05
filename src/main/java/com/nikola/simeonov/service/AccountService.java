package com.nikola.simeonov.service;

import static java.util.UUID.randomUUID;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.inject.Inject;

import com.nikola.simeonov.exception.AccountNotFoundException;
import com.nikola.simeonov.exception.CurrencyMismatchException;
import com.nikola.simeonov.exception.InsufficientBalanceException;
import com.nikola.simeonov.model.Account;
import com.nikola.simeonov.model.Transaction;
import com.nikola.simeonov.model.TransactionLog;
import com.nikola.simeonov.model.TransactionStatus;
import com.nikola.simeonov.persistence.AccountRepository;

public class AccountService {

    private final AccountRepository accountRepository;

    @Inject
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    Transaction deposit(Transaction deposit) {
        Account account = accountRepository.getAccountById(deposit.getReceiverAccountId());
        if (account != null) {
            return finalizeReceivedTransaction(deposit);
        } else {
            return rejectTransaction(deposit);
        }
    }


    void withdraw(Transaction withdraw) {
        Account account = accountRepository.getAccountById(withdraw.getOriginatorAccountId());
        if (account == null) {
            throw new AccountNotFoundException(withdraw.getOriginatorAccountId());
        }
        synchronized (account) {
            if (account.getBalance().subtract(withdraw.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
                rejectTransaction(withdraw);
                throw new InsufficientBalanceException(account.getId());
            }
            if (!account.getCurrency().equals(withdraw.getCurrency())) {
                rejectTransaction(withdraw);
                throw new CurrencyMismatchException(account.getCurrency(),withdraw.getCurrency());
            }
            account.getTransactionLogList().put(randomUUID().toString(), new TransactionLog(withdraw,
              account.getBalance().subtract(withdraw.getAmount()),
              account.getReservedBalance().add(withdraw.getAmount()),withdraw.getCurrency()));
        }
    }

    void abortTransaction(Transaction transaction) {
        Transaction aborted =
          new Transaction(transaction.getId(), transaction.getAmount(), LocalDateTime.now(),
            transaction.getOriginatorAccountId(), transaction.getReceiverAccountId(), TransactionStatus.ABORTED,
            transaction.getCurrency());
        Account account = accountRepository.getAccountById(transaction.getOriginatorAccountId());
        synchronized (account) {
            account.getTransactionLogList().put(randomUUID().toString(), new TransactionLog(aborted,
              account.getBalance().add(aborted.getAmount()),
              account.getReservedBalance().subtract(aborted.getAmount()),
            transaction.getCurrency()));
        }
    }

    Transaction rejectTransaction(Transaction transactionAsReceived) {
        Transaction rejectedTransaction = new Transaction(transactionAsReceived.getId(), transactionAsReceived.getAmount(),
          transactionAsReceived.getTimestamp(), transactionAsReceived.getOriginatorAccountId(),
          transactionAsReceived.getReceiverAccountId(), TransactionStatus.REJECTED,
          transactionAsReceived.getCurrency());
        Account account = accountRepository.getAccountById(rejectedTransaction.getOriginatorAccountId());
        if (account != null) {
            synchronized (account) {
                account.getTransactionLogList().put(randomUUID().toString(), new TransactionLog(rejectedTransaction,
                  account.getBalance(),
                  account.getReservedBalance(),transactionAsReceived.getCurrency()));
            }
        }
        return rejectedTransaction;
    }


     void finalizeSendTransaction(Transaction transaction) {
        Account account = accountRepository.getAccountById(transaction.getOriginatorAccountId());
        Transaction finished = new Transaction(
          transaction.getId(),
          transaction.getAmount(),
          transaction.getTimestamp(),
          transaction.getOriginatorAccountId(),
          transaction.getReceiverAccountId(),
          TransactionStatus.EXECUTED,
          transaction.getCurrency());
        synchronized (account) {
            account.getTransactionLogList()
              .put(randomUUID().toString(), new TransactionLog(finished, account.getBalance(),
                account.getReservedBalance().subtract(finished.getAmount()),transaction.getCurrency()));
        }
    }

    private Transaction finalizeReceivedTransaction(Transaction deposit) {
        Account account = accountRepository.getAccountById(deposit.getReceiverAccountId());
        Transaction finished = new Transaction(
          deposit.getId(),
          deposit.getAmount(),
          deposit.getTimestamp(),
          deposit.getOriginatorAccountId(),
          deposit.getReceiverAccountId(),
          TransactionStatus.EXECUTED,
          deposit.getCurrency());
        synchronized (account) {
            account.getTransactionLogList()
              .put(randomUUID().toString(), new TransactionLog(finished,
                account.getBalance().add(finished.getAmount()),
                account.getReservedBalance(),deposit.getCurrency()));
            return finished;
        }
    }
}
