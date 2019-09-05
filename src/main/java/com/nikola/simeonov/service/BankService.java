package com.nikola.simeonov.service;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

import com.nikola.simeonov.client.InterBankOperationsClient;
import com.nikola.simeonov.exception.BankNotFoundException;
import com.nikola.simeonov.model.Account;
import com.nikola.simeonov.model.Bank;
import com.nikola.simeonov.model.BankTransferResponse;
import com.nikola.simeonov.model.Transaction;
import com.nikola.simeonov.model.TransactionStatus;
import com.nikola.simeonov.model.TransferResponse;
import com.nikola.simeonov.persistence.BankRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BankService implements SenderBankService, ReceiverBankService {

    @Inject
    BankRepository bankRepository;

    @Inject
    AccountService accountService;

    @Inject
    InterBankOperationsClient interBankOperationsClient;


    public Future<BankTransferResponse> acceptTransaction(Transaction transactionAsReceived) {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        return executor.submit(() -> {
            Transaction finished = accountService.deposit(transactionAsReceived);
            return BankTransferResponse
              .builder()
              .transaction(finished)
              .timeStamp(LocalDateTime.now())
              .senderBankId(getBankByAccountId(transactionAsReceived.getOriginatorAccountId()).getId())
              .build();
        });
    }

    public TransferResponse sendTransaction(Transaction transaction) {
        accountService.withdraw(transaction);
        if (!bankExistForAccountId(transaction.getReceiverAccountId())) {
            accountService.rejectTransaction(transaction);
            throw new BankNotFoundException(getBankByAccountId(transaction.getReceiverAccountId()).getId());
        }
        BankTransferResponse bankTransferResponse = null;
        try {
            bankTransferResponse = interBankOperationsClient.send(transaction).get(5000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            accountService.abortTransaction(transaction);
            log.error("Error occurred when trying to send transaction={}, error={} ", transaction, e.getCause());
        }
        if (bankTransferResponse.getTransaction().getTransactionStatus() != TransactionStatus.EXECUTED) {
            accountService.abortTransaction(transaction);
        } else {
            accountService.finalizeSendTransaction(bankTransferResponse.getTransaction());
        }
        return new TransferResponse(bankTransferResponse.toString());
    }

    private Account getOriginatorAccount(Transaction transactionAsReceived) {
        return getBankByAccountId(transactionAsReceived.getOriginatorAccountId())
          .getAccounts()
          .get(transactionAsReceived.getOriginatorAccountId());
    }

    private Bank getBankByAccountId(String accountId) {
        return bankRepository.getBankById(extractCountryCodeAndBankIdFromAccountId(accountId));
    }

    private boolean bankExistForAccountId(String accountId) {
        return bankRepository.getBankById(extractCountryCodeAndBankIdFromAccountId(accountId)) != null;
    }

    private String extractCountryCodeAndBankIdFromAccountId(String accountId) {
        return accountId.subSequence(0, 8).toString();
    }
}
