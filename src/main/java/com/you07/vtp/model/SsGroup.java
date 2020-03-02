package com.you07.vtp.model;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author cs
 * @Date 2020/3/2 12:30
 * @Version 2.2.2.0
 **/
@Table(name = "ss_group")
public class SsGroup {
    @Id
    @Column(name = "group_id")
    private Integer groupId;
    private String groupName;
    private String color;
    private Timestamp updateTime;
    private Integer orderId;
    private String memo;
    private String personCount;
    private String[] specialPersonId;
    private List<String> specialPersonIdList;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPersonCount() {
        return personCount;
    }

    public void setPersonCount(String personCount) {
        this.personCount = personCount;
        if(specialPersonIdList.size() > 0){
            this.personCount = specialPersonIdList.size() + "人";
        }
    }

    public String[] getSpecialPersonId() {
        return specialPersonId;
    }

    public void setSpecialPersonId(String[] specialPersonId) {
        this.specialPersonId = specialPersonId;
    }

    public List<String> getSpecialPersonIdList() {
        return specialPersonIdList;
    }

    public void setSpecialPersonIdList(List<String> specialPersonIdList) {
        this.specialPersonIdList = specialPersonIdList;
        if(specialPersonId != null){
            this.specialPersonIdList = Arrays.asList(specialPersonId);
        }
    }
}
