package com.nikola.simeonov.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;

public class BankNotFoundException extends WebApplicationException {
    public BankNotFoundException(String bankId) {
        super(Response.status(HttpStatus.NOT_FOUND_404,
          String.format("The bank with id=%s was not found.", bankId)).build());
    }
}
