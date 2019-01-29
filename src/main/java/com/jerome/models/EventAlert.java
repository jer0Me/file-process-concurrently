package com.jerome.models;

public class EventAlert {

    private final String id;
    private final Long duration;
    private final Boolean alert;

    public EventAlert(String id, Long duration, Boolean alert) {
        this.id = id;
        this.duration = duration;
        this.alert = alert;
    }

    public String getId() {
        return id;
    }

    public Long getDuration() {
        return duration;
    }

    public Boolean isAlert() {
        return alert;
    }
}
