package org.example.springbatchproject.job.lawd;

import org.example.springbatchproject.core.entity.Lawd;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class LawdFieldSetMapper implements FieldSetMapper<Lawd> {

    protected static final String LAWD_CD = "lawdCd";
    protected static final String LAWD_DONG = "lawdDong";
    protected static final String EXIST = "exist";
    protected static final String EXIST_TRUE = "존재";

    @Override
    public Lawd mapFieldSet(FieldSet fieldSet) throws BindException {
        Lawd lawd = new Lawd();
        lawd.setLawdCd(fieldSet.readString(LAWD_CD));
        lawd.setLawdDong(fieldSet.readString(LAWD_DONG));
        lawd.setExist(fieldSet.readBoolean(EXIST, EXIST_TRUE));
        return lawd;
    }
}
