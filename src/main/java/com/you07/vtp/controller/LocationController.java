package com.you07.vtp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphhopper.storage.GraphHopperStorage;
import com.graphhopper.storage.index.LocationIndex;
import com.vividsolutions.jts.algorithm.Angle;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.you07.eas.model.StudentInfo;
import com.you07.eas.model.TeacherInfo;
import com.you07.eas.service.StudentInfoService;
import com.you07.eas.service.TeacherInfoService;
import com.you07.map.filter.GeoTrackFilter;
import com.you07.util.RestTemplateUtil;
import com.you07.util.message.MessageBean;
import com.you07.util.message.MessageListBean;
import com.you07.util.route.Routing;
import com.you07.vtp.model.LocationHistory;
import com.you07.vtp.model.LocationTrackManager;
import com.you07.vtp.service.LocationHitoryService;
import com.you07.vtp.service.LocationTrackManagerService;
import com.you07.vtp.vo.CoordinateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;

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

    private static GeometryCollection LINES = null;

    private static Map<String, GraphHopperStorage> GRAPH_HOPPER_STROAGE_MAP = new HashMap<>();
    private static Map<String, LocationIndex> LOCATION_INDEX_MAP = new HashMap<>();
    private static SimpleDateFormat SDF = new SimpleDateFormat("HH:mm");
    private static Double FORWARD_ANGEL_MIN = 15D;
    private static Double FORWARD_ANGEL_MAX = 165D;


    @ApiOperation("根据用户查询位置")
    @GetMapping("/loadUserLocation")
    @ResponseBody
    public String loadUserLocation(@ApiParam(name = "userids", value = "用户id，多个以','分隔", required = true) @RequestParam("userids") String userids,
                                   @ApiParam(name = "startTime", value = "开始时间，格式：'yyyy-MM-dd HH:mm:ss'", required = true) @RequestParam("startTime") String startTime,
                                   @ApiParam(name = "endTime", value = "结束时间，格式：'yyyy-MM-dd HH:mm:ss'", required = false) @RequestParam(name = "endTime", required = false, defaultValue = "") String endTime,
                                   @ApiParam(name = "inSchool", value = "校内校外，1校内，2校外", required = false) @RequestParam("inSchool") Integer inSchool,
                                   @ApiParam(name = "campusId", value = "校区ID", required = false) @RequestParam("campusId") Integer campusId,
                                   @ApiParam(name = "managerId", value = "管理员ID", required = false) @RequestParam("managerId") String managerId) {
        MessageListBean<LocationHistory> messageListBean = new MessageListBean<LocationHistory>();
        try {
            if (hasPrivilege(userids, managerId)) {
                List<LocationHistory> list = locationHitoryService.selectByUserids(userids, startTime, endTime, inSchool, campusId);
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
            } else {
                messageListBean.setStatus(false);
                messageListBean.setCode(10003);
                messageListBean.setMessage("没有查看权限或者没有该用户");
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageListBean.setStatus(false);
            messageListBean.setCode(10001);
            messageListBean.setMessage("接口错误");
        }

        return JSON.toJSONString(messageListBean, SerializerFeature.DisableCircularReferenceDetect);
    }

    @ApiOperation("根据单用户查询位置")
    @GetMapping("/loadOneUserLocation")
    @ResponseBody
    public String loadOneUserLocation(@ApiParam(name = "userid", value = "用户id", required = true) @RequestParam("userid") String userid,
                                      @ApiParam(name = "range", value = "有效时间范围，单位分钟", required = true) @RequestParam("range") Integer range) {
        MessageBean<LocationHistory> messageListBean = new MessageBean<>();
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            messageListBean.setStatus(false);
            messageListBean.setCode(10001);
            messageListBean.setMessage("接口错误");
        }

        return JSON.toJSONString(messageListBean, SerializerFeature.DisableCircularReferenceDetect);
    }

    @ApiOperation("根据组织机构查询位置")
    @GetMapping("/loadOrgLocation")
    @ResponseBody
    public String loadOrgLocation(@ApiParam(name = "orgCodes", value = "组织机构代码，多个以','分隔", required = true) @RequestParam("orgCodes") String orgCodes,
                                  @ApiParam(name = "startTime", value = "开始时间，格式：'yyyy-MM-dd HH:mm:ss'", required = true) @RequestParam("startTime") String startTime,
                                  @ApiParam(name = "endTime", value = "结束时间，格式：'yyyy-MM-dd HH:mm:ss'", required = false) @RequestParam(name = "endTime", required = false, defaultValue = "") String endTime,
                                  @ApiParam(name = "inSchool", value = "校内校外，1校内，2校外", required = false) @RequestParam("inSchool") Integer inSchool,
                                  @ApiParam(name = "campusId", value = "校区ID", required = false) @RequestParam("campusId") Integer campusId) {
        MessageListBean<LocationHistory> messageListBean = new MessageListBean<LocationHistory>();
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            messageListBean.setStatus(false);
            messageListBean.setCode(10001);
            messageListBean.setMessage("接口错误");
        }

        return JSON.toJSONString(messageListBean, SerializerFeature.DisableCircularReferenceDetect);
    }

    @ApiOperation("查询全校位置")
    @GetMapping("/loadAllLocation")
    @ResponseBody
    public String loadAllLocation(@ApiParam(name = "startTime", value = "开始时间，格式：'yyyy-MM-dd HH:mm:ss'", required = true) @RequestParam("startTime") String startTime,
                                  @ApiParam(name = "endTime", value = "结束时间，格式：'yyyy-MM-dd HH:mm:ss'", required = false) @RequestParam(name = "endTime", required = false, defaultValue = "") String endTime,
                                  @ApiParam(name = "inSchool", value = "校内校外，1校内，2校外", required = false) @RequestParam("inSchool") Integer inSchool,
                                  @ApiParam(name = "campusId", value = "校区ID", required = false) @RequestParam("campusId") Integer campusId) {
        MessageListBean<LocationHistory> messageListBean = new MessageListBean<LocationHistory>();
        try {
            List<LocationHistory> list = locationHitoryService.selectAll(startTime, endTime, inSchool, campusId);
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
        } catch (Exception e) {
            e.printStackTrace();
            messageListBean.setStatus(false);
            messageListBean.setCode(10001);
            messageListBean.setMessage("接口错误");
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
                            @ApiParam(name = "managerId", value = "管理员ID", required = false) @RequestParam("managerId") String managerId) {
        MessageListBean<LocationHistory> messageListBean = new MessageListBean<LocationHistory>();
        try {
            if (hasPrivilege(userid, managerId)) {
                List<LocationHistory> locationHistories = locationHitoryService.selectTrack(userid, startTime, endTime, inSchool, campusId);

                if (locationHistories.size() > 0) {

//                    locationHistoryList.add(locationHistories.get(0));
                    //过滤连续重复点
                    LocationHistory last = locationHistories.get(0);
                    for (int i = 1; i < locationHistories.size(); ) {
                        LocationHistory current = locationHistories.get(i);
                        if (last.getLat().equals(current.getLat()) && last.getLng().equals(current.getLng())) {
                            locationHistories.remove(current);
                        } else {
                            last = current;
                            i++;
                        }
                    }

                    //卡尔曼滤波去躁柔化
                    List<CoordinateVO> coordinateVOS = new LinkedList<>();
                    GeoTrackFilter filter = new GeoTrackFilter(1.0D);
                    locationHistories.forEach(h -> {
                        filter.update_velocity2d(h.getLat(), h.getLng(), 0D);
                        double[] latlon = filter.get_lat_long();
                        h.setLat(latlon[0]);
                        h.setLng(latlon[1]);
                        coordinateVOS.add(new CoordinateVO(latlon));
                    });

                    //向cmgis请求路径规划
                    String jsonArray = RestTemplateUtil.postJSONObjectFormCmGis("/map/route/v3/bind/road/"+locationHistories.get(0).getZoneId(), coordinateVOS).getJSONArray("data").toJSONString();
                    List<CoordinateVO> list = JSON.parseArray(jsonArray, CoordinateVO.class);
                    List<Double[]> trackList = new LinkedList<>();
                    list.forEach(c->trackList.add(c.toArray()));

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
        } catch (Exception e) {
            e.printStackTrace();
            messageListBean.setStatus(false);
            messageListBean.setCode(10001);
            messageListBean.setMessage("接口错误");
        }

        return JSON.toJSONString(messageListBean, SerializerFeature.DisableCircularReferenceDetect);
    }

    @ApiOperation("根据班级代码查询位置未知学生")
    @GetMapping("/loadOrgUnknown")
    @ResponseBody
    public String loadOrgUnknown(@ApiParam(name = "orgCodes", value = "组织机构代码，多个以','分隔", required = true) @RequestParam("orgCodes") String orgCodes,
                                 @ApiParam(name = "startTime", value = "开始时间，格式：'yyyy-MM-dd HH:mm:ss'", required = true) @RequestParam("startTime") String startTime,
                                 @ApiParam(name = "endTime", value = "结束时间，格式：'yyyy-MM-dd HH:mm:ss'", required = false) @RequestParam(name = "endTime", required = false, defaultValue = "") String endTime,
                                 @ApiParam(name = "campusId", value = "校区ID", required = false) @RequestParam("campusId") Integer campusId) {
        MessageListBean<StudentInfo> messageListBean = new MessageListBean<StudentInfo>();
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            messageListBean.setStatus(false);
            messageListBean.setCode(10001);
            messageListBean.setMessage("接口错误");
        }

        return JSON.toJSONString(messageListBean, SerializerFeature.DisableCircularReferenceDetect);
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
            if (studentInfo != null && studentInfo.getClassInfo() != null) {
                orgCode = studentInfo.getClassInfo().getClasscode();
            }
            if (teacherInfo != null && teacherInfo.getDepartmentInfo() != null) {
                orgCode = teacherInfo.getDepartmentInfo().getXsbmdm();
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
                    if (privilegeOrgCodes.contains(orgCode + ",") || privilegeOrgCodes.contains("," + orgCode)) {
                        return true;
                    } else {
                        return false;
                    }
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
}
