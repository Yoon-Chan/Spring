package org.example.springbatchproject.job.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

public class LawdCdParameterValidator implements JobParametersValidator {

    private static final String LAWD_CD = "lawdCd";

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        String lawdCd = parameters.getString(LAWD_CD);
        if(isNotValid(lawdCd)) {
            throw new JobParametersInvalidException(LAWD_CD + "이 빈 문자열이거나 존재하지 않습니다.");
        }
    }

    private boolean isValid(String lawdCd) {
        return StringUtils.hasText(lawdCd) && lawdCd.matches("^[0-9]{5}$");
    }

    private boolean isNotValid(String lawdCd) {
        return !isValid(lawdCd);
    }
}
