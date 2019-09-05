package com.nikola.simeonov.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class BankTransferResponse {

    private final Transaction transaction;
    @NotNull
    private final LocalDateTime timeStamp;
    @NotBlank
    private final String senderBankId;
}
