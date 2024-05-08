package com.example.spring_rest_doc.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MemberApiTest {

    @Autowired
    private MockMvc mockMvc;

    //단일 조회 테스트


    @Test
    public void member_page_test() throws Exception {
        mockMvc.perform(
                        get("/api/members")
                                .param("size", "10")
                                .param("page", "0")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    public void member_get() throws Exception {
        mockMvc.perform(
                        get("/api/members/{id}", 1L)
                                .param("size", "10")
                                .param("page", "0")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void member_create() throws Exception {
        mockMvc.perform(
                        post("/api/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "    \"email\" : \"bbb@bbb.com\",\n" +
                                        "    \"name\": \"bbb\"\n" +
                                        "}")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void member_modify() throws Exception {
        mockMvc.perform(
                        put("/api/members/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "    \"name\": \"bbb\"\n" +
                                        "}")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}