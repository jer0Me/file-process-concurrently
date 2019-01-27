import models.Event;
import models.EventLog;
import models.EventLogState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.*;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EventProcessorThreadTest {

    private BlockingQueue<EventLog> eventLogQueue;
    private EventProcessor eventProcessor;
    private EventProcessorThread eventProcessorThread;

    @Captor
    private ArgumentCaptor<Event> eventCaptor;
    private EventLog eventStartLog;
    private EventLog eventFinishLog;
    private ExecutorService service;


    @Before
    public void setUp() {
        eventLogQueue = new LinkedBlockingQueue<>();
        eventProcessor = mock(EventProcessor.class);
        eventProcessorThread = new EventProcessorThread(eventLogQueue, eventProcessor);

        service = Executors.newSingleThreadExecutor();
        service.execute(eventProcessorThread);

        String eventId = "myEvent";
        eventStartLog = new EventLog(eventId, EventLogState.STARTED, 1491377495210L);
        eventFinishLog = new EventLog(eventId, EventLogState.FINISHED, 1491377495218L);
    }

    @After
    public void tearDown() {
        service.shutdown();
    }

    @Test
    public void shouldProcessEventIfBothEventLogsBelongToTheSameEvent() throws InterruptedException {
        eventLogQueue.put(eventStartLog);
        eventLogQueue.put(eventFinishLog);

        verify(eventProcessor, timeout(1000)).processEvent(eventCaptor.capture());

        assertEquals(eventStartLog, eventCaptor.getValue().getStartedEventLog());
        assertEquals(eventFinishLog, eventCaptor.getValue().getFinishedEventLog());
    }

    @Test
    public void shouldProcessEventRegardlessOfTheOrderThatEventLogsCome() throws InterruptedException {
        eventLogQueue.put(eventStartLog);
        eventLogQueue.put(eventFinishLog);

        verify(eventProcessor, timeout(1000)).processEvent(eventCaptor.capture());

        assertEquals(eventStartLog, eventCaptor.getValue().getStartedEventLog());
        assertEquals(eventFinishLog, eventCaptor.getValue().getFinishedEventLog());
    }

    @Test
    public void shouldDiscardAllEventLogsThatDontBelongToTheSameEventAndWaitForTheCorrectEventFinishLog() throws InterruptedException {
        eventLogQueue.put(eventStartLog);
        eventLogQueue.put(new EventLog("other event", EventLogState.STARTED, 1491377495210L));
        eventLogQueue.put(eventFinishLog);

        verify(eventProcessor, timeout(1000)).processEvent(eventCaptor.capture());

        assertEquals(eventStartLog, eventCaptor.getValue().getStartedEventLog());
        assertEquals(eventFinishLog, eventCaptor.getValue().getFinishedEventLog());

    }
}
