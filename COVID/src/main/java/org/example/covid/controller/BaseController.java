package org.example.covid.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BaseController implements ErrorController {

    //root 페이지
    @GetMapping("/")
    public String root() {
        return "index";
    }

    //root 페이지
    @RequestMapping("/error")
    public String error() {
        return "error";
    }
}