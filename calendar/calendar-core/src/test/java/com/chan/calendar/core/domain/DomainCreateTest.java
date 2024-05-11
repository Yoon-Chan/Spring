package com.chan.calendar.core.domain;

import com.chan.calendar.core.domain.entity.Engagement;
import com.chan.calendar.core.domain.entity.Schedule;
import com.chan.calendar.core.domain.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DomainCreateTest {

    @Test
    void eventCreate() {
        final User me = new User(
                "meme", "email", "pw", LocalDate.now()
        );
        final Schedule taskSchedule = Schedule.task("할일", "청소하기", LocalDateTime.now(), me);

        assertEquals(taskSchedule.getScheduleType(), ScheduleType.TASK);
        assertEquals(taskSchedule.getTitle(), "할일");
    }
}