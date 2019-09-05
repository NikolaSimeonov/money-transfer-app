package com.nikola.simeonov.model;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TransferResponse {

    @NotBlank
    private String responseMessage;

    public TransferResponse(String responseMessage){
        this.responseMessage = responseMessage;
    }
}
