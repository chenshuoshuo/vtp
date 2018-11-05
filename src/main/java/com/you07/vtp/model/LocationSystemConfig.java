package com.you07.vtp.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 系统配置实体
 * @version 1.0
 * @author RY
 * @2018-8-22 14:48:56
 */
@Table(name = "location_system_config")
@ApiModel(value = "locationSystemConfig", description = "系统设置实体对象")
public class LocationSystemConfig {
    @Id
    @Column(name = "config_id")
    @ApiModelProperty(value = "信息ID，添加时不需要", name = "configId", dataType = "Integer", example = "1", required = false)
    private Integer configId;
    @ApiModelProperty(value = "系统名称", name = "systemName", dataType = "String", example = "可视化轨迹平台", required = false)
    private String systemName;
    @ApiModelProperty(value = "系统LOGO", name = "systemLogo", dataType = "String", example = "http://xxx.com/xx.png", required = false)
    private String systemLogo;
    @ApiModelProperty(value = "GIS2.0接口地址", name = "lqMapGisUrl", dataType = "String", example = "http://xxx.com/lqmap", required = false)
    private String lqMapGisUrl;
    @ApiModelProperty(value = "高德地图API-KEY", name = "amapKey", dataType = "String", example = "WERW.SWEFR-WER", required = false)
    private String amapKey;
    @ApiModelProperty(value = "系统top背景色", name = "topBgColor", dataType = "String", example = "AAAAAA", required = false)
    private String topBgColor;
    @ApiModelProperty(value = "右侧背景色上", name = "rightTopBgColor", dataType = "String", example = "AAAAAA", required = false)
    private String rightTopBgColor;
    @ApiModelProperty(value = "右侧背景色下", name = "rightBottomBgColor", dataType = "String", example = "AAAAAA", required = false)
    private String rightBottomBgColor;
    @ApiModelProperty(value = "学校LOGO", name = "logo", dataType = "String", example = "http://xxx.com/xx.png", required = false)
    private String logo;
    @ApiModelProperty(value = "水印LOGO", name = "waterMarkLogo", dataType = "String", example = "http://xxx.com/xx.png", required = false)
    private String waterMarkLogo;
    @ApiModelProperty(value = "校门风景图", name = "gateImg", dataType = "String", example = "http://xxx.com/xx.png", required = false)
    private String gateImg;
    @ApiModelProperty(value = "轨迹查询图标", name = "trackIcon", dataType = "String", example = "http://xxx.com/xx.png", required = false)
    private String trackIcon;
    @ApiModelProperty(value = "位置查询图标", name = "locationIcon", dataType = "String", example = "http://xxx.com/xx.png", required = false)
    private String locationIcon;
    @ApiModelProperty(value = "安全管理图标", name = "safetyIcon", dataType = "String", example = "http://xxx.com/xx.png", required = false)
    private String safetyIcon;
    @ApiModelProperty(value = "校区切换图标", name = "campusSwitchIcon", dataType = "String", example = "http://xxx.com/xx.png", required = false)
    private String campusSwitchIcon;
    @ApiModelProperty(value = "权限分配联系号码", name = "contact", dataType = "String", example = "139XXXXXXX", required = false)
    private String contact;
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    public Integer getConfigId() {
        return configId;
    }

    public void setConfigId(Integer configId) {
        this.configId = configId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemLogo() {
        return systemLogo;
    }

    public void setSystemLogo(String systemLogo) {
        this.systemLogo = systemLogo;
    }

    public String getLqMapGisUrl() {
        return lqMapGisUrl;
    }

    public void setLqMapGisUrl(String lqMapGisUrl) {
        this.lqMapGisUrl = lqMapGisUrl;
    }

    public String getAmapKey() {
        return amapKey;
    }

    public void setAmapKey(String amapKey) {
        this.amapKey = amapKey;
    }

    public String getTopBgColor() {
        return topBgColor;
    }

    public void setTopBgColor(String topBgColor) {
        this.topBgColor = topBgColor;
    }

    public String getRightTopBgColor() {
        return rightTopBgColor;
    }

    public void setRightTopBgColor(String rightTopBgColor) {
        this.rightTopBgColor = rightTopBgColor;
    }

    public String getRightBottomBgColor() {
        return rightBottomBgColor;
    }

    public void setRightBottomBgColor(String rightBottomBgColor) {
        this.rightBottomBgColor = rightBottomBgColor;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getWaterMarkLogo() {
        return waterMarkLogo;
    }

    public void setWaterMarkLogo(String waterMarkLogo) {
        this.waterMarkLogo = waterMarkLogo;
    }

    public String getGateImg() {
        return gateImg;
    }

    public void setGateImg(String gateImg) {
        this.gateImg = gateImg;
    }

    public String getTrackIcon() {
        return trackIcon;
    }

    public void setTrackIcon(String trackIcon) {
        this.trackIcon = trackIcon;
    }

    public String getLocationIcon() {
        return locationIcon;
    }

    public void setLocationIcon(String locationIcon) {
        this.locationIcon = locationIcon;
    }

    public String getSafetyIcon() {
        return safetyIcon;
    }

    public void setSafetyIcon(String safetyIcon) {
        this.safetyIcon = safetyIcon;
    }

    public String getCampusSwitchIcon() {
        return campusSwitchIcon;
    }

    public void setCampusSwitchIcon(String campusSwitchIcon) {
        this.campusSwitchIcon = campusSwitchIcon;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
