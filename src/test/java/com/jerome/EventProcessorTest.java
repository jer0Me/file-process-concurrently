package com.jerome;

import com.jerome.jooq.tables.pojos.EventAlert;
import com.jerome.models.Event;
import com.jerome.models.EventLog;
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
    private EventProcessor eventProcessor;

    @Captor
    private ArgumentCaptor<EventAlert> eventAlertCaptor;

    @Before
    public void setUp() {
        eventAlertDao = mock(EventAlertDao.class);
        eventProcessor = new EventProcessor(eventAlertDao);
    }

    @Test
    public void shouldSaveEventToTheDatabaseIfTheEventIsAnAlert() {
        String eventName = "scsmbstgrc";
        Event event = Event.newInstance(
                new EventLog(eventName, EventLog.State.STARTED, 1491377495210L),
                new EventLog(eventName, EventLog.State.FINISHED, 1491377495218L)
        );

        eventProcessor.processEvent(event);
        verify(eventAlertDao).saveEventAlert(eventAlertCaptor.capture());

        assertEquals(eventName, eventAlertCaptor.getValue().getEventId());
        assertTrue(eventAlertCaptor.getValue().getAlert());
    }

    @Test
    public void shouldNotSaveAnEventIfItsNotAnAlert() {
        String eventName = "scsmbstgrc";
        Event event = Event.newInstance(
                new EventLog(eventName, EventLog.State.STARTED, 1491377495210L),
                new EventLog(eventName, EventLog.State.FINISHED, 1491377495214L)
        );        verify(eventAlertDao, never()).saveEventAlert(any());
        eventProcessor.processEvent(event);
    }
}
