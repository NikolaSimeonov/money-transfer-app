package com.nikola.simeonov.persistence;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import com.nikola.simeonov.model.Account;

public class AccountRepository {

    private Map<String, Account> accounts = new ConcurrentHashMap<>();
    private final BankRepository bankRepository;

    @Inject
    public AccountRepository(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public void saveAccount(Account account){
            this.accounts.put(account.getId(), account);
            bankRepository.addAccountToBank(account);
    }


    public Account getAccountById(String id){
        return this.accounts.get(id);
    }
}
