package org.example.springbatchproject.job.lawd;

import org.example.springbatchproject.BatchTestConfig;
import org.example.springbatchproject.service.LawdService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SpringBootTest
@SpringBatchTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {LawdInsertJobConfig.class, BatchTestConfig.class})
public class LawdInsertJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
//
    @MockBean
    private LawdService lawdService;

    @Test
    public void success() throws Exception {
        //when
        JobParameters parameters = new JobParametersBuilder()
                .addString("filePath", "LAND_CODE.txt")
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(parameters);

        assertEquals(jobExecution.getExitStatus(), ExitStatus.COMPLETED);
        verify(lawdService, times(5)).upsert(any());

    }


    @Test
    public void fail_whenFileNotFound(){

    }

}
