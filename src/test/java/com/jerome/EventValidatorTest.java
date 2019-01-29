package com.jerome;

import com.jerome.models.Event;
import com.jerome.models.EventLog;
import com.jerome.models.EventLogState;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventValidatorTest {

    @Test
    public void shouldReturnTrueIfEventDurationIsLongerThanFourSeconds() {
        String eventId = "scsmbstgrc";
        assertTrue(
                new EventValidator().isEventDurationLongerThanFourSeconds(
                        new Event(
                                new EventLog(eventId, EventLogState.STARTED, 1491377495210L),
                                new EventLog(eventId, EventLogState.FINISHED, 1491377495218L)
                        )
                )
        );
    }

    @Test
    public void shouldReturnFalseIfEventDurationIsLessThanFourSeconds() {
        String eventId = "scsmbstgrb";
        assertFalse(
                new EventValidator().isEventDurationLongerThanFourSeconds(
                        new Event(
                                new EventLog(eventId, EventLogState.STARTED, 1491377495213L),
                                new EventLog(eventId, EventLogState.FINISHED, 1491377495216L)
                        )
                )
        );
    }

    @Test
    public void shouldReturnFalseIfEventDurationEqualsFourSeconds() {
        String eventId = "scsmbstgrb";
        assertFalse(
                new EventValidator().isEventDurationLongerThanFourSeconds(
                        new Event(
                                new EventLog(eventId, EventLogState.STARTED, 1491377495213L),
                                new EventLog(eventId, EventLogState.FINISHED, 1491377495217L)
                        )
                )
        );
    }
}
