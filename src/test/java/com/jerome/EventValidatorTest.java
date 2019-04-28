package com.jerome;

import com.jerome.models.Event;
import com.jerome.models.EventLog;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventValidatorTest {

    @Test
    public void shouldReturnTrueIfEventDurationIsLongerThanFourSeconds() {
        String eventId = "scsmbstgrc";
        assertTrue(
                new EventValidator().isEventDurationLongerThanFourSeconds(
                        Event.newInstance(
                                new EventLog(eventId, EventLog.State.STARTED, 1491377495210L),
                                new EventLog(eventId, EventLog.State.FINISHED, 1491377495218L)
                        )
                )
        );
    }

    @Test
    public void shouldReturnFalseIfEventDurationIsLessThanFourSeconds() {
        String eventId = "scsmbstgrb";
        assertFalse(
                new EventValidator().isEventDurationLongerThanFourSeconds(
                        Event.newInstance(
                                new EventLog(eventId, EventLog.State.STARTED, 1491377495213L),
                                new EventLog(eventId, EventLog.State.FINISHED, 1491377495216L)
                        )
                )
        );
    }

    @Test
    public void shouldReturnFalseIfEventDurationEqualsFourSeconds() {
        String eventId = "scsmbstgrb";
        assertFalse(
                new EventValidator().isEventDurationLongerThanFourSeconds(
                        Event.newInstance(
                                new EventLog(eventId, EventLog.State.STARTED, 1491377495213L),
                                new EventLog(eventId, EventLog.State.FINISHED, 1491377495217L)
                        )
                )
        );
    }
}
