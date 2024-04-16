package org.example.springbatchproject.job.notify;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springbatchproject.core.dto.NotificationDto;
import org.example.springbatchproject.core.entity.AptNotification;
import org.example.springbatchproject.core.repository.AptNotificationRepository;
import org.example.springbatchproject.job.validator.DealDateParameterValidator;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.Collections;

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
                .arguments(Arrays.asList())
                .sorts(Collections.singletonMap("aptNotificationId", Sort.Direction.DESC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<AptNotification, NotificationDto> aptNotificationItemProcessor() {
        return item -> {

            return null;
        };
    }

    @StepScope
    @Bean
    public ItemWriter<NotificationDto> aptNotificationItemWriter() {
        return chunk -> {


        };
    }

}
