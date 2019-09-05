package com.nikola.simeonov.resources;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.nikola.simeonov.model.TransferRequest;
import com.nikola.simeonov.model.TransferResponse;
import com.nikola.simeonov.service.TransferService;

@Path("/transfer")
public class TransferResource {

    private TransferService transferService;

    @Inject
    public TransferResource(TransferService transferService) {
        this.transferService = transferService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{initiate}")
    public TransferResponse transfer(TransferRequest transferRequest) {
        return transferService.transfer(transferRequest.getOriginatorAccountId(),
          transferRequest.getReceiverAccountId(), transferRequest.getTransferAmount(),transferRequest.getCurrency());
    }

}
