package com.you07.eas.service;

import com.you07.config.datasource.DataBaseContextHolder;
import com.you07.config.datasource.annotation.DataSourceConnection;
import com.you07.eas.dao.TeacherInfoDao;
import com.you07.eas.model.TeacherInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherInfoService {
    @Autowired
    private TeacherInfoDao teacherInfoDao;

    @DataSourceConnection(DataBaseContextHolder.DataBaseType.POSTGRESEAS)
    public List<TeacherInfo> listAll(){
        return teacherInfoDao.listAll();
    }

    @DataSourceConnection(DataBaseContextHolder.DataBaseType.POSTGRESEAS)
    public TeacherInfo get(String teacherCode){
        return teacherInfoDao.loadByTeacherCode(teacherCode);
    }

    @DataSourceConnection(DataBaseContextHolder.DataBaseType.POSTGRESEAS)
    public List<TeacherInfo> listQuery(String xsbmdm){
        return teacherInfoDao.listQuery(xsbmdm);
    }

    @DataSourceConnection(DataBaseContextHolder.DataBaseType.POSTGRESEAS)
    public List<TeacherInfo> searchWithCodeName(String keyword){
        return teacherInfoDao.searchWithCodeName(keyword);
    }
}
