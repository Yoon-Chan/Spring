package org.example.covid.service;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.example.covid.constant.ErrorCode;
import org.example.covid.constant.EventStatus;
import org.example.covid.dto.EventDTO;
import org.example.covid.exception.GeneralException;
import org.example.covid.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;

    public List<EventDTO> getEvents(Predicate predicate) {
        try {
            return StreamSupport.stream(eventRepository.findAll(predicate).spliterator(), false)
                    .map(EventDTO::of)
                    .toList();
        } catch (Exception e) {
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }

    public List<EventDTO> getEvents(
            Long placeId,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDatetime,
            LocalDateTime eventEndDatetime
    ) {
        try {
            return null;
        } catch (Exception e) {
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }

    public Optional<EventDTO> getEvent(Long eventId) {
        try {
            return eventRepository.findById(eventId).map(EventDTO::of);
        } catch (Exception e) {
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }

    public boolean createEvent(EventDTO eventDTO) {
        try {
            if (eventDTO == null) {
                return false;
            }

            eventRepository.save(eventDTO.toEntity());
            return true;
        } catch (Exception e) {
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }

    public boolean modifyEvent(Long eventId, EventDTO dto) {
        try {
            if (eventId == null || dto == null) {
                return false;
            }

            eventRepository.findById(eventId)
                    .ifPresent(event -> eventRepository.save(dto.updateEntity(event)));

            return true;
        } catch (Exception e) {
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }

    public boolean removeEvent(Long eventId) {
        try {
            if (eventId == null) {
                return false;
            }

            eventRepository.deleteById(eventId);
            return true;
        } catch (Exception e) {
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }
}