package com.paymentology.aka.recon.model;

import java.util.Date;

/**
 * The transaction entity which represents a transaction record.
 */
public class Transaction {

    private String transactionId;

    private Date transactionDate;

    private String transactionType;

    private String profileName;

    private Long amount;

    private String description;

    private String walletReference;

    private String narrative;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWalletReference() {
        return walletReference;
    }

    public void setWalletReference(String walletReference) {
        this.walletReference = walletReference;
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }
}
