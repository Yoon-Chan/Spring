package com.chan.calendar.api;

import com.chan.calendar.core.SimpleEntity;
import com.chan.calendar.core.SimpleEntityRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@EntityScan("com.chan.calendar.core")
@EnableJpaRepositories("com.chan.calendar.core")
@RestController
@SpringBootApplication
public class ApiApplication {

    private final SimpleEntityRepository repository;

    public ApiApplication(SimpleEntityRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<SimpleEntity> findAll() {
        return  repository.findAll();
    }

    @PostMapping("/save")
    public SimpleEntity saveOne() {
        final SimpleEntity simpleEntity = new SimpleEntity();
        simpleEntity.setName("hello");
        return repository.save(simpleEntity);
    }


    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
