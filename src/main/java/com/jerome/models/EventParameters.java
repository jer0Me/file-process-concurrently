package com.jerome.models;

public class EventParameters {

    private final String filePath;
    private final int numberOfThreads;

    public EventParameters(String filePath, int numberOfThreads) {
        this.filePath = filePath;
        this.numberOfThreads = numberOfThreads;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }
}
