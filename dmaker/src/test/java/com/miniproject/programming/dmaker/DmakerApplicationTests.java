package com.miniproject.programming.dmaker;

import com.miniproject.programming.dmaker.code.StatusCode;
import com.miniproject.programming.dmaker.dto.CreateDeveloper;
import com.miniproject.programming.dmaker.dto.DeveloperDetailDto;
import com.miniproject.programming.dmaker.entity.Developer;
import com.miniproject.programming.dmaker.exception.DMakerErrorCode;
import com.miniproject.programming.dmaker.exception.DMakerException;
import com.miniproject.programming.dmaker.repository.DeveloperRepository;
import com.miniproject.programming.dmaker.repository.RetiredDeveloperRepository;
import com.miniproject.programming.dmaker.service.DMakerService;
import com.miniproject.programming.dmaker.type.DeveloperLevel;
import com.miniproject.programming.dmaker.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

//단순 자바 테스트가 아니라 모키토라는 외부 기능을 활용해서 이 테스트를 진행하겠다는 의미이다
@ExtendWith(MockitoExtension.class)
class DmakerApplicationTests {

    //Autowired는 @SpringBootTest에서만 사용 가능하다.
//    //@Autowired를 사용하면 자동으로 dMakerService를 사용할 수 있다.
//    @Autowired
//    private DMakerService dMakerService;

    //가상의 목의 레파지토리를 만든다.
    @Mock
    private DeveloperRepository developerRepository;

    @Mock
    private RetiredDeveloperRepository retiredDeveloperRepository;

    //가짜 dMakerService 목을 만들기 위해 @InjectMocks를 사용한다.
    //이 객체를 생성할 때 위에 레파지토리 목을 사용해서 생성하게 된다.
    @InjectMocks
    private DMakerService dMakerService;


    private final Developer defaultDeveloper = Developer.builder()
            .developerLevel(DeveloperLevel.JUNIOR)
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .experienceYears(3)
                .statusCode(StatusCode.EMPLOYED)
                .name("name")
                .age(12)
                .build();
    @Test
    void testEx() {


        //목들의 동작을 정의한다.
        given(developerRepository.findByMemberId(anyString())).willReturn(Optional.of(defaultDeveloper));

        //getDeveloperDetail에는 findByMemberId라는 호출이 있다.
        //이 findByMember 호춣을 하면 위에 given에 주어진 findByMemberId가 실행하게 된다.
        DeveloperDetailDto developer = dMakerService.getDeveloperDetail("memberId");

        assertEquals(DeveloperLevel.JUNIOR, developer.getDeveloperLevel());
        assertEquals(DeveloperSkillType.FRONT_END, developer.getDeveloperSkillType());
        assertEquals(3, developer.getExperienceYears());
    }

    //개발자 생성 성공 테스트
    @Test
    void createDeveloperTest_success() {
        //given
        CreateDeveloper.Request request = CreateDeveloper.Request.builder()
                .developerLevel(DeveloperLevel.JUNIOR)
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .experienceYears(3)
                .name("name")
                .memberId("memberId")
                .age(12)
                .build();

        //모킹
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());

        //저장된 것을 갭쳐하기 위함
        ArgumentCaptor<Developer> captor =
                ArgumentCaptor.forClass(Developer.class);

        //when
        CreateDeveloper.Response developer = dMakerService.createDeveloper(request);

        //then
        verify(developerRepository, times(1))
                //캡처된 데이터를 뽑아올 수 있다.
                .save(captor.capture());

        Developer savedDeveloper = captor.getValue();
        assertEquals(DeveloperLevel.JUNIOR, savedDeveloper.getDeveloperLevel());
        assertEquals(DeveloperSkillType.FRONT_END, savedDeveloper.getDeveloperSkillType());
        assertEquals(3, savedDeveloper.getExperienceYears());
    }

    //중복 에러 검증 코드
    @Test
    void createDeveloperTest_failed_with_duplicated() {
        //given
        CreateDeveloper.Request request = CreateDeveloper.Request.builder()
                .developerLevel(DeveloperLevel.JUNIOR)
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .experienceYears(3)
                .name("name")
                .memberId("memberId")
                .age(12)
                .build();

        //모킹
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));

        //when
        //then
        DMakerException dMakerException = assertThrows(DMakerException.class , () -> dMakerService.createDeveloper(request));
        assertEquals(DMakerErrorCode.DUPLICATED_MEMBER_ID, dMakerException.getDMakerErrorCode());
    }
}
