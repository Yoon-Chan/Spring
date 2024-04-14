package org.example.springbatchproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
//생성일시 수정일시를 할 때 현재 시간, 수정된 시간을 직접 넣어도 되지만, 어노테이션으로도 사용이 가능하다.
@EnableJpaAuditing
public class HouseBatchJpaConfig {
}
