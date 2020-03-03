package com.you07.vtp.model.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author cs
 * @Date 2020/3/2 17:28
 * @Version 2.2.2.0
 **/
public class LocationExcelVO{

    private String userId;
    private String realName;
    private String orgName;
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

    public String getFormatLocationTime(){
        if(locationTime != null){
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(locationTime);
        } else{
            return "";
        }
    }
}
