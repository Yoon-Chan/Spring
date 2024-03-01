package org.example.covid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

//기본 루트의 값을 admin으로 시작하게 설정할 수 있다.
//우선 이 어노테이션이 매핑이 되고 다음 매핑에 연결이 된다.
@RequestMapping("/admin")
@Controller
public class AdminController {

    // admin/places가 되는 것이다.
    @GetMapping("/places")
    public String adminPlaces() {
        return "admin/places";
    }


    @GetMapping("/places/{placeId}")
    public String adminPlaceDetail(@PathVariable Integer placeId) {
        return "admin/place-detail";
    }


    @GetMapping("/events")
    public String adminEvents() {
        return "admin/events";
    }


    @GetMapping("/events/{eventId}")
    public String adminEventDetail(@PathVariable Integer eventId) {
        return "admin/event-detail";
    }
}
