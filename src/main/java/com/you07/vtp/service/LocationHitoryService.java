package com.you07.vtp.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.you07.vtp.dao.LocationHistoryDao;
import com.you07.vtp.dao.SsGroupDao;
import com.you07.vtp.form.UserLocationForm;
import com.you07.vtp.model.LocationHistory;
import com.you07.vtp.model.SsGroup;
import com.you07.vtp.model.vo.DateUtils;
import com.you07.vtp.model.vo.LocationExcelVO;
import com.you07.vtp.model.vo.LocationQueryVO;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LocationHitoryService {
    @Autowired
    private LocationHistoryDao locationHistoryDao;
    @Autowired
    private SsGroupDao groupDao;

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
                return locationHistoryDao.selectByUserids(addQuot(form.getKeyWord()), addQuot(form.getKeyWord()), addQuot(form.getKeyWord()), addQuot(form.getOrgCodes()), addQuot(form.getClassCodes()), addQuot(form.getNation()), addQuot(form.getBirthplace()),
                        form.getInSchool(), form.getCampusId());

        } else{
            if (StringUtils.isBlank(locationHistoryDao.selectTableName(tableName)))
                return new ArrayList<>();
            /*return locationHistoryDao.selectByUseridsTimeZone(addQuot(form.getUserIds()),addQuot(form.getOrgCodes()), addQuot(form.getClassCodes()), addQuot(form.getNation()), addQuot(form.getBirthplace()),
                    getTableName(startTime, endTime), startTime, endTime, form.getInSchool(), form.getCampusId());*/
            return locationHistoryDao.selectByUserInfoTimeZone(addQuot(form.getUserIds()), addQuot(form.getKeyWord()),
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
    public List<LocationHistory> selectByOrgCodes(String orgCodes, String startTime, String endTime, Integer inSchool, Integer campusId) throws ParseException {
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
     * 根据分组编号、时间范围
     * 查询分组用户最新位置
     * @param groupName
     * @param startTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public PageInfo<LocationHistory> selectByGroupIds(String groupName, String startTime, String endTime, Integer campusId, Integer page, Integer pageSize) throws ParseException {

        List<SsGroup> groupList = groupDao.queryAll(groupName);

        StringBuilder userIds = new StringBuilder();
        for (SsGroup group : groupList) {
            if(group.getIdString() != null){
                userIds.append(Arrays.asList(group.getIdString().split(","))
                        .stream()
                        .map(v ->"'" + v + "'")
                        .collect(Collectors.joining(",")));
            }
        }
        PageHelper.startPage(page,pageSize);
        List<LocationHistory> userLocationList = new ArrayList<>();
        if(!"".equals(userIds.toString().trim())){
            userLocationList = locationHistoryDao.selectLastLngByUserIds(userIds.toString(),getTableName(startTime, endTime),startTime,endTime,campusId);
            userLocationList.forEach(v ->{
                SsGroup group = groupDao.existWithUserId(v.getUserid());
                if(group != null){
                    v.setIcon(group.getIcon());
                }
            });
        }
        return new PageInfo<LocationHistory>(userLocationList);

    }
    /**
     * 根据时间范围
     * 查询用户轨迹
     * @param startTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public Object selectUserTrackWithTimeZone(String userId,String startTime, String endTime, Integer campusId) throws ParseException {

//        List<LocationHistory> locationHistoryList = locationHistoryDao.selectUserTrackWithTimeZone(userId,getTableName(startTime, endTime), startTime, endTime, campusId);
//        StringBuilder multiPoint = new StringBuilder();
//        if(locationHistoryList.size() > 0){
//            for (LocationHistory history : locationHistoryList) {
//                multiPoint .append(history.getLng())
//                        .append(" ")
//                        .append(history.getLat())
//                        .append(",");
//            }
//        }
//        String multiPointString = "multipoint(" + multiPoint.deleteCharAt(multiPoint.length() - 1) + ")";

        return JSON.parseObject(locationHistoryDao.queryRangeOfActivities(userId,getTableName(startTime, endTime), startTime, endTime, campusId));
    }

    /**
     * 根据范围
     * 查询疑似受影响人员
     * @return
     * @throws ParseException
     */
    public PageInfo<LocationHistory> loadEffectUserWithTrack(LocationQueryVO locationQueryVO) throws ParseException {

        List<LocationHistory> locationHistoryList = locationHistoryDao.selectEffectUserWithTrack(JSON.toJSONString(locationQueryVO.getGeojson()),
                getTableName(locationQueryVO.getStartTime(), locationQueryVO.getEndTime()), locationQueryVO.getStartTime(), locationQueryVO.getEndTime(), locationQueryVO.getCampusCode());

        List<String> userIds = new ArrayList<>();
        if(locationHistoryList.size() > 0){
            for (LocationHistory history : locationHistoryList) {
                userIds.add(history.getUserid());
            }
        }
        String userIdArray = "'" + userIds.stream().collect(Collectors.joining("','")) + "'";
        PageHelper.startPage(locationQueryVO.getPage(),locationQueryVO.getPageSize());

        List<LocationHistory> lastLocationList = locationHistoryDao.selectBulkUserLocation(userIdArray);

        return new PageInfo<LocationHistory>(lastLocationList);
    }

    /**
     * 导出疑似受影响人员
     */
    public ResponseEntity<StreamingResponseBody> download(LocationQueryVO locationQueryVO, OutputStream os) throws IOException,ParseException {

        List<LocationHistory> locationHistoryList = locationHistoryDao.selectEffectUserWithTrack(JSON.toJSONString(locationQueryVO.getGeojson()),
                getTableName(locationQueryVO.getStartTime(), locationQueryVO.getEndTime()), locationQueryVO.getStartTime(), locationQueryVO.getEndTime(), locationQueryVO.getCampusCode());

        StreamingResponseBody body =(outputStream -> {

            SXSSFWorkbook workbook = new SXSSFWorkbook(10);
            Sheet sheet = workbook.createSheet();

            //设置头
            Row rootRow = sheet.createRow(0);
            rootRow.createCell(0).setCellValue("学工号");
            rootRow.createCell(1).setCellValue("姓名");
            rootRow.createCell(2).setCellValue("组织机构名称");
            rootRow.createCell(3).setCellValue("最后经过时间");
            for (int i = 0; i < locationHistoryList.size(); i++) {
                LocationHistory history = JSONObject.parseObject(JSONObject.toJSONString(locationHistoryList.get(i)), LocationHistory.class);
                Row dataRow = sheet.createRow(i + 1);
                dataRow.createCell(0).setCellValue(history.getUserid());
                dataRow.createCell(1).setCellValue(history.getRealname());
                dataRow.createCell(2).setCellValue(history.getOrgName());
                dataRow.createCell(3).setCellValue(history.getLocationTime());
                dataRow.createCell(3).setCellValue(DateUtils.getDefaultInstance().formatDate((Date) history.getLocationTime(),"yyyy-MM-dd HH:mm:ss"));
            }
            workbook.write(os);
            workbook.dispose();
        });

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment;filename="+ "疑似受影响人员.xlsx")
                .body(body);

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

    /**
     * 转换Location对象为Excel对象
     */
    private  List<LocationExcelVO> transformHistoryToExcel(List<LocationHistory> locationHistoryList){
        List<LocationExcelVO> excelVOList = new ArrayList<>();
        locationHistoryList.forEach(v ->{
            LocationExcelVO excelVO = new LocationExcelVO();
            excelVO.setLocationTime(v.getLocationTime());
            excelVO.setOrgName(v.getOrgName());
            excelVO.setRealName(v.getRealname());
            excelVO.setUserId(v.getUserid());
            excelVOList.add(excelVO);
        });
        return excelVOList;
    }



}
