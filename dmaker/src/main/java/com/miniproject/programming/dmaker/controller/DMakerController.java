package com.miniproject.programming.dmaker.controller;


import com.miniproject.programming.dmaker.dto.CreateDeveloper;
import com.miniproject.programming.dmaker.dto.DeveloperDetailDto;
import com.miniproject.programming.dmaker.dto.DeveloperDto;
import com.miniproject.programming.dmaker.dto.EditDeveloper;
import com.miniproject.programming.dmaker.service.DMakerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//RestController는 DMakerController를
@Slf4j
@RestController
@RequiredArgsConstructor
public class DMakerController {

    //서비스라는 빈을 주입시켜야 한다.
    private final DMakerService dMakerService;

    //DeveloperDto
    //API로 응답을 내려줄 때 그냥 Developer를 쓰면 안된다.
    //꼭 안되는 것은 아니지만 좋지 않은 방법이기 때문에 쓰면 안된다.
    //불필요한 정보가 나갈 수 있고 그 정보를 접근할 때 트랜잭션이 제대로 없는 상태에서
    //정보를 접근하려고 하다 보면 문제가 발생할 수 있다.
    //그래서 DeveloperDto를 통해서 entity와 응답을 내려주는 데이터를 서로 분리해주는 것이 유연성이나 다른 면에서도 좋다.
    @GetMapping("/developers")
    public List<DeveloperDto> getAllDevelopers() {
        log.info("GET /developers HTTP/1.1");
        return dMakerService.getAllEmployedDevelopers();
    }

    @GetMapping("/developer/{memberId}")
    public DeveloperDetailDto getMemberDeveloper(@PathVariable String memberId){
        return dMakerService.getDeveloperDetail(memberId);
    }

    @PostMapping("/create-developers")
    public CreateDeveloper.Response createDevelopers(
            //요청 바디를 넣는 곳
            //리퀘스트 바디에 들어온 값을 자바 빈 validation을 하고 문제가 생기면 진입하기 전에 메소드 아규먼트 낫 벨리드 익셉션이 나타난다.
            @Valid @RequestBody CreateDeveloper.Request request
    ) {
        log.info("request: {}", request);
        return dMakerService.createDeveloper(request);
    }

    //수정
    // put은 모든 정보를 다 수정하겠다는 의미이며
    // 패치는 우리가 해당 리소스 중에 특정 데이터만 수정을 해주겠다는 의미이다.
    @PutMapping("/developer/{memberId}")
    public DeveloperDetailDto editDeveloper(
        @PathVariable String memberId,
        @Valid @RequestBody EditDeveloper.Request request
    ){
        log.info("PUT UPDATE");

        return dMakerService.editDeveloper(memberId, request);
    }

    @DeleteMapping("/developer/{memberId}")
    public DeveloperDetailDto deleteDeveloper(
            @PathVariable String memberId
    ){
        log.info("");

        return dMakerService.deleteDeveloper(memberId);
    }


}
