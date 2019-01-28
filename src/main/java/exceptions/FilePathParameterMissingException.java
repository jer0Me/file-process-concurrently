package exceptions;

public class FilePathParameterMissingException extends RuntimeException {

    public FilePathParameterMissingException() {
        super("File path parameter is missing");
    }
}
