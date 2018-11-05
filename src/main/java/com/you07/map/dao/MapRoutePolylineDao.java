package com.you07.map.dao;

import com.you07.common.BaseDao;
import com.you07.map.model.MapRoutePolyline;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapRoutePolylineDao extends BaseDao<MapRoutePolyline>{

    @Select({
            "select * from map.map_route_polyline where zoneid = #{campusId}"
    })
    List<MapRoutePolyline> selectByCampusId(@Param("campusId") Integer campusId);

    @Select({
            "select * from map.map_route_polyline"
    })
    List<MapRoutePolyline> queryAll();

    @Results({
            @Result(column = "routeid", property = "routeid", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "road_type", property = "roadType", jdbcType = JdbcType.INTEGER),
            @Result(column = "direction", property = "direction", jdbcType = JdbcType.DOUBLE),
            @Result(column = "geom", property = "geom", jdbcType = JdbcType.OTHER),
            @Result(column = "memo", property = "memo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "source", property = "source", jdbcType = JdbcType.BIGINT),
            @Result(column = "target", property = "target", jdbcType = JdbcType.BIGINT),
            @Result(column = "roadattr_id", property = "roadattrId", jdbcType = JdbcType.BIGINT),
            @Result(column = "zoneid", property = "zoneid", jdbcType = JdbcType.BIGINT),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR)
    })
    @Select("SELECT * FROM map.map_route_polyline WHERE zoneid=#{mapId}")
    List<MapRoutePolyline> selectPolyLineByMapId(@Param("mapId") Long mapId);
}
