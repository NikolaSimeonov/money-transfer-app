package com.nikola.simeonov.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

import javax.inject.Inject;

import com.nikola.simeonov.model.Transaction;
import com.nikola.simeonov.model.TransactionStatus;
import com.nikola.simeonov.model.TransferResponse;

public class TransferService {

    @Inject
    private SenderBankService servicingBank;


    public TransferResponse transfer(String originatorAccountId, String receiverAccountId, BigDecimal amount,
                                     Currency currency) {
        Transaction transaction =
          new Transaction((UUID.randomUUID().toString()), amount, LocalDateTime.now(), originatorAccountId,
            receiverAccountId, TransactionStatus.PENDING, currency);

        return servicingBank.sendTransaction(transaction);
    }
}
