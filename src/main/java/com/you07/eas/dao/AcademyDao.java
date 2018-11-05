package com.you07.eas.dao;

import com.you07.common.BaseDao;
import com.you07.eas.model.Academy;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcademyDao extends BaseDao<Academy> {
    @Select({
            "select * from eas_acdamey order by acadamey_code"
    })
    List<Academy> queryAll();
}
