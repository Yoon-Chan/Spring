package com.example.spring_rest_doc.member;

import lombok.Getter;

@Getter
public class MemberResponse {
    private final String name;
    private final String email;
    private final Long id;

    public MemberResponse(final Member member) {
        this.email = member.getEmail();
        this.name = member.getName();
        this.id = member.getId();
    }
}
