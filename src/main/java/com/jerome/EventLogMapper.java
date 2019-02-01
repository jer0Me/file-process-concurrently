package com.jerome;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jerome.models.EventLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

class EventLogMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventLogMapper.class);

    private ObjectMapper objectMapper;

    public EventLogMapper() {
        this.objectMapper = new ObjectMapper();
    }

    public Optional<EventLog> mapEventLogLineToEventLogObject(String eventLogLine) {
        try {
            return Optional.of(
                    objectMapper.readValue(eventLogLine, EventLog.class)
            );
        } catch (IOException e) {
            LOGGER.error("There was an error mapping the event log line: {}", eventLogLine, e);
            return Optional.empty();
        }
    }
}
