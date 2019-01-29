import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.ProcessingEventException;
import models.Event;
import models.EventLog;
import models.EventLogState;
import models.EventParameters;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventLogsFileProcessor {

    private static final Logger logger = LoggerFactory.getLogger(EventLogsFileProcessor.class);

    private EventProcessor eventProcessor;
    private Map<String, EventLog> eventLogsMap;
    private ObjectMapper objectMapper;

    private ExecutorService executorService;

    private EventParameters eventParameters;

    public EventLogsFileProcessor(EventParameters eventParameters) {
        this.eventParameters = eventParameters;
        eventLogsMap = new HashMap<>();
        eventProcessor = new EventProcessor(new EventValidator(), new EventDao());
        objectMapper = new ObjectMapper();
    }

    public void processEventLogsFile() {
        try {
            executorService = Executors.newFixedThreadPool(eventParameters.getNumberOfThreads());
            doProcessEventLogsFile(eventParameters.getFilePath());
            executorService.shutdown();
        } catch (IOException e) {
            logger.error("There was an error processing event logs file", e);
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
        Optional<EventLog> eventLogOptional = mapEventLogLineToEventLogObject(eventLogLine);
        eventLogOptional.ifPresent(this::processEventLog);
    }

    private void processEventLog(EventLog eventLog) {
        if (logger.isDebugEnabled()) {
            logger.debug("Processing new EventLog -> Event Id: {}, State: {}",
                    eventLog.getId(),
                    eventLog.getState().name()
            );
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
                    logger.debug("Processing Event -> id: {}", lastEventLog.getId());

                    if (lastEventLog.getState().equals(EventLogState.STARTED)) {
                        eventProcessor.processEvent(
                                new Event(lastEventLog, eventLogsMap.get(lastEventLog.getId()))
                        );
                    } else {
                        eventProcessor.processEvent(
                                new Event(eventLogsMap.get(lastEventLog.getId()), lastEventLog)
                        );
                    }
                    removeFirstEventLogPreviouslySaved(lastEventLog);
                }
        , executorService).exceptionally(exception -> {
            logger.error("There was an error processing the Event: {}", lastEventLog.getId(), exception);
            throw new ProcessingEventException();
        });
    }

    private void removeFirstEventLogPreviouslySaved(EventLog lastEventLog) {
        eventLogsMap.remove(lastEventLog.getId());
    }

    private boolean hasFirstLogForThisEventAlreadyArrived(String eventId) {
        return eventLogsMap.containsKey(eventId);
    }

    private Optional<EventLog> mapEventLogLineToEventLogObject(String eventLogLine) {
        try {
            return Optional.of(
                    objectMapper.readValue(eventLogLine, EventLog.class)
            );
        } catch (IOException e) {
            logger.error("There was an error mapping the event log line: {}", eventLogLine, e);
            return Optional.empty();
        }
    }

}
