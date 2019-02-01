package com.jerome;

import com.jerome.jooq.tables.pojos.EventAlert;
import com.jerome.models.Event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

class EventProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventProcessor.class);

    private EventValidator eventValidator;
    private EventAlertDao eventAlertDao;

    public EventProcessor(EventValidator eventValidator, EventAlertDao eventAlertDao) {
        this.eventValidator = eventValidator;
        this.eventAlertDao = eventAlertDao;
    }

    public void processEvent(Event event) {
        if (eventValidator.isEventDurationLongerThanFourSeconds(event)) {
            eventAlertDao.saveEventAlert(buildEventAlert(event));
            LOGGER.debug("Event: {} flagged as an Alert", event.getEventId());
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
