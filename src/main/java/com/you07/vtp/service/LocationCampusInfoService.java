package com.you07.vtp.service;

import com.you07.vtp.dao.LocationCampusInfoDao;
import com.you07.vtp.model.LocationCampusInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationCampusInfoService {
    @Autowired
    private LocationCampusInfoDao locationCampusInfoDao;

    public void deleteWithSchoolId(Integer schoolId){
        locationCampusInfoDao.deleteWithSchoolId(schoolId);
    }

    public int add(LocationCampusInfo locationCampusInfo){
        return locationCampusInfoDao.insertSelective(locationCampusInfo);
    }

    public List<LocationCampusInfo> selectAll(){
        return locationCampusInfoDao.selectAll();
    }

    public List<LocationCampusInfo> queryAll(){
        return locationCampusInfoDao.queryAll();
    }

    public List<LocationCampusInfo> queryAllDisplay(){
        return locationCampusInfoDao.queryAllDisplay();
    }

    public int update(LocationCampusInfo locationCampusInfo){
        return locationCampusInfoDao.updateByPrimaryKeySelective(locationCampusInfo);
    }

}
