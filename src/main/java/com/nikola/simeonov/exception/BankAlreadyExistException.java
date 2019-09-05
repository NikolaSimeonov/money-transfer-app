package com.nikola.simeonov.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;

public class BankAlreadyExistException extends WebApplicationException {


    public BankAlreadyExistException(String bankId) {
        super(Response.status(HttpStatus.CONFLICT_409,
          String.format("The bank you tried to create with id=%s already exists.",bankId)).build());
    }
}
