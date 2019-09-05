package com.nikola.simeonov.service;

import com.nikola.simeonov.model.Transaction;
import com.nikola.simeonov.model.TransferResponse;

public interface SenderBankService {

    TransferResponse sendTransaction(Transaction transaction);
}
