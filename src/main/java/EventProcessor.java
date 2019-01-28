import models.Event;
import models.EventAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventProcessor {

    private static final Logger logger = LoggerFactory.getLogger(EventProcessor.class);

    private EventValidator eventValidator;
    private EventDao eventDao;

    public EventProcessor(EventValidator eventValidator, EventDao eventDao) {
        this.eventValidator = eventValidator;
        this.eventDao = eventDao;
    }

    public void processEvent(Event event) {
        if (eventValidator.isEventDurationLongerThanFourSeconds(event)) {
            eventDao.saveEventAlert(
                    new EventAlert(
                            event.getStartedEventLog().getId(),
                            event.getFinishedEventLog().getTimestamp() - event.getStartedEventLog().getTimestamp(),
                            Boolean.TRUE
                    )
            );
            logger.info("Event: " + event.getEventId() + " flagged as an Alert");
        }

    }
}
