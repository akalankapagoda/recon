package com.paymentology.aka.recon.services;

import com.paymentology.aka.recon.model.Transaction;

/**
 * Transaction reference key generator.
 *
 * Generates a string key using selected fields which can be hashed.
 */
public final class RefKeyGenerator {

    /**
     * Generate a key combining wallet ref + amount + descriptoin.
     *
     * @param transaction The transaction to generate the key for
     *
     * @return Generated key string
     */
    public static final String getWalletRefKey(Transaction transaction) {
        return transaction.getWalletReference() + transaction.getAmount() + transaction.getDescription();
    }

    /**
     * Generate a key combining description + narrative + amount.
     *
     * @param transaction The transaction to generate the key for
     *
     * @return Generated key string
     */
    public static final String getOptionalRefKey(Transaction transaction) {
        return transaction.getDescription() +
                transaction.getNarrative() + transaction.getAmount();
    }

}
