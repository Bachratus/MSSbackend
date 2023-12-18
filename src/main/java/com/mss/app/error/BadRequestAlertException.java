package com.mss.app.error;

public class BadRequestAlertException extends RuntimeException {

    private final String entityName;
    private final String errorKey;

    public BadRequestAlertException(String message, String entityName, String errorKey) {
        super(message);
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    // Gettery
    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }
}