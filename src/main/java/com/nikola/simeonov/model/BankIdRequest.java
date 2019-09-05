package com.nikola.simeonov.model;


import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BankIdRequest {

    @NotBlank
    private final String id;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public BankIdRequest(@JsonProperty("id") String id) {
        this.id = id;
    }
}
