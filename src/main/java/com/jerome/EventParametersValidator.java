package com.jerome;

import com.jerome.exceptions.FilePathParameterMissingException;
import com.jerome.models.EventParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EventParametersValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventParametersValidator.class);

    public static final int DEFAULT_THREAD_POOL_SIZE = 1;

    private static final int FILE_PATH_PARAMETER_INDEX = 0;
    private static final int NUMBER_OF_THREADS_PARAMETER_INDEX = 1;

    private final String[] vars;

    public EventParametersValidator(String... vars) {
        this.vars = vars.clone();
    }

    public EventParameters getEventParameters() {
        return new EventParameters(getFilePath(), getNumberOfThreadsParameter());
    }

    private String getFilePath() {
        try {
            return vars[FILE_PATH_PARAMETER_INDEX];
        } catch (ArrayIndexOutOfBoundsException e) {
            LOGGER.error("File path parameter is missing");
            throw new FilePathParameterMissingException(e);
        }
    }

    private int getNumberOfThreadsParameter() {
        try {
            return Integer.valueOf(vars[NUMBER_OF_THREADS_PARAMETER_INDEX]);
        } catch (NumberFormatException e) {
            LOGGER.info("{} should be a number", vars[NUMBER_OF_THREADS_PARAMETER_INDEX]);
            LOGGER.info("Using Default thread pool size: {}", DEFAULT_THREAD_POOL_SIZE);
            return DEFAULT_THREAD_POOL_SIZE;
        } catch (ArrayIndexOutOfBoundsException e) {
            LOGGER.info("Default thread pool size: {}", DEFAULT_THREAD_POOL_SIZE);
            return DEFAULT_THREAD_POOL_SIZE;
        }
    }
}
