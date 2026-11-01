package org.example;

/** Single checked exception for XML parsing, validation, and transformation errors. */
public class TariffProcessingException extends Exception {

    private static final long serialVersionUID = 1L;

    public TariffProcessingException(String message) {
        super(message);
    }

    public TariffProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}