package com.nikola.simeonov.bindings;

import javax.inject.Singleton;

import com.nikola.simeonov.client.InterBankOperationsClient;
import com.nikola.simeonov.model.Bank;
import com.nikola.simeonov.persistence.AccountRepository;
import com.nikola.simeonov.persistence.BankRepository;
import com.nikola.simeonov.service.AccountService;
import com.nikola.simeonov.service.BankService;
import com.nikola.simeonov.service.ReceiverBankService;
import com.nikola.simeonov.service.SenderBankService;
import com.nikola.simeonov.service.TransferService;
import org.glassfish.jersey.internal.inject.AbstractBinder;

public class PaymentTransferAppBinder extends AbstractBinder {
    @Override
    protected void configure() {

        bind(AccountRepository.class).to(AccountRepository.class).in(Singleton.class);
        bind(BankRepository.class).to(BankRepository.class).in(Singleton.class);
        bind(TransferService.class).to(TransferService.class).in(Singleton.class);
        bind(BankService.class).to(SenderBankService.class).in(Singleton.class);
        bind(AccountService.class).to(AccountService.class).in(Singleton.class);
        bind(InterBankOperationsClient.class).to(InterBankOperationsClient.class).in(Singleton.class);
        bind(BankService.class).to(ReceiverBankService.class).in(Singleton.class);
        bind(Bank.class).to(Bank.class);

    }
}