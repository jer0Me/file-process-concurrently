package com.jerome;

import com.jerome.models.Event;
import com.jerome.models.EventAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EventProcessor {

    private static final Logger logger = LoggerFactory.getLogger(EventProcessor.class);

    private EventValidator eventValidator;
    private EventDao eventDao;

    EventProcessor(EventValidator eventValidator, EventDao eventDao) {
        this.eventValidator = eventValidator;
        this.eventDao = eventDao;
    }

    void processEvent(Event event) {
        if (eventValidator.isEventDurationLongerThanFourSeconds(event)) {
            eventDao.saveEventAlert(
                    new EventAlert(
                            event.getStartedEventLog().getId(),
                            event.getFinishedEventLog().getTimestamp() - event.getStartedEventLog().getTimestamp(),
                            Boolean.TRUE
                    )
            );
            logger.debug("Event: {} flagged as an Alert", event.getEventId());
        }
    }
}
