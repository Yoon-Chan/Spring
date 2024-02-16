package com.miniproject.programming.dmaker.repository;


import com.miniproject.programming.dmaker.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//repository는 스프링 내부에 녹아져있지만 완전히 Spring은 아니다
//JPA라는 기술을 통해서 활용한다.
//Spring JPA를 이용해 레파지토리를 생성
@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {

    Optional<Developer> findByMemberId(String memberId);
}
