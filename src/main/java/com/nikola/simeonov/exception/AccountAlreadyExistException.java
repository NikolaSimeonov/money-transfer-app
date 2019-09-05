package com.nikola.simeonov.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;

public class AccountAlreadyExistException extends WebApplicationException {

    public AccountAlreadyExistException(String accountId){
        super(Response.status(HttpStatus.CONFLICT_409,
          String.format("The account you tried to create with id=%s already exists.",accountId)).build());
    }
}
