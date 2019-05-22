package com.jerome;

import com.jerome.models.Event;
import com.jerome.models.EventLog;
import org.junit.Test;

import static org.junit.Assert.*;

public class EventTest {

    @Test
    public void shouldReturnTheDuration() {
        EventLog eventLogStarted = new EventLog("myEventLog", EventLog.State.STARTED, 50L);
        EventLog eventLogFinished = new EventLog("myEventLog", EventLog.State.FINISHED, 100L);
        Event event = Event.newInstance(eventLogStarted, eventLogFinished);
        assertEquals(event.getDuration(), 50L, 0);
    }

    @Test
    public void shouldBeValidIfDurationIsLessThanFourSeconds() {
        EventLog eventLogStarted = new EventLog("myEventLog", EventLog.State.STARTED, 1L);
        EventLog eventLogFinished = new EventLog("myEventLog", EventLog.State.FINISHED, 3L);
        Event event = Event.newInstance(eventLogStarted, eventLogFinished);
        assertTrue(event.isValid());
    }

    @Test
    public void shouldNotBeValidIfDurationIsMoreThanFourSeconds() {
        EventLog eventLogStarted = new EventLog("myEventLog", EventLog.State.STARTED, 1L);
        EventLog eventLogFinished = new EventLog("myEventLog", EventLog.State.FINISHED, 6L);
        Event event = Event.newInstance(eventLogStarted, eventLogFinished);
        assertFalse(event.isValid());
    }

    @Test
    public void shouldBeValidIfDurationIsFourSeconds() {
        EventLog eventLogStarted = new EventLog("myEventLog", EventLog.State.STARTED, 1L);
        EventLog eventLogFinished = new EventLog("myEventLog", EventLog.State.FINISHED, 5L);
        Event event = Event.newInstance(eventLogStarted, eventLogFinished);
        assertTrue(event.isValid());
    }
}
