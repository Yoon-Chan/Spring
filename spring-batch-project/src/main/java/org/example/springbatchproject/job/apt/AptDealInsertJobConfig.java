package org.example.springbatchproject.job.apt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springbatchproject.adapter.ApartmentApiResource;
import org.example.springbatchproject.core.dto.AptDealDto;
import org.example.springbatchproject.job.validator.FilePathParameterValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.YearMonth;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AptDealInsertJobConfig {

    private final ApartmentApiResource apartmentApiResource;

    @Bean
    public Job aptDealInsertJob(
            JobRepository jobRepository,
            @Qualifier("aptDealInsertStep") Step aptDealInsertStep) {
        return new JobBuilder("aptDealInsertJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(aptDealInsertStep)
                .build();
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

    @Bean
    @StepScope
    public StaxEventItemReader<AptDealDto> aptDealResourceReader(
            Jaxb2Marshaller jaxb2Marshaller) {
        return new StaxEventItemReaderBuilder<AptDealDto>()
                .name("aptDealResourceReader")
                .resource(apartmentApiResource.getResource("41135", YearMonth.of(2021, 7)))
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
