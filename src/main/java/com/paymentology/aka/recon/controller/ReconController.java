package com.paymentology.aka.recon.controller;

import com.paymentology.aka.recon.exception.ReconException;
import com.paymentology.aka.recon.model.ReconStatus;
import com.paymentology.aka.recon.model.Response;
import com.paymentology.aka.recon.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Transaction reconciliation controller service.
 */
@RestController
public class ReconController {

    private final StorageService storageService;

    @Autowired
    public ReconController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/reconcile")
    public Response reconcileTransactions(@RequestParam("source") String sourceIdentifier,
                                              @RequestParam("target") String targetIdentifier) throws ReconException {

        return new Response(ReconStatus.SUCCESS, "Successfully uploaded the file");
    }
}
