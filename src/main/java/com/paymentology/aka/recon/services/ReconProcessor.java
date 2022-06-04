package com.paymentology.aka.recon.services;

import com.paymentology.aka.recon.model.ProcessingResults;
import com.paymentology.aka.recon.model.ReconStatus;
import com.paymentology.aka.recon.model.ReconciliationResults;
import com.paymentology.aka.recon.model.Transaction;

import java.util.*;

/**
 * A worker thread to perform reconciliation.
 */
public class ReconProcessor implements Runnable {

    private StorageService storageService;

    private ProcessingResults sourceResults;
    private ProcessingResults targetResults;

    ReconciliationResults reconResults;

    public ReconProcessor(StorageService storageService, ProcessingResults sourceResults,
                          ProcessingResults targetResults, ReconciliationResults reconResults) {

        this.storageService = storageService;
        this.sourceResults = sourceResults;
        this.targetResults = targetResults;
        this.reconResults = reconResults;

        reconResults.setSourceRecordsCount(sourceResults.getRecordsCount());
        reconResults.setTargetRecordsCount(targetResults.getRecordsCount());
    }

    /**
     * Go through two set of pre-processed data and compare.
     *
     * 1. Compare transaction Id which is a direct match
     * 2. Compare wallet ref and description + amount match to suggest a possible match
     * 3. Compare description + narrative + amount match to suggest a possible match
     */
    @Override
    public void run() {

        Map<String, Transaction> sourceTransactions = sourceResults.getTransactions();
        Map<String, Transaction> sourceTransactionsWalletRef = sourceResults.getTransactionsWalletRef();
        Map<String, Transaction> sourceTransactionsOptionalRef = sourceResults.getTransactionsOptionalRef();

        Map<String, String> targetWalletRefToOptionalRef = targetResults.getWalletRefToOptionalRef();

        Set<String> matchedTransactions = Collections.synchronizedSet(new HashSet<>());
        List<String> suggestedTargetWalletRefKeys = Collections.synchronizedList(new ArrayList<>());
        List<String> suggestedTargetOptionalRefKeys = Collections.synchronizedList(new ArrayList<>());


        targetResults.getTransactions().entrySet().parallelStream().forEach(entry -> {
            Transaction transaction = entry.getValue();
            String walletRefKey = RefKeyGenerator.getWalletRefKey(transaction);

            String transactionId = transaction.getTransactionId();
            String walletRefOptionalKey = targetWalletRefToOptionalRef.get(walletRefKey);

            if (sourceTransactions.containsKey(transactionId)) { // Direct match
                matchedTransactions.add(transactionId);
                reconResults.incrementMatchCount();
            } else if (sourceTransactionsWalletRef.containsKey(walletRefKey)) {  // Possible match with wallet ref

                suggestedTargetWalletRefKeys.add(walletRefKey);
                reconResults.addSuggestion(sourceTransactionsWalletRef.get(walletRefKey), transaction);

            } else if (sourceTransactionsOptionalRef.containsKey(walletRefOptionalKey)) { // Possible match with other fields
                suggestedTargetOptionalRefKeys.add(walletRefOptionalKey);
                reconResults.addSuggestion(sourceTransactionsOptionalRef.get(walletRefOptionalKey), transaction);
            } else { // Unmatched

                reconResults.addUnmatchedTargetTransaction(transaction);
            }

        });


        // Now we've gone through the target file and got the numbers.
        // Next is to cross reference the identified matches and possible matches on the source and filter out unmatched
        // ones in the source.

        sourceResults.getTransactions().entrySet().parallelStream().forEach(entry -> {
            Transaction transaction = entry.getValue();
            String walletRefKey = RefKeyGenerator.getWalletRefKey(transaction);
            String optionalRefKey = RefKeyGenerator.getOptionalRefKey(transaction);

            String transactionId = transaction.getTransactionId();

            if (!(matchedTransactions.contains(transactionId) ||
                    suggestedTargetWalletRefKeys.contains(walletRefKey) ||
                    suggestedTargetOptionalRefKeys.contains(optionalRefKey))) { // Not matched or suggested
                reconResults.addUnmatchedSourceTransaction(transaction);
            }

        });

        // Finally update the results status

        reconResults.setStatus(ReconStatus.SUCCESS);

        // This is not needed in our in-memory storage, but will be required if we're using a DB
        storageService.saveReconciliationResults(reconResults);

    }
}
