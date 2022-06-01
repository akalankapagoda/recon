package com.paymentology.aka.recon.services;

import com.paymentology.aka.recon.model.ProcessingResults;
import com.paymentology.aka.recon.model.ReconStatus;
import com.paymentology.aka.recon.model.ReconciliationResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ReconciliationService {

    private static final int EXECUTOR_THREAD_POOL_SIZE = 10; // TODO: This should come from a config and default to 10

    private ExecutorService executorService = Executors.newFixedThreadPool(EXECUTOR_THREAD_POOL_SIZE);


    private final StorageService storageService;

    @Autowired
    public ReconciliationService(StorageService storageService) {
        this.storageService = storageService;
    }

    public void submitFileForProcessing(String identifier, InputStream inputStream) {

        // As of now we're hardcoding to CSV, but we can separate this out later with a file type
        executorService.submit(new CSVFileProcessor(storageService, identifier, inputStream));
    }

    public String getProcessingStatus(String identifier) {

        return "pending";
    }

    public void reconcileTransactions(String source, String target) {

        ReconciliationResults reconResults = new ReconciliationResults();
        reconResults.setSource(source);
        reconResults.setTarget(target);

        ProcessingResults sourceResults = storageService.getProcessingResults(source);
        ProcessingResults targetResults = storageService.getProcessingResults(target);

        if (sourceResults == null || targetResults == null) {
            reconResults.setStatus(ReconStatus.ERROR);
            reconResults.setMessage("Source or target is missing");
            storageService.saveReconciliationResults(reconResults);
        } else {
            executorService.submit(new ReconProcessor(storageService, sourceResults, targetResults));
        }


    }

}

