package com.jerome;

import com.jerome.exceptions.FilePathParameterMissingException;
import com.jerome.models.EventParameters;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class EventParametersValidator {

    public static final int DEFAULT_THREAD_POOL_SIZE = 1;

    private static final int FILE_PATH_PARAMETER_INDEX = 0;
    private static final int NUMBER_OF_THREADS_PARAMETER_INDEX = 1;

    private final String[] vars;

    public EventParametersValidator(String... vars) {
        this.vars = vars.clone();
    }

    public EventParameters getEventParameters() {
        return EventParameters.newInstance(getFilePath(), getNumberOfThreadsParameter());
    }

    private String getFilePath() {
        try {
            return vars[FILE_PATH_PARAMETER_INDEX];
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("File path parameter is missing");
            throw new FilePathParameterMissingException(e);
        }
    }

    private int getNumberOfThreadsParameter() {
        try {
            return Integer.valueOf(vars[NUMBER_OF_THREADS_PARAMETER_INDEX]);
        } catch (NumberFormatException e) {
            log.info("{} should be a number", vars[NUMBER_OF_THREADS_PARAMETER_INDEX]);
            log.info("Using Default thread pool size: {}", DEFAULT_THREAD_POOL_SIZE);
            return DEFAULT_THREAD_POOL_SIZE;
        } catch (ArrayIndexOutOfBoundsException e) {
            log.info("Default thread pool size: {}", DEFAULT_THREAD_POOL_SIZE);
            return DEFAULT_THREAD_POOL_SIZE;
        }
    }
}
