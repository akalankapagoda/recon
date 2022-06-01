package com.paymentology.aka.recon.controller;

import com.paymentology.aka.recon.exception.ReconException;
import com.paymentology.aka.recon.model.ProcessingResults;
import com.paymentology.aka.recon.model.ReconStatus;
import com.paymentology.aka.recon.model.Response;
import com.paymentology.aka.recon.model.Transaction;
import com.paymentology.aka.recon.services.ReconciliationService;
import com.paymentology.aka.recon.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller to handle file uploads.
 */
@RestController
@RequestMapping("file")
public class FileUploadController {

    private final Logger logger = Logger.getLogger(FileUploadController.class.getName());

    /**
     * A temporary work directory to save uploaded files temporarily.
     */
    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir") + File.separator + "recon";

    private final StorageService storageService;
    private final ReconciliationService reconciliationService;

    @Autowired
    public FileUploadController(StorageService storageService, ReconciliationService reconciliationService) {
        this.storageService = storageService;
        this.reconciliationService = reconciliationService;
    }

    /**
     * A health check endpoint to validate the deployment.
     *
     * @return A sample transaction record
     */
    @GetMapping("/hello")
    List<Transaction> all() {

        Transaction sample = new Transaction();

        sample.setDescription("Hello");

        List<Transaction> tempList = new ArrayList<>();
        tempList.add(sample);

        return tempList;
    }

    /**
     * Uploads a file for processing.
     *
     * The file is then submitted for pre-processing.
     *
     * @param file The file object
     * @param identifier The file identifier
     *
     * @return Service response with file upload status
     * @throws ReconException
     */
    @PostMapping
    public Response handleFileUpload(@RequestParam("file") MultipartFile file,
                                     @RequestParam("identifier") String identifier) throws ReconException {

        storageService.saveFile(identifier, file);

        ProcessingResults processingResults = new ProcessingResults(identifier);
        processingResults.setStatus(ReconStatus.PROCESSING);

        storageService.saveProcessingResults(identifier, processingResults);

        reconciliationService.submitFileForPreProcessing(identifier, processingResults);

        return new Response(ReconStatus.SUCCESS, "Successfully uploaded the file");
    }

    /**
     * Get file pre-processing status.
     *
     * A reconciliation request cannot be submitted until pre-processing is complete.
     *
     * @param identifier The file identifier to check
     *
     * @return The file pre-processing status or {@link ReconStatus#NONE} if the file has not been submitted
     * @throws ReconException
     */
    @GetMapping("/status")
    public Response getFileUploadStatus(@RequestParam("identifier") String identifier) throws ReconException {

        Response response = new Response();

        ProcessingResults processingResults = storageService.getProcessingResults(identifier);

        if (processingResults != null) {
            response.setStatus(processingResults.getStatus());
        } else {
            response.setStatus(ReconStatus.NONE);
            response.setMessage("File has not bee submitted");
        }

        return response;
    }

    /**
     * Handle service layer errors.
     *
     * @param e The exception occurred
     * @return A service error response
     */
    @ExceptionHandler(ReconException.class)
    public ResponseEntity<?> handleStorageFileNotFound(ReconException e) {

        logger.log(Level.SEVERE, "File operation failed at the server", e);

        Response response = new Response(ReconStatus.ERROR, "File operation failed at the server");

        return ResponseEntity.internalServerError().body(response);
    }
}
