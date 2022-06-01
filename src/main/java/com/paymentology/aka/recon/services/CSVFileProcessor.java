package com.paymentology.aka.recon.services;

import com.paymentology.aka.recon.exception.ReconException;
import com.paymentology.aka.recon.model.ProcessingResults;
import com.paymentology.aka.recon.model.ReconStatus;
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

    public CSVFileProcessor(StorageService storageService, String identifier, ProcessingResults processingResults) {
        super(storageService, identifier, processingResults);
    }

    @Override
    public void run() {

        try {

            InputStream inputStream = storageService.readFile(identifier);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));



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

                processingResults.addTransaction(transaction);
            }

            processingResults.setStatus(ReconStatus.SUCCESS);
        } catch (ReconException | IOException e) {
            logger.log(Level.SEVERE, "Failed to process file : " +  identifier, e);

            processingResults.setStatus(ReconStatus.ERROR);
            processingResults.setMessage(e.getMessage()); // TODO: Sanitize this message for public viewing
        }

        storageService.saveProcessingResults(identifier, processingResults);
    }
}
