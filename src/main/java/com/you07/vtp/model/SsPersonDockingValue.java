package com.you07.vtp.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * @author cs
 * @Date 2020/3/2 12:30
 * @Version 2.2.2.0
 **/
@Table(name = "ss_person_docking_value")
public class SsPersonDockingValue {
    private Integer parameterCode;
    private Integer groupId;
    private String parameterValue;

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    public Integer getParameterCode() {
        return parameterCode;
    }

    public void setParameterCode(Integer parameterCode) {
        this.parameterCode = parameterCode;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
