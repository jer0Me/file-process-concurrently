package com.jerome.models;

import com.jerome.enums.EventType;

public class EventAlert {

    private final String id;
    private final Long duration;
    private final Boolean alert;
    private final EventType type;
    private final String host;

    public EventAlert(String id, Long duration, Boolean alert, EventType type, String host) {
        this.id = id;
        this.duration = duration;
        this.alert = alert;
        this.type = type;
        this.host = host;
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

    public EventType getType() {
        return type;
    }

    public String getHost() {
        return host;
    }
}
