package com.you07.vtp.service;

import com.vividsolutions.jts.geom.*;
import com.you07.eas.model.StudentInfo;
import com.you07.eas.model.TeacherInfo;
import com.you07.eas.service.StudentInfoService;
import com.you07.eas.service.TeacherInfoService;
import com.you07.map.model.MapZone;
import com.you07.map.service.MapZoneService;
import com.you07.vtp.dao.LocationLatestDao;
import com.you07.vtp.model.LocationLatest;
import org.geotools.geometry.jts.JTS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LocationLatestService {
    private static final GeometryFactory geometryFactory = new GeometryFactory();
    @Autowired
    private StudentInfoService studentInfoService;
    @Autowired
    private TeacherInfoService teacherInfoService;
    @Autowired
    private LocationLatestDao locationLatestDao;
    @Autowired
    private MapZoneService mapZoneService;

    public int saveLocation(LocationLatest locationLatest){
        StudentInfo stu = studentInfoService.get(locationLatest.getUserid());
        TeacherInfo tea = teacherInfoService.get(locationLatest.getUserid());
        LocationLatest ll = locationLatestDao.selectByPrimaryKey(locationLatest.getUserid());

        locationLatestDao.invalidMac(locationLatest.getAccountMac());
        if(locationLatest.getInSchool()==null && locationLatest.getFloorid()!=null){
            //判断经纬度是否在校内
            String zoneId = locationLatest.getFloorid().trim().substring(0,4);
            MapZone mapZone = mapZoneService.query2ZoneId(zoneId);
            Polygon polygon = JTS.toGeometry(new Envelope(mapZone.getLeftBottomLon(), mapZone.getRightTopLon(), mapZone.getLeftBottomLat(), mapZone.getRightTopLat()),
                    geometryFactory);
            Coordinate coordinate = new Coordinate(locationLatest.getLng(),locationLatest.getLat());
            Point point = geometryFactory.createPoint(coordinate);
            Geometry geometry = geometryFactory.createGeometry(polygon);
            boolean contains = geometry.contains(point);
            if(contains){
                locationLatest.setInSchool(1);
            }else {
                locationLatest.setInSchool(2);
            }
        }
        if(ll == null){
            String realname, gender, orgCode, orgName;
            realname = gender = orgCode = orgName = "";
            if(stu != null || tea != null) {
                if (stu != null) {
                    realname = stu.getName();
                    orgCode = stu.getClassInfo().getClasscode();
                    orgName = stu.getClassInfo().getClassname();
                }
                if (tea != null) {
                    realname = tea.getName();
                    orgCode = tea.getDepartmentInfo().getXsbmdm();
                    orgName = tea.getDepartmentInfo().getXsbmmc();
                }
                locationLatest.setGender(gender);
                locationLatest.setRealname(realname);
                locationLatest.setOrgCode(orgCode);
                locationLatest.setOrgName(orgName);
                locationLatest.setUsrUpdateTime(new Date());
                locationLatest.setLocationTime(new Date());
                return locationLatestDao.insertSelective(locationLatest);
            } else {
                return 0;
            }
        }else {
            ll.setInSchool(locationLatest.getInSchool());
            ll.setFloorid(locationLatest.getFloorid());
            ll.setInDoor(locationLatest.getInDoor());
            ll.setLat(locationLatest.getLat());
            ll.setLng(locationLatest.getLng());
            ll.setAccountMac(locationLatest.getAccountMac());
            ll.setLocationTime(new Date());
            return locationLatestDao.updateByPrimaryKeySelective(ll);
        }
    }

}
