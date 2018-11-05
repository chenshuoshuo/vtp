package com.you07.vtp.service;

import com.you07.eas.model.StudentInfo;
import com.you07.eas.model.TeacherInfo;
import com.you07.eas.service.StudentInfoService;
import com.you07.eas.service.TeacherInfoService;
import com.you07.vtp.dao.LocationLatestDao;
import com.you07.vtp.model.LocationLatest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LocationLatestService {
    @Autowired
    private StudentInfoService studentInfoService;
    @Autowired
    private TeacherInfoService teacherInfoService;
    @Autowired
    private LocationLatestDao locationLatestDao;

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

}
