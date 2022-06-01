package com.paymentology.aka.recon.model;

import java.util.HashMap;
import java.util.Map;

public class ReconciliationResults extends Response {

    private String source;

    private String target;

    private int matchCount;

    private int sourceRecordsCount;

    private int targetRecordsCount;

    private Map<Transaction, Transaction> suggestions = new HashMap<>();

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

    public void incrementMatchCount() {
        matchCount++;
    }

    public void addSuggestion(Transaction sourceTransaction, Transaction targetTransaction) {
        suggestions.put(sourceTransaction, targetTransaction);
    }
}
