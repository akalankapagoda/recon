package com.paymentology.aka.recon.model;

import com.paymentology.aka.recon.services.RefKeyGenerator;

import java.util.HashMap;
import java.util.Map;

public class ProcessingResults extends Response {

    private String identifier;

    private Map<String, Transaction> transactions = new HashMap<>();

    private Map<String, Transaction> transactionsWalletRef = new HashMap<>();

    private Map<String, Transaction> transactionsOptionalRef = new HashMap<>();

    private int recordsCount;

    /**
     * A mapping between wallet ref and optional ref.
     */
    private Map<String, String> walletRefToOptionalRef = new HashMap<>();

    public ProcessingResults(String identifier) {
        this.identifier = identifier;
    }

    public void addTransaction(Transaction transaction) {

        transactions.put(transaction.getTransactionId(), transaction);

        String walletRefKey = RefKeyGenerator.getWalletRefKey(transaction);
        String optionalRef = RefKeyGenerator.getOptionalRefKey(transaction);

        transactionsWalletRef.put(walletRefKey, transaction);

        transactionsOptionalRef.put(optionalRef, transaction);

        walletRefToOptionalRef.put(walletRefKey, optionalRef);

        recordsCount++;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getRecordsCount() {
        return recordsCount;
    }

    public Map<String, Transaction> getTransactions() {
        return transactions;
    }

    public Map<String, Transaction> getTransactionsWalletRef() {
        return transactionsWalletRef;
    }

    public Map<String, Transaction> getTransactionsOptionalRef() {
        return transactionsOptionalRef;
    }

    public Map<String, String> getWalletRefToOptionalRef() {
        return walletRefToOptionalRef;
    }

}
