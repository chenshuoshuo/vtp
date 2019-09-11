package com.you07.vtp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.graphhopper.storage.GraphHopperStorage;
import com.graphhopper.storage.index.LocationIndex;
import com.vividsolutions.jts.algorithm.Angle;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.you07.eas.model.StudentInfo;
import com.you07.eas.model.TeacherInfo;
import com.you07.eas.service.StudentInfoService;
import com.you07.eas.service.TeacherInfoService;
import com.you07.util.message.MessageBean;
import com.you07.util.message.MessageListBean;
import com.you07.util.route.Routing;
import com.you07.vtp.model.LocationHistory;
import com.you07.vtp.model.LocationTrackManager;
import com.you07.vtp.service.LocationHitoryService;
import com.you07.vtp.service.LocationTrackManagerService;
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
 * @version 1.0
 * @author RY
 */

@RestController
@CrossOrigin
@RequestMapping("/location")
@Api(value="位置、轨迹查询接口controller",tags={"位置、轨迹查询接口"})
public class LocationController {
    @Autowired
    private LocationHitoryService locationHitoryService;
    @Autowired
    private StudentInfoService studentInfoService;
    @Autowired
    private LocationTrackManagerService locationTrackManagerService;
    @Autowired
    private TeacherInfoService teacherInfoService;

    private static GeometryCollection LINES = null;

    private static Map<String, GraphHopperStorage> GRAPH_HOPPER_STROAGE_MAP = new HashMap<>();
    private static Map<String, LocationIndex> LOCATION_INDEX_MAP = new HashMap<>();
    private static SimpleDateFormat SDF = new SimpleDateFormat("HH:mm");
    private static Double FORWARD_ANGEL_MIN = 15D;
    private static Double FORWARD_ANGEL_MAX = 165D;


    @ApiOperation("根据用户查询位置")
    @GetMapping("/loadUserLocation")
    @ResponseBody
    public String loadUserLocation(@ApiParam(name="userids",value="用户id，多个以','分隔",required=true) @RequestParam("userids") String userids,
                             @ApiParam(name="startTime",value="开始时间，格式：'yyyy-MM-dd HH:mm:ss'",required=true) @RequestParam("startTime") String startTime,
                             @ApiParam(name="endTime",value="结束时间，格式：'yyyy-MM-dd HH:mm:ss'",required=false) @RequestParam(name = "endTime", required = false, defaultValue = "") String endTime,
                             @ApiParam(name="inSchool",value="校内校外，1校内，2校外",required=false) @RequestParam("inSchool") Integer inSchool,
                               @ApiParam(name="campusId",value="校区ID",required=false) @RequestParam("campusId") Integer campusId,
                               @ApiParam(name="managerId",value="管理员ID",required=false) @RequestParam("managerId") String managerId){
        MessageListBean<LocationHistory> messageListBean = new MessageListBean<LocationHistory>();
        try {
            if(hasPrivilege(userids, managerId)){
                List<LocationHistory> list = locationHitoryService.selectByUserids(userids, startTime, endTime, inSchool, campusId);
                if(list.size() > 0){
                    messageListBean.setData(list);
                    messageListBean.setStatus(true);
                    messageListBean.setCode(200);
                    messageListBean.setMessage("获取成功");
                } else{
                    messageListBean.setStatus(false);
                    messageListBean.setCode(10002);
                    messageListBean.setMessage("没有查询到数据");
                }
            } else{
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
    public String loadOneUserLocation(@ApiParam(name="userid",value="用户id",required=true) @RequestParam("userid") String userid,
                                      @ApiParam(name="range",value="有效时间范围，单位分钟",required=true) @RequestParam("range") Integer range){
        MessageBean<LocationHistory> messageListBean = new MessageBean<LocationHistory>();
        try {
            LocationHistory locationHistory = locationHitoryService.selectByUserid(userid);

            if(locationHistory != null && (System.currentTimeMillis() - locationHistory.getLocationTime().getTime()) / 1000 / 60 > range){
                locationHistory = null;
            }

            if(locationHistory != null){
                messageListBean.setData(locationHistory);
                messageListBean.setStatus(true);
                messageListBean.setCode(200);
                messageListBean.setMessage("获取成功");
            } else{
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
    public String loadOrgLocation(@ApiParam(name="orgCodes",value="组织机构代码，多个以','分隔",required=true) @RequestParam("orgCodes") String orgCodes,
                             @ApiParam(name="startTime",value="开始时间，格式：'yyyy-MM-dd HH:mm:ss'",required=true) @RequestParam("startTime") String startTime,
                             @ApiParam(name="endTime",value="结束时间，格式：'yyyy-MM-dd HH:mm:ss'",required=false) @RequestParam(name = "endTime", required = false, defaultValue = "") String endTime,
                                  @ApiParam(name="inSchool",value="校内校外，1校内，2校外",required=false) @RequestParam("inSchool") Integer inSchool,
                                  @ApiParam(name="campusId",value="校区ID",required=false) @RequestParam("campusId") Integer campusId){
        MessageListBean<LocationHistory> messageListBean = new MessageListBean<LocationHistory>();
        try {
            List<LocationHistory> list = locationHitoryService.selectByOrgCodes(orgCodes, startTime, endTime, inSchool, campusId);
            if(list.size() > 0){
                messageListBean.setData(list);
                messageListBean.setStatus(true);
                messageListBean.setCode(200);
                messageListBean.setMessage("获取成功");
            } else{
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
    public String loadAllLocation(@ApiParam(name="startTime",value="开始时间，格式：'yyyy-MM-dd HH:mm:ss'",required=true) @RequestParam("startTime") String startTime,
                                  @ApiParam(name="endTime",value="结束时间，格式：'yyyy-MM-dd HH:mm:ss'",required=false) @RequestParam(name = "endTime", required = false, defaultValue = "") String endTime,
                                  @ApiParam(name="inSchool",value="校内校外，1校内，2校外",required=false) @RequestParam("inSchool") Integer inSchool,
                                  @ApiParam(name="campusId",value="校区ID",required=false) @RequestParam("campusId") Integer campusId){
        MessageListBean<LocationHistory> messageListBean = new MessageListBean<LocationHistory>();
        try {
            List<LocationHistory> list = locationHitoryService.selectAll(startTime, endTime, inSchool, campusId);
            if(list.size() > 0){
                messageListBean.setData(list);
                messageListBean.setStatus(true);
                messageListBean.setCode(200);
                messageListBean.setMessage("获取成功");
            } else{
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
    public String loadTrack(@ApiParam(name="userid",value="用户ID",required=true) @RequestParam("userid") String userid,
                                  @ApiParam(name="startTime",value="开始时间，格式：'yyyy-MM-dd HH:mm:ss'",required=true) @RequestParam("startTime") String startTime,
                                  @ApiParam(name="endTime",value="结束时间，格式：'yyyy-MM-dd HH:mm:ss'",required=false) @RequestParam(name = "endTime", required = false, defaultValue = "") String endTime,
                                  @ApiParam(name="inSchool",value="校内校外，1校内，2校外",required=false) @RequestParam("inSchool") Integer inSchool,
                                @ApiParam(name="campusId",value="校区ID",required=false) @RequestParam("campusId") Integer campusId,
                                @ApiParam(name="managerId",value="管理员ID",required=false) @RequestParam("managerId") String managerId){
        MessageListBean<LocationHistory> messageListBean = new MessageListBean<LocationHistory>();
        try {
            if(hasPrivilege(userid,  managerId)){
                List<LocationHistory> list = locationHitoryService.selectTrack(userid, startTime, endTime, inSchool, campusId);

                if(list.size() > 0){
                    ArrayList<Double[]> arrayList = new ArrayList<>();
                    List<LocationHistory> locationHistoryList = new ArrayList<>();

                    locationHistoryList.add(list.get(0));

                    for(int i = 1; i < list.size(); i++){
                        LocationHistory startLocation = locationHistoryList.get(locationHistoryList.size() - 1);
                        LocationHistory endLocation = list.get(i);

                        if(!startLocation.getLng().equals(endLocation.getLng()) || !startLocation.getLat().equals(endLocation.getLat())
                                || !startLocation.getFloorid().equals(endLocation.getFloorid())){

                            List<Routing> routings = findRoutePath(campusId.toString(), startLocation.getLat(), startLocation.getLng(), endLocation.getLat(), endLocation.getLng());

                            if(routings != null && routings.size() > 0){
                                for(int j = 0; j < routings.size(); j++){
                                    if(routings.get(j).getPointList().size() > 1){
                                        arrayList.addAll( routings.get(j).getPointList());
                                        locationHistoryList.add(endLocation);
                                    }

                                }
                            }

                        }
                    }

                    ArrayList<Double[]> trackList = new ArrayList<>();
                    if(arrayList.size() > 0){
                        trackList.add(arrayList.get(0));
                        for(int k = 1; k < arrayList.size(); k++){
                            Double[] lastPoint = arrayList.get(k - 1);
                            Double[] thisPoint = arrayList.get(k);

                            if(!Arrays.equals(lastPoint, thisPoint)){
                                trackList.add(thisPoint);
                            }
                        }
                    }

                    messageListBean.setData(locationHistoryList);
                    messageListBean.setStatus(true);
                    messageListBean.setCode(200);
                    messageListBean.addPropertie("track", trackList.toArray());
                    messageListBean.setMessage("获取成功");
                } else{
                    messageListBean.setStatus(false);
                    messageListBean.setCode(10002);
                    messageListBean.setMessage("没有查询到数据");
                }
            } else{
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
    public String loadOrgUnknown(@ApiParam(name="orgCodes",value="组织机构代码，多个以','分隔",required=true) @RequestParam("orgCodes") String orgCodes,
                                  @ApiParam(name="startTime",value="开始时间，格式：'yyyy-MM-dd HH:mm:ss'",required=true) @RequestParam("startTime") String startTime,
                                  @ApiParam(name="endTime",value="结束时间，格式：'yyyy-MM-dd HH:mm:ss'",required=false) @RequestParam(name = "endTime", required = false, defaultValue = "") String endTime,
                                 @ApiParam(name="campusId",value="校区ID",required=false) @RequestParam("campusId") Integer campusId){
        MessageListBean<StudentInfo> messageListBean = new MessageListBean<StudentInfo>();
        try {
            List<StudentInfo> studentInfoList = studentInfoService.loadWithClassCodes(orgCodes);
            Map<String ,StudentInfo> studentInfoMap = new HashMap<String ,StudentInfo>();
            for(StudentInfo studentInfo : studentInfoList){
                if(!studentInfoMap.containsKey(studentInfo.getStudentno())){
                    studentInfoMap.put(studentInfo.getStudentno(), studentInfo);
                }
            }


            List<LocationHistory> inSchoolList = locationHitoryService.selectByOrgCodes(orgCodes, startTime, endTime, 1, campusId);
            List<LocationHistory> outSchoolList = locationHitoryService.selectByOrgCodes(orgCodes, startTime, endTime, 2, campusId);

            for(LocationHistory locationHistory :inSchoolList){
                if(studentInfoMap.containsKey(locationHistory.getUserid())){
                    studentInfoMap.remove(locationHistory.getUserid());
                }
            }

            for(LocationHistory locationHistory :outSchoolList){
                if(studentInfoMap.containsKey(locationHistory.getUserid())){
                    studentInfoMap.remove(locationHistory.getUserid());
                }
            }

            if(studentInfoMap.values().size() > 0){
                messageListBean.setStatus(true);
                messageListBean.setCode(200);
                messageListBean.setMessage("获取成功");
                for(StudentInfo studentInfo : studentInfoMap.values()){
                    messageListBean.addData(studentInfo);
                }
            } else{
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
     * 路径规划（修改，由cmgis提供）
     * @param mapid
     * @return
     */
    private List<Routing> findRoutePath(String mapid,
                                        Double startLat,
                                        Double startLng,
                                        Double endLat,
                                        Double endLng) {
        return null;
    }

    /**
     * 根据查询用户ID、管理员ID判断是否有权限查看
     * 因为查询多用户时，是在有权限的用户中进行选择
     * 所以只需要判断查询单用户时的权限
     * @param userid
     * @param managerId
     * @return
     */
    public Boolean hasPrivilege(String userid, String managerId){
        // 传入的用户账号为单个
        if(!userid.contains(",")){
            // 检查查询用户的信息
            StudentInfo studentInfo = studentInfoService.get(userid);
            TeacherInfo teacherInfo = teacherInfoService.get(userid);
            // 定义用户组织机构代码
            String orgCode = null;
            if(studentInfo != null && studentInfo.getClassInfo() != null){
                orgCode = studentInfo.getClassInfo().getClasscode();
            }
            if(teacherInfo != null && teacherInfo.getDepartmentInfo() != null){
                orgCode = teacherInfo.getDepartmentInfo().getXsbmdm();
            }
            // 要查的用户不为空，且能正确查到所属组织机构
            if(orgCode != null){
                // 获取管理员
                LocationTrackManager manager = locationTrackManagerService.get(managerId);
                // 管理员不为空，且权限不为空
                if(manager != null && manager.getOrgCodes() != null){
                    String privilegeOrgCodes = manager.getOrgCodes();
                    // 管理员权限组织机构代码包含查询用户的组织机构代码
                    // 才返回成功
                    if(privilegeOrgCodes.contains(orgCode + ",") || privilegeOrgCodes.contains("," + orgCode)){
                        return true;
                } else{
                    return false;
                }
                } else{
                    return false;
                }

            } else{
                return false;
            }

        } else{
            return true;
        }
    }

    /**
     * 根据路径（两条边，且第一条边的终点为第二条边的起点）的三个坐标（第一条边的起点、终点，第二条边的终点）
     * 判断是否是直行
     * 注意：这里只考虑直行与掉头，不考虑左转/右转
     * @param edgeStartLng
     * @param edgeStartLat
     * @param edgeEndLng
     * @param edgeEndLat
     * @param nextEdgeEndLng
     * @param nextEdgeEndLat
     * @return
     */
    public Boolean isForward(Double edgeStartLng, Double edgeStartLat, Double edgeEndLng, Double edgeEndLat,
                            Double nextEdgeEndLng, Double nextEdgeEndLat) {
        Double anglePrev = Angle.angle(new Coordinate(edgeEndLng, edgeEndLat), new Coordinate(edgeStartLng, edgeStartLat));
        Double angleNext = Angle.angle(new Coordinate(edgeEndLng, edgeEndLat), new Coordinate(nextEdgeEndLng, nextEdgeEndLat));

        Double angle = Angle.toDegrees(Angle.diff(angleNext, anglePrev));
        System.out.println(edgeStartLng + "," + edgeStartLat + ";" + edgeEndLng + "," + edgeEndLat + ";" + nextEdgeEndLng + "," + nextEdgeEndLat + ";" + angle);
        if(angle < FORWARD_ANGEL_MIN || angle > FORWARD_ANGEL_MAX){
            return false;
        } else{
            return true;
        }
    }


    public String getRootPath(){
        return ClassUtils.getDefaultClassLoader().getResource("").getPath();
    }
}
