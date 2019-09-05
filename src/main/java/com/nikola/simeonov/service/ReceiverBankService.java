package com.nikola.simeonov.service;

import java.util.concurrent.Future;

import com.nikola.simeonov.model.BankTransferResponse;
import com.nikola.simeonov.model.Transaction;

public interface ReceiverBankService {

    Future<BankTransferResponse> acceptTransaction(Transaction transactionAsReceived);
}
