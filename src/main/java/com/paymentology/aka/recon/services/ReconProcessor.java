package com.paymentology.aka.recon.services;

import com.paymentology.aka.recon.model.ProcessingResults;
import com.paymentology.aka.recon.model.ReconciliationResults;
import com.paymentology.aka.recon.model.Transaction;
import org.apache.tomcat.jni.Proc;

import java.util.*;

public class ReconProcessor implements Runnable {

    private StorageService storageService;

    private ProcessingResults sourceResults;
    private ProcessingResults targetResults;

    ReconciliationResults reconResults;

    public ReconProcessor(StorageService storageService, ProcessingResults sourceResults, ProcessingResults targetResults) {
        this.storageService = storageService;
        this.sourceResults = sourceResults;
        this.targetResults = targetResults;
        reconResults = new ReconciliationResults();

        reconResults.setSource(sourceResults.getIdentifier());
        reconResults.setTarget(targetResults.getIdentifier());
        reconResults.setSourceRecordsCount(sourceResults.getRecordsCount());
        reconResults.setTargetRecordsCount(targetResults.getRecordsCount());
    }

    @Override
    public void run() {

        Map<String, Transaction> sourceTransactionsWalletRef = sourceResults.getTransactionsWalletRef();
        Map<String, Transaction> sourceTransactionsOptionalRef = sourceResults.getTransactionsOptionalRef();

        Map<String, String> targetWalletRefToOptionalRef = targetResults.getWalletRefToOptionalRef();

        int matchCount = 0;

        List<String> matchedTargetWalletRefKeys = Collections.synchronizedList(new ArrayList<>());
        List<String> unmatchedTargetWalletRefKeys = Collections.synchronizedList(new ArrayList<>());


        targetResults.getTransactionsWalletRef().entrySet().parallelStream().forEach(entry -> {
            String walletRefKey = entry.getKey();
            Transaction transaction = entry.getValue();

            if (sourceTransactionsWalletRef.containsKey(walletRefKey)) {
                matchedTargetWalletRefKeys.add(walletRefKey);
            } else if (sourceTransactionsOptionalRef.containsKey(targetWalletRefToOptionalRef.get(walletRefKey))) {
                // Possible match
            } else {
                // Unmatched
            }

        });



    }
}
