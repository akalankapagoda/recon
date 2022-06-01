package com.paymentology.aka.recon.controller;

import com.paymentology.aka.recon.exception.ReconException;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller to handle file uploads.
 */
@RestController
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

    @PostMapping("/uploadFile")
    public Response handleFileUpload(@RequestParam("file") MultipartFile file,
                                     @RequestParam("identifier") String identifier) throws ReconException {

        storageService.saveFile(identifier, file);

        InputStream stream = storageService.readFile(identifier);

        reconciliationService.submitFileForProcessing(identifier, stream);

        return new Response(ReconStatus.SUCCESS, "Successfully uploaded the file");
    }

    @ExceptionHandler(ReconException.class)
    public ResponseEntity<?> handleStorageFileNotFound(ReconException e) {

        logger.log(Level.SEVERE, "File operation failed at the server", e);

        Response response = new Response(ReconStatus.ERROR, "File operation failed at the server");

        return ResponseEntity.internalServerError().body(response);
    }
}
