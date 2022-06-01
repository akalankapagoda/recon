package com.paymentology.aka.recon.services;

import com.paymentology.aka.recon.model.ProcessingResults;
import com.paymentology.aka.recon.model.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CSVFileProcessor extends FileProcessor {

    private static final String SEPARATOR = ",";
    private final Logger logger = Logger.getLogger(CSVFileProcessor.class.getName());

    public CSVFileProcessor(StorageService storageService, String identifier, InputStream inputStream) {
        super(storageService, identifier, inputStream);
    }

    @Override
    public void run() {

        ProcessingResults results = new ProcessingResults(identifier);

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {

            if (reader.ready()) { // Skip the first line which contains the headings
                reader.readLine();
            }


            while (reader.ready()) {
                String line = reader.readLine();

                String[] fields = line.split(SEPARATOR);

                if (fields.length != 8) { // This line is probably erroneous
                    continue;
                }

                Transaction transaction = new Transaction();
                transaction.setProfileName(fields[0]);
                transaction.setTransactionDate(fields[1]);

                try {
                    transaction.setAmount(new BigInteger(fields[2].trim()));
                } catch (NumberFormatException e) {
                    logger.log(Level.WARNING, "Failed to parse the amount : " + fields[2] +
                            " in file : " + identifier, e);
                    continue;
                }

                transaction.setNarrative(fields[3]);
                transaction.setDescription(fields[4]);
                transaction.setTransactionId(fields[5]);
                transaction.setTransactionType(fields[6]);
                transaction.setWalletReference(fields[7]);

                results.addTransaction(transaction);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to process file : " +  identifier, e);
        }

        storageService.saveProcessingResults(identifier, results);
    }
}
