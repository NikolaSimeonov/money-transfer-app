package com.nikola.simeonov.model;

import java.math.BigDecimal;
import java.util.Currency;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public
class TransactionLog {
    private final  Transaction transaction;
    @NotNull
    private final BigDecimal balanceAfterTransaction;
    @DecimalMin(value = "0.0", inclusive = false)
    private final BigDecimal reservedBalanceAfterTransaction;
    @NotNull
    private final Currency currency;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public TransactionLog(
      @JsonProperty("transaction") Transaction transaction,
      @JsonProperty("balanceAfterTransaction") BigDecimal balanceAfterTransaction,
      @JsonProperty("reservedBalanceAfterTransaction") BigDecimal reservedBalanceAfterTransaction,
      @JsonProperty("currency") Currency currency ) {
        this.transaction = transaction;
        this.balanceAfterTransaction = balanceAfterTransaction;
        this.reservedBalanceAfterTransaction = reservedBalanceAfterTransaction;
        this.currency = currency;
    }
}
