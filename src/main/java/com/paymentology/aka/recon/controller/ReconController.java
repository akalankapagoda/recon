package com.paymentology.aka.recon.controller;

import com.paymentology.aka.recon.exception.ReconException;
import com.paymentology.aka.recon.model.ReconStatus;
import com.paymentology.aka.recon.model.ReconciliationResults;
import com.paymentology.aka.recon.model.Response;
import com.paymentology.aka.recon.services.ReconciliationService;
import com.paymentology.aka.recon.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Transaction reconciliation controller service.
 */
@RestController
@RequestMapping("reconcile")
public class ReconController {

    private final StorageService storageService;
    private final ReconciliationService reconciliationService;

    @Autowired
    public ReconController(StorageService storageService, ReconciliationService reconciliationService) {
        this.storageService = storageService;
        this.reconciliationService = reconciliationService;
    }

    /**
     * Start reconciliation of two uploaded files.
     *
     * @param sourceIdentifier The source file identifier
     * @param targetIdentifier The target file identifier
     *
     * @return Success response if analysis process was started
     * @throws ReconException
     */
    @PostMapping
    public Response reconcileTransactions(@RequestParam("source") String sourceIdentifier,
                                              @RequestParam("target") String targetIdentifier) throws ReconException {

        // TODO: We need to append something like the session Id to the identifier here so that
        //  if multiple people use the same file name, they won't interfere with each other

        Response response;

        // See if this is already processing

        ReconciliationResults reconResults = storageService.readReconciliationResults(sourceIdentifier, targetIdentifier);

        if (reconResults == null) { // This is not already submitted

            if (storageService.getProcessingResults(sourceIdentifier) != null &&
                    storageService.getProcessingResults(targetIdentifier) != null &&
                    storageService.getProcessingResults(sourceIdentifier).getStatus() == ReconStatus.SUCCESS &&
                    storageService.getProcessingResults(targetIdentifier).getStatus() == ReconStatus.SUCCESS) {

                reconciliationService.reconcileTransactions(sourceIdentifier, targetIdentifier);

                response = new Response(ReconStatus.SUCCESS, "Successfully submitted for reconciliation");

            } else {
                response = new Response(ReconStatus.ERROR, "Source or target file was not uploaded or " +
                        "pre-processing is not complete. Please upload the files before executing " +
                        "reconciliation and wait for pre-processing to complete!");
            }


        } else {
            response = new Response(ReconStatus.SUCCESS, "Files already submitted for reconciliation");
        }

        return response;
    }

    /**
     * Get the results of a submitted reconciliation process.
     *
     * @param sourceIdentifier The source identifier of the submitted reconciliation
     * @param targetIdentifier The target identifier of the submitted reconciliation
     *
     * @return The execution status of the reconciliation or {@link ReconStatus#NONE} if not submitted before
     * @throws ReconException
     */
    @GetMapping("/results")
    public Response getReconResults(@RequestParam("source") String sourceIdentifier,
                                          @RequestParam("target") String targetIdentifier) throws ReconException {

        Response response;

        ReconciliationResults reconResults = storageService.readReconciliationResults(sourceIdentifier, targetIdentifier);

        if (reconResults != null) {
            response =  reconResults;
        } else {
            response = new Response();
            response.setStatus(ReconStatus.NONE);
            response.setMessage("Reconciliation has not been triggered for the provided files");
        }

        // TODO: There is a risk of reconResults being too big for a network request.
        //  We need to back the results fetching by a DB and implement pagination to address this
        return response;
    }
}
