package com.miniproject.programming.dmaker.controller;

import com.miniproject.programming.dmaker.dto.DeveloperDto;
import com.miniproject.programming.dmaker.service.DMakerService;
import com.miniproject.programming.dmaker.type.DeveloperLevel;
import com.miniproject.programming.dmaker.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//컨트롤러 테스트

//WebMvcTest는 모키토링 비슷하다.
//컨트롤러와 관련된 빈들만 올려서 컨트롤러도 원하는 컨트롤러만 올려서 많이 테스트를 진행할 때 쓰면 좋다.
//컨트롤러 어드바이스나 필터도 올려주기 때문에 이 컨트롤러까지 접근하기 위한 경로가 되는 기반들이 많아 따라 올라가서 컨트롤러 테스트가 용이할 수 있다.
@WebMvcTest(DMakerController.class)
class DMakerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DMakerService dMakerService;


    protected MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType()
            , MediaType.APPLICATION_JSON.getSubtype()
            , StandardCharsets.UTF_8);

    @Test
    void getAllDevelopers() throws Exception {
        DeveloperDto developerJunior = DeveloperDto.builder()
                .developerSkillType(DeveloperSkillType.BACK_END)
                .developerLevel(DeveloperLevel.JUNIOR)
                .memberId("memberId1")
                .build();

        DeveloperDto developerSenior = DeveloperDto.builder()
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .developerLevel(DeveloperLevel.SENIOR)
                .memberId("memberId1")
                .build();

        DeveloperDto developerJungnior = DeveloperDto.builder()
                .developerSkillType(DeveloperSkillType.BACK_END)
                .developerLevel(DeveloperLevel.JUNGNIOR)
                .memberId("memberId1")
                .build();
        //모키토와 비슷하게 모킹하기
        given(dMakerService.getAllEmployedDevelopers())
                .willReturn(List.of(
                        developerJunior, developerJungnior, developerSenior
                ));

        mockMvc.perform(get("/developers").contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.[0].developerSkillType", is(DeveloperSkillType.BACK_END.name()))
                )
                .andExpect(
                        jsonPath("$.[0].developerLevel", is(DeveloperLevel.JUNIOR.name()))
                )
                .andExpect(
                        jsonPath("$.[2].developerSkillType", is(DeveloperSkillType.FRONT_END.name()))
                )
                .andExpect(
                        jsonPath("$.[2].developerLevel", is(DeveloperLevel.SENIOR.name()))
                );

    }

}