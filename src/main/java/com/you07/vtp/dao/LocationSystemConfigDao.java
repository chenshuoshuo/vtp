package com.you07.vtp.dao;

import com.you07.common.BaseDao;
import com.you07.vtp.model.LocationSystemConfig;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationSystemConfigDao extends BaseDao<LocationSystemConfig>{
    @Select({
            "select * from location_system_config limit 1"
    })
    LocationSystemConfig loadDefault();
}
