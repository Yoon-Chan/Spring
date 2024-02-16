package com.miniproject.programming.dmaker.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class RetiredDeveloper {
    //Entity를 사용하려면 몇가지 규약의 프로퍼티를 만들어야 한다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    //특정 멤버 아이디
    private String memberId;

    //이름과 나이
    private String name;
    private Integer age;

    //자동으로 값을 세팅할수 있도록 하는 @CreatedDate와 @LastModifiedDate
    //이를 사용하려면 Application에 @EnableJpaAuditing을 사용해야한다.
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}