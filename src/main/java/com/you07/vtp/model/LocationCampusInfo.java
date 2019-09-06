package com.you07.vtp.model;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 校区信息
 * @author RY
 * @since 2018-8-8 17:04:37
 */

@Table(name = "location_campus_info")
public class LocationCampusInfo {
    /**
     * 校区ID
     */
    @Id
    @Column(name = "campus_id")
    @ApiModelProperty(value = "信息ID，添加时不需要", name = "campusId", dataType = "Integer", example = "1", required = false)
    private Integer campusId;
    /**
     * 学校ID
     */
    @ApiModelProperty(value = "学校ID", name = "schoolId", dataType = "Integer", example = "1", required = false)
    private Integer schoolId;
    /**
     * 校区名称
     */
    @ApiModelProperty(value = "校区名称", name = "campusName", dataType = "String", example = "XX大学XX校区", required = false)
    private String campusName;
    /**
     * 校区LOGO
     */
    @ApiModelProperty(value = "校区LOGO", name = "campusLogo", dataType = "String", example = "http://XXX.COM/XX.PNG", required = false)
    private String campusLogo;

    @ApiModelProperty(hidden = true)
    private String coordinates;

    /**
     * 是否默认，1是，0否
     */
    @ApiModelProperty(value = "是否默认，1是，0否", name = "isDefault", dataType = "Integer", example = "1", required = false)
    private Integer isDefault;
    /**
     * 是否显示，1是，0否
     */
    @ApiModelProperty(value = "是否显示，1是，0否", name = "isDisplay", dataType = "Integer", example = "1", required = false)
    private Integer isDisplay;
    /**
     * 校区坐标（高德）
     */
    @ApiModelProperty(value = "校区坐标（高德）", name = "amapLngLat", dataType = "String", example = "103.1551,31.561", required = false)
    private String amapLngLat;
    /**
     * 排序
     */
    @ApiModelProperty(hidden = true)
    private Integer orderid;
    /**
     * 备注
     */
    @ApiModelProperty(hidden = true)
    private String memo;

    public Integer getCampusId() {
        return campusId;
    }

    public void setCampusId(Integer campusId) {
        this.campusId = campusId;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }

    public String getCampusLogo() {
        return campusLogo;
    }

    public void setCampusLogo(String campusLogo) {
        this.campusLogo = campusLogo;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public Integer getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(Integer isDisplay) {
        this.isDisplay = isDisplay;
    }

    public String getAmapLngLat() {
        return amapLngLat;
    }

    public void setAmapLngLat(String amapLngLat) {
        this.amapLngLat = amapLngLat;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
