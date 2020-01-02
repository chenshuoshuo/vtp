package com.you07.vtp.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author egan
 * @date 2020/1/2 10:18
 * @desc
 */
public class UserLocationForm {

    private String userIds;

    private String orgCodes;

    private String classCodes;

    private String startTime;

    private String endTime;
    @NotNull
    private Integer inSchool;
    @NotNull
    private Integer campusId;
    @NotBlank
    private String managerId;

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getInSchool() {
        return inSchool;
    }

    public void setInSchool(Integer inSchool) {
        this.inSchool = inSchool;
    }

    public Integer getCampusId() {
        return campusId;
    }

    public void setCampusId(Integer campusId) {
        this.campusId = campusId;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getOrgCodes() {
        return orgCodes;
    }

    public void setOrgCodes(String orgCodes) {
        this.orgCodes = orgCodes;
    }

    public String getClassCodes() {
        return classCodes;
    }

    public void setClassCodes(String classCodes) {
        this.classCodes = classCodes;
    }
}
