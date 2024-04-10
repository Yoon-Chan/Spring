package org.example.springbatch.job;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

//@Configuration
//@RequiredArgsConstructor
//public class HelloJobConfig {
//
//    @Bean
//    public Job helloJob(JobRepository jobRepository, Step helloStep) {
//        return new JobBuilder("helloJob", jobRepository)
//                .incrementer(new RunIdIncrementer())
//                .start(helloStep)
//                .build();
//    }
//
//    @JobScope
//    @Bean
//    public Step helloStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, Tasklet tasklet) {
//        return new StepBuilder("helloStep", jobRepository)
//                .tasklet(tasklet, transactionManager)
//                .build();
//    }
//
//    @StepScope
//    @Bean
//    public Tasklet myTasklet() {
//        return (contribution, chunkContext) -> {
//            System.out.println("Hello Spring Batch");
//            return RepeatStatus.FINISHED;
//        };
//    }
//}
