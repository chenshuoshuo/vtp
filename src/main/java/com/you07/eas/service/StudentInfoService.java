package com.you07.eas.service;

import com.you07.config.datasource.DataBaseContextHolder;
import com.you07.config.datasource.annotation.DataSourceConnection;
import com.you07.eas.dao.StudentInfoDao;
import com.you07.eas.model.StudentInfo;
import com.you07.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentInfoService {
    @Autowired
    private StudentInfoDao studentInfoDao;

    @DataSourceConnection(DataBaseContextHolder.DataBaseType.POSTGRESEAS)
    public List<StudentInfo> listAll(){
        return studentInfoDao.listAll();
    }

    @DataSourceConnection(DataBaseContextHolder.DataBaseType.POSTGRESEAS)
    public StudentInfo get(String studentNo){
        return studentInfoDao.loadByStudentNo(studentNo);
    }

    @DataSourceConnection(DataBaseContextHolder.DataBaseType.POSTGRESEAS)
    public List<StudentInfo> loadWithClassCodes(String classCodes){
        classCodes = "'" + classCodes.replaceAll(",", "','") + "'";

        return studentInfoDao.loadWithClassCodes(classCodes);
    }

    @DataSourceConnection(DataBaseContextHolder.DataBaseType.POSTGRESEAS)
    public List<StudentInfo> queryWithClass(String classCode){
        return studentInfoDao.queryWithClassCode(classCode);
    }

    @DataSourceConnection(DataBaseContextHolder.DataBaseType.POSTGRESEAS)
    public List<StudentInfo> searchWithCodeName(String keyword){
        return studentInfoDao.searchWithCodeName(keyword);
    }

    @DataSourceConnection(DataBaseContextHolder.DataBaseType.POSTGRESEAS)
    public List<StudentInfo> selectWithPrivilegeOrgCodes(String keyWord, String privilegeOrgCodes){
        return studentInfoDao.selectWithPrivilegeOrgCodes(keyWord,"'" + privilegeOrgCodes + "'", DateUtil.getDefaultInstance().getSchoolYear());
    }
}
