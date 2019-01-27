import models.Event;
import models.EventAlert;
import models.EventLog;
import models.EventLogState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EventProcessorTest {

    private EventDao eventDao;
    private EventValidator eventValidator;
    private EventProcessor eventProcessor;

    @Captor
    private ArgumentCaptor<EventAlert> eventAlertCaptor;

    @Before
    public void setUp() {
        eventDao = mock(EventDao.class);
        eventValidator = mock(EventValidator.class);
        eventProcessor = new EventProcessor(eventValidator, eventDao);
    }

    @Test
    public void shouldSaveEventToTheDatabaseIfTheEventIsAnAlert() {
        String eventName = "scsmbstgrc";
        Long startEvent = 1491377495210L;
        Long finishEvent = 1491377495218L;
        Long duration = finishEvent - startEvent;

        Event event = new Event(
                new EventLog(eventName, EventLogState.STARTED, startEvent),
                new EventLog(eventName, EventLogState.FINISHED, finishEvent)
        );

        when(eventValidator.isEventDurationLongerThanFourSeconds(event)).thenReturn(Boolean.TRUE);
        eventProcessor.processEvent(event);
        verify(eventDao).saveEventAlert(eventAlertCaptor.capture());

        assertEquals(eventName, eventAlertCaptor.getValue().getId());
        assertEquals(duration, eventAlertCaptor.getValue().getDuration(), 0);
        assertTrue(eventAlertCaptor.getValue().isAlert());
    }

    @Test
    public void shouldNotSaveAnEventIfItsNotAnAlert() {
        Event event = new Event(null, null);
        when(eventValidator.isEventDurationLongerThanFourSeconds(event)).thenReturn(Boolean.FALSE);
        verify(eventDao, never()).saveEventAlert(any());
        eventProcessor.processEvent(event);
    }
}
