package com.paymentology.aka.recon.model;

/**
 * Valid statuses for actions within the reconciliation application.
 */
public enum ReconStatus {

    /**
     * The action has been completed.
     */
    SUCCESS,

    /**
     * Action in progress.
     */
    PROCESSING,

    /**
     * The action has failed.
     */
    ERROR,

    /**
     * Data related to the specified action/entity is not found
     */
    NONE
}
