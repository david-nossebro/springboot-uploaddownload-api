package com.example.uploaddownloadservice.uploaddownloadapi.exceptions;

public class FileStorageException extends RuntimeException {
    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable t) {
        super(message, t);
    }
}
