package com.jerome.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jerome.enums.EventLogState;
import com.jerome.enums.EventType;

public class EventLog {

    private final String id;
    private final EventLogState state;
    private final Long timestamp;

    @JsonIgnoreProperties
    private final EventType type;

    @JsonIgnoreProperties
    private final String host;

    @JsonCreator
    public EventLog(@JsonProperty("id") String id,
                    @JsonProperty("state") EventLogState state,
                    @JsonProperty("timestamp") Long timestamp,
                    @JsonProperty("type") EventType type,
                    @JsonProperty("host") String host) {

        this.id = id;
        this.state = state;
        this.timestamp = timestamp;
        this.type = type;
        this.host = host;
    }

    public EventLog(String id,
                    EventLogState state,
                    Long timestamp) {

        this.id = id;
        this.state = state;
        this.timestamp = timestamp;
        this.type = EventType.UNKNOWN;
        this.host = "";
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

    public EventType getType() {
        return type;
    }

    public String getHost() {
        return host;
    }
}
