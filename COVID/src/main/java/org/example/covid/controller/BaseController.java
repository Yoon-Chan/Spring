package org.example.covid.controller;

import org.example.covid.exception.GeneralException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BaseController implements ErrorController {

    //root 페이지
    @GetMapping("/")
    public String root() throws GeneralException {
        return "index";
    }

}
