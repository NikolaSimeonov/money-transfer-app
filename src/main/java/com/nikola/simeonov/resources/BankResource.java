package com.nikola.simeonov.resources;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.nikola.simeonov.model.Bank;
import com.nikola.simeonov.model.BankIdRequest;
import com.nikola.simeonov.persistence.BankRepository;

@Path("/bank")
public class BankResource {

    private BankRepository bankRepository;

    @Inject
    public BankResource(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/create")
    public Response save(BankIdRequest bankId) {
        bankRepository.saveBank(bankId.getId());
        return Response.status(Response.Status.CREATED).entity(bankId).build();
    }




    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{bankId}")
    public Response get(@PathParam("bankId") String bankId) {
        Bank bank = bankRepository.getBankById(bankId);
        if(bank != null) {
            return Response.status(Response.Status.OK).entity(bank).build();
        } else  return Response.status(Response.Status.NOT_FOUND).entity(bank).build();
    }

}
