package com.jerome;

import com.jerome.models.Event;
import com.jerome.models.EventAlert;
import com.jerome.models.EventLog;
import com.jerome.enums.EventLogState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;

@RunWith(MockitoJUnitRunner.class)
public class EventProcessorTest {

    private EventAlertDao eventAlertDao;
    private EventValidator eventValidator;
    private EventProcessor eventProcessor;

    @Captor
    private ArgumentCaptor<EventAlert> eventAlertCaptor;

    @Before
    public void setUp() {
        eventAlertDao = mock(EventAlertDao.class);
        eventValidator = mock(EventValidator.class);
        eventProcessor = new EventProcessor(eventValidator, eventAlertDao);
    }

    @Test
    public void shouldSaveEventToTheDatabaseIfTheEventIsAnAlert() {
        String eventName = "scsmbstgrc";
        long startEvent = 1491377495210L;
        long finishEvent = 1491377495218L;
        long duration = finishEvent - startEvent;

        Event event = new Event(
                new EventLog(eventName, EventLogState.STARTED, startEvent),
                new EventLog(eventName, EventLogState.FINISHED, finishEvent)
        );

        when(eventValidator.isEventDurationLongerThanFourSeconds(event)).thenReturn(Boolean.TRUE);
        eventProcessor.processEvent(event);
        verify(eventAlertDao).saveEventAlert(eventAlertCaptor.capture());

        assertEquals(eventName, eventAlertCaptor.getValue().getId());
        assertEquals(duration, eventAlertCaptor.getValue().getDuration(), 0);
        assertTrue(eventAlertCaptor.getValue().getAlert());
    }

    @Test
    public void shouldNotSaveAnEventIfItsNotAnAlert() {
        Event event = new Event(null, null);
        when(eventValidator.isEventDurationLongerThanFourSeconds(event)).thenReturn(Boolean.FALSE);
        verify(eventAlertDao, never()).saveEventAlert(any());
        eventProcessor.processEvent(event);
    }
}
