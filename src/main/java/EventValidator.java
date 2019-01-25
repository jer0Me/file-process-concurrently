import models.Event;

class EventValidator {

    public Boolean isEventDurationLongerThanFourSeconds(Event event) {
        return event.getFinishedEventLog().getTimestamp() - event.getStartedEventLog().getTimestamp() > 4;
    }
}
