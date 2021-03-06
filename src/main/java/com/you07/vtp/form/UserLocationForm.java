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

    private String keyWord;

    private String nationCodes;

    private String originCodes;

    private String provicesCodes;

    private String cityCodes;

    private String nation;

    private String birthplace;

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

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getNationCodes() {
        return nationCodes;
    }

    public void setNationCodes(String nationCodes) {
        this.nationCodes = nationCodes;
    }

    public String getOriginCodes() {
        return originCodes;
    }

    public void setOriginCodes(String originCodes) {
        this.originCodes = originCodes;
    }

    public String getProvicesCodes() {
        return provicesCodes;
    }

    public void setProvicesCodes(String provicesCodes) {
        this.provicesCodes = provicesCodes;
    }

    public String getCityCodes() {
        return cityCodes;
    }

    public void setCityCodes(String cityCodes) {
        this.cityCodes = cityCodes;
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

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }
}
