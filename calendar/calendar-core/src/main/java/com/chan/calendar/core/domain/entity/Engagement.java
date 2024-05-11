package com.chan.calendar.core.domain.entity;

import com.chan.calendar.core.domain.RequestStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Table(name = "engagements")
@Entity
public class Engagement extends BaseEntity {

    @JoinColumn(name = "event_id")
    @ManyToOne
    private Schedule event;

    @JoinColumn(name = "attendee_id")
    @ManyToOne
    private User attendee;
    private RequestStatus requestStatus;

    public Engagement(Schedule event, User attendee, RequestStatus requestStatus) {
        this.event = event;
        this.attendee = attendee;
        this.requestStatus = requestStatus;
    }
}
