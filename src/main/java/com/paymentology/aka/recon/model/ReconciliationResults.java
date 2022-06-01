package com.paymentology.aka.recon.model;

import java.util.*;

public class ReconciliationResults extends Response {

    private String source;

    private String target;

    private int matchCount;

    private int sourceRecordsCount;

    private int targetRecordsCount;

    /**
     * A possible match.
     *
     * <Source Transaction, Target Transaction>
     */
    private Map<Transaction, Transaction> suggestions = Collections.synchronizedMap(new HashMap<>());

    private List<Transaction> unmatchedSourceTransactions = Collections.synchronizedList(new ArrayList<>());
    private List<Transaction> unmatchedTargetTransactions = Collections.synchronizedList(new ArrayList<>());

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getMatchCount() {
        return matchCount;
    }

    public void setMatchCount(int matchCount) {
        this.matchCount = matchCount;
    }

    public int getSourceRecordsCount() {
        return sourceRecordsCount;
    }

    public void setSourceRecordsCount(int sourceRecordsCount) {
        this.sourceRecordsCount = sourceRecordsCount;
    }

    public int getTargetRecordsCount() {
        return targetRecordsCount;
    }

    public void setTargetRecordsCount(int targetRecordsCount) {
        this.targetRecordsCount = targetRecordsCount;
    }

    public Map<Transaction, Transaction> getSuggestions() {
        return suggestions;
    }

    public List<Transaction> getUnmatchedSourceTransactions() {
        return unmatchedSourceTransactions;
    }

    public List<Transaction> getUnmatchedTargetTransactions() {
        return unmatchedTargetTransactions;
    }

    public void incrementMatchCount() {
        matchCount++;
    }

    public void addSuggestion(Transaction sourceTransaction, Transaction targetTransaction) {
        suggestions.put(sourceTransaction, targetTransaction);
    }

    public void addUnmatchedSourceTransaction(Transaction transaction) {
        unmatchedSourceTransactions.add(transaction);
    }

    public void addUnmatchedTargetTransaction(Transaction transaction) {
        unmatchedTargetTransactions.add(transaction);
    }
}
