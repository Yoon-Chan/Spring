package org.example.springbatchproject.core.repository;

import org.example.springbatchproject.core.entity.AptNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AptNotificationRepository extends JpaRepository<AptNotification, Long> {
    Page<AptNotification> findByEnabledIsTrue(Pageable pageable);
}
