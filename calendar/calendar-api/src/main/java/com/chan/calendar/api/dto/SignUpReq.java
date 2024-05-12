package com.chan.calendar.api.dto;

import com.chan.calendar.core.domain.dto.UserCreateReq;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SignUpReq {
    private final String name;
    private final String email;
    private final String password;
    private final LocalDate birthday;

    public UserCreateReq toUserCreateReq() {
        return new UserCreateReq(
                name,
                email,
                password,
                birthday
        );
    }
}
