package com.you07.vtp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import com.graphhopper.storage.GraphHopperStorage;
import com.graphhopper.storage.index.LocationIndex;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.you07.eas.model.StudentInfo;
import com.you07.eas.model.TeacherInfo;
import com.you07.eas.service.StudentInfoService;
import com.you07.eas.service.TeacherInfoService;
import com.you07.util.RestTemplateUtil;
import com.you07.util.message.MessageBean;
import com.you07.util.message.MessageListBean;
import com.you07.vtp.form.UserLocationForm;
import com.you07.vtp.model.LocationHistory;
import com.you07.vtp.model.LocationTrackManager;
import com.you07.vtp.model.vo.LocationExcelVO;
import com.you07.vtp.model.vo.LocationQueryVO;
import com.you07.vtp.service.LocationHitoryService;
import com.you07.vtp.service.LocationTrackManagerService;
import com.you07.vtp.vo.CoordinateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 位置、轨迹查询接口
 *
 * @author RY
 * @version 1.0
 */

@RestController
@CrossOrigin
@RequestMapping("/location")
@Api(value = "位置、轨迹查询接口controller", tags = {"位置、轨迹查询接口"})
public class LocationController {
    @Autowired
    private LocationHitoryService locationHitoryService;
    @Autowired
    private LocationTrackManagerService locationTrackManagerService;
    @Autowired
    private TeacherInfoService teacherInfoService;
    @Autowired
    private StudentInfoService studentInfoService;
    @Value("${vtp.track-distance}")
    private double distance;

    private Logger logger = LoggerFactory.getLogger(LocationController.class);

    private static GeometryCollection LINES = null;

    private static Map<String, GraphHopperStorage> GRAPH_HOPPER_STROAGE_MAP = new HashMap<>();
    private static Map<String, LocationIndex> LOCATION_INDEX_MAP = new HashMap<>();
    private static SimpleDateFormat SDF = new SimpleDateFormat("HH:mm");
    private static Double FORWARD_ANGEL_MIN = 15D;
    private static Double FORWARD_ANGEL_MAX = 165D;


    @ApiOperation("根据用户查询位置")
    @PostMapping("/loadUserLocation")
    @ResponseBody
    public String loadUserLocation(@Valid @RequestBody UserLocationForm form) throws ParseException {
        MessageListBean<LocationHistory> messageListBean = new MessageListBean<LocationHistory>();
            List<LocationHistory> list = locationHitoryService.selectByUserids(form);
            if (list.size() > 0) {
                messageListBean.setData(list);
                messageListBean.setStatus(true);
                messageListBean.setCode(200);
                messageListBean.setMessage("获取成功");
            } else {
                messageListBean.setStatus(false);
                messageListBean.setCode(10002);
                messageListBean.setMessage("没有查询到数据");
            }

        return JSON.toJSONString(messageListBean, SerializerFeature.DisableCircularReferenceDetect);
    }

    @ApiOperation("根据单用户查询位置")
    @GetMapping("/loadOneUserLocation")
    @ResponseBody
    public String loadOneUserLocation(@ApiParam(name = "userid", value = "用户id", required = true) @RequestParam("userid") String userid,
                                      @ApiParam(name = "range", value = "有效时间范围，单位分钟", required = true) @RequestParam("range") Integer range) throws ParseException {
        MessageBean<LocationHistory> messageListBean = new MessageBean<>();
        LocationHistory locationHistory = locationHitoryService.selectByUserid(userid);

        if (locationHistory != null && (System.currentTimeMillis() - locationHistory.getLocationTime().getTime()) / 1000 / 60 > range) {
            locationHistory = null;
        }

        if (locationHistory != null) {
            messageListBean.setData(locationHistory);
            messageListBean.setStatus(true);
            messageListBean.setCode(200);
            messageListBean.setMessage("获取成功");
        } else {
            messageListBean.setStatus(false);
            messageListBean.setCode(10002);
            messageListBean.setMessage("没有查询到数据");
        }


        return JSON.toJSONString(messageListBean, SerializerFeature.DisableCircularReferenceDetect);
    }

    @ApiOperation("根据组织机构查询位置")
    @GetMapping("/loadOrgLocation")
    @ResponseBody
    public String loadOrgLocation(@ApiParam(name = "orgCodes", value = "组织机构代码，多个以','分隔", required = true) @RequestParam("orgCodes") String orgCodes,
                                  @ApiParam(name = "startTime", value = "开始时间，格式：'yyyy-MM-dd HH:mm:ss'", required = true) @RequestParam("startTime") String startTime,
                                  @ApiParam(name = "endTime", value = "结束时间，格式：'yyyy-MM-dd HH:mm:ss'") @RequestParam(name = "endTime", required = false, defaultValue = "") String endTime,
                                  @ApiParam(name = "inSchool", value = "校内校外，1校内，2校外") @RequestParam("inSchool") Integer inSchool,
                                  @ApiParam(name = "campusId", value = "校区ID") @RequestParam("campusId") Integer campusId) throws ParseException {
        MessageListBean<LocationHistory> messageListBean = new MessageListBean<LocationHistory>();
        List<LocationHistory> list = locationHitoryService.selectByOrgCodes(orgCodes, startTime, endTime, inSchool, campusId);
        if (list.size() > 0) {
            messageListBean.setData(list);
            messageListBean.setStatus(true);
            messageListBean.setCode(200);
            messageListBean.setMessage("获取成功");
        } else {
            messageListBean.setStatus(false);
            messageListBean.setCode(10002);
            messageListBean.setMessage("没有查询到数据");
        }


        return JSON.toJSONString(messageListBean, SerializerFeature.DisableCircularReferenceDetect);
    }

    @ApiOperation("查询全校位置")
    @GetMapping("/loadAllLocation")
    @ResponseBody
    public String loadAllLocation(@ApiParam(name = "startTime", value = "开始时间，格式：'yyyy-MM-dd HH:mm:ss'", required = true) @RequestParam("startTime") String startTime,
                                  @ApiParam(name = "endTime", value = "结束时间，格式：'yyyy-MM-dd HH:mm:ss'", required = false) @RequestParam(name = "endTime", required = false, defaultValue = "") String endTime,
                                  @ApiParam(name = "inSchool", value = "校内校外，1校内，2校外", required = false) @RequestParam("inSchool") Integer inSchool,
                                  String managerId,
                                  @ApiParam(name = "campusId", value = "校区ID", required = false) @RequestParam("campusId") Integer campusId) throws ParseException {
        MessageListBean<LocationHistory> messageListBean = new MessageListBean<LocationHistory>();
        LocationTrackManager manager = locationTrackManagerService.get(managerId);
        if (manager == null)
            throw new NullPointerException("管理员不存在");
        List<LocationHistory> list = locationHitoryService.selectAll(startTime, endTime, inSchool, campusId);
        //权限判定
        List<String> orgs = Arrays.asList(manager.getOrgCodes().split(","));
        for (int i = 0; i < list.size(); i++) {
            LocationHistory history = list.get(i);
            if (!orgs.contains(history.getOrgCode())) {
                list.remove(i--);
            }
        }
        if (list.size() > 0) {
            messageListBean.setData(list);
            messageListBean.setStatus(true);
            messageListBean.setCode(200);
            messageListBean.setMessage("获取成功");
        } else {
            messageListBean.setStatus(false);
            messageListBean.setCode(10002);
            messageListBean.setMessage("没有查询到数据");
        }

        return JSON.toJSONString(messageListBean, SerializerFeature.DisableCircularReferenceDetect);
    }

    @ApiOperation("单用户查询轨迹")
    @GetMapping("/loadTrack")
    @ResponseBody
    public String loadTrack(@ApiParam(name = "userid", value = "用户ID", required = true) @RequestParam("userid") String userid,
                            @ApiParam(name = "startTime", value = "开始时间，格式：'yyyy-MM-dd HH:mm:ss'", required = true) @RequestParam("startTime") String startTime,
                            @ApiParam(name = "endTime", value = "结束时间，格式：'yyyy-MM-dd HH:mm:ss'", required = false) @RequestParam(name = "endTime", required = false, defaultValue = "") String endTime,
                            @ApiParam(name = "inSchool", value = "校内校外，1校内，2校外", required = false) @RequestParam("inSchool") Integer inSchool,
                            @ApiParam(name = "campusId", value = "校区ID", required = false) @RequestParam("campusId") Integer campusId,
                            @ApiParam(name = "managerId", value = "管理员ID", required = false) @RequestParam("managerId") String managerId) throws ParseException, JsonProcessingException {
        MessageListBean<LocationHistory> messageListBean = new MessageListBean<LocationHistory>();
        if (hasPrivilege(userid, managerId)) {
            List<LocationHistory> locationHistories = locationHitoryService.selectTrack(userid, startTime, endTime, inSchool, campusId);

            if (locationHistories.size() > 0) {

//                    locationHistoryList.add(locationHistories.get(0));
                //过滤距离小于x的点
                LocationHistory last = locationHistories.get(0);
                for (int i = 1; i < locationHistories.size(); i++) {
                    LocationHistory current = locationHistories.get(i);
                    if (getDistance(current, last) < distance)
                        locationHistories.remove(i--);
                    last = locationHistories.get(i);
                }

                List<CoordinateVO> coordinateVOS = new LinkedList<>();
                //卡尔曼滤波去躁柔化(由于偏差太大，暂时不使用该算法)
//                GeoTrackFilter filter = new GeoTrackFilter(1.0D);
//                locationHistories.forEach(h -> {
//                    filter.update_velocity2d(h.getLat(), h.getLng(), 0D);
//                    double[] latlon = filter.get_lat_long();
//                    h.setLat(latlon[0]);
//                    h.setLng(latlon[1]);
//                    coordinateVOS.add(new CoordinateVO(latlon));
//                });
                locationHistories.forEach(h -> coordinateVOS.add(new CoordinateVO(h)));

                //向cmgis请求路径规划
                if (coordinateVOS.size() == 0 || locationHistories.size() == 0 || StringUtils.isBlank(locationHistories.get(0).getZoneId())) {
                    messageListBean.setStatus(false);
                    messageListBean.setCode(10002);
                    messageListBean.setMessage("没有查询到数据");
                    return JSON.toJSONString(messageListBean, SerializerFeature.DisableCircularReferenceDetect);
                }
                String jsonArray = RestTemplateUtil.postJSONObjectFormCmGis("/map/route/v3/bind/road/" + campusId, coordinateVOS)
                        .getJSONArray("data").toJSONString();
                List<CoordinateVO> list = JSON.parseArray(jsonArray, CoordinateVO.class);
                List<Double[]> trackList = new LinkedList<>();
                list.forEach(c -> trackList.add(c.toArray()));

                messageListBean.setData(locationHistories);
                messageListBean.setStatus(true);
                messageListBean.setCode(200);
                messageListBean.addPropertie("track", trackList.toArray());
                messageListBean.setMessage("获取成功");
            } else {
                messageListBean.setStatus(false);
                messageListBean.setCode(10002);
                messageListBean.setMessage("没有查询到数据");
            }
        } else {
            messageListBean.setStatus(false);
            messageListBean.setCode(10003);
            messageListBean.setMessage("没有查看权限或者没有该用户");
        }

        return JSON.toJSONString(messageListBean, SerializerFeature.DisableCircularReferenceDetect);
    }

    @ApiOperation("根据班级代码查询位置未知学生")
    @GetMapping("/loadOrgUnknown")
    @ResponseBody
    public String loadOrgUnknown(@ApiParam(name = "orgCodes", value = "组织机构代码，多个以','分隔", required = true) @RequestParam("orgCodes") String orgCodes,
                                 @ApiParam(name = "startTime", value = "开始时间，格式：'yyyy-MM-dd HH:mm:ss'", required = true) @RequestParam("startTime") String startTime,
                                 @ApiParam(name = "endTime", value = "结束时间，格式：'yyyy-MM-dd HH:mm:ss'", required = false) @RequestParam(name = "endTime", required = false, defaultValue = "") String endTime,
                                 @ApiParam(name = "campusId", value = "校区ID", required = false) @RequestParam("campusId") Integer campusId
//                                 @RequestParam Integer page, @RequestParam Integer pageSize
    ) throws ParseException {
        try {
            MessageListBean<StudentInfo> messageListBean = new MessageListBean<StudentInfo>();
            List<StudentInfo> studentInfoList = studentInfoService.loadWithClassCodes(orgCodes);
            Map<String, StudentInfo> studentInfoMap = new HashMap<String, StudentInfo>();
            for (StudentInfo studentInfo : studentInfoList) {
                if (!studentInfoMap.containsKey(studentInfo.getStudentno())) {
                    studentInfoMap.put(studentInfo.getStudentno(), studentInfo);
                }
            }

            List<LocationHistory> inSchoolList = locationHitoryService.selectByOrgCodes(orgCodes, startTime, endTime, 1, campusId);
            List<LocationHistory> outSchoolList = locationHitoryService.selectByOrgCodes(orgCodes, startTime, endTime, 2, campusId);

            for (LocationHistory locationHistory : inSchoolList) {
                if (studentInfoMap.containsKey(locationHistory.getUserid())) {
                    studentInfoMap.remove(locationHistory.getUserid());
                }
            }

            for (LocationHistory locationHistory : outSchoolList) {
                if (studentInfoMap.containsKey(locationHistory.getUserid())) {
                    studentInfoMap.remove(locationHistory.getUserid());
                }
            }

            if (studentInfoMap.values().size() > 0) {
                messageListBean.setStatus(true);
                messageListBean.setCode(200);
                messageListBean.setMessage("获取成功");
                for (StudentInfo studentInfo : studentInfoMap.values()) {
                    messageListBean.addData(studentInfo);
                }
            } else {
                messageListBean.setStatus(false);
                messageListBean.setCode(10002);
                messageListBean.setMessage("没有查询到数据");
            }


            return JSON.toJSONString(messageListBean, SerializerFeature.DisableCircularReferenceDetect);
        }catch (Exception e) {
            return "未知异常";
        }
    }

    /**
     * 根据校区和分组查询分组下所有人员最后位置信息
     */
    @GetMapping("/loadUserLocationWithGroup")
    @ResponseBody
    public String loadUserLocationWithGroup(@RequestParam(name = "groupName",required = false) String groupName,
                                            @RequestParam("startTime") String startTime,
                                            @RequestParam("endTime") String endTime,
                                            @RequestParam("campusCode") Integer campusCode,
                                            @RequestParam("page")Integer page,
                                            @RequestParam("pageSize")Integer pageSize) throws ParseException {
        MessageBean<PageInfo<LocationHistory>> messageListBean = new MessageBean<>();
        PageInfo<LocationHistory> pageInfo = locationHitoryService.selectByGroupIds(groupName,startTime,endTime,campusCode,page,pageSize);


        if (pageInfo.getList().size() > 0) {
            messageListBean.setData(pageInfo);
            messageListBean.setStatus(true);
            messageListBean.setCode(200);
            messageListBean.setMessage("获取成功");
        } else {
            messageListBean.setStatus(false);
            messageListBean.setCode(10002);
            messageListBean.setMessage("没有查询到数据");
        }


        return JSON.toJSONString(messageListBean, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 获取指定用户活动范围
     */
    @GetMapping("/loadUserGeomWithTimeZone")
    @ResponseBody
    public String loadUserGeomWithTimeZone(@RequestParam(name = "userId") String userId,
                                            @RequestParam("startTime") String startTime,
                                            @RequestParam("endTime") String endTime,
                                            @RequestParam("campusCode") Integer campusCode) throws ParseException {
        MessageBean<Object> messageListBean = new MessageBean<>();
        Object result = locationHitoryService.selectUserTrackWithTimeZone(userId,startTime,endTime,campusCode);


        if (result != null) {
            messageListBean.setData(result);
            messageListBean.setStatus(true);
            messageListBean.setCode(200);
            messageListBean.setMessage("获取成功");
        } else {
            messageListBean.setStatus(false);
            messageListBean.setCode(10002);
            messageListBean.setMessage("没有查询到数据");
        }
        return JSON.toJSONString(messageListBean);
    }

    /**
     * 根据活动范围获取该范围内受影响人员列表
     */
    @PostMapping("/loadEffectUserWithTrack")
    @ResponseBody
    public String loadEffectUserWithTrack(@RequestBody LocationQueryVO locationQueryVO) throws ParseException {
        MessageBean<PageInfo<LocationHistory>> messageListBean = new MessageBean<>();

        PageInfo<LocationHistory> list = locationHitoryService.loadEffectUserWithTrack(locationQueryVO);

        if (list.getList().size() > 0) {
            messageListBean.setData(list);
            messageListBean.setStatus(true);
            messageListBean.setCode(200);
            messageListBean.setMessage("获取成功");
        } else {
            messageListBean.setStatus(false);
            messageListBean.setCode(10002);
            messageListBean.setMessage("没有查询到数据");
        }

        return JSON.toJSONString(messageListBean, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 导出疑似受影响人员
     * @throws IOException IO异常
     */
    @ApiOperation("导出疑似受影响人员")
    @PostMapping("/download")
    @ResponseBody
    public ResponseEntity<StreamingResponseBody> download(@RequestBody LocationQueryVO locationQueryVO, OutputStream os) throws IOException,ParseException{
        return locationHitoryService.download(locationQueryVO,os);
    }


    /**
     * 根据查询用户ID、管理员ID判断是否有权限查看
     * 因为查询多用户时，是在有权限的用户中进行选择
     * 所以只需要判断查询单用户时的权限
     *
     * @param userid
     * @param managerId
     * @return
     */
    public Boolean hasPrivilege(String userid, String managerId) {

//         传入的用户账号为单个
        if (!userid.contains(",")) {
            // 检查查询用户的信息
            StudentInfo studentInfo = studentInfoService.get(userid);
            TeacherInfo teacherInfo = teacherInfoService.get(userid);
            // 定义用户组织机构代码
            String orgCode = null;
            if (studentInfo != null) {
                orgCode = studentInfo.getClassCode();
            }
            if (teacherInfo != null) {
                orgCode = teacherInfo.getOrgCode();
            }
            // 要查的用户不为空，且能正确查到所属组织机构
            if (orgCode != null) {
                // 获取管理员
                LocationTrackManager manager = locationTrackManagerService.get(managerId);
                // 管理员不为空，且权限不为空
                if (manager != null && manager.getOrgCodes() != null) {
                    String privilegeOrgCodes = manager.getOrgCodes();
                    // 管理员权限组织机构代码包含查询用户的组织机构代码
                    // 才返回成功
                    return privilegeOrgCodes.contains(orgCode);
                } else {
                    return false;
                }

            } else {
                return false;
            }

        } else {
            return true;
        }
    }

    private double getDistance(LocationHistory h1, LocationHistory h2) {
        final  double EARTH_RADIUS = 6371000;//赤道半径(单位m)
        final  double INTEGR_NUM = 10000;

        double lat1 = h1.getLat(), lng1 = h1.getLng(), lat2 = h2.getLat(), lng2 = h2.getLng();
        double x1 = Math.cos(lat1) * Math.cos(lng1);
        double y1 = Math.cos(lat1) * Math.sin(lng1);
        double z1 = Math.sin(lat1);
        double x2 = Math.cos(lat2) * Math.cos(lng2);
        double y2 = Math.cos(lat2) * Math.sin(lng2);
        double z2 = Math.sin(lat2);
        double lineDistance =
                Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2));
        double s = EARTH_RADIUS * Math.PI * 2 * Math.asin(0.5 * lineDistance) / 180;
        return Math.round(s * INTEGR_NUM) / INTEGR_NUM;
    }
}
