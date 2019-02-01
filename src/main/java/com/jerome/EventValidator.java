package com.jerome;

import com.jerome.models.Event;

class EventValidator {

    public Boolean isEventDurationLongerThanFourSeconds(Event event) {
        return event.getFinishedEventLog().getTimestamp() - event.getStartedEventLog().getTimestamp() > 4;
    }
}
