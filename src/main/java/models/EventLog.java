package models;

public class EventLog {

    private final String id;
    private final EventLogState state;
    private final Long timestamp;
    private final EventLogType type;
    private final String hostname;

    public EventLog(String id, EventLogState state, Long timestamp, EventLogType type, String hostname) {
        this.id = id;
        this.state = state;
        this.timestamp = timestamp;
        this.type = type;
        this.hostname = hostname;
    }

    public EventLog(String id, EventLogState state, Long timestamp) {
        this.id = id;
        this.state = state;
        this.timestamp = timestamp;
        this.type = null;
        this.hostname = null;
    }

    public String getId() {
        return id;
    }

    public EventLogState getState() {
        return state;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public EventLogType getType() {
        return type;
    }

    public String getHostname() {
        return hostname;
    }
}
