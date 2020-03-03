package com.you07.vtp.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import io.swagger.annotations.ApiModel;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author cs
 * @Date 2020/3/2 12:30
 * @Version 2.2.2.0
 **/
@Table(name = "ss_group")
public class SsGroup {

    private Integer groupId;

    private Integer dockingId;

    private String groupName;

    private String groupEnName;

    private String icon;

    private String color;

    private Timestamp updateTime;

    private Integer orderId;

    private String memo;

    private String idString;

    private String personCount;

    private String[] specialPersonId;

    @Id
    @Column(name = "group_id")
    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @Column(name = "id_string")
    public String getIdString() {
        return idString;
    }

    public void setIdString(String idString) {
        this.idString = idString;
    }

    @Column(name = "special_person_id")
    public String[] getSpecialPersonId() {
        return specialPersonId;
    }

    public void setSpecialPersonId(String[] specialPersonId) {
        this.specialPersonId = specialPersonId;
    }

    @Column(name = "docking_id")
    public Integer getDockingId() {
        return dockingId;
    }

    public void setDockingId(Integer dockingId) {
        this.dockingId = dockingId;
    }

    @Column(name = "group_name")
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Column(name = "color")
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Column(name = "update_time")
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = new Timestamp(new Date().getTime());
    }

    @Column(name = "order_id")
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Column(name = "memo")
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Column(name = "group_en_name")
    public String getGroupEnName() {
        return groupEnName;
    }

    public void setGroupEnName(String groupEnName) {
        this.groupEnName = groupEnName;
    }

    @Column(name = "icon")
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Transient
    public String getPersonCount() {
        if(idString != null){
            String[] array = idString.split(",");
            this.personCount = array.length + "人";
        }else {
            this.personCount = "0人";
        }
        return personCount;
    }

    public void setPersonCount(String personCount) {
        this.personCount = personCount;

    }

    public String getFormatUpdateTime(){
        if(updateTime != null){
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(updateTime);
        } else{
            return "";
        }
    }
}
