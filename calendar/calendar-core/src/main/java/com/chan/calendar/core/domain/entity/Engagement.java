package com.chan.calendar.core.domain.entity;

import com.chan.calendar.core.domain.Event;
import com.chan.calendar.core.domain.RequestStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Table(name = "engagements")
@Entity
public class Engagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "event_id")
    @ManyToOne
    private Schedule event;

    @JoinColumn(name = "attendee_id")
    @ManyToOne
    private User attendee;
    private LocalDateTime createdAt = LocalDateTime.now();
    private RequestStatus requestStatus;

    public Engagement(Schedule event, User attendee, LocalDateTime createdAt, RequestStatus requestStatus) {
        this.event = event;
        this.attendee = attendee;
        this.createdAt = createdAt;
        this.requestStatus = requestStatus;
    }
}
