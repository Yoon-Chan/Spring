package org.example.springbatchproject.core.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.example.springbatchproject.core.dto.AptDealDto;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "apt_deal")
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class AptDeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aptDealId;

    @ManyToOne
    @JoinColumn(name = "apt_id")
    private Apt apt;

    @Column(nullable = false)
    private Double exclusiveArea;

    @Column(nullable = false)
    private LocalDate dealDate;

    @Column(nullable = false)
    private Long dealAmount;

    @Column(nullable = false)
    private Integer floor;

    @Column
    private boolean dealCanceled;

    @Column
    private LocalDate dealCanceledDate;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;


    public static AptDeal of(AptDealDto dto, Apt apt) {
        AptDeal aptDeal = new AptDeal();
        aptDeal.setApt(apt);
        aptDeal.setExclusiveArea(dto.getExclusiveArea());
        aptDeal.setDealDate(dto.getDealDate());
        aptDeal.setFloor(dto.getFloor());
        aptDeal.setDealAmount(dto.getDealAmount());
        aptDeal.setDealCanceled(dto.isDealCanceled());
        aptDeal.setDealCanceledDate(dto.getDealCanceledDate());
        return aptDeal;
    }
}