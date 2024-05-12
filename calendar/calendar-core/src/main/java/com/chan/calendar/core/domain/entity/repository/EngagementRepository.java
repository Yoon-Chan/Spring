package com.chan.calendar.core.domain.entity.repository;

import com.chan.calendar.core.domain.entity.Engagement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EngagementRepository extends JpaRepository<Engagement, Long> {
}
