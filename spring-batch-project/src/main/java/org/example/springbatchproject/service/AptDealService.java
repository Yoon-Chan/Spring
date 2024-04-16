package org.example.springbatchproject.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.springbatchproject.core.dto.AptDealDto;
import org.example.springbatchproject.core.entity.Apt;
import org.example.springbatchproject.core.entity.AptDeal;
import org.example.springbatchproject.core.repository.AptDealRepository;
import org.example.springbatchproject.core.repository.AptRepository;
import org.springframework.stereotype.Service;

/*
 *   AptDealDto에 있는 값을 Apt, AptDeal 엔티티로 저장한다.
 * */
@AllArgsConstructor
@Service
public class AptDealService {

    private final AptRepository aptRepository;
    private final AptDealRepository aptDealRepository;

    @Transactional
    public void upsert(AptDealDto dto) {
        Apt apt =getAptOrNew(dto);

        saveAptDeal(dto, apt);
    }

    private void saveAptDeal(AptDealDto dto, Apt apt) {
        AptDeal aptDeal = aptDealRepository.findAptDealByAptAndExclusiveAreaAndDealDateAndDealAmountAndFloor(
                apt, dto.getExclusiveArea(), dto.getDealDate(), dto.getDealAmount(), dto.getFloor()
        ).orElseGet(() -> AptDeal.from(dto, apt));
        aptDeal.setDealCanceled(dto.isDealCanceled());
        aptDeal.setDealCanceledDate(dto.getDealCanceledDate());
        aptDealRepository.save(aptDeal);
    }

    private Apt getAptOrNew(AptDealDto dto) {
        Apt apt = aptRepository.findAptByAptNameAndJibun(dto.getAptName(), dto.getJibun()).orElseGet(() -> Apt.from(dto));
        return aptRepository.save(apt);
    }

}
