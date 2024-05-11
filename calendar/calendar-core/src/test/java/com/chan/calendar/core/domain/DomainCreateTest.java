package com.chan.calendar.core.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DomainCreateTest {

    @Test
    void eventCreate() {
        final User writer = new User("writer", "email@email", "pw", LocalDate.now(), LocalDateTime.now());
        final User attendee = new User("attendee", "email@email", "pw", LocalDate.now(), LocalDateTime.now());
        final Event event = new Event(
                LocalDateTime.now(),
                LocalDateTime.now(),
                "title",
                "desc",
                writer,
                LocalDateTime.now()
        );

        event.addEngagement(new Engagement(event, attendee, LocalDateTime.now(), RequestStatus.REQUESTED));

        assertEquals(event.getEngagements().get(0).getEvent().getWriter().getName(), "writer");

    }
}