package com.you07.map.model;

import javax.persistence.Table;

/**
 * 灵奇地图map_zone实体映射对象
 * @version 1.0
 * @author RY
 * @since 2018-7-23 15:03:27
 */

@Table(name = "map_zone")
public class MapZone {
    /**
     * 区域ID
     */
    private Integer zoneid;
    /**
     * 区域名称
     */
    private String name;
    /**
     * 左下经度
     */
    private Double leftBottomLon;
    /**
     * 左下纬度
     */
    private Double leftBottomLat;
    /**
     * 右上经度
     */
    private Double rightTopLon;
    /**
     * 右上纬度
     */
    private Double rightTopLat;
    /**
     * 最小放大级别
     */
    private Integer minZoom;
    /**
     * 最大放大界别
     */
    private Integer maxZoom;
    /**
     * 默认放大级别
     */
    private Integer defaultZoom;
    /**
     * 最大解决方案
     */
    private Double maxResolution;
    /**
     * 图层组名称
     */
    private String layerName;
    /**
     * 导航版本
     */
    private String navigationVersion;
    /**
     * 中心点经度
     */
    private Double centerLon;
    /**
     * 中心点纬度
     */
    private Double centerLat;
    /**
     * 备注
     */
    private String memo;
    /**
     * 地图类型：2d,3d
     */
    private String maptype;
    /**
     * 基础图层名称
     */
    private String baseLayerName;
    /**
     * 灵奇地图校区ID
     */
    private Integer campusid;
    /**
     * 是否有室内导航
     */
    private Integer openIndoorNavigation;

    public Integer getZoneid() {
        return zoneid;
    }

    public void setZoneid(Integer zoneid) {
        this.zoneid = zoneid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLeftBottomLon() {
        return leftBottomLon;
    }

    public void setLeftBottomLon(Double leftBottomLon) {
        this.leftBottomLon = leftBottomLon;
    }

    public Double getLeftBottomLat() {
        return leftBottomLat;
    }

    public void setLeftBottomLat(Double leftBottomLat) {
        this.leftBottomLat = leftBottomLat;
    }

    public Double getRightTopLon() {
        return rightTopLon;
    }

    public void setRightTopLon(Double rightTopLon) {
        this.rightTopLon = rightTopLon;
    }

    public Double getRightTopLat() {
        return rightTopLat;
    }

    public void setRightTopLat(Double rightTopLat) {
        this.rightTopLat = rightTopLat;
    }

    public Integer getMinZoom() {
        return minZoom;
    }

    public void setMinZoom(Integer minZoom) {
        this.minZoom = minZoom;
    }

    public Integer getMaxZoom() {
        return maxZoom;
    }

    public void setMaxZoom(Integer maxZoom) {
        this.maxZoom = maxZoom;
    }

    public Integer getDefaultZoom() {
        return defaultZoom;
    }

    public void setDefaultZoom(Integer defaultZoom) {
        this.defaultZoom = defaultZoom;
    }

    public Double getMaxResolution() {
        return maxResolution;
    }

    public void setMaxResolution(Double maxResolution) {
        this.maxResolution = maxResolution;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public String getNavigationVersion() {
        return navigationVersion;
    }

    public void setNavigationVersion(String navigationVersion) {
        this.navigationVersion = navigationVersion;
    }

    public Double getCenterLon() {
        return centerLon;
    }

    public void setCenterLon(Double centerLon) {
        this.centerLon = centerLon;
    }

    public Double getCenterLat() {
        return centerLat;
    }

    public void setCenterLat(Double centerLat) {
        this.centerLat = centerLat;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMaptype() {
        return maptype;
    }

    public void setMaptype(String maptype) {
        this.maptype = maptype;
    }

    public String getBaseLayerName() {
        return baseLayerName;
    }

    public void setBaseLayerName(String baseLayerName) {
        this.baseLayerName = baseLayerName;
    }

    public Integer getCampusid() {
        return campusid;
    }

    public void setCampusid(Integer campusid) {
        this.campusid = campusid;
    }

    public Integer getOpenIndoorNavigation() {
        return openIndoorNavigation;
    }

    public void setOpenIndoorNavigation(Integer openIndoorNavigation) {
        this.openIndoorNavigation = openIndoorNavigation;
    }
}
