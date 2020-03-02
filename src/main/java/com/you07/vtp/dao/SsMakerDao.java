package com.you07.vtp.dao;

import com.you07.common.BaseDao;
import com.you07.vtp.model.SsGroup;
import com.you07.vtp.model.SsMarker;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SsMakerDao extends BaseDao<SsMarker> {

    /**
     * 根据条件获取分组列表
     */
    @Select({"<script>",
            "select * from ss_marker where 1=1 and campus_code = #{campusCode}" +
                    "<if test = ' markerName != null and makerName != \"\"'> and marker_name = #{makerName}</if>" +
                    "order by update_time desc" +
                    "</script>"
    })
    List<SsMarker> queryAll(@Param("campusCode")Integer campusCode,@Param("markerName") String markerName);

    /**
     * 根据校区获取大楼标注列表
     */
    @Select({
            "select * from ss_marker where campus_code = #{campusCode}" +
                    "order by update_time desc"
    })
    List<SsMarker> listQuery(@Param("campusCode")Integer campusCode);


}
