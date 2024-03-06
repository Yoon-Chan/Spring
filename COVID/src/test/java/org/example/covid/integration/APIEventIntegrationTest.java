package org.example.covid.integration;

import org.example.covid.constant.ErrorCode;
import org.example.covid.constant.EventStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
//웹 환경이 전혀 필요하지 않을 경우 아래와 같이 NONE을 사용하여 지정할 수 있다.
//이러면 실제 웹서버와 관련된 설정을 전혀 사용하지 않게 된다.
//NONE 이외에도 RANDOM_PORT, DEFINED_PORT, MOCK 이 있다.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class APIEventIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void test() throws Exception {
        //Given
        //When
        //then
        mvc.perform(
                        get("/api/events")
                                .queryParam("placeId", "1")
                                .queryParam("eventName", "운동")
                                .queryParam("eventStatus", EventStatus.OPENED.name())
                                .queryParam("eventStartDatetime", "2021-01-01T00:00:00")
                                .queryParam("eventEndDatetime", "2021-01-02T00:00:00")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()))
                .andDo(print());
    }

}
