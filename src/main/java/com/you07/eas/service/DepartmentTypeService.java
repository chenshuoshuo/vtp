package com.you07.eas.service;

import com.you07.config.datasource.DataBaseContextHolder;
import com.you07.config.datasource.annotation.DataSourceConnection;
import com.you07.eas.dao.DepartmentTypeDao;
import com.you07.eas.model.DepartmentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentTypeService {
    @Autowired
    private DepartmentTypeDao departmentTypeDao;

    @DataSourceConnection(DataBaseContextHolder.DataBaseType.POSTGRESEAS)
    public List<DepartmentType> queryAll(){
        return departmentTypeDao.queryAll();
    }
}
