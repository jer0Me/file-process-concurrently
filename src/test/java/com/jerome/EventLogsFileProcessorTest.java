package com.jerome;

import com.jerome.models.EventLog;
import com.jerome.models.EventParameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EventLogsFileProcessor.class)
public class EventLogsFileProcessorTest {

    private HashMap<String, EventLog> mockEventLogsMap;
    private EventProcessor mockEventProcessor;

    @Before
    public void setUp() throws Exception {
        mockEventLogsMap = mock(HashMap.class);
        mockEventProcessor = mock(EventProcessor.class);
        whenNew(HashMap.class).withAnyArguments().thenReturn(mockEventLogsMap);
        whenNew(EventAlertDao.class).withAnyArguments().thenReturn(mock(EventAlertDao.class));
        whenNew(EventProcessor.class).withAnyArguments().thenReturn(mockEventProcessor);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldSaveTheLogIfItsTheFirstForTheEvent() {
        EventLogsFileProcessor eventLogsFileProcessor = new EventLogsFileProcessor(
                buildEventParameters("first_event_log.txt")
        );
        eventLogsFileProcessor.processEventLogsFile();
        verify(mockEventLogsMap).put(any(), any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldSaveAsManyLogsAsDifferentEventsThereAre() {
        EventLogsFileProcessor eventLogsFileProcessor = new EventLogsFileProcessor(
                buildEventParameters("two_event_logs_of_different_events.txt")
        );
        eventLogsFileProcessor.processEventLogsFile();
        verify(mockEventLogsMap, times(2)).put(any(), any());
    }

    @Test
    public void shouldProcessEventIfBothStartedAndFinishedLogsHaveArrived() {
        when(mockEventLogsMap.containsKey(any()))
                .thenReturn(false)
                .thenReturn(true);
        EventLogsFileProcessor eventLogsFileProcessor = new EventLogsFileProcessor(
                buildEventParameters("two_event_logs_of_same_event.txt")
        );
        eventLogsFileProcessor.processEventLogsFile();
        verify(mockEventProcessor).processEvent(any());
    }

    private EventParameters buildEventParameters(String s) {
        return new EventParameters(
                ClassLoader.getSystemClassLoader().getResource(s).getPath(),
                1
        );
    }
}
