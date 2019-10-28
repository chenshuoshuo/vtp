package com.you07.vtp.service;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.you07.util.RestTemplateUtil;
import com.you07.vtp.dao.LocationCampusInfoDao;
import com.you07.vtp.model.LocationCampusInfo;
import com.you07.vtp.vo.MapZoneVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    public LocationCampusInfo queryById(String zoneId){
        return locationCampusInfoDao.selectByPrimaryKey(Integer.parseInt(zoneId));
    }

    /**
     * egan
     * 从cmgis读取校区信息并存入校区表
     * @date 2019/9/5 15:53
     * @param
     **/
    public void refreshCampus(){


        List<MapZoneVO> zoneList = RestTemplateUtil.getJSONObjectForCmGis("/map/v3/zone/list/2")
                .getJSONArray("data").toJavaList(MapZoneVO.class);

        for (MapZoneVO z : zoneList) {
            String id = z.getId();
            MapZoneVO mapZoneVO = RestTemplateUtil.getJSONObjectForCmGis( "/map/v2/zone/" + id)
                    .getObject("data", MapZoneVO.class);



//            System.out.println(locationCampusInfo);
            LocationCampusInfo locationCampusInfo = this.queryById(mapZoneVO.getId());
            if(locationCampusInfo == null) {
                locationCampusInfo = new LocationCampusInfo();
                locationCampusInfo.setCampusId(Integer.parseInt(mapZoneVO.getId()));
                locationCampusInfo.setIsDefault(0);
                locationCampusInfo.setIsDisplay(1);
            }
            locationCampusInfo.setCampusName(mapZoneVO.getName());
            locationCampusInfo.setCoordinates(convertCoordinateToStr(mapZoneVO.getPolygonBBox().getCoordinates()));
            this.update(locationCampusInfo);
        }

    }

    public String convertCoordinateToStr(List<List<String>> coordinates){
        StringBuilder str = new StringBuilder();
        for (List<String> cl : coordinates){
            for(String c : cl){
                c = c.replace("]", "").replace("[", "").replace(" ", "");
                    str.append(" ");
                str.append(c);
            }

        }
        return str.toString().substring(1);
    }

}
