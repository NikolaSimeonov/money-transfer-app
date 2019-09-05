package com.nikola.simeonov.client;

import java.util.concurrent.Future;
import javax.inject.Inject;
import javax.inject.Provider;

import com.nikola.simeonov.model.BankTransferResponse;
import com.nikola.simeonov.model.Transaction;
import com.nikola.simeonov.service.ReceiverBankService;

/**
 * Simulation of network transaction transfer.
 * In our case we dont have networking and instead will call
 * the receiving Bank Service directly
 *
 */
public class InterBankOperationsClient {

    @Inject
    Provider<ReceiverBankService> receiverBankServiceProvider;

    public Future<BankTransferResponse> send(Transaction transaction) {
        return receiverBankServiceProvider.get().acceptTransaction(transaction);
    }
}
