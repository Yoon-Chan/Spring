package org.example.springbatchproject.job.notify;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springbatchproject.adapter.FakeSendService;
import org.example.springbatchproject.core.dto.AptDto;
import org.example.springbatchproject.core.dto.NotificationDto;
import org.example.springbatchproject.core.entity.AptNotification;
import org.example.springbatchproject.core.repository.AptNotificationRepository;
import org.example.springbatchproject.core.repository.LawdRepository;
import org.example.springbatchproject.job.validator.DealDateParameterValidator;
import org.example.springbatchproject.service.AptDealService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AptNotificationJobConfig {

    @Bean
    public Job aptNotificationJob(
            JobRepository jobRepository,
            @Qualifier("aptNotificationStep") Step aptNotificationStep) {
        return new JobBuilder("aptNotificationJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .validator(new DealDateParameterValidator())
                .start(aptNotificationStep)
                .build();
    }

    @JobScope
    @Bean
    @Qualifier("aptNotificationStep")
    public Step aptNotificationStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            RepositoryItemReader<AptNotification> aptNotificationRepositoryItemReader,
            ItemProcessor<AptNotification, NotificationDto> aptNotificationItemProcessor,
            ItemWriter<NotificationDto> aptNotificationItemWriter)
    {
        return new StepBuilder("aptNotificationStep", jobRepository)
                .<AptNotification, NotificationDto>chunk(10,transactionManager)
                .reader(aptNotificationRepositoryItemReader)
                .processor(aptNotificationItemProcessor)
                .writer(aptNotificationItemWriter)
                .build();
    }

    @StepScope
    @Bean
    public RepositoryItemReader<AptNotification> aptNotificationRepositoryItemReader(AptNotificationRepository aptNotificationRepository) {
        return new RepositoryItemReaderBuilder<AptNotification>()
                .name("aptNotificationRepositoryItemReader")
                .repository(aptNotificationRepository)
                .methodName("findByEnabledIsTrue")
                .pageSize(10)
                .arguments(List.of())
                .sorts(Collections.singletonMap("aptNotificationId", Sort.Direction.DESC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<AptNotification, NotificationDto> aptNotificationItemProcessor(
            @Value("#{jobParameters['dealDate']}") String dealDate,
            AptDealService aptDealService,
            LawdRepository lawdRepository
    ) {
        return aptNotification -> {

            List<AptDto> aptDtoList =
                    aptDealService.findByGuLawdAndDealDate(aptNotification.getGuLawdCd(), LocalDate.parse(dealDate));

            if(aptDtoList.isEmpty()) { return null; }

            String guName =
                    lawdRepository.findByLawdCd(aptNotification.getGuLawdCd() + "00000").orElseThrow().getLawdDong();


            return NotificationDto.builder()
                    .email(aptNotification.getEmail())
                    .guName(guName)
                    .count(aptDtoList.size())
                    .aptDeals(aptDtoList)
                    .build();
        };
    }

    @StepScope
    @Bean
    public ItemWriter<NotificationDto> aptNotificationItemWriter(FakeSendService fakeSendService) {
        return chunk -> {
            chunk.forEach(item -> fakeSendService.send(item.getEmail(), item.toMessage()));
        };
    }

}
