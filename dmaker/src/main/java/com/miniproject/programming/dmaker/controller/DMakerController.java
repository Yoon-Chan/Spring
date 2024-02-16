package com.miniproject.programming.dmaker.controller;


import com.miniproject.programming.dmaker.dto.CreateDeveloper;
import com.miniproject.programming.dmaker.service.DMakerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

//RestController는 DMakerController를
@Slf4j
@RestController
@RequiredArgsConstructor
public class DMakerController {

    //서비스라는 빈을 주입시켜야 한다.
    private final DMakerService dMakerService;

    @GetMapping("/developers")
    public List<String> getAllDevelopers() {
        log.info("GET /developers HTTP/1.1");
        return Arrays.asList("snow", "elsa", "Olaf");
    }

    @PostMapping("/create-developers")
    public List<String> createDevelopers(
            //요청 바디를 넣는 곳
            //리퀘스트 바디에 들어온 값을 자바 빈 validation을 하고 문제가 생기면 진입하기 전에 메소드 아규먼트 낫 벨리드 익셉션이 나타난다.
            @Valid @RequestBody CreateDeveloper.Request request
    ) {
        log.info("request: {}", request);
        dMakerService.createDeveloper(request);
        return List.of("Olaf");
    }

}
