package com.you07.vtp.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 用户最新位置信息
 * @author RY
 * @since 2018-8-8 15:48:49
 */
@Table(name = "location_latest")
@ApiModel(value = "locationInfo", description = "用户位置信息实体对象")
public class LocationLatest {
    /**
     * 用户ID
     */
    @Id
    @Column(name = "userid")
    @ApiModelProperty(value = "用户ID(学工号)", name = "userid", dataType = "String", example = "xxx1", required = true)
    private String userid;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名", name = "realname", dataType = "String", example = "xxx1", required = false, hidden = true)
    private String realname;
    /**
     * 性别
     */
    @ApiModelProperty(value = "性别：男性、女性", name = "gender", dataType = "String", example = "xxx1", required = false, hidden = true)
    private String gender;
    /**
     * 上网账号/MAC
     */
    @ApiModelProperty(value = "上网账号/MAC", name = "accountMac", dataType = "String", example = "xxx1", required = false)
    private String accountMac;
    /**
     * 组织机构代码
     */
    @ApiModelProperty(value = "组织机构代码", name = "orgCode", dataType = "String", example = "xxx1", required = false, hidden = true)
    private String orgCode;
    /**
     * 组织机构名称
     */
    @ApiModelProperty(value = "组织机构名称", name = "orgName", dataType = "String", example = "xxx1", required = false, hidden = true)
    private String orgName;
    /**
     * 位置经度
     */
    @ApiModelProperty(value = "位置经度", name = "lng", dataType = "Double", example = "103.212", required = true)
    private Double lng;
    /**
     * 位置纬度
     */
    @ApiModelProperty(value = "位置纬度", name = "lat", dataType = "Double", example = "30.15", required = true)
    private Double lat;
    /**
     * 灵奇地图楼层ID
     */
    @ApiModelProperty(value = "灵奇地图楼层ID", name = "lat", dataType = "Double", example = "xxx1", required = true)
    private String floorid;
    /**
     * 定位时间
     */
    @ApiModelProperty(value = "定位时间", name = "locationTime", dataType = "timestamp", required = false, hidden = true)
    private Date locationTime;
    /**
     * 用户信息更新时间
     */
    @ApiModelProperty(value = "用户信息更新时间", name = "usrUpdateTime", dataType = "timestamp", required = false, hidden = true)
    private Date usrUpdateTime;
    /**
     * 定位方式
     */
    @ApiModelProperty(value = "定位方式", name = "locationMode", dataType = "String", example = "xxx1", required = false)
    private String locationMode;
    /**
     * 室内/室外，1室内，2室外
     */
    @ApiModelProperty(value = "室内/室外，1室内，2室外", name = "inDoor", dataType = "Integer", example = "1", required = true)
    private Integer inDoor;
    /**
     * 校内/校外，1校内，2校外
     */
    @ApiModelProperty(value = "校内/校外，1校内，2校外", name = "inSchool", dataType = "Integer", example = "1", required = true)
    private Integer inSchool;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAccountMac() {
        return accountMac;
    }

    public void setAccountMac(String accountMac) {
        this.accountMac = accountMac;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getFloorid() {
        return floorid;
    }

    public void setFloorid(String floorid) {
        this.floorid = floorid;
    }

    public Date getLocationTime() {
        return locationTime;
    }

    public void setLocationTime(Date locationTime) {
        this.locationTime = locationTime;
    }

    public Date getUsrUpdateTime() {
        return usrUpdateTime;
    }

    public void setUsrUpdateTime(Date usrUpdateTime) {
        this.usrUpdateTime = usrUpdateTime;
    }

    public String getLocationMode() {
        return locationMode;
    }

    public void setLocationMode(String locationMode) {
        this.locationMode = locationMode;
    }

    public Integer getInDoor() {
        return inDoor;
    }

    public void setInDoor(Integer inDoor) {
        this.inDoor = inDoor;
    }

    public Integer getInSchool() {
        return inSchool;
    }

    public void setInSchool(Integer inSchool) {
        this.inSchool = inSchool;
    }
}
