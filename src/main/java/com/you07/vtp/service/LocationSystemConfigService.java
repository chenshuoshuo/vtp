package com.you07.vtp.service;

import com.you07.vtp.dao.LocationSystemConfigDao;
import com.you07.vtp.model.LocationSystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationSystemConfigService {
    @Autowired
    private LocationSystemConfigDao locationSystemConfigDao;

    public int add(LocationSystemConfig locationSystemConfig){
        return locationSystemConfigDao.insertSelective(locationSystemConfig);
    }

    public int update(LocationSystemConfig locationSystemConfig){
        return locationSystemConfigDao.updateByPrimaryKeySelective(locationSystemConfig);
    }

    public LocationSystemConfig get(Integer configId){
        return locationSystemConfigDao.selectByPrimaryKey(configId);
    }

    public LocationSystemConfig loadDefault(){
        return locationSystemConfigDao.loadDefault();
    }
}
