package org.example.springbatchproject.job.apt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springbatchproject.adapter.ApartmentApiResource;
import org.example.springbatchproject.core.dto.AptDealDto;
import org.example.springbatchproject.core.repository.LawdRepository;
import org.example.springbatchproject.job.validator.LawdCdParameterValidator;
import org.example.springbatchproject.job.validator.YearMonthParameterValidator;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
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
            @Qualifier("guLawdCdStep") Step guLawdCdStep,
            @Qualifier("contextPrintStep") Step contextPrintStep
    ) {
        return new JobBuilder("aptDealInsertJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .validator(apartDealJobParametersValidator())
                .start(guLawdCdStep)
                .next(contextPrintStep)
                .next(aptDealInsertStep)
                .build();
    }

    private JobParametersValidator apartDealJobParametersValidator() {
        CompositeJobParametersValidator validator = new CompositeJobParametersValidator();
        validator.setValidators(List.of(
                //new LawdCdParameterValidator(),
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

            //step 간 데이터 전달
            StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
            ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();

            List<String> guLawdCds = lawdRepository.findDistinctGuLawdCd();

            //데이터 하나 전달
            System.out.println("[guLawdCdTasklet] guLawdCd : " + guLawdCds.get(0));
            executionContext.putString("guLawdCd", guLawdCds.get(0));


            return RepeatStatus.FINISHED;
        };
    }

    @JobScope
    @Bean
    @Qualifier("contextPrintStep")
    public Step contextPrintStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,@Qualifier("contextPrintTasklet") Tasklet tasklet) {
        return new StepBuilder("contextPrintStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }

    @StepScope
    @Bean
    @Qualifier("contextPrintTasklet")
    public Tasklet contextPrintTasklet(
            @Value("#{jobExecutionContext['guLawdCd']}") String guLawdCd) {
        return (contribution, chunkContext) -> {
            System.out.println("[contextPrintTasklet] guLawdCd : " + guLawdCd);
            Object str = chunkContext.getStepContext().getJobExecutionContext().get("guLawdCd");

            System.out.println("[contextPrintTasklet] guLawdCd : " + guLawdCd);
            System.out.println("[contextPrintTasklet] str : " + str);
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    @StepScope
    public StaxEventItemReader<AptDealDto> aptDealResourceReader(
            @Value("#{jobParameters['yearMonth']}") String yearMonthStr,
            @Value("#{jobExecutionContext['guLawdCd']}") String lawdCd,
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
