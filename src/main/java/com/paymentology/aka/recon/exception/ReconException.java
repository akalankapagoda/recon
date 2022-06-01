package com.paymentology.aka.recon.exception;

/**
 * File reconciliation exception.
 */
public class ReconException extends Exception {

    public ReconException(String message) {
        super(message);
    }

    public ReconException(String message, Throwable cause) {
        super(message, cause);
    }
}
