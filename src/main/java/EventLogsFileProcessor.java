import com.fasterxml.jackson.databind.ObjectMapper;
import models.EventLog;
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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class EventLogsFileProcessor {

    private static final Logger logger = LoggerFactory.getLogger(EventLogsFileProcessor.class);

    private EventProcessor eventProcessor;
    private Map<String, BlockingQueue<EventLog>> eventLogBlockingQueues;
    private ObjectMapper objectMapper;

    private ExecutorService executorService;

    public EventLogsFileProcessor(Integer numberOfThreads) {
        eventLogBlockingQueues = new HashMap<>();
        eventProcessor = new EventProcessor(new EventValidator(), new EventDao());
        objectMapper = new ObjectMapper();
        executorService = Executors.newFixedThreadPool(numberOfThreads);
    }

    public void processEventLogsFile(String filePath) {
        try {
            doProcessEventLogsFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doProcessEventLogsFile(String filePath) throws IOException {
        try (LineIterator it = FileUtils.lineIterator(new File(filePath), StandardCharsets.UTF_8.name())) {
            while (it.hasNext()) {
                processEventLogLine(it.nextLine());
            }
        }
        executorService.shutdown();
    }

    private void processEventLogLine(String eventLogLine) {
        Optional<EventLog> eventLogOptional = mapEventLogLineToEventLogObject(eventLogLine);
        eventLogOptional.ifPresent(this::processEventLog);
    }

    private void processEventLog(EventLog eventLog) {
        logger.debug("Processing new EventLog -> Event Id: " + eventLog.getId() + ", State: "
                + eventLog.getState().name()
        );

        if (!alreadyExistEventProcessorThreadForThisEvent(eventLog.getId())) {
            createNewEventProcessorThread(eventLog);
        }
        sendEventLogToTheEventProcessorThread(eventLog);
    }

    private boolean alreadyExistEventProcessorThreadForThisEvent(String eventId) {
        return eventLogBlockingQueues.containsKey(eventId);
    }

    private void createNewEventProcessorThread(EventLog eventLog) {
        BlockingQueue<EventLog> eventLogBlockingQueue = new LinkedBlockingQueue<>();

        EventProcessorThread eventProcessorThread = new EventProcessorThread(eventLogBlockingQueue, eventProcessor);

        eventLogBlockingQueues.put(eventLog.getId(), eventLogBlockingQueue);

        executorService.submit(eventProcessorThread);

        logger.debug("Created new thread for an Event -> Id: " + eventLog.getId());
    }

    private void sendEventLogToTheEventProcessorThread(EventLog eventLog) {
        try {
            eventLogBlockingQueues.get(eventLog.getId()).put(eventLog);
        } catch (InterruptedException e) {
            logger.error("There was an error processing the Event: " + eventLog.getId(), e);
        }
    }

    private Optional<EventLog> mapEventLogLineToEventLogObject(String eventLogLine) {
        try {
            return Optional.of(
                    objectMapper.readValue(eventLogLine, EventLog.class)
            );
        } catch (IOException e) {
            logger.error("There was an error mapping the event log line: " + eventLogLine, e);
            return Optional.empty();
        }
    }

}
