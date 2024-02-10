package com.miniproject.programming.dmaker.service;

import com.miniproject.programming.dmaker.entity.Developer;
import com.miniproject.programming.dmaker.repository.DeveloperRepository;
import com.miniproject.programming.dmaker.type.DeveloperLevel;
import com.miniproject.programming.dmaker.type.DeveloperSkillType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//비즈니스 로직을 담당해주는 부분이다.
//서비스나 컨트롤러는 RequiredArgsConstructor를 사용하면 편하다.
// RequiredArgsConstructor를 등로하고 아래의 repository를 등록하면
//해당 레파지토리를 자동으로 인젝션 해주게 된다.
//인젝션 동작 방식은 예전에는 Autowired나 Inject를 사용해야하는데
//문제점으로는 서비스 코드가 너무 어노테이션들의 종속적으로 만들어져있기때문에 단독으로 테스트하고싶어도 하기 어려워지는 문제점이 있었다.
// 그 이후에 나온 방식이 2번이다.
// 하지만 2번 방식은 레파지토리가 많아질 경우 고치는 것이 까다로울 수 있다.

//그래서 RequiredArgsConstructor와 받을 프로퍼티를 final로 지정하게 되면 자동으로 인젝션 받을 수 있도록 할 수 있다.
@Service
@RequiredArgsConstructor
public class DMakerService {

    //@Autowired
    //@Inject
    private final DeveloperRepository developerRepository;

    //2번 방식: 생성자에 주입을 받는 방식
    //public DMakerService(DeveloperRepository developerRepository) {
//        this.developerRepository = developerRepository
//    }

    @Transactional
    public void createDeveloper() {
        Developer developer = Developer.builder()
                .developerLevel(DeveloperLevel.JUNIOR)
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .experienceYears(2)
                .name("Olaf")
                .age(5)
                .build();

        developerRepository.save(developer);
    }
}
