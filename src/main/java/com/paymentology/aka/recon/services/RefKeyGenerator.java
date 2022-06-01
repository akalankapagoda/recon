package com.paymentology.aka.recon.services;

import com.paymentology.aka.recon.model.Transaction;

/**
 * Transaction reference key generator.
 *
 * Generates a string key using selected fields which can be hashed.
 */
public final class RefKeyGenerator {

    public static final String getWalletRefKey(Transaction transaction) {
        return transaction.getWalletReference() + transaction.getAmount() + transaction.getDescription();
    }

    public static final String getOptionalRefKey(Transaction transaction) {
        return transaction.getTransactionId() + transaction.getDescription() +
                transaction.getNarrative() + transaction.getAmount();
    }

}
