package com.you07.vtp.dao;

import com.you07.common.BaseDao;
import com.you07.vtp.model.LocationCampusInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationCampusInfoDao extends BaseDao<LocationCampusInfo> {

    @Delete({
            "delete from location_campus_info where school_id = #{schoolId}"
    })
    void deleteWithSchoolId(@Param("schoolId") Integer schoolId);

    /**
     * 根据学校ID获取默认校区
     * @param schoolId
     * @return
     */
    @Select({
            "select * from location_campus_info where school_id = #{schoolId} and is_default = 1"
    })
    LocationCampusInfo loadDefaultCampus(@Param("schoolId") Integer schoolId);

    @Select({
            "select * from location_campus_info where is_display = 1 order by is_default desc"
    })
    List<LocationCampusInfo> queryAllDisplay();

    @Select({
            "select * from location_campus_info order by is_default desc"
    })
    List<LocationCampusInfo> queryAll();
}
