package com.you07.eas.dao;

import com.you07.common.BaseDao;
import com.you07.eas.model.Major;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MajorDao extends BaseDao<Major> {

    @Select({
            "select * from eas_major where acadamey_code = #{academyCode} order by major_code"
    })
    List<Major> queryWithAcademy(@Param("academyCode") String academyCode);
}
