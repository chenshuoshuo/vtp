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
                if(locationLatest.getInSchool()==null && locationLatest.getFloorid()!=null){
                    //判断经纬度是否在校内
                    String zoneId = locationLatest.getFloorid().trim().substring(0,4);
                    MapZone mapZone = mapZoneService.query2ZoneId(zoneId);
                    Polygon polygon = JTS.toGeometry(new Envelope(mapZone.getLeftBottomLon(), mapZone.getLeftBottomLat(), mapZone.getRightTopLon(), mapZone.getRightTopLat()),
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
        }
        locationLatest.setLocationTime(new Date());
        return locationLatestDao.updateByPrimaryKeySelective(locationLatest);
    }


    public static void main(String[] args) {
        Polygon polygon = JTS.toGeometry(new Envelope(113.535712, 34.827623, 113.551726, 34.836861),
                geometryFactory);
        Coordinate coordinate = new Coordinate(104.5576062,30.8208133);
        Point point = geometryFactory.createPoint(coordinate);
        Geometry geometry = geometryFactory.createGeometry(polygon);
        boolean contains = geometry.contains(point);
        System.out.println(contains);
    }
}
