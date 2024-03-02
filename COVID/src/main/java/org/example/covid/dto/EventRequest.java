package org.example.covid.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.example.covid.constant.EventStatus;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public record EventRequest(
        Long placeId,
        String eventName,
        EventStatus eventStatus,
        LocalDateTime eventStartDatetime,
        LocalDateTime eventEndDatetime,
        Integer currentNumberOfPeople,
        Integer capacity,
        String memo
) {
    public static EventRequest of(
            @NotNull @Positive Long placeId,
            @NotBlank String eventName,
            @NotNull EventStatus eventStatus,
            @NotNull LocalDateTime eventStartDatetime,
            @NotNull LocalDateTime eventEndDatetime,
            @NotNull @PositiveOrZero Integer currentNumberOfPeople,
            @NotNull @Positive Integer capacity,
            String memo
    ) {
        return new EventRequest(
                placeId,
                eventName,
                eventStatus,
                eventStartDatetime,
                eventEndDatetime,
                currentNumberOfPeople,
                capacity,
                memo
        );
    }

    public EventDTO toDTO() {
        return EventDTO.of(
                this.placeId(),
                this.eventName(),
                this.eventStatus(),
                this.eventStartDatetime(),
                this.eventEndDatetime(),
                this.currentNumberOfPeople(),
                this.capacity(),
                this.memo(),
                null,
                null
        );
    }
}
