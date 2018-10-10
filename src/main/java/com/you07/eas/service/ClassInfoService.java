package com.you07.eas.service;

import com.you07.config.datasource.DataBaseContextHolder;
import com.you07.config.datasource.annotation.DataSourceConnection;
import com.you07.eas.dao.ClassInfoDao;
import com.you07.eas.model.ClassInfo;
import com.you07.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassInfoService {
    @Autowired
    private ClassInfoDao classInfoDao;

    @DataSourceConnection(DataBaseContextHolder.DataBaseType.POSTGRESEAS)
    public List<ClassInfo> queryWithMajor(String majorCode){
        return classInfoDao.queryWithMajor(majorCode);
    }

    @DataSourceConnection(DataBaseContextHolder.DataBaseType.POSTGRESEAS)
    public List<ClassInfo> queryInSchoolWithAcadamey(String acadameyCode, Integer schoolYear){
        return classInfoDao.queryInSchoolWithAcadamey(acadameyCode, schoolYear);
    }

    @DataSourceConnection(DataBaseContextHolder.DataBaseType.POSTGRESEAS)
    public List<ClassInfo> selectWithPrivilegeOrgCodes(String keyWord, String privilegeOrgCodes){
        return classInfoDao.selectWithPrivilegeOrgCodes(keyWord, "'" + privilegeOrgCodes + "'", DateUtil.getDefaultInstance().getSchoolYear());
    }
}
