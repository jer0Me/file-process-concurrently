package com.jerome.models;

import lombok.Value;

@Value
public class EventParameters {

    private final String filePath;
    private final int numberOfThreads;

    private EventParameters(String filePath, int numberOfThreads) {
        this.filePath = filePath;
        this.numberOfThreads = numberOfThreads;
    }

    public static EventParameters newInstance(String filePath, int numberOfThreads) {
        return new EventParameters(filePath, numberOfThreads);
    }

}
