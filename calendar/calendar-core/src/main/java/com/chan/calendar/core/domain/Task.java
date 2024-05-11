package com.chan.calendar.core.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class Task {

    private Long id;
    private LocalDateTime taskAt;
    private String title;
    private String description;
    private User writer;
    private LocalDateTime createdAt;

}
