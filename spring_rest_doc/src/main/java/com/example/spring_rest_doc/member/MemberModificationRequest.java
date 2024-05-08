package com.example.spring_rest_doc.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class MemberModificationRequest {
    @NotEmpty
    private String name;

}
