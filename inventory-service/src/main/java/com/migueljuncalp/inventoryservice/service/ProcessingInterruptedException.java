package com.migueljuncalp.inventoryservice.service;

public class ProcessingInterruptedException extends RuntimeException {

    public ProcessingInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}
