package com.you07.vtp.model;

import java.util.List;

public class SystemConfigVO {
    private LocationSystemConfig locationSystemConfig;
    private List<LocationCampusInfo> campusInfoList;

    public LocationSystemConfig getLocationSystemConfig() {
        return locationSystemConfig;
    }

    public void setLocationSystemConfig(LocationSystemConfig locationSystemConfig) {
        this.locationSystemConfig = locationSystemConfig;
    }

    public List<LocationCampusInfo> getCampusInfoList() {
        return campusInfoList;
    }

    public void setCampusInfoList(List<LocationCampusInfo> campusInfoList) {
        this.campusInfoList = campusInfoList;
    }
}
