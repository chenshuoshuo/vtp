package com.you07.vtp.service;

import com.you07.vtp.dao.LocationTrackManagerDao;
import com.you07.vtp.model.LocationTrackManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationTrackManagerService {
    @Autowired
    private LocationTrackManagerDao locationTrackManagerDao;

    public LocationTrackManager get(String userid){
        return locationTrackManagerDao.selectByPrimaryKey(userid);
    }

    public int add(LocationTrackManager locationTrackManager){
        return locationTrackManagerDao.insertSelective(locationTrackManager);
    }

    public int update(LocationTrackManager locationTrackManager){
        return locationTrackManagerDao.updateByPrimaryKeySelective(locationTrackManager);
    }

    public void delete(String userid){
        locationTrackManagerDao.deleteByPrimaryKey(userid);
    }

    public List<LocationTrackManager> listQuery(String userid, String username){
        return locationTrackManagerDao.listQuery(userid, username);
    }

}
