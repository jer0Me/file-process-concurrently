package models;

public class EventParameters {

    private final String filePath;
    private final Integer numberOfThreads;

    public EventParameters(String filePath, Integer numberOfThreads) {
        this.filePath = filePath;
        this.numberOfThreads = numberOfThreads;
    }

    public String getFilePath() {
        return filePath;
    }

    public Integer getNumberOfThreads() {
        return numberOfThreads;
    }
}
