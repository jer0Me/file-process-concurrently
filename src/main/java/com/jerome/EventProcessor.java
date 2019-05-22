package com.jerome;

import com.jerome.jooq.tables.pojos.EventAlert;
import com.jerome.models.Event;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
class EventProcessor {

    private EventAlertDao eventAlertDao;

    EventProcessor(EventAlertDao eventAlertDao) {
        this.eventAlertDao = eventAlertDao;
    }

    void processEvent(Event event) {
        if (!event.isValid()) {
            EventAlert eventAlert = buildEventAlert(event);
            eventAlertDao.saveEventAlert(eventAlert);
            log.info("There is a new alert {}", eventAlert);
        }
    }

    private EventAlert buildEventAlert(Event event) {
        return new EventAlert(
                UUID.randomUUID(),
                event.getStartedEventLog().getId(),
                event.getDuration().intValue(),
                event.getFinishedEventLog().getType().name(),
                event.getFinishedEventLog().getHost(),
                Boolean.TRUE
        );
    }
}
