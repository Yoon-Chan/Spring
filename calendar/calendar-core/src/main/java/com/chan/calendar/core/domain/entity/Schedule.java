package com.chan.calendar.core.domain.entity;

import com.chan.calendar.core.domain.Event;
import com.chan.calendar.core.domain.Notification;
import com.chan.calendar.core.domain.ScheduleType;
import com.chan.calendar.core.domain.Task;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "schedules")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class Schedule extends BaseEntity {


    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String title;
    private String description;

    @JoinColumn(name = "writer_id")
    @ManyToOne
    private User writer;

    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    public static Schedule event(String title, String description, LocalDateTime startAt, LocalDateTime endAt, User user) {
        return Schedule.builder()
                .title(title)
                .description(description)
                .startAt(startAt)
                .endAt(endAt)
                .writer(user)
                .scheduleType(ScheduleType.EVENT)
                .build();
    }

    public static Schedule task(String title, String description, LocalDateTime taskAt, User user) {
        return Schedule.builder()
                .title(title)
                .description(description)
                .startAt(taskAt)
                .writer(user)
                .scheduleType(ScheduleType.TASK)
                .build();
    }

    public static Schedule notification(String title, LocalDateTime notify, User user) {
        return Schedule.builder()
                .title(title)
                .startAt(notify)
                .writer(user)
                .scheduleType(ScheduleType.NOTIFICATION)
                .build();
    }

    public Task toTask() {
        return new Task(this);
    }

    public Event toEvent() {
        return new Event(this);
    }

    public Notification toNotification() {
        return new Notification(this);
    }
}
