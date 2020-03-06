package com.you07.vtp.dao;

import com.you07.common.BaseDao;
import com.you07.vtp.model.SsGroup;
import com.you07.vtp.model.SsMarker;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SsMakerDao extends BaseDao<SsMarker> {

    /**
     * 根据条件获取分组列表
     */
    @Select({"<script>",
            "select * from ss_marker where 1=1 " +
                    "<if test = ' markerName != null and markerName != \"\"'> and marker_name like \'%${markerName}%\'</if>" +
                    "order by update_time desc" +
                    "</script>"
    })
    List<SsMarker> queryAll(@Param("markerName") String markerName);

    /**
     * 根据校区获取大楼标注列表
     */
    @Select({
            "select * from ss_marker where campus_code = #{campusCode} " +
                    "order by update_time desc"
    })
    List<SsMarker> listQuery(@Param("campusCode")Integer campusCode);

    @Select("select case when max(t.marker_id) is null then 1 else (max(t.marker_id) + 1) end from ss_marker t")
    Integer queryNewColumnId();

    @Insert("INSERT INTO ss_marker ( marker_id,marker_name,module_id,geom,icon,polygon_name,update_time,order_id,memo,campus_code ) VALUES( #{marker.markerId},#{marker.markerName},#{marker.moduleId}," +
            "#{marker.geom},#{marker.icon},#{marker.polygonName},#{marker.updateTime},#{marker.orderId},#{marker.memo},#{marker.campusCode}) ")
    int add(@Param("marker") SsMarker marker);


    @Update("UPDATE ss_marker set marker_name = #{marker.markerName},module_id = #{marker.moduleId},geom = #{marker.geom},icon = #{marker.icon},polygon_name = #{marker.polygonName}," +
            "update_time = #{marker.updateTime},order_id = #{marker.orderId},memo = #{marker.memo} where marker_id = #{marker.markerId}")
    int update(@Param("marker") SsMarker marker);

    @Select("select * from ss_marker where marker_id =#{markerId}")
    SsMarker get(@Param("markerId")Integer markerId);

    @Delete("delete from ss_marker where marker_id = #{markerId}")
    int deleteById(@Param("markerId")Integer markerId);


}
