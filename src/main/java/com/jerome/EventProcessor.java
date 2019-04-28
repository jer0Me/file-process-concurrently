package com.jerome;

import com.jerome.jooq.tables.pojos.EventAlert;
import com.jerome.models.Event;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
class EventProcessor {

    private EventValidator eventValidator;
    private EventAlertDao eventAlertDao;

    public EventProcessor(EventValidator eventValidator, EventAlertDao eventAlertDao) {
        this.eventValidator = eventValidator;
        this.eventAlertDao = eventAlertDao;
    }

    public void processEvent(Event event) {
        if (eventValidator.isEventDurationLongerThanFourSeconds(event)) {
            EventAlert eventAlert = buildEventAlert(event);
            eventAlertDao.saveEventAlert(eventAlert);
            log.info("There is a new alert {}", eventAlert);
        }
    }

    private EventAlert buildEventAlert(Event event) {
        Long duration = (event.getFinishedEventLog().getTimestamp() - event.getStartedEventLog().getTimestamp());
        return new EventAlert(
                UUID.randomUUID(),
                event.getStartedEventLog().getId(),
                duration.intValue(),
                event.getFinishedEventLog().getType().name(),
                event.getFinishedEventLog().getHost(),
                Boolean.TRUE
        );
    }
}
