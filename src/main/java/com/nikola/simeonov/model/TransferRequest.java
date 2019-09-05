package com.nikola.simeonov.model;

import java.math.BigDecimal;
import java.util.Currency;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import lombok.ToString;

@Getter
@ToString
public class TransferRequest {

    @DecimalMin(value = "0.0", inclusive = false)
    private final BigDecimal transferAmount;
    @NotBlank
    private final String originatorAccountId;
    @NotBlank
    private final String receiverAccountId;
    @NotNull
    private final Currency currency;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public TransferRequest(@JsonProperty("from") String from,
                           @JsonProperty("to") String to,
                           @JsonProperty("transferAmount") BigDecimal transferAmount,
                           @JsonProperty("currency") Currency currency) {
        this.originatorAccountId = from;
        this.receiverAccountId = to;
        this.transferAmount = transferAmount;
        this.currency = currency;

    }
}
