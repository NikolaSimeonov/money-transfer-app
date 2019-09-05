package com.nikola.simeonov.persistence;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.nikola.simeonov.exception.AccountAlreadyExistException;
import com.nikola.simeonov.exception.BankAlreadyExistException;
import com.nikola.simeonov.exception.BankNotFoundException;
import com.nikola.simeonov.model.Account;
import com.nikola.simeonov.model.Bank;

public class BankRepository {

    private Map<String, Bank> banks = new ConcurrentHashMap<>();

    public void saveBank(String bankId){
        if(banks.containsKey(bankId))
        {
            throw new BankAlreadyExistException(bankId);
        }
        banks.put(bankId,new Bank(bankId));
    }
    public Bank getBankById(String id){
        return this.banks.get(id);
    }

    void addAccountToBank(Account account){
        Bank bank = banks.get(account.getBankId());
        if(bank != null){
            if(bank.getAccounts().get(account.getId()) == null){
            bank.getAccounts().put(account.getId(),account);
            } else {
                throw new AccountAlreadyExistException(account.getId());
            }
        } else {
            throw new BankNotFoundException(account.getBankId());
        }
    }
}
