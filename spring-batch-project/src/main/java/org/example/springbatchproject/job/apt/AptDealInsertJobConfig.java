package org.example.springbatchproject.job.apt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springbatchproject.adapter.ApartmentApiResource;
import org.example.springbatchproject.core.dto.AptDealDto;
import org.example.springbatchproject.core.repository.LawdRepository;
import org.example.springbatchproject.job.validator.LawdCdParameterValidator;
import org.example.springbatchproject.job.validator.YearMonthParameterValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;

import javax.swing.plaf.nimbus.State;
import java.time.YearMonth;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AptDealInsertJobConfig {

    private final ApartmentApiResource apartmentApiResource;
    private final LawdRepository lawdRepository;


    @Bean
    public Job aptDealInsertJob(
            JobRepository jobRepository,
            @Qualifier("aptDealInsertStep") Step aptDealInsertStep,
            @Qualifier("guLawdCdStep") Step guLawdCdStep
            ) {
        return new JobBuilder("aptDealInsertJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .validator(apartDealJobParametersValidator())
                .start(guLawdCdStep)
                .build();
    }

    private JobParametersValidator apartDealJobParametersValidator() {
        CompositeJobParametersValidator validator = new CompositeJobParametersValidator();
        validator.setValidators(List.of(
                new LawdCdParameterValidator(),
                new YearMonthParameterValidator()
        ));
        return validator;
    }

    @Bean
    @JobScope
    @Qualifier("aptDealInsertStep")
    public Step aptDealInsertStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            StaxEventItemReader<AptDealDto> aptDealResourceReader,
            ItemWriter<AptDealDto> aptDealWriter) {
        return new StepBuilder("aptDealInsertStep", jobRepository)
                .<AptDealDto, AptDealDto>chunk(10, transactionManager)
                .reader(aptDealResourceReader)
                .writer(aptDealWriter)
                .build();
    }

    @JobScope
    @Bean
    @Qualifier("guLawdCdStep")
    public Step guLawdCdStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, Tasklet guLawdCdTasklet) {
        return new StepBuilder("guLawdCdStep", jobRepository)
                .tasklet(guLawdCdTasklet, transactionManager)
                .build();
    }

    @StepScope
    @Bean
    public Tasklet guLawdCdTasklet(JobRepository jobRepository) {
        return (contribution, chunkContext) -> {
            lawdRepository.findDistinctGuLawdCd()
                    .forEach(System.out::println);
          return RepeatStatus.FINISHED;
        };
    }

    @Bean
    @StepScope
    public StaxEventItemReader<AptDealDto> aptDealResourceReader(
            @Value("#{jobParameters['yearMonth']}") String yearMonthStr,
            @Value("#{jobParameters['lawdCd']}") String lawdCd,
            Jaxb2Marshaller jaxb2Marshaller) {
        return new StaxEventItemReaderBuilder<AptDealDto>()
                .name("aptDealResourceReader")
                .resource(apartmentApiResource.getResource(lawdCd, YearMonth.parse(yearMonthStr)))
                //내가 읽어낼 루트 element 설정하기
                .addFragmentRootElements("item")
                //파일을 객체에 매핑할 때 사용.
                .unmarshaller(jaxb2Marshaller)
                .build();
    }

    @Bean
    @StepScope
    public Jaxb2Marshaller aptDealDtoMarshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        //만들어준 Dto 클래스 넘겨주기
        jaxb2Marshaller.setClassesToBeBound(AptDealDto.class);
        return jaxb2Marshaller;
    }

    @Bean
    @StepScope
    public ItemWriter<AptDealDto> aptDealWriter() {
        return chunk -> {
            chunk.forEach(System.out::println);
        };
    }
}
