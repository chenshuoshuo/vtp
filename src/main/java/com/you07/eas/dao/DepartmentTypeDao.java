package com.you07.eas.dao;

import com.you07.common.BaseDao;
import com.you07.eas.model.DepartmentType;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentTypeDao extends BaseDao<DepartmentType> {

    @Select({
            "select * from eas_zzjg_bmlb order by bmlbdm"
    })
    List<DepartmentType> queryAll();
}
