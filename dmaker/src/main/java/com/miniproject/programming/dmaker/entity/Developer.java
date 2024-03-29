package com.miniproject.programming.dmaker.entity;

import com.miniproject.programming.dmaker.type.DeveloperLevel;
import com.miniproject.programming.dmaker.type.DeveloperSkillType;
import com.miniproject.programming.dmaker.code.StatusCode;
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
public class Developer {
    //Entity를 사용하려면 몇가지 규약의 프로퍼티를 만들어야 한다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    // 주니어, 중니어, 시니어 선택
    @Enumerated(EnumType.STRING)
    private DeveloperLevel developerLevel;

    //백엔드, 프론트, 풀스택 선택
    @Enumerated
    private DeveloperSkillType developerSkillType;

    //경력 연수
    private Integer experienceYears;

    //특정 멤버 아이디
    private String memberId;

    //이름과 나이
    private String name;
    private Integer age;

    @Enumerated(EnumType.STRING)
    private StatusCode statusCode;


    //자동으로 값을 세팅할수 있도록 하는 @CreatedDate와 @LastModifiedDate
    //이를 사용하려면 Application에 @EnableJpaAuditing을 사용해야한다.
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
