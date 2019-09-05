package com.nikola.simeonov.exception;

import java.util.Currency;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;

public class CurrencyMismatchException extends WebApplicationException {
    public CurrencyMismatchException(Currency accountCurrency, Currency operationCurrency) {
        super(Response.status(HttpStatus.CONFLICT_409,
          String.format("The account currency=%s and the operation currency=%s  are not the same .",accountCurrency,operationCurrency)).build());
    }
}
