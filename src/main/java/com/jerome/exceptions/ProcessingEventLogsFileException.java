package com.jerome.exceptions;

import java.io.IOException;

public class ProcessingEventLogsFileException extends RuntimeException {
    public ProcessingEventLogsFileException(IOException e) {
        super(e);
    }
}
