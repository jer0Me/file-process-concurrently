
import models.Event;
import models.EventLog;
import models.EventLogState;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventValidatorTest {


    @Test
    public void shouldReturnTrueIfEventDurationIsLongerThanFourSeconds() {
        assertTrue(
                new EventValidator().isEventDurationLongerThanFourSeconds(
                        new Event(
                                new EventLog("scsmbstgrc", EventLogState.STARTED, 1491377495210L),
                                new EventLog("scsmbstgrc", EventLogState.FINISHED, 1491377495218L)
                        )

                )
        );
    }

    @Test
    public void shouldReturnFalseIfEventDurationIsLessThanFourSeconds() {
        assertFalse(
                new EventValidator().isEventDurationLongerThanFourSeconds(
                        new Event(
                                new EventLog("scsmbstgrb", EventLogState.STARTED, 1491377495213L),
                                new EventLog("scsmbstgrb", EventLogState.FINISHED, 1491377495216L)
                        )

                )
        );
    }

    @Test
    public void shouldReturnFalseIfEventDurationEqualsFourSeconds() {
        assertFalse(
                new EventValidator().isEventDurationLongerThanFourSeconds(
                        new Event(
                                new EventLog("scsmbstgrb", EventLogState.STARTED, 1491377495213L),
                                new EventLog("scsmbstgrb", EventLogState.FINISHED, 1491377495217L)
                        )

                )
        );
    }

}
