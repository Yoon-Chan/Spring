package org.example.springbatchproject.job.apt;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.springbatchproject.core.repository.LawdRepository;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.List;

/*
 *   ExecutionContext에 저장할 데이터
 *   1. guLawdCdList - 구 코드 리스트
 *   2. guLawdCd - 구 코드 -> 다음 스텝에서 활용할 값.
 *   3. itemCount - 남아있는 구 코드의 개수
 *
 * */
@RequiredArgsConstructor
public class GuLawdTasklet implements Tasklet {

    private final LawdRepository lawdRepository;
    private List<String> guLawdCds;
    private int itemCount;


    private static final String KEY_GU_LAWD_CD = "guLawdCd";
    private static final String ITEM_COUNT = "itemCount";
    private static final String GU_LAWD_CD_LIST = "guLawdCdList";

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        //step 간 데이터 전달
        ExecutionContext executionContext = getExecutionContext(chunkContext);

        //List<String> guLawdCds = lawdRepository.findDistinctGuLawdCd();

        //데이터 하나 전달
//            System.out.println("[guLawdCdTasklet] guLawdCd : " + guLawdCds.get(0));
//            executionContext.putString("guLawdCd", guLawdCds.get(0));

        //데이터가 있으면 다음 스텝을 실행하도록 하고, 데이터가 없으면 종료되도록 한다.
        //데이터가 있으면 -> CONTINUABLE
        // 1. guLawdCdList
        // 2. guLawdCd
        // 3. itemCount

        //List<String> guLawdCds;

        initList(executionContext);
        initItemCOunt(executionContext);

        if (itemCount == 0) {
            contribution.setExitStatus(ExitStatus.COMPLETED);
            return RepeatStatus.FINISHED;
        }

        itemCount--;
        executionContext.putString(KEY_GU_LAWD_CD, guLawdCds.get(itemCount));
        executionContext.putInt(ITEM_COUNT, itemCount);

        contribution.setExitStatus(new ExitStatus("CONTINUABLE"));
        return RepeatStatus.FINISHED;
    }

    private void initList(ExecutionContext executionContext) {
        if (executionContext.containsKey(GU_LAWD_CD_LIST)) {
            guLawdCds = (List<String>) executionContext.get(GU_LAWD_CD_LIST);
        } else {
            guLawdCds = lawdRepository.findDistinctGuLawdCd();
            executionContext.put(GU_LAWD_CD_LIST, guLawdCds);
            executionContext.putInt(ITEM_COUNT, guLawdCds.size());
        }
    }

    private void initItemCOunt(ExecutionContext executionContext) {
        if (executionContext.containsKey(ITEM_COUNT)) {
            itemCount = executionContext.getInt(ITEM_COUNT);
        } else {
            itemCount = guLawdCds.size();
        }
    }

    private ExecutionContext getExecutionContext(ChunkContext chunkContext) {
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        return stepExecution.getJobExecution().getExecutionContext();
    }
}

