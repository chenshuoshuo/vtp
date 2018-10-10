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

    public void saveUser(String sAccout, String sMac, String mode){
        StudentInfo stu = studentInfoService.get(sAccout);
        TeacherInfo tea = teacherInfoService.get(sAccout);
        LocationLatest ll = locationLatestDao.selectByPrimaryKey(sAccout);

        locationLatestDao.invalidMac(sMac);

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
            }

            ll = new LocationLatest();

            ll.setUserid(sAccout);
            ll.setGender(gender);
            ll.setRealname(realname);
            ll.setOrgCode(orgCode);
            ll.setOrgName(orgName);
            ll.setAccountMac(sMac);
            ll.setLocationMode(mode);
            ll.setUsrUpdateTime(new Date());
            locationLatestDao.insertSelective(ll);
        }

        ll.setAccountMac(sMac);
        ll.setLocationMode(mode);
        ll.setUsrUpdateTime(new Date());
        locationLatestDao.updateByPrimaryKeySelective(ll);
    }

    /**
     * 根据MAC地址
     * 注销绑定该MAC的所有
     * @param mac
     */
    public void invalidMac(String mac){
        locationLatestDao.invalidMac(mac);
    }

    public int update(LocationLatest locationLatest){
        return locationLatestDao.updateByPrimaryKeySelective(locationLatest);
    }

    public LocationLatest loadByAccountMac(String accountMac){
        return locationLatestDao.loadByAccountMac(accountMac);
    }
}
