package com.jerome.exceptions;

public class ProcessingEventException extends RuntimeException {
    public ProcessingEventException(Throwable exception) {
        super(exception);
    }
}
