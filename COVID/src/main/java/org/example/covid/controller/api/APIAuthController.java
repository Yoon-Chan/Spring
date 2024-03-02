package org.example.covid.controller.api;

import org.example.covid.dto.APIDataResponse;
import org.springframework.web.bind.annotation.*;
import org.example.covid.dto.*;

@RequestMapping("/api")
@RestController
public class APIAuthController {


    @PostMapping("/sign-up")
    public APIDataResponse<String> signUp(@RequestBody AdminRequest adminRequest) {
        return APIDataResponse.empty();
    }

    @PostMapping("/login")
    public APIDataResponse<String> login(@RequestBody LoginRequest loginRequest) {
        return APIDataResponse.empty();
    }

}
