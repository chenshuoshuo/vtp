package com.you07.vtp.service;

import com.you07.vtp.dao.LocationHistoryDao;
import com.you07.vtp.form.UserLocationForm;
import com.you07.vtp.model.LocationHistory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LocationHitoryService {
    @Autowired
    private LocationHistoryDao locationHistoryDao;

    private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat DF = new SimpleDateFormat("yyyyMMdd");

    /**
     * 根据用户ID、时间范围
     * 获取用户最新位置
     * @param form
     * @return
     * @throws ParseException
     */
    public List<LocationHistory> selectByUserids(UserLocationForm form) throws ParseException {
        String startTime = form.getStartTime(), endTime = form.getEndTime();
        String tableName = getTableName(startTime, endTime);
        if(endTime == null || "".equals(endTime.trim())){
                return locationHistoryDao.selectByUserids(addQuot(form.getUserIds()), addQuot(form.getOrgCodes()), addQuot(form.getClassCodes()), addQuot(form.getNation()), addQuot(form.getBirthplace()),
                        form.getInSchool(), form.getCampusId());

        } else{
            if(StringUtils.isBlank(locationHistoryDao.selectTableName(tableName)))
                return new ArrayList<>();
            return locationHistoryDao.selectByUseridsTimeZone(addQuot(form.getUserIds()),addQuot(form.getOrgCodes()), addQuot(form.getClassCodes()), addQuot(form.getNation()), addQuot(form.getBirthplace()),
                    getTableName(startTime, endTime), startTime, endTime, form.getInSchool(), form.getCampusId());
        }
    }

    /**
     * 根据单用户ID
     * 获取单用户最新位置
     * @param userid
     * @return
     * @throws ParseException
     */
    public LocationHistory selectByUserid(String userid) throws ParseException {
            return locationHistoryDao.selectOneUserLocation(userid);
    }

    /**
     * 根据时间范围
     * 获取全校用户最新位置
     * @param startTime
     * @param endTime
     * @param inSchool
     * @return
     * @throws ParseException
     */
    public List<LocationHistory> selectAll(String startTime, String endTime, Integer inSchool, Integer campusId) throws ParseException {
        if(endTime == null || "".equals(endTime.trim())){
            return locationHistoryDao.selectAll(inSchool, campusId);
        } else{
            return locationHistoryDao.selectAllTimeZone(getTableName(startTime, endTime), startTime, endTime, inSchool, campusId);
        }
    }

    /**
     * 根据组织机构代码、时间范围
     * 查询所属组织机构下用户最新位置
     * @param orgCodes
     * @param startTime
     * @param endTime
     * @param inSchool
     * @return
     * @throws ParseException
     */
    public List<LocationHistory> selectByOrgCodes(String orgCodes,String startTime, String endTime, Integer inSchool, Integer campusId) throws ParseException {
        if(endTime == null || "".equals(endTime.trim())){
            if(orgCodes.indexOf(",") == -1){
                return locationHistoryDao.selectByOrgCode(orgCodes, inSchool, campusId);
            } else{
                return locationHistoryDao.selectByOrgCodes(addQuot(orgCodes), inSchool, campusId);
            }
        } else{
            if(orgCodes.indexOf(",") == -1){
                return locationHistoryDao.selectByOrgCodeTimeZone(orgCodes, getTableName(startTime, endTime), startTime, endTime, inSchool, campusId);
            } else{
                return locationHistoryDao.selectByOrgCodesTimeZone(addQuot(orgCodes), getTableName(startTime, endTime), startTime, endTime, inSchool, campusId);
            }
        }
    }

    /**
     * 根据用户、时间范围
     * 查询用户轨迹
     * @param userid
     * @param startTime
     * @param endTime
     * @param inSchool
     * @return
     * @throws ParseException
     */
    public List<LocationHistory> selectTrack(String userid, String startTime, String endTime, Integer inSchool, Integer campusId) throws ParseException {
        return locationHistoryDao.selectTrackWithUseridTimeZone(userid, getTableName(startTime, endTime), startTime, endTime, inSchool, campusId);
    }

    /**
     * 根据时间范围
     * 查询用户轨迹
     * @param startTime
     * @param endTime
     * @param inSchool
     * @return
     * @throws ParseException
     */
    public List<LocationHistory> selectTrackWithTimeZone(String startTime, String endTime, Integer inSchool, Integer campusId) throws ParseException {
        return locationHistoryDao.selectTrackWithTimeZone(getTableName(startTime, endTime), startTime, endTime, inSchool, campusId);
    }

    /**
     * 根据时间范围
     * 获取检索数据表的表名
     * @param startTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public String getTableName(String startTime, String endTime) throws ParseException {
        Long hourDiff = (SDF.parse(endTime).getTime() - SDF.parse(startTime).getTime()) / 1000 / 60 / 60;

        String tableName = "";

        if(hourDiff <= 1){
            tableName = "location_" + DF.format(SDF.parse(endTime));
        }else if(hourDiff > 1  && hourDiff < 31 * 24){
            tableName = "location_history_day";
        } else if(hourDiff >= 31 * 24 && hourDiff < 365 * 24){
            tableName = "location_history_month";
        } else if(hourDiff >= 365 * 24){
            tableName = "location_history_year";
        }

        return tableName;
    }

    /**
     * 把以“,”分隔的字符串各加上“'”
     * @param str
     * @return
     */
    public String addQuot(String str){
        if(StringUtils.isBlank(str))
            return "''";
        return "'" + (str.contains(",") ? str.replaceAll(",", "','") : str) + "'";
    }


}
