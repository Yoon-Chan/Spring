package com.chan.calendar.api.service;

import com.chan.calendar.api.dto.LoginReq;
import com.chan.calendar.api.dto.SignUpReq;
import com.chan.calendar.core.domain.entity.User;
import com.chan.calendar.core.domain.entity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
//서비스는 하위에 여러 빈들을 주입받는다.
//그 타입에 맞는 것을 롬복이 자동으로 생성하기 때문에 코드가 깔끔한 효과가 있다.
@ComponentScan(basePackages = {
        "com.chan.calendar.core"
})
@RequiredArgsConstructor
public class LoginService {

    private final static String LOGIN_SESSION_KEY= "USER_ID";
    private final UserService userService;

    //회원가입
    @Transactional
    public void signUp(SignUpReq signUpReq, HttpSession session) {
        /*
        *   UserService의 Create를 담당한다.(이미 존재하는 경우의 유저 검증은 UserService의 몫)
        *   생성이 되면 session에 담고 리턴
        * */
        final User user = userService.create(signUpReq.toUserCreateReq());
        //User 정보를 value에 담을 수 있다. (name, value) 파라미터를 받는다.
        session.setAttribute(LOGIN_SESSION_KEY, user.getId());
    }

    //로그인
    @Transactional
    public void login(LoginReq loginReq, HttpSession session) {
        /*
        *   세션 값이 있으면 리턴
        *   세션 값이 없으면 비밀번호 체크 후에 로그인 & 세션에 담고 리턴
        * */
        final Long userId = (Long) session.getAttribute(LOGIN_SESSION_KEY);
        if(userId != null) {
            return;
        }
        final Optional<User> user =
                userService.findPwdMatchUser(loginReq.getEmail(), loginReq.getPassword());
        if(user.isPresent()){
            session.setAttribute(LOGIN_SESSION_KEY, user.get().getId());
        }else {
            throw new RuntimeException("password or email not match");
        }
    }

    //로그아웃

    public void logout(HttpSession session) {
        /*
        *   세션 값 제거
        * */
        session.removeAttribute(LOGIN_SESSION_KEY);
    }

}
