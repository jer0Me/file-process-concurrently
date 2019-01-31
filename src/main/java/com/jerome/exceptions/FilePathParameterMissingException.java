package com.jerome.exceptions;

public class FilePathParameterMissingException extends RuntimeException {

    public FilePathParameterMissingException(Exception e) {
        super("File path parameter is missing", e);
    }
}
