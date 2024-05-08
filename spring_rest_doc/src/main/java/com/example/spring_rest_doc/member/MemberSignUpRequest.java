package com.example.spring_rest_doc.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class MemberSignUpRequest {

    @Email
    private String email;

    @NotEmpty
    private String name;

    public Member toEntity() {
        return new Member(email, name);
    }
}
