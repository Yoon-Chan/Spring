package org.example.springbatchproject.core.repository;

import org.example.springbatchproject.core.entity.Lawd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LawdRepository extends JpaRepository<Lawd, Long> {

    Optional<Lawd> findByLawdCd(String lawdCd);

    //select distinct substring(lawd_Cd,1,5) from lawd where exist = 1 and lawd_cd not like "%0000000"
    @Query("select distinct substring(l.lawdCd, 1, 5) from Lawd l where l.exist = true and l.lawdCd not like '%0000000'")
    List<String> findDistinctGuLawdCd();

}
