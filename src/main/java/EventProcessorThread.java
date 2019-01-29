import models.Event;
import models.EventLog;
import models.EventLogState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

public class EventProcessorThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(EventProcessorThread.class);

    private EventLog firstEventLog;
    private Boolean eventProcessed;

    private BlockingQueue<EventLog> eventLogQueue;
    private EventProcessor eventProcessor;

    public EventProcessorThread(BlockingQueue<EventLog> eventLogQueue, EventProcessor eventProcessor) {
        this.eventLogQueue = eventLogQueue;
        this.eventProcessor = eventProcessor;
        this.eventProcessed = Boolean.FALSE;
    }

    @Override
    public void run() {
        try {
            processNextEventLogFromTheQueue();
        } catch (InterruptedException e) {
            logger.error("There was an error processing the next EventLog", e);
        }
    }

    private void processNextEventLogFromTheQueue() throws InterruptedException {
        while (!eventProcessed) {

            EventLog eventLog = eventLogQueue.take();

            if (firstEventLog == null) {
                firstEventLog = eventLog;
            } else {
                processEventLog(eventLog);
            }
        }
    }

    private void processEventLog(EventLog lastEventLog) {
        if (doesLastEvengLogBelongToTheSameEventAsTheFirstEventLog(lastEventLog)) {

            logger.debug("Processing Event -> Id: {}", firstEventLog.getId());

            if (lastEventLog.getState().equals(EventLogState.STARTED)) {
                eventProcessor.processEvent(
                        new Event(lastEventLog, firstEventLog)
                );
            } else {
                eventProcessor.processEvent(
                        new Event(firstEventLog, lastEventLog)
                );
            }

            eventProcessed = Boolean.TRUE;

            logger.debug("Event -> id: {} processed", lastEventLog.getId());
        }
    }

    private Boolean doesLastEvengLogBelongToTheSameEventAsTheFirstEventLog(EventLog lastEventLog) {
        return firstEventLog.getId().equals(lastEventLog.getId());
    }

}
