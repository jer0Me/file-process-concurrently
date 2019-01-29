package com.jerome;

import com.jerome.exceptions.FilePathParameterMissingException;
import com.jerome.models.EventParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EventParametersValidator {

    static final Integer DEFAULT_THREAD_POOL_SIZE = 1;

    private static final Logger logger = LoggerFactory.getLogger(EventParametersValidator.class);

    private static final Integer FILE_PATH_PARAMETER_INDEX = 0;
    private static final Integer NUMBER_OF_THREADS_PARAMETER_INDEX = 1;

    private final String[] vars;

    EventParametersValidator(String... vars) {
        this.vars = vars.clone();
    }

    EventParameters getEventParameters() {
        return new EventParameters(getFilePath(), getNumberOfThreadsParameter());
    }

    private String getFilePath() {
        try {
            return vars[FILE_PATH_PARAMETER_INDEX];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new FilePathParameterMissingException(e);
        }
    }

    private Integer getNumberOfThreadsParameter() {
        try {
            return Integer.valueOf(vars[NUMBER_OF_THREADS_PARAMETER_INDEX]);
        } catch (NumberFormatException e) {
            logger.info("{} should be a number", vars[NUMBER_OF_THREADS_PARAMETER_INDEX]);
            logger.info("Using Default thread pool size: {}", DEFAULT_THREAD_POOL_SIZE);
            return DEFAULT_THREAD_POOL_SIZE;
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.info("Default thread pool size: {}", DEFAULT_THREAD_POOL_SIZE);
            return DEFAULT_THREAD_POOL_SIZE;
        }
    }
}
