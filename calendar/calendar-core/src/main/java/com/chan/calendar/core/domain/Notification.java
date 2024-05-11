package com.chan.calendar.core.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class Notification {

    private Long id;
    private LocalDateTime notifyAt;
    private String title;
    private User writer;
    private LocalDateTime createdAt;
}
