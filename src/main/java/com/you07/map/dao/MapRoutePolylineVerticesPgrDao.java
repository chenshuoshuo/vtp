package com.you07.map.dao;

import com.you07.map.model.MapRoutePolylineVerticesPgr;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapRoutePolylineVerticesPgrDao {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table map.map_route_polyline_vertices_pgr
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    @Delete({
        "delete from map.map_route_polyline_vertices_pgr",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table map.map_route_polyline_vertices_pgr
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    @Insert({
        "insert into map.map_route_polyline_vertices_pgr (id, cnt, ",
        "chk, ein, eout, ",
        "the_geom)",
        "values (#{id,jdbcType=BIGINT}, #{cnt,jdbcType=INTEGER}, ",
        "#{chk,jdbcType=INTEGER}, #{ein,jdbcType=INTEGER}, #{eout,jdbcType=INTEGER}, ",
        "#{theGeom,jdbcType=OTHER})"
    })
    int insert(MapRoutePolylineVerticesPgr record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table map.map_route_polyline_vertices_pgr
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    @Select({
        "select",
        "id, cnt, chk, ein, eout, the_geom",
        "from map.map_route_polyline_vertices_pgr",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="cnt", property="cnt", jdbcType=JdbcType.INTEGER),
        @Result(column="chk", property="chk", jdbcType=JdbcType.INTEGER),
        @Result(column="ein", property="ein", jdbcType=JdbcType.INTEGER),
        @Result(column="eout", property="eout", jdbcType=JdbcType.INTEGER),
        @Result(column="the_geom", property="theGeom", jdbcType=JdbcType.OTHER)
    })
    MapRoutePolylineVerticesPgr selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table map.map_route_polyline_vertices_pgr
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    @Select({
        "select",
        "id, cnt, chk, ein, eout, the_geom",
        "from map.map_route_polyline_vertices_pgr"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="cnt", property="cnt", jdbcType=JdbcType.INTEGER),
        @Result(column="chk", property="chk", jdbcType=JdbcType.INTEGER),
        @Result(column="ein", property="ein", jdbcType=JdbcType.INTEGER),
        @Result(column="eout", property="eout", jdbcType=JdbcType.INTEGER),
        @Result(column="the_geom", property="theGeom", jdbcType=JdbcType.OTHER)
    })
    List<MapRoutePolylineVerticesPgr> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table map.map_route_polyline_vertices_pgr
     *
     * @mbg.generated Tue Dec 19 13:42:31 CST 2017
     */
    @Update({
        "update map.map_route_polyline_vertices_pgr",
        "set cnt = #{cnt,jdbcType=INTEGER},",
          "chk = #{chk,jdbcType=INTEGER},",
          "ein = #{ein,jdbcType=INTEGER},",
          "eout = #{eout,jdbcType=INTEGER},",
          "the_geom = #{theGeom,jdbcType=OTHER}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(MapRoutePolylineVerticesPgr record);

    @Select("SELECT * FROM map.map_route_polyline_vertices_pgr\n" +
            "\t\tWHERE \"id\" IN (SELECT \"source\" FROM map.map_route_polyline " +
            "WHERE zoneid=#{mapId})\n" + "\t\tOR \"id\" IN " +
            "(SELECT target FROM map.map_route_polyline WHERE zoneid=#{mapId})")
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="cnt", property="cnt", jdbcType=JdbcType.INTEGER),
            @Result(column="chk", property="chk", jdbcType=JdbcType.INTEGER),
            @Result(column="ein", property="ein", jdbcType=JdbcType.INTEGER),
            @Result(column="eout", property="eout", jdbcType=JdbcType.INTEGER),
            @Result(column="the_geom", property="theGeom", jdbcType=JdbcType.OTHER)
    })
    List<MapRoutePolylineVerticesPgr> selectAllByMapId(@Param("mapId") Long mapId);
}