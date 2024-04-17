package org.example.springbatchproject.core.dto;

import lombok.Builder;
import lombok.Getter;
import org.antlr.v4.runtime.misc.Pair;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class NotificationDto {

    private String email;
    private String guName;
    private Integer count;
    private List<AptDto> aptDeals;

    public String toMessage() {
        DecimalFormat df = new DecimalFormat();
        return String.format("%s 아파트 실 거래가 알림\n총 %d개 거래가 발생했습니다.\n", guName, count)
                + aptDeals.stream().map(deal -> String.format("- %s : %s원\n", deal.getName(), df.format(deal.getPrice())))
                    .collect(Collectors.joining());
    }

}
