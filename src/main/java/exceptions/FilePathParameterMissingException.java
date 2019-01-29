package exceptions;

public class FilePathParameterMissingException extends RuntimeException {

    public FilePathParameterMissingException(Throwable cause) {
        super("File path parameter is missing", cause);
    }
}
