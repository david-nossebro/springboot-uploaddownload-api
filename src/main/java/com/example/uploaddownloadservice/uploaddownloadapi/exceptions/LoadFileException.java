package com.example.uploaddownloadservice.uploaddownloadapi.exceptions;

public class LoadFileException extends RuntimeException {
    public LoadFileException(String message) {
        super(message);
    }

    public LoadFileException(String message, Throwable t) {
        super(message, t);
    }
}
