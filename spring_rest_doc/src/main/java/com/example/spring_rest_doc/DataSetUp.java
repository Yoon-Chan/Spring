package com.example.spring_rest_doc;

import com.example.spring_rest_doc.member.Member;
import com.example.spring_rest_doc.member.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class DataSetUp implements ApplicationRunner {

    private final MemberRepository memberRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        final List<Member> members = new ArrayList<>();
        members.add(new Member("vsvx1@naver.com", "chan"));
        members.add(new Member("vsvx2@naver.com", "c"));
        members.add(new Member("vsvx3@naver.com", "d"));
        members.add(new Member("vsvx4@naver.com", "e"));

        memberRepository.saveAll(members);
    }
}
