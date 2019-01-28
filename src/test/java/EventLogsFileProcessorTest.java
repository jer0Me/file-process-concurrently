import models.EventParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.concurrent.LinkedBlockingQueue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EventLogsFileProcessor.class)
public class EventLogsFileProcessorTest {

    @Test
    public void shouldCreateANewEventProcessorThreadIfItsTheFirstLogForAnEvent() throws Exception {
        whenNew(EventProcessorThread.class).withAnyArguments().thenReturn(mock(EventProcessorThread.class));
        EventLogsFileProcessor eventLogsFileProcessor = new EventLogsFileProcessor(
                buildEventParameters("first_event_log.txt")
        );
        eventLogsFileProcessor.processEventLogsFile();
        verifyNew(EventProcessorThread.class).withArguments(any(), any());
    }

    @Test
    public void shouldCreateJustOneEventProcessorThreadForAnEvent() throws Exception {
        whenNew(EventProcessorThread.class).withAnyArguments().thenReturn(mock(EventProcessorThread.class));
        EventLogsFileProcessor eventLogsFileProcessor = new EventLogsFileProcessor(
                buildEventParameters("two_event_logs_of_same_event.txt")
        );
        eventLogsFileProcessor.processEventLogsFile();
        verifyNew(EventProcessorThread.class, times(1)).withArguments(any(), any());
    }

    @Test
    public void shouldCreateAsEventProcessorThreadsAsDifferentEventsThereAre() throws Exception {
        whenNew(EventProcessorThread.class).withAnyArguments().thenReturn(mock(EventProcessorThread.class));

        EventLogsFileProcessor eventLogsFileProcessor = new EventLogsFileProcessor(
                buildEventParameters("two_event_logs_of_different_events.txt")
        );
        eventLogsFileProcessor.processEventLogsFile();

        verifyNew(EventProcessorThread.class, times(2)).withArguments(any(), any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldSendEventLogToTheBlockingQueueAssociatedToTheEventProcessorThread() throws Exception {
        LinkedBlockingQueue mockBlockingQueue = mock(LinkedBlockingQueue.class);
        whenNew(LinkedBlockingQueue.class).withAnyArguments().thenReturn(mockBlockingQueue);EventLogsFileProcessor eventLogsFileProcessor = new EventLogsFileProcessor(
                buildEventParameters("first_event_log.txt")
        );
        eventLogsFileProcessor.processEventLogsFile();
        verify(mockBlockingQueue).put(any());
    }

    private EventParameters buildEventParameters(String s) {
        return new EventParameters(
                ClassLoader.getSystemClassLoader().getResource(s).getPath(),
                1
        );
    }

}
