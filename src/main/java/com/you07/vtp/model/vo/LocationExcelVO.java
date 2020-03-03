package com.you07.vtp.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

/**
 * @author cs
 * @Date 2020/3/2 17:28
 * @Version 2.2.2.0
 **/
public class LocationExcelVO extends BaseRowModel {

    @ExcelProperty(value = {"学工号"},index = 0)
    private String userId;
    @ExcelProperty(value = {"姓名"},index = 1)
    private String realName;
    @ExcelProperty(value = {"组织机构名称"},index = 2)
    private String orgName;
    @ExcelProperty(value = {"最后经过时间"},index = 3)
    private Date locationTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Date getLocationTime() {
        return locationTime;
    }

    public void setLocationTime(Date locationTime) {
        this.locationTime = locationTime;
    }
}
