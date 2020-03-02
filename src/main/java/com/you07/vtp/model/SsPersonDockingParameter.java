package com.you07.vtp.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author cs
 * @Date 2020/3/2 12:30
 * @Version 2.2.2.0
 **/
@Table(name = "ss_person_docking_parameter")
public class SsPersonDockingParameter {
    @Id
    @Column(name = "parameter_code")
    private Integer parameterCode;
    private Integer dockingId;
    private String parameterName;
    private String parameterEnName;

    public Integer getParameterCode() {
        return parameterCode;
    }

    public void setParameterCode(Integer parameterCode) {
        this.parameterCode = parameterCode;
    }

    public Integer getDockingId() {
        return dockingId;
    }

    public void setDockingId(Integer dockingId) {
        this.dockingId = dockingId;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterEnName() {
        return parameterEnName;
    }

    public void setParameterEnName(String parameterEnName) {
        this.parameterEnName = parameterEnName;
    }

}
