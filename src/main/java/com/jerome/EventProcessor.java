package com.jerome;

import com.jerome.models.Event;
import com.jerome.models.EventAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EventProcessor {

    private static final Logger logger = LoggerFactory.getLogger(EventProcessor.class);

    private EventValidator eventValidator;
    private EventAlertDao eventAlertDao;

    EventProcessor(EventValidator eventValidator, EventAlertDao eventAlertDao) {
        this.eventValidator = eventValidator;
        this.eventAlertDao = eventAlertDao;
    }

    void processEvent(Event event) {
        if (eventValidator.isEventDurationLongerThanFourSeconds(event)) {
            eventAlertDao.saveEventAlert(
                    new EventAlert(
                            event.getStartedEventLog().getId(),
                            event.getFinishedEventLog().getTimestamp() - event.getStartedEventLog().getTimestamp(),
                            Boolean.TRUE,
                            event.getFinishedEventLog().getType(),
                            event.getFinishedEventLog().getHost()
                    )
            );
            logger.debug("Event: {} flagged as an Alert", event.getEventId());
        }
    }
}
