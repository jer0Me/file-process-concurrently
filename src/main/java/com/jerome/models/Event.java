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

    public String getEventId() {
        if (startedEventLog != null) {
            return startedEventLog.getId();
        }

        if (finishedEventLog != null) {
            return finishedEventLog.getId();
        }
        return "";
    }

    @Override
    public String toString() {
        return "Event{" +
                "startedEventLog=" + startedEventLog +
                ", finishedEventLog=" + finishedEventLog +
                '}';
    }
}
