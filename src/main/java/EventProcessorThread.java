import models.Event;
import models.EventLog;
import models.EventLogState;

import java.util.concurrent.BlockingQueue;

public class EventProcessorThread implements Runnable {

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

        }
    }

    private void processNextEventLogFromTheQueue() throws InterruptedException {
        while(!eventProcessed) {

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
        }
    }

    private Boolean doesLastEvengLogBelongToTheSameEventAsTheFirstEventLog(EventLog lastEventLog) {
        return firstEventLog.getId().equals(lastEventLog.getId());
    }



}
