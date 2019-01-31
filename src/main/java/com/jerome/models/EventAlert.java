package com.jerome.models;

public class EventAlert {

    private final String id;
    private final Long duration;
    private final Boolean alert;
    private final EventLogType type;
    private final String host;

    public EventAlert(String id, Long duration, Boolean alert, EventLogType type, String host) {
        this.id = id;
        this.duration = duration;
        this.alert = alert;
        this.type = type;
        this.host = host;
    }

    public EventAlert(String id, Long duration, Boolean alert) {
        this.id = id;
        this.duration = duration;
        this.alert = alert;
        this.type = EventLogType.UNKNOWN;
        this.host = "";
    }

    public String getId() {
        return id;
    }

    public Long getDuration() {
        return duration;
    }

    public Boolean getAlert() {
        return alert;
    }

    public EventLogType getType() {
        return type;
    }

    public String getHost() {
        return host;
    }
}
