package org.example.springbatchproject.job.lawd;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springbatchproject.core.entity.Lawd;
import org.example.springbatchproject.job.validator.FilePathParameterValidator;
import org.example.springbatchproject.service.LawdService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import static org.example.springbatchproject.job.lawd.LawdFieldSetMapper.*;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class LawdInsertJobConfig {

    private final LawdService lawdService;

    @Bean
    public Job lawdInsertJob(
            JobRepository jobRepository,
            Step lawdInsertStep) {
        return new JobBuilder("lawdInsertJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .validator(new FilePathParameterValidator())
                .start(lawdInsertStep)
                .build();
    }

    @Bean
    @JobScope
    public Step lawdInsertStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            FlatFileItemReader<Lawd> lawdFlatFileItemReader,
            ItemWriter<Lawd> lawditemWriter) {
        return new StepBuilder("lawdInsertStep", jobRepository)
                .<Lawd, Lawd>chunk(1000, transactionManager)
                .reader(lawdFlatFileItemReader)
                .writer(lawditemWriter)
                .build();
    }


    @Bean
    @StepScope
    public FlatFileItemReader<Lawd> lawdFileItemReader(@Value("#{jobParameters['filePath']}") String filePath) {
        return new FlatFileItemReaderBuilder<Lawd>()
                .name("lawdFileItemReader")
                .delimited()
                .delimiter("\t")
                .names(LAWD_CD, LAWD_DONG, EXIST)
                .linesToSkip(1)
                .fieldSetMapper(new LawdFieldSetMapper())
                .resource(new ClassPathResource(filePath))
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<Lawd> lawditemWriter() {
        return (item) -> item.forEach(lawdService::upsert);
    }
}
