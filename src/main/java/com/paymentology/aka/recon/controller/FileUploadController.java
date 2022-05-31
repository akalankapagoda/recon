package com.paymentology.aka.recon.controller;

import com.paymentology.aka.recon.model.Transaction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller to handle file uploads.
 */
@RestController
public class FileUploadController {

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
}
