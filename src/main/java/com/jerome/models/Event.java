package com.jerome.models;

public class Event {

    private final EventLog startedEventLog;
    private final EventLog finishedEventLog;

    public Event(EventLog startedEventLog, EventLog finishedEventLog) {
        this.startedEventLog = startedEventLog;
        this.finishedEventLog = finishedEventLog;
    }

    public EventLog getStartedEventLog() {
        return startedEventLog;
    }

    public EventLog getFinishedEventLog() {
        return finishedEventLog;
    }

    @Override
    public String toString() {
        return "Event{" +
                "startedEventLog=" + startedEventLog +
                ", finishedEventLog=" + finishedEventLog +
                '}';
    }
}
