package com.jerome.models;

import lombok.Data;

@Data
public class Event {

    private final EventLog startedEventLog;
    private final EventLog finishedEventLog;

    private Event(EventLog startedEventLog, EventLog finishedEventLog) {
        this.startedEventLog = startedEventLog;
        this.finishedEventLog = finishedEventLog;
    }

    public static Event newInstance(EventLog startedEventLog, EventLog finishedEventLog) {
        return new Event(startedEventLog, finishedEventLog);
    }

    public Long getDuration() {
        return finishedEventLog.getTimestamp() - startedEventLog.getTimestamp();
    }

    public boolean isValid() {
        return getDuration() <= 4;
    }
}
