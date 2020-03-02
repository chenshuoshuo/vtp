package com.you07.vtp.dao;

import com.you07.vtp.model.LocationHistory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationHistoryDao {

    /**
     * 单用户查询最新位置
     * @param userid
     * @param inSchool
     * @param campusId
     * @return
     */
    @Select({
            "select * from location_latest where userid = #{userid} and zone_id = '${campusId}' and lng is not null",
            "and in_school = #{inSchool}"
    })
    List<LocationHistory> selectByUserid(@Param("userid") String userid,
                                   @Param("inSchool") Integer inSchool,
                                   @Param("campusId") Integer campusId);

    /**
     * egan
     * 判断表是否存在
     * @date 2020/1/2 10:53
     * @param
     **/
    @Select({"select 1 from pg_tables where schemaname = 'public' and tablename = #{tablename}"})
    String selectTableName(@Param("tablename") String tablename);


    /**
     * 单用户查询最新位置
     * 只获取位置
     * @param userid
     * @return
     */
    @Select({
            "select * from location_latest where userid = #{userid} and lng is not null limit 1"
    })
    LocationHistory selectOneUserLocation(@Param("userid") String userid);


    /**
     * 多用户查询最新位置
     * @param userids
     * @param inSchool
     * @param campusId
     * @return
     */
    @Select({
            "select * from location_latest " +
            "where (userid in (${userids}) or realname in (${userNames}) or account_mac in (${accountMacs}) or org_code in (${org}) or class_code in (${cls}) or nation in (${nations}) or birthplace in (${birthplaces}))" +
            "and zone_id = '${campusId}' and lng is not null",
            "and in_school = #{inSchool}"
    })
    List<LocationHistory> selectByUserids(@Param("userids") String userids,
                                          @Param("userNames") String userNames,
                                          @Param("accountMacs") String accountMacs,
                                          @Param("org") String orgCodes,
                                          @Param("cls") String classCodes,
                                          @Param("nations") String nations,
                                          @Param("birthplaces") String birthplaces,
                                          @Param("inSchool") Integer inSchool,
                                          @Param("campusId") Integer campusId);

    /**
     * 全校用户最新位置
     * @param inSchool
     * @param campusId
     * @return
     */
    @Select({
            "select * from location_latest where lng is not null and  zone_id  = '${campusId}'",
            "and in_school = #{inSchool}"
    })
    List<LocationHistory> selectAll(@Param("inSchool") Integer inSchool,
                                    @Param("campusId") Integer campusId);

    /**
     * 根据单个组织机构代码查询最新位置
     * @param orgCode
     * @param inSchool
     * @param campusId
     * @return
     */
    @Select({
            /*"select * from location_latest where org_code = #{orgCode} and zone_id = '${campusId}' and lng is not null",
            "and in_school = #{inSchool}"*/
            "select * from location_latest where (nation = '02' or birthplace = '02') and org_code = '09' and zone_id = '8' and lng is not null and in_school = '1'"
    })
    List<LocationHistory> selectByOrgCode(@Param("orgCode") String orgCode,
                                          @Param("inSchool") Integer inSchool,
                                          @Param("campusId") Integer campusId);

    /**
     * 根据多个组织机构代码查询最新位置
     * @param orgCodes
     * @param inSchool
     * @param campusId
     * @return
     */
    @Select({
            "select * from location_latest where org_code in (${orgCodes}) and zone_id = '${campusId}' and lng is not null",
            "and in_school = #{inSchool}"
    })
    List<LocationHistory> selectByOrgCodes(@Param("orgCodes") String orgCodes,
                                           @Param("inSchool") Integer inSchool,
                                           @Param("campusId") Integer campusId);

    /**
     * 根据时间范围查询单用户最新位置
     * @param userid
     * @param tableName
     * @param startTime
     * @param endTime
     * @param inSchool
     * @param campusId
     * @return
     */
    @Select({
            "select * from ${tableName} _location,",
            "(select userid, max(location_time) _last",
            "from ${tableName}",
            "where userid = #{userid}",
            "and  zone_id = '${campusId}'",
            "and location_time > to_timestamp(#{startTime},'yyyy-mm-dd hh24:mi:ss')",
            "and location_time < to_timestamp(#{endTime},'yyyy-mm-dd hh24:mi:ss')",
            "and lng is not null",
            "and in_school = #{inSchool}",
            "group by userid) as _group",
             "where _location.location_time = _group._last",
             "and _location.userid = _group.userid"
    })
    List<LocationHistory> selectByUseridTimeZone(@Param("userid") String userid,
                                                 @Param("org") String orgCodes,
                                                 @Param("cls") String classCodes,

                                           @Param("tableName") String tableName,
                                           @Param("startTime") String startTime,
                                           @Param("endTime") String endTime,
                                           @Param("inSchool") Integer inSchool,
                                                 @Param("campusId") Integer campusId);

    /**
     * 根据时间范围查询多用户最新位置
     * @param userids
     * @param tableName
     * @param startTime
     * @param endTime
     * @param inSchool
     * @param campusId
     * @return
     */
    @Select({
            "select * from ${tableName} _location,",
            "(select userid, max(location_time) _last",
            "from ${tableName}",
            "where (userid in (${userids}) or org_code in (${org}) or class_code in (${cls}) or nation in (${nations}) or birthplace in (${birthplaces}))" ,
            "and zone_id = '${campusId}'",
            "and location_time > to_timestamp(#{startTime},'yyyy-mm-dd hh24:mi:ss')",
            "and location_time < to_timestamp(#{endTime},'yyyy-mm-dd hh24:mi:ss')",
            "and lng is not null",
            "and in_school = #{inSchool}",
            "group by userid) as _group",
            "where _location.location_time = _group._last",
            "and _location.userid = _group.userid"
    })
    List<LocationHistory> selectByUseridsTimeZone(@Param("userids") String userids,
                                                  @Param("org") String orgCodes,
                                                  @Param("cls") String classCodes,
                                                  @Param("nations") String nations,
                                                  @Param("birthplaces") String birthplaces,
                                                  @Param("tableName") String tableName,
                                                  @Param("startTime") String startTime,
                                                  @Param("endTime") String endTime,
                                                  @Param("inSchool") Integer inSchool,
                                                  @Param("campusId") Integer campusId);

    /**
     * 根据时间范围查询多用户最新位置
     * @param userids
     * @param tableName
     * @param startTime
     * @param endTime
     * @param campusId
     * @return
     */
    @Select({
            "select * from ${tableName} _location,",
            "(select userid, max(location_time) _last",
            "from ${tableName}",
            "where userid in (${userids} " ,
            "and zone_id = '${campusId}'",
            "and location_time > to_timestamp(#{startTime},'yyyy-mm-dd hh24:mi:ss')",
            "and location_time < to_timestamp(#{endTime},'yyyy-mm-dd hh24:mi:ss')",
            "and lng is not null",
            "group by userid) as _group",
            "where _location.location_time = _group._last",
            "and _location.userid = _group.userid"
    })
    List<LocationHistory> selectLastLngByUserIds(@Param("userids") String userids,
                                                  @Param("tableName") String tableName,
                                                  @Param("startTime") String startTime,
                                                  @Param("endTime") String endTime,
                                                  @Param("campusId") Integer campusId);

    /**
     * 根据时间范围查询全校用户最新位置
     * @param tableName
     * @param startTime
     * @param endTime
     * @param inSchool
     * @param campusId
     * @return
     */
    @Select({
            "select * from ${tableName} _location,",
            "(select userid, max(location_time) _last",
            "from ${tableName}",
            "where zone_id = '${campusId}' and location_time > to_timestamp(#{startTime},'yyyy-mm-dd hh24:mi:ss')",
            "and location_time < to_timestamp(#{endTime},'yyyy-mm-dd hh24:mi:ss')",
            "and lng is not null",
            "and in_school = #{inSchool}",
            "group by userid) as _group",
            "where _location.location_time = _group._last",
            "and _location.userid = _group.userid"
    })
    List<LocationHistory> selectAllTimeZone(@Param("tableName") String tableName,
                                            @Param("startTime") String startTime,
                                            @Param("endTime") String endTime,
                                            @Param("inSchool") Integer inSchool,
                                            @Param("campusId") Integer campusId);

    /**
     * 根据时间范围、单个组织机构代码查询用户最新位置
     * @param orgCode
     * @param tableName
     * @param startTime
     * @param endTime
     * @param inSchool
     * @param campusId
     * @return
     */
    @Select({
            "select * from ${tableName} _location,",
            "(select userid, max(location_time) _last",
            "from ${tableName}",
            "where org_code = #{orgCode} and  zone_id = '${campusId}'" ,
            "and location_time > to_timestamp(#{startTime},'yyyy-mm-dd hh24:mi:ss')",
            "and location_time < to_timestamp(#{endTime},'yyyy-mm-dd hh24:mi:ss')",
            "and lng is not null",
            "and in_school = #{inSchool}",
            "group by userid) as _group",
            "where _location.location_time = _group._last",
            "and _location.userid = _group.userid"
    })
    List<LocationHistory> selectByOrgCodeTimeZone(@Param("orgCode") String orgCode,
                                          @Param("tableName") String tableName,
                                          @Param("startTime") String startTime,
                                          @Param("endTime") String endTime,
                                          @Param("inSchool") Integer inSchool,
                                          @Param("campusId") Integer campusId);

    /**
     * 根据时间范围、多个组织机构代码查询用户最新位置
     * @param orgCodes
     * @param tableName
     * @param startTime
     * @param endTime
     * @param inSchool
     * @param campusId
     * @return
     */
    @Select({
            "select * from ${tableName} _location,",
            "(select userid, max(location_time) _last",
            "from ${tableName}",
            "where org_code in (${orgCodes}) and zone_id = '${campusId}'" ,
            "and location_time > to_timestamp(#{startTime},'yyyy-mm-dd hh24:mi:ss')",
            "and location_time < to_timestamp(#{endTime},'yyyy-mm-dd hh24:mi:ss')",
            "and lng is not null",
            "and in_school = #{inSchool}",
            "group by userid) as _group",
            "where _location.location_time = _group._last",
            "and _location.userid = _group.userid"
    })
    List<LocationHistory> selectByOrgCodesTimeZone(@Param("orgCodes") String orgCodes,
                                           @Param("tableName") String tableName,
                                           @Param("startTime") String startTime,
                                           @Param("endTime") String endTime,
                                           @Param("inSchool") Integer inSchool,
                                           @Param("campusId") Integer campusId);

    /**
     * 根据用户、时间范围查询轨迹信息
     * @param userid
     * @param tableName
     * @param startTime
     * @param endTime
     * @param inSchool
     * @param campusId
     * @return
     */
    @Select({
            "select * from ${tableName} where userid = #{userid} and zone_id = '${campusId}'",
            "and location_time > to_timestamp(#{startTime},'yyyy-mm-dd hh24:mi:ss')",
            "and location_time < to_timestamp(#{endTime},'yyyy-mm-dd hh24:mi:ss')",
            "and lng is not null",
            "and in_school = #{inSchool}",
            "order by location_time"
    })
    List<LocationHistory> selectTrackWithUseridTimeZone(@Param("userid") String userid,
                                                        @Param("tableName") String tableName,
                                                        @Param("startTime") String startTime,
                                                        @Param("endTime") String endTime,
                                                        @Param("inSchool") Integer inSchool,
                                                        @Param("campusId") Integer campusId);
    /**
     * 根据用户、时间范围查询轨迹信息
     * @param userid
     * @param tableName
     * @param startTime
     * @param endTime
     * @param campusId
     * @return
     */
    @Select({
            "select * from ${tableName} where userid = #{userid} and zone_id = '${campusId}'",
            "and location_time > to_timestamp(#{startTime},'yyyy-mm-dd hh24:mi:ss')",
            "and location_time < to_timestamp(#{endTime},'yyyy-mm-dd hh24:mi:ss')",
            "and lng is not null",
            "order by location_time"
    })
    List<LocationHistory> selectUserTrackWithTimeZone(@Param("userid") String userid,
                                                        @Param("tableName") String tableName,
                                                        @Param("startTime") String startTime,
                                                        @Param("endTime") String endTime,
                                                        @Param("campusId") Integer campusId);

    /**
     * 根据时间范围查询轨迹信息
     * @param tableName
     * @param startTime
     * @param endTime
     * @param inSchool
     * @param campusId
     * @return
     */
    @Select({
            "select * from ${tableName} where zone_id = '${campusId}'",
            "and location_time > to_timestamp(#{startTime},'yyyy-mm-dd hh24:mi:ss')",
            "and location_time < to_timestamp(#{endTime},'yyyy-mm-dd hh24:mi:ss')",
            "and lng is not null",
            "and in_school = #{inSchool}",
            "order by location_time"
    })
    List<LocationHistory> selectTrackWithTimeZone(@Param("tableName") String tableName,
                                                        @Param("startTime") String startTime,
                                                        @Param("endTime") String endTime,
                                                        @Param("inSchool") Integer inSchool,
                                                        @Param("campusId") Integer campusId);

    /**
     * 用户轨迹经纬度转面
     */
    @Select({
            "select ST_ConcaveHull(ST_GeomFromText(#{multiPoint})"
    })
    String transformLngLatToPolygon(@Param("multiPoint")String multiPoint);
}
