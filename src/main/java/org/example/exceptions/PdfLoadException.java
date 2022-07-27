package org.example.exceptions;

public class PdfLoadException extends Exception{
    public PdfLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
