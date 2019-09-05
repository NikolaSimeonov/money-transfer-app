package com.nikola.simeonov.model;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Bank {


    private Map<String, Account> accounts = new ConcurrentHashMap<>();

    @NotBlank
    private  String id;

    public String getId() {
        return id;
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Bank(@JsonProperty("id") String id) {
        this.id = id;
    }
}
