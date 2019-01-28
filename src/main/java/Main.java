import exceptions.FilePathParameterMissingException;

public class Main {

    public static void main(String[] vars) throws FilePathParameterMissingException {

        new EventLogsFileProcessor(
                new EventParametersValidator(vars).getEventParameters()

        ).processEventLogsFile();
    }
}
