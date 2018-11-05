package com.you07.eas.service;

import com.you07.config.datasource.DataBaseContextHolder;
import com.you07.config.datasource.annotation.DataSourceConnection;
import com.you07.eas.dao.DepartmentInfoDao;
import com.you07.eas.model.DepartmentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentInfoService {
    @Autowired
    private DepartmentInfoDao departmentInfoDao;

    @DataSourceConnection(DataBaseContextHolder.DataBaseType.POSTGRESEAS)
    public List<DepartmentInfo> listQuery(String bmlbdm){
        return departmentInfoDao.listQuery(bmlbdm);
    }
}
