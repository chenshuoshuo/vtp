package com.you07.vtp.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.you07.util.RestTemplateUtil;
import com.you07.vtp.dao.LocationCampusInfoDao;
import com.you07.vtp.model.LocationCampusInfo;
import com.you07.vtp.vo.TagContentVO;
import com.you07.vtp.vo.MapZoneVO;
import com.you07.vtp.vo.TagVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class LocationCampusInfoService {

    private Logger logger = LoggerFactory.getLogger(LocationCampusInfoService.class);

    @Autowired
    private LocationCampusInfoDao locationCampusInfoDao;

    public void deleteWithSchoolId(Integer schoolId) {
        locationCampusInfoDao.deleteWithSchoolId(schoolId);
    }

    public int add(LocationCampusInfo locationCampusInfo) {
        return locationCampusInfoDao.insertSelective(locationCampusInfo);
    }

    public List<LocationCampusInfo> selectAll() {
        return locationCampusInfoDao.selectAll();
    }

    public List<LocationCampusInfo> queryAll() {
        return locationCampusInfoDao.queryAll();
    }

    public List<LocationCampusInfo> queryAllDisplay() {
        return locationCampusInfoDao.queryAllDisplay();
    }

    public int update(LocationCampusInfo locationCampusInfo) {
        return locationCampusInfoDao.updateByPrimaryKeySelective(locationCampusInfo);
    }

    public LocationCampusInfo queryById(String zoneId) {
        return locationCampusInfoDao.selectByPrimaryKey(Integer.parseInt(zoneId));
    }

    /**
     * egan
     * 从cmgis读取校区信息并存入校区表
     *
     * @param
     * @date 2019/9/5 15:53
     **/
    public void refreshCampus() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<MapZoneVO> zoneList = RestTemplateUtil.getJSONObjectForCmGis("/map/v3/zone/list/2")
                .getJSONArray("data").toJavaList(MapZoneVO.class);


        for (MapZoneVO z : zoneList) {
            String id = z.getId();
            MapZoneVO mapZoneVO = RestTemplateUtil.getJSONObjectForCmGis("/map/v2/zone/" + id)
                    .getObject("data", MapZoneVO.class);

            List<TagContentVO> content = RestTemplateUtil.getJSONObjectForCmGis("/map/search/v3/tag?page=0&pageSize=10&tagName=boundary&type=line&zoneId="+id)
                    .getObject("data", TagVO.class).getContent();
            if(content == null || content.size() < 1){
                logger.warn("校区"+id+"无法获取电子围栏，该校区已被忽略");
                continue;
            }

//            System.out.println(locationCampusInfo);
            boolean isUpdate = true;
            LocationCampusInfo locationCampusInfo = this.queryById(mapZoneVO.getId());
            if (locationCampusInfo == null) {
                locationCampusInfo = new LocationCampusInfo();
                locationCampusInfo.setCampusId(Integer.parseInt(mapZoneVO.getId()));
                locationCampusInfo.setIsDefault(0);
                locationCampusInfo.setIsDisplay(1);
                isUpdate = false;
            }
            locationCampusInfo.setAmapLngLat(mapZoneVO.getCenter());
            locationCampusInfo.setCampusName(mapZoneVO.getName());
            locationCampusInfo.setCoordinates(convertCoordinateToStr(content.get(0).getGeom()));
            if (isUpdate) {
                this.update(locationCampusInfo);
            } else {
                locationCampusInfoDao.insert(locationCampusInfo);
            }
        }

    }

    public String convertCoordinateToStr(String coordinates){
        coordinates = coordinates.replace("]", "")
                .replace("[", "");

        String[] array = coordinates.split(",");
        StringBuilder stringBuilder = new StringBuilder(array[0]);
        for(int i=1; i<array.length; i++){
            if(i%2!=0){
                stringBuilder.append(",");
            }else {
                stringBuilder.append(" ");
            }
            stringBuilder.append(array[i]);
        }
        return stringBuilder.toString();
    }

}
