import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static final Integer FILE_PATH_PARAMETER_INDEX = 0;
    private static final Integer NUMBER_OF_THREADS_PARAMETER_INDEX = 1;

    public static void main(String[] vars) {
        String filePath = getFilePath(vars);
        Integer numberOfThreads = getNumberOfThreadsParameter(vars);

        new EventLogsFileProcessor(numberOfThreads)
                .processEventLogsFile(filePath);
    }

    private static String getFilePath(String[] vars) {
        return vars[FILE_PATH_PARAMETER_INDEX];
    }

    private static Integer getNumberOfThreadsParameter(String[] vars) {
        Integer defaultThreadPoolSize = 1;
        try {
            return Integer.valueOf(vars[NUMBER_OF_THREADS_PARAMETER_INDEX]);
        } catch (NumberFormatException e) {
            logger.info(vars[NUMBER_OF_THREADS_PARAMETER_INDEX] + " should be a number");
            logger.info("Using Default thread pool size: " + defaultThreadPoolSize);
            return defaultThreadPoolSize;
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.info("Default thread pool size: " + defaultThreadPoolSize);
            return defaultThreadPoolSize;
        }
    }
}
