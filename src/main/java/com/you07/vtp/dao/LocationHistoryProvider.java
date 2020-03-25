package com.you07.vtp.dao;

/**
 * @ClassName: LocationHistoryProvider
 * @Author: chenguo
 * @Data: 2020年03月23日 10时49分
 * @Description:
 * @Company: 成都灵奇空间软件
 * @Version: 1.0
 */
public class LocationHistoryProvider {

    public String userIdsTimeZoneSQL(String userids,
                                     String org,
                                     String cls,
                                     String nations,
                                     String birthplaces,
                                     String tableName,
                                     String startTime,
                                     String endTime,
                                     Integer inSchool,
                                     Integer campusId){
        StringBuffer sql=new StringBuffer("select * from ").append(tableName).append(" _location,");
        sql.append("(select userid, max(location_time) _last ");
        sql.append("from ").append(tableName);
        sql.append(" where 1=1 ");
        if ((userids != null && !userids.equals("''"))
                || (org != null && !org.equals("''"))
                || (cls != null && !cls.equals("''"))
                || (nations != null && !nations.equals("''"))
                || (birthplaces != null && !birthplaces.equals("''"))) {
            sql.append("and (");
            StringBuffer sql_child = new StringBuffer();
            if (userids != null && !userids.equals("''")) {
                sql_child.append("userid in (").append(userids).append(") ");
            }
            if (org != null && !org.equals("''")) {
                sql_child.append("or org_code in (").append(org).append(")");
            }
            if (cls != null && !cls.equals("''")) {
                sql_child.append("or class_code in (").append(cls).append(")");
            }
            if (nations != null && !nations.equals("''")) {
                sql_child.append("or nation in (").append(nations).append(") ");
            }
            if (birthplaces != null && !birthplaces.equals("''")) {
                sql_child.append("or birthplace in (").append(birthplaces).append(") ");
            }
            if (sql_child.toString().startsWith("or")) {
                sql.append(sql_child.substring(2));
            } else {
                sql.append(sql_child);
            }
            sql.append(")");
        }
        sql.append("and zone_id = '").append(campusId).append("' ");
        sql.append("and location_time > to_timestamp(#{startTime},'yyyy-mm-dd hh24:mi:ss') ");
        sql.append("and location_time < to_timestamp(#{endTime},'yyyy-mm-dd hh24:mi:ss') ");
        sql.append("and lng is not null ");
        sql.append("and in_school = #{inSchool} ");
        sql.append("group by userid) as _group ");
        sql.append("where _location.location_time = _group._last ");
        sql.append("and _location.userid = _group.userid ");

        return sql.toString();
    }


    public String userInfoTimeZoneSQL(String userids, String keyWord, String tableName, String startTime,
                                      String endTime, Integer inSchool, Integer campusId) {
        StringBuffer sql=new StringBuffer("select * from ").append(tableName).append(" _location,");
        sql.append("(select userid, max(location_time) _last ");
        sql.append("from ").append(tableName);
        sql.append(" where 1=1 ");
        if ((userids != null && !userids.equals("''")) || (keyWord != null && !keyWord.equals("''"))) {
            sql.append("and (");
            StringBuffer sql_child = new StringBuffer();
            if (userids != null && !userids.equals("''")) {
                sql_child.append("userid in (").append(userids).append(") ");
            }
            if (keyWord != null && !keyWord.equals("''")) {
                sql_child.append("or userid in (").append(keyWord).append(")");
                sql_child.append("or realname in (").append(keyWord).append(")");
                sql_child.append("or account_mac in (").append(keyWord).append(")");
            }
            if (sql_child.toString().startsWith("or")) {
                sql.append(sql_child.substring(2));
            } else {
                sql.append(sql_child);
            }
            sql.append(")");
        }
        sql.append("and zone_id = '").append(campusId).append("' ");
        sql.append("and location_time > to_timestamp(#{startTime},'yyyy-mm-dd hh24:mi:ss') ");
        sql.append("and location_time < to_timestamp(#{endTime},'yyyy-mm-dd hh24:mi:ss') ");
        sql.append("and lng is not null ");
        sql.append("and in_school = #{inSchool} ");
        sql.append("group by userid) as _group ");
        sql.append("where _location.location_time = _group._last ");
        sql.append("and _location.userid = _group.userid ");

        return sql.toString();
    }
}
