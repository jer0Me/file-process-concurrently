package com.jerome;

import com.jerome.models.EventLog;
import com.jerome.models.EventParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EventLogsFileProcessor.class)
public class EventLogsFileProcessorTest {

    @Test
    @SuppressWarnings("unchecked")
    public void shouldSaveTheLogIfItsTheFirstForTheEvent() throws Exception {
        HashMap<String, EventLog> eventLogsMap = mock(HashMap.class);
        whenNew(HashMap.class).withAnyArguments().thenReturn(eventLogsMap);
        EventLogsFileProcessor eventLogsFileProcessor = new EventLogsFileProcessor(
                buildEventParameters("first_event_log.txt")
        );
        eventLogsFileProcessor.processEventLogsFile();
        verify(eventLogsMap).put(any(), any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldSaveAsManyLogsAsDifferentEventsThereAre() throws Exception {
        HashMap<String, EventLog> eventLogsMap = mock(HashMap.class);
        whenNew(HashMap.class).withAnyArguments().thenReturn(eventLogsMap);
        EventLogsFileProcessor eventLogsFileProcessor = new EventLogsFileProcessor(
                buildEventParameters("two_event_logs_of_different_events.txt")
        );
        eventLogsFileProcessor.processEventLogsFile();
        verify(eventLogsMap, times(2)).put(any(), any());
    }

    @Test
    public void shouldProcessEventIfBothStartedAndFinishedLogsHaveArrived() throws Exception {
        EventProcessor eventProcessor = mock(EventProcessor.class);
        whenNew(EventProcessor.class).withAnyArguments().thenReturn(eventProcessor);
        EventLogsFileProcessor eventLogsFileProcessor = new EventLogsFileProcessor(
                buildEventParameters("two_event_logs_of_same_event.txt")
        );
        eventLogsFileProcessor.processEventLogsFile();
        verify(eventProcessor).processEvent(any());
    }

    private EventParameters buildEventParameters(String s) {
        return new EventParameters(
                ClassLoader.getSystemClassLoader().getResource(s).getPath(),
                1
        );
    }
}
