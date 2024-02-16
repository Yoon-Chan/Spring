package com.miniproject.programming.dmaker.dto;

import com.miniproject.programming.dmaker.code.StatusCode;
import com.miniproject.programming.dmaker.entity.Developer;
import com.miniproject.programming.dmaker.type.DeveloperLevel;
import com.miniproject.programming.dmaker.type.DeveloperSkillType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeveloperDetailDto {
    private DeveloperLevel developerLevel;
    private DeveloperSkillType developerSkillType;
    //경력 연수
    private Integer experienceYears;
    //특정 멤버 아이디
    private String memberId;
    //이름과 나이
    private String name;
    private Integer age;
    private StatusCode statusCode;

    public static DeveloperDetailDto fromEntity(Developer developer) {
        return DeveloperDetailDto.builder()
                .developerLevel(developer.getDeveloperLevel())
                .developerSkillType(developer.getDeveloperSkillType())
                .memberId(developer.getMemberId())
                .age(developer.getAge())
                .experienceYears(developer.getExperienceYears())
                .name(developer.getName())
                .statusCode(developer.getStatusCode())
                .build();
    }
}
