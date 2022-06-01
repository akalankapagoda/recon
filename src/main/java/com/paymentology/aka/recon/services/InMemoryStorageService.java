package com.paymentology.aka.recon.services;

import com.paymentology.aka.recon.exception.ReconException;
import com.paymentology.aka.recon.model.ProcessingResults;
import com.paymentology.aka.recon.model.ReconciliationResults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class InMemoryStorageService implements StorageService {

    private Map<String, MultipartFile> files = new HashMap<>();

    private Map<String, ReconciliationResults> reconciliationResults = new HashMap<>();

    private Map<String, ProcessingResults> processingResultsMap = new HashMap<>();

    @Override
    public void saveFile(String identifier, MultipartFile file) {
        files.put(identifier, file);
    }

    @Override
    public InputStream readFile(String identifier) throws ReconException {

        MultipartFile file = files.get(identifier);

        InputStream stream = null;

        if (file != null) {
            try {
                stream = file.getInputStream();
            } catch (IOException e) {
                throw new ReconException("Failed to load file : " + identifier, e);
            }
        }

        return stream;
    }

    @Override
    public boolean deleteFile(String identifier) {

        if (files.remove(identifier) != null) {
            return true;
        }

        return false;
    }

    @Override
    public void saveReconciliationResults(ReconciliationResults results) {
        reconciliationResults.put(results.getSource() + results.getTarget(), results);
    }

    @Override
    public ReconciliationResults readReconciliationResults(String source, String target) {
        return reconciliationResults.get(source + target);
    }

    @Override
    public void saveProcessingResults(String identifier, ProcessingResults processingResults) {
        processingResultsMap.put(identifier, processingResults);
    }

    @Override
    public ProcessingResults getProcessingResults(String identifier) {
        return processingResultsMap.get(identifier);
    }
}
