package com.you07.vtp.model.vo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author cs
 * @Date 2020/3/2 17:28
 * @Version 2.2.2.0
 **/
public class LocationQueryVO {

    private String geojson;
    private Integer campusCode;
    private String startTime;
    private String endTime;
    private Integer page = 0;
    private Integer pageSize = 5;

    public String getGeojson() {
        return geojson;
    }

    public void setGeojson(String geojson) {
        try {
            this.geojson = URLDecoder.decode(geojson, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public Integer getCampusCode() {
        return campusCode;
    }

    public void setCampusCode(Integer campusCode) {
        this.campusCode = campusCode;
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

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
