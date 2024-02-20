package com.miniproject.programming.dmaker.dto;

import com.miniproject.programming.dmaker.entity.Developer;
import com.miniproject.programming.dmaker.type.DeveloperLevel;
import com.miniproject.programming.dmaker.type.DeveloperSkillType;
import lombok.*;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
@Setter
@ToString
public class DeveloperDto {
    private DeveloperLevel developerLevel;
    private DeveloperSkillType developerSkillType;
    private String memberId;

    public static DeveloperDto fromEntity(Developer developer) {
        return DeveloperDto.builder()
                .developerLevel(developer.getDeveloperLevel())
                .developerSkillType(developer.getDeveloperSkillType())
                .memberId(developer.getMemberId())
                .build();
    }
}
