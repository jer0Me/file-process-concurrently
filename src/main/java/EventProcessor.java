import models.Event;
import models.EventAlert;

public class EventProcessor {

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
        }

    }
}
