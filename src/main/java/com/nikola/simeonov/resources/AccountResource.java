package com.nikola.simeonov.resources;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.nikola.simeonov.model.Account;
import com.nikola.simeonov.persistence.AccountRepository;

@Path("/account")
public class AccountResource {

    private AccountRepository accountRepository;

    @Inject
    public AccountResource(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/create")
    public Response save(Account account) {
        accountRepository.saveAccount(account);
        return Response.status(Response.Status.CREATED).entity(account).build();

    }


    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{accountId}")
    public Response get(@PathParam("accountId") String accountId) {
        Account account = accountRepository.getAccountById(accountId);
        if(account != null) {
            return Response.status(Response.Status.OK).entity(account).build();
        } else  return Response.status(Response.Status.NOT_FOUND).build();
    }

}
