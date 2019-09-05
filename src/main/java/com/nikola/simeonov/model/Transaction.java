package com.nikola.simeonov.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Transaction {

    @NotBlank
    private final String id;
    @DecimalMin(value = "0.0", inclusive = false)
    private final BigDecimal amount;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @NotNull
    private final LocalDateTime timestamp;
    @NotBlank
    private final String originatorAccountId;
    @NotBlank
    private final String receiverAccountId;
    private final TransactionStatus transactionStatus;
    @NotNull
    private final Currency currency;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Transaction(
      @JsonProperty("id") String id,
      @JsonProperty("amount") BigDecimal amount,
      @JsonProperty("timestamp") LocalDateTime timestamp,
      @JsonProperty("originatorAccountId") String originatorAccountId,
      @JsonProperty("receiverAccountId") String receiverAccountId,
      @JsonProperty("transactionStatus") TransactionStatus transactionStatus,
      @JsonProperty("currency")  Currency currency) {
        this.id = id;
        this.amount = amount;
        this.timestamp = timestamp;
        this.originatorAccountId = originatorAccountId;
        this.receiverAccountId = receiverAccountId;
        this.transactionStatus = transactionStatus;
        this.currency = currency;
    }
}
