package com.miniproject.programming.dmaker.service;

import com.miniproject.programming.dmaker.dto.CreateDeveloper;
import com.miniproject.programming.dmaker.dto.DeveloperDetailDto;
import com.miniproject.programming.dmaker.dto.DeveloperDto;
import com.miniproject.programming.dmaker.entity.Developer;
import com.miniproject.programming.dmaker.exception.DMakerException;
import com.miniproject.programming.dmaker.repository.DeveloperRepository;
import com.miniproject.programming.dmaker.type.DeveloperLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.miniproject.programming.dmaker.exception.DMakerErrorCode.*;

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
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {

        //비즈니스 검증
        validateCreateDeveloperRequest(request);
        //business logic start
        Developer developer = Developer.builder()
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .experienceYears(request.getExperienceYears())
                .name(request.getName())
                .memberId(request.getMemberId())
                .age(request.getAge())
                .build();

        //여기서 여러 db 관련 작업을 진행한다.
        developerRepository.save(developer);
        //business logic end
        return CreateDeveloper.Response.fromEntity(developer);
    }

    private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {
        DeveloperLevel developerLevel = request.getDeveloperLevel();
        Integer experienceYears = request.getExperienceYears();

        if (developerLevel == DeveloperLevel.SENIOR && experienceYears < 10) {
            //특정 비즈니스에서의 어떤 예외적인 상황이 발생했을 때, 이런 일반적인 Exception(RuntimeException) 말고 커스텀 익셉션을 사용해야 정확히 어떤 에러가 났는지
            //확인할 수 있고 원하는 기능을 넣을 수 있기 때문에
            //throw new RuntimeException("SENIOR need 10 years experience");
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }

        if (developerLevel == DeveloperLevel.JUNGNIOR &&
                experienceYears < 4 || experienceYears >= 10) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }

        if (developerLevel == DeveloperLevel.JUNIOR && experienceYears >= 4) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }

//        Optional<Developer> developer =developerRepository.findByMemberId(request.getMemberId());
//        if(developer.isPresent()) throw new DMakerException(DUPLICATED_MEMBER_ID);
        //아래의 코드로 한번에 해결할 수 있다.
        developerRepository.findByMemberId(request.getMemberId()).ifPresent(
                developer -> {
                    throw new DMakerException(DUPLICATED_MEMBER_ID);
                }
        );
    }

    public List<DeveloperDto> getAllDevelopers() {
        return developerRepository.findAll().stream().map(DeveloperDto::fromEntity
        ).collect(Collectors.toList());

    }

    public DeveloperDetailDto getDeveloperDetail(String memberId) {
        return developerRepository.findByMemberId(memberId).map(DeveloperDetailDto::fromEntity)
                .orElseThrow(() -> new DMakerException(NO_DEVELOPER));
    }
}
