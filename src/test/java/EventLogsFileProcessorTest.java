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
        EventLogsFileProcessor eventLogsFileProcessor = new EventLogsFileProcessor(1);
        eventLogsFileProcessor.processEventLogsFile(
                ClassLoader.getSystemClassLoader().getResource("first_event_log.txt").getPath()
        );
        verifyNew(EventProcessorThread.class).withArguments(any(), any());
    }

    @Test
    public void shouldCreateJustOneEventProcessorThreadForAnEvent() throws Exception {
        whenNew(EventProcessorThread.class).withAnyArguments().thenReturn(mock(EventProcessorThread.class));
        EventLogsFileProcessor eventLogsFileProcessor = new EventLogsFileProcessor(1);
        eventLogsFileProcessor.processEventLogsFile(
                ClassLoader.getSystemClassLoader().getResource("two_event_logs_of_same_event.txt").getPath()
        );
        verifyNew(EventProcessorThread.class, times(1)).withArguments(any(), any());
    }

    @Test
    public void shouldCreateAsEventProcessorThreadsAsDifferentEventsThereAre() throws Exception {
        whenNew(EventProcessorThread.class).withAnyArguments().thenReturn(mock(EventProcessorThread.class));

        EventLogsFileProcessor eventLogsFileProcessor = new EventLogsFileProcessor(1);
        eventLogsFileProcessor.processEventLogsFile(
                ClassLoader.getSystemClassLoader().getResource("two_event_logs_of_different_events.txt").getPath()
        );

        verifyNew(EventProcessorThread.class, times(2)).withArguments(any(), any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldSendEventLogToTheBlockingQueueAssociatedToTheEventProcessorThread() throws Exception {
        LinkedBlockingQueue mockBlockingQueue = mock(LinkedBlockingQueue.class);
        whenNew(LinkedBlockingQueue.class).withAnyArguments().thenReturn(mockBlockingQueue);
        EventLogsFileProcessor eventLogsFileProcessor = new EventLogsFileProcessor(1);
        eventLogsFileProcessor.processEventLogsFile(
                ClassLoader.getSystemClassLoader().getResource("first_event_log.txt").getPath()
        );
        verify(mockBlockingQueue).put(any());
    }

}
