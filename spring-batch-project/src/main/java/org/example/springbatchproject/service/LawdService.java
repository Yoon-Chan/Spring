package org.example.springbatchproject.service;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.springbatchproject.core.entity.Lawd;
import org.example.springbatchproject.core.repository.LawdRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LawdService {

    private final LawdRepository lawdRepository;

    @Transactional
    public void upsert(Lawd lawd) {
        Lawd saved = lawdRepository.findByLawdCd(lawd.getLawdCd()).orElseGet(Lawd::new);
        saved.setLawdCd(lawd.getLawdCd());
        saved.setLawdDong(lawd.getLawdDong());
        saved.setExist(lawd.getExist());
        lawdRepository.save(saved);
    }
}
