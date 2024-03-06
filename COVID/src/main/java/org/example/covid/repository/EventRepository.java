package org.example.covid.repository;


import org.example.covid.constant.EventStatus;
import org.example.covid.domain.Event;
import org.example.covid.dto.EventDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// TODO: 인스턴스 생성 편의를 위해 임시로 default 사용. repository layer 구현이 완성되면 삭제할 것
public interface EventRepository extends
        JpaRepository<Event, Long>,
        QuerydslPredicateExecutor<Event>,
        QuerydslBinderCustomizer<QEvent> {
//    default List<EventDTO> findEvents(
//            Long placeId,
//            String eventName,
//            EventStatus eventStatus,
//            LocalDateTime eventStartDatetime,
//            LocalDateTime eventEndDatetime
//    ) { return List.of(); }
//    default Optional<EventDTO> findEvent(Long eventId) { return Optional.empty(); }
//    default boolean insertEvent(EventDTO eventDTO) { return false; }
//    default boolean updateEvent(Long eventId, EventDTO dto) { return false; }
//    default boolean deleteEvent(Long eventId) { return false; }

    @Override
    default void customize(QuerydslBindings bindings, QEvent root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.placeId, root.eventName, root.eventStatus, root.eventStartDatetime, root.eventEndDatetime);
        bindings.bind(root.eventName).first(StringExpression::likeIgnoreCase);
        bindings.bind(root.eventStartDatetime).first(ComparableExpression::goe);
        bindings.bind(root.eventEndDatetime).first(ComparableExpression::loe);
    }

}