package com.you07.vtp.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Geometry;
import com.you07.util.fastjson.JacksonGeometryDeserializer;
import com.you07.util.fastjson.JacksonGeometrySerializer;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author cs
 * @Date 2020/3/2 12:30
 * @Version 2.2.2.0
 **/
@Table(name = "ss_marker")
public class SsMarker {

    private Integer markerId;
    private String markerName;
    private Integer moduleId;
    @JsonSerialize(using = JacksonGeometrySerializer.class)
    @JsonDeserialize(using = JacksonGeometryDeserializer.class)
    private Geometry geom;
    private String icon;
    private String polygonName;
    private Timestamp updateTime;
    private Integer orderId;
    private String memo;
    private Integer campusCode;

    @Id
    @Column(name = "marker_id")
    public Integer getMarkerId() {
        return markerId;
    }

    public void setMarkerId(Integer markerId) {
        this.markerId = markerId;
    }

    @Column(name = "marker_name")
    public String getMarkerName() {
        return markerName;
    }

    public void setMarkerName(String markerName) {
        this.markerName = markerName;
    }

    @Column(name = "module_id")
    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    @Column(name = "geom")
    public Geometry getGeom() {
        return geom;
    }

    public void setGeom(Geometry geom) {
        this.geom = geom;
    }

    @Column(name = "icon")
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Column(name = "polygon_name")
    public String getPolygonName() {
        return polygonName;
    }

    public void setPolygonName(String polygonName) {
        this.polygonName = polygonName;
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

    @Column(name = "campus_code")
    public Integer getCampusCode() {
        return campusCode;
    }

    public void setCampusCode(Integer campusCode) {
        this.campusCode = campusCode;
    }

    public String getFormatUpdateTime(){
        if(updateTime != null){
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(updateTime);
        } else{
            return "";
        }
    }

    public String getModuleName(){
        if(moduleId != null){
            return "疫情监控";
        }else {
            return "";
        }
    }
}
