package com.chan.calendar.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.RestController;

@EnableJpaAuditing
@RestController
@SpringBootApplication
public class ApiApplication {

//    private final SimpleEntityRepository repository;
//
//    public ApiApplication(SimpleEntityRepository repository) {
//        this.repository = repository;
//    }

//    @GetMapping
//    public List<SimpleEntity> findAll() {
//        return  repository.findAll();
//    }
//
//    @PostMapping("/save")
//    public SimpleEntity saveOne() {
//        final SimpleEntity simpleEntity = new SimpleEntity();
//        simpleEntity.setName("hello");
//        return repository.save(simpleEntity);
//    }


    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
