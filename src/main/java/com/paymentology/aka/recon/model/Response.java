package com.paymentology.aka.recon.model;

/**
 * A generic service response message.
 *
 * This is the building block for all the response messages.
 */
public class Response {

    private ReconStatus status;

    private String message;

    public Response(ReconStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public Response() {}

    public ReconStatus getStatus() {
        return status;
    }

    public void setStatus(ReconStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
