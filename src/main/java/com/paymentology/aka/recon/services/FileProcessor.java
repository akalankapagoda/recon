package com.paymentology.aka.recon.services;

import java.io.InputStream;

/**
 * A worker thread for processing submitted files.
 */
public abstract class FileProcessor implements Runnable {

    protected StorageService storageService;

    protected String identifier;

    protected InputStream inputStream;

    FileProcessor(StorageService storageService, String identifier, InputStream inputStream) {
        this.storageService = storageService;
        this.identifier = identifier;
        this.inputStream = inputStream;
    }
}