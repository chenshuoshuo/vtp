package com.you07.vtp.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 用户信息实体
 * @version 1.0
 * @author RY
 * @since 2018-8-27 09:38:00
 */

@Table(name = "location_trajectory_manager")
@ApiModel(value = "locationTrackManager", description = "用户信息实体对象")
public class LocationTrackManager {
    @Id
    @ApiModelProperty(value = "用户学工号", name = "userid", dataType = "String", example = "20012345", required = true)
    private String userid;
    @ApiModelProperty(value = "用户姓名", name = "userid", dataType = "String", example = "XXXX", required = true)
    private String username;
    @ApiModelProperty(value = "部门名称", name = "deptname", dataType = "String", example = "开发部", required = true)
    private String deptname;
    @ApiModelProperty(value = "头像", name = "avatar", dataType = "String", example = "http://xxx.com/xxx.png", required = false)
    private String avatar;
    @ApiModelProperty(value = "密码", name = "password", dataType = "String", example = "xxxxx", required = false, hidden = true)
    private String password;
    @ApiModelProperty(value = "是否可以登录后台，1是，0否", name = "isManager", dataType = "Integer", example = "1", required = true)
    private Integer isManager;
    @ApiModelProperty(hidden = true)
    private Date posttime;
    @ApiModelProperty(value = "可以查看的组织机构代码，多个以','分隔", name = "orgCodes", dataType = "String", example = "11,12,13", required = true)
    private String orgCodes;
    @ApiModelProperty(value = "可以查看的组织机构名称，多个以','分隔", name = "orgNames", dataType = "String", example = "技术部,开发部", required = true)
    private String orgNames;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getIsManager() {
        return isManager;
    }

    public void setIsManager(Integer isManager) {
        this.isManager = isManager;
    }

    public Date getPosttime() {
        return posttime;
    }

    public void setPosttime(Date posttime) {
        this.posttime = posttime;
    }

    public String getOrgCodes() {
        return orgCodes;
    }

    public void setOrgCodes(String orgCodes) {
        this.orgCodes = orgCodes;
    }

    public String getOrgNames() {
        return orgNames;
    }

    public void setOrgNames(String orgNames) {
        this.orgNames = orgNames;
    }
}
