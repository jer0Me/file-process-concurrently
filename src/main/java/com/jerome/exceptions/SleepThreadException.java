package com.jerome.exceptions;

public class SleepThreadException extends RuntimeException {
    public SleepThreadException(InterruptedException e) {
        super(e);
    }
}
