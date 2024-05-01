package org.example.springbatch.job;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springbatch.job.validator.LocalDateParameterValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
@AllArgsConstructor
@Slf4j
public class AdvancedJobConfig {


    @Bean
    public Job advancedJob(JobRepository jobRepository, Step step) {
        return new JobBuilder("advancedJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .validator(new LocalDateParameterValidator("targetDate"))
                .start(step)
                .build();
    }

    @JobScope
    @Bean
    public Step advancedStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, Tasklet tasklet) {
        return new StepBuilder("advancedStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet advancedTasklet(@Value("#{jobParameters['targetDate']}") String targetDate) {
        return ((contribution, chunkContext) -> {
            log.info("[advancedJobConfig] JobParameter - targetDate = {}", targetDate);
            log.info("[advancedJobConfig] excuted advancedTasklet");
            return RepeatStatus.FINISHED;
        });
    }


}
