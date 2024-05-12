package com.chan.calendar.core.domain.dto;

import com.chan.calendar.core.domain.entity.User;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserCreateReq {
    private final String name;
    private final String email;
    private final String password;
    private final LocalDate birthday;

    public User toUser() {
        return new User(name, email, password, birthday);
    }
}
