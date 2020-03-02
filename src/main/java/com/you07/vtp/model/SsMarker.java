package com.you07.vtp.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author cs
 * @Date 2020/3/2 12:30
 * @Version 2.2.2.0
 **/
@Table(name = "ss_marker")
public class SsMarker {
    @Id
    @Column(name = "marker_id")
    private Integer markerId;
    private String markerName;
    private Integer moduleId;
    private Object geom;
    private String icon;
    private String polygonName;
    private Timestamp updateTime;
    private Integer orderId;
    private String memo;
    private Integer campusCode;


    public Integer getMarkerId() {
        return markerId;
    }

    public void setMarkerId(Integer markerId) {
        this.markerId = markerId;
    }

    public String getMarkerName() {
        return markerName;
    }

    public void setMarkerName(String markerName) {
        this.markerName = markerName;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public Object getGeom() {
        return geom;
    }

    public void setGeom(Object geom) {
        this.geom = geom;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPolygonName() {
        return polygonName;
    }

    public void setPolygonName(String polygonName) {
        this.polygonName = polygonName;
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

    public Integer getCampusCode() {
        return campusCode;
    }

    public void setCampusCode(Integer campusCode) {
        this.campusCode = campusCode;
    }
}
