package com.you07.map.dao;

import com.you07.map.model.MapZone;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapZoneDao {
    /**
     * 根据地图类别
     * 获取所有地图区域信息
     * @return
     */
    @Select({
            "select * from map_zone where maptype = #{mapType}"
    })
    List<MapZone> queryWithMapType(@Param("mapType") String mapType);


    /**
     * @Author wells
     * @Description //TODO 根据zoneid 获取地图信息
     * @Date 16:57 2019/6/28
     * @Param [zoneid]
     * @return java.util.List<com.you07.map.model.MapZone>
     **/
    @Select({
            "select * from map_zone where zoneid = #{zoneid}"
    })
    MapZone queryWithId(@Param("zoneid") String zoneid);
}
