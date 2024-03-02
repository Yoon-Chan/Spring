package org.example.covid.service;

import org.example.covid.constant.EventStatus;
import org.example.covid.dto.EventDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    public List<EventDTO> getEvents(
            Long placeId,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDatetime,
            LocalDateTime eventEndDatetime
    ) {
        return List.of();
    }

    public Optional<EventDTO> getEvent(Long eventId){
        return Optional.empty();
    }

    public boolean createEvent(EventDTO eventDTO) {
        return true;
    }

    public boolean modifyEvent(Long eventId ,EventDTO eventDTO){
        return true;
    }

    public boolean removeEvent(Long eventId) {
        return true;
    }

}
