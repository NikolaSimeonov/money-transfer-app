package com.nikola.simeonov.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;

public class AccountNotFoundException extends WebApplicationException {
    public AccountNotFoundException(String accountId) {
        super(Response.status(HttpStatus.NOT_FOUND_404,
          String.format("The account with id=%s was not found.", accountId)).build());
    }
}
