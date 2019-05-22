package com.jerome;

import com.jerome.exceptions.ProcessingEventLogsFileException;
import com.jerome.exceptions.SleepThreadException;
import com.jerome.models.Event;
import com.jerome.models.EventLog;
import com.jerome.models.EventParameters;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
class EventLogsFileProcessor {

    private EventProcessor eventProcessor;
    private EventLogMapper eventLogMapper;
    private Map<String, EventLog> eventLogsMap;
    private ExecutorService executorService;
    private EventParameters eventParameters;

    EventLogsFileProcessor(EventParameters eventParameters) {
        this.eventParameters = eventParameters;
        eventLogsMap = new HashMap<>();
        eventProcessor = new EventProcessor(new EventAlertDao());
        eventLogMapper = new EventLogMapper();
    }

    void processEventLogsFile() {
        try {
            executorService = Executors.newFixedThreadPool(eventParameters.getNumberOfThreads());
            doProcessEventLogsFile(eventParameters.getFilePath());
            sleepThread();
            executorService.shutdown();
        } catch (IOException e) {
            log.error("There was an error processing event logs file");
            throw new ProcessingEventLogsFileException(e);
        }
    }

    private void sleepThread() {
        try {
            // This is needed because it seems HSQLDB needs some time to commit the changes to the
            // file before the program finishes.
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.error("There was an error trying to sleep the main thread");
            throw new SleepThreadException(e);
        }
    }

    private void doProcessEventLogsFile(String filePath) throws IOException {
        try (LineIterator it = FileUtils.lineIterator(new File(filePath), StandardCharsets.UTF_8.name())) {
            while (it.hasNext()) {
                processEventLogLine(it.nextLine());
            }
        }
    }

    private void processEventLogLine(String eventLogLine) {
        Optional<EventLog> eventLogOptional = eventLogMapper.mapEventLogLineToEventLogObject(eventLogLine);
        eventLogOptional.ifPresent(this::processEventLog);
    }

    private void processEventLog(EventLog eventLog) {
        if (log.isDebugEnabled()) {
            log.debug("Processing new {}", eventLog);
        }

        if (hasFirstLogForThisEventAlreadyArrived(eventLog.getId())) {
            processEvent(eventLog);
        } else {
            saveFirstEventLogUntilTheLastEventLogArrives(eventLog);
        }
    }

    private void saveFirstEventLogUntilTheLastEventLogArrives(EventLog eventLog) {
        eventLogsMap.put(eventLog.getId(), eventLog);
    }

    private void processEvent(EventLog lastEventLog) {
        CompletableFuture.runAsync(() -> {
            Event event = buildEvent(lastEventLog);

            log.debug("Processing {}", event);

            eventProcessor.processEvent(event);

            removeFirstEventLogPreviouslySaved(lastEventLog);

        }, executorService);
    }

    private Event buildEvent(EventLog lastEventLog) {
        Event event;

        if (lastEventLog.getState().equals(EventLog.State.STARTED)) {
            event = Event.newInstance(lastEventLog, eventLogsMap.get(lastEventLog.getId()));
        } else {
            event = Event.newInstance(eventLogsMap.get(lastEventLog.getId()), lastEventLog);
        }

        return event;
    }

    private void removeFirstEventLogPreviouslySaved(EventLog lastEventLog) {
        eventLogsMap.remove(lastEventLog.getId());
    }

    private boolean hasFirstLogForThisEventAlreadyArrived(String eventId) {
        return eventLogsMap.containsKey(eventId);
    }

}
