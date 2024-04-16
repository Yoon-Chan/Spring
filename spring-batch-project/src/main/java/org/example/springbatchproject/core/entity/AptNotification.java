package org.example.springbatchproject.core.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "apt_notification")
@EntityListeners(AuditingEntityListener.class)
public class AptNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aptNotificationId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String guLawdCd;

    @Column(nullable = false)
    private boolean enabled;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
