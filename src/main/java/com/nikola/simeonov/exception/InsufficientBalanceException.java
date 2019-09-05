package com.nikola.simeonov.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;

public class InsufficientBalanceException extends WebApplicationException {
    public InsufficientBalanceException(String accountId) {
        super(Response.status(HttpStatus.CONFLICT_409,
          String.format("The account with id=%s doesnt have sufficient balance to finalize operation.",accountId)).build());
    }
}
