package org.example.springbatchproject.job.apt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springbatchproject.adapter.ApartmentApiResource;
import org.example.springbatchproject.core.dto.AptDealDto;
import org.example.springbatchproject.core.repository.LawdRepository;
import org.example.springbatchproject.job.validator.LawdCdParameterValidator;
import org.example.springbatchproject.job.validator.YearMonthParameterValidator;
import org.example.springbatchproject.service.AptDealService;
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



    @Bean
    public Job aptDealInsertJob(
            JobRepository jobRepository,
            @Qualifier("aptDealInsertStep") Step aptDealInsertStep,
            @Qualifier("guLawdCdStep") Step guLawdCdStep
            //@Qualifier("contextPrintStep") Step contextPrintStep
    ) {
        return new JobBuilder("aptDealInsertJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .validator(new YearMonthParameterValidator())
                .start(guLawdCdStep)
                .on("CONTINUABLE").to(aptDealInsertStep).next(guLawdCdStep)
                .from(guLawdCdStep).on("*").end()
                .end()
                .build();
    }

//    private JobParametersValidator apartDealJobParametersValidator() {
//        CompositeJobParametersValidator validator = new CompositeJobParametersValidator();
//        validator.setValidators(List.of(
//                new YearMonthParameterValidator()
//        ));
//        return validator;
//    }

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

    /*
    *   ExecutionContext에 저장할 데이터
    *   1. guLawdCdList - 구 코드 리스트
    *   2. guLawdCd - 구 코드 -> 다음 스텝에서 활용할 값.
    *   3. itemCount - 남아있는 구 코드의 개수
    *
    * */
    @StepScope
    @Bean
    public Tasklet guLawdCdTasklet(LawdRepository lawdRepository) {
        return new GuLawdTasklet(lawdRepository);
    }

//    @JobScope
//    @Bean
//    @Qualifier("contextPrintStep")
//    public Step contextPrintStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,@Qualifier("contextPrintTasklet") Tasklet tasklet) {
//        return new StepBuilder("contextPrintStep", jobRepository)
//                .tasklet(tasklet, transactionManager)
//                .build();
//    }
//
//    @StepScope
//    @Bean
//    @Qualifier("contextPrintTasklet")
//    public Tasklet contextPrintTasklet(
//            @Value("#{jobExecutionContext['guLawdCd']}") String guLawdCd) {
//        return (contribution, chunkContext) -> {
//            System.out.println("[contextPrintTasklet] guLawdCd : " + guLawdCd);
//            String str = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().getString("guLawdCd");
//
//            System.out.println("[contextPrintTasklet] guLawdCd : " + guLawdCd);
//            System.out.println("[contextPrintTasklet] str : " + str);
//            return RepeatStatus.FINISHED;
//        };
//    }

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
    public ItemWriter<AptDealDto> aptDealWriter(AptDealService aptDealService) {
        return chunk -> {
            chunk.forEach(aptDealService::upsert);
            System.out.println("=============== Write Completed ================");
        };
    }
}
