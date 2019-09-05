package com.nikola.simeonov.model;

import java.math.BigDecimal;
import java.util.Currency;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.collections4.map.LinkedMap;

@Getter
@ToString
public class Account {

    @NotBlank
    private final String id;

    @NotBlank
    private final String bankId;

    private final LinkedMap<String, TransactionLog> transactionLogList;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Account(@JsonProperty("id") String id, @JsonProperty("bankId") String bankId,
                   @JsonProperty("transactionLogList") LinkedMap<String, TransactionLog> transactionLogList) {
        this.id = id;
        this.bankId = bankId;
        this.transactionLogList = transactionLogList;
    }


    public synchronized BigDecimal getBalance() {
        BigDecimal balance;
        if (!transactionLogList.isEmpty()) {
            balance = transactionLogList.get(transactionLogList.lastKey())
              .getBalanceAfterTransaction();
        } else {
            balance = BigDecimal.ZERO;
        }
        return balance;
    }

    public synchronized Currency getCurrency() {
        Currency currency;
        if (!transactionLogList.isEmpty()) {
            currency = transactionLogList.get(transactionLogList.lastKey())
              .getCurrency();
        } else {
            currency = Currency.getInstance("EUR");
        }
        return currency;
    }

    public synchronized BigDecimal getReservedBalance() {
        BigDecimal reservedBalance;
        if (!transactionLogList.isEmpty()) {
            reservedBalance = transactionLogList.get(transactionLogList.lastKey()).getReservedBalanceAfterTransaction();
        } else {
            reservedBalance = BigDecimal.ZERO;
        }
        return reservedBalance;
    }

}
