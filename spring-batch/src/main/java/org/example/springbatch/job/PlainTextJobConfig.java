package org.example.springbatch.job;


import lombok.RequiredArgsConstructor;
import org.example.springbatch.core.domain.PlainText;
import org.example.springbatch.core.repository.PlainTextRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class PlainTextJobConfig {

    private final PlainTextRepository plainTextRepository;

    @Bean
    public Job helloJob(JobRepository jobRepository, Step helloStep) {
        return new JobBuilder("plainTextJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(helloStep)
                .build();
    }

    @JobScope
    @Bean
    public Step helloStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("plainStep", jobRepository)
                .<PlainText, String>chunk(5, transactionManager)
                .reader(plainTextReader())
                .processor(plainTextProcessor())
                .writer(plainTextWriter())
                .build();
    }

    @StepScope
    @Bean
    public RepositoryItemReader<PlainText> plainTextReader() {
        return new RepositoryItemReaderBuilder<PlainText>()
                .name("plainTextReader")
                .repository(plainTextRepository)
                .methodName("findBy")
                .pageSize(5)
                .arguments(List.of())
                .sorts(Collections.singletonMap("id", Sort.Direction.DESC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<PlainText, String> plainTextProcessor() {
        return (plainText) -> "processed " + plainText.getText();
    }

    @StepScope
    @Bean
    public ItemWriter<String> plainTextWriter() {
        return items -> {
            items.forEach(System.out::println);
            System.out.println("=== chunck is finished ===");
        };
    }
}
