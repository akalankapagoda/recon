package com.paymentology.aka.recon.services;

import com.paymentology.aka.recon.model.ProcessingResults;

import java.io.InputStream;

/**
 * A worker thread for processing submitted files.
 */
public abstract class FileProcessor implements Runnable {

    protected StorageService storageService;

    protected String identifier;

    protected ProcessingResults processingResults;

    FileProcessor(StorageService storageService, String identifier, ProcessingResults processingResults) {
        this.storageService = storageService;
        this.identifier = identifier;
        this.processingResults =  processingResults;
    }
}