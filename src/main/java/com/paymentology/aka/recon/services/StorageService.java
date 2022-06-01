package com.paymentology.aka.recon.services;

import com.paymentology.aka.recon.exception.ReconException;
import com.paymentology.aka.recon.model.ProcessingResults;
import com.paymentology.aka.recon.model.ReconciliationResults;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Component
public interface StorageService {

    void saveFile(String identifier, MultipartFile file);

    InputStream readFile(String identifier) throws ReconException;

    boolean deleteFile(String identifier);

    void saveReconciliationResults(ReconciliationResults results);

    ReconciliationResults readReconciliationResults(String source, String target);

    void saveProcessingResults(String identifier, ProcessingResults processingResults);

    ProcessingResults getProcessingResults(String identifier);
}
