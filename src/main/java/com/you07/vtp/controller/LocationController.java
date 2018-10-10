package com.you07.vtp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.graphhopper.routing.AStar;
import com.graphhopper.routing.Path;
import com.graphhopper.routing.weighting.ShortestWeighting;
import com.graphhopper.routing.weighting.Weighting;
import com.graphhopper.storage.GraphHopperStorage;
import com.graphhopper.storage.index.LocationIndex;
import com.graphhopper.storage.index.QueryResult;
import com.graphhopper.util.InstructionList;
import com.graphhopper.util.Translation;
import com.graphhopper.util.TranslationMap;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.you07.eas.model.StudentInfo;
import com.you07.eas.model.TeacherInfo;
import com.you07.eas.service.StudentInfoService;
import com.you07.eas.service.TeacherInfoService;
import com.you07.map.model.NavigationInfo;
import com.you07.map.service.MapRouteDataService;
import com.you07.util.message.MessageBean;
import com.you07.util.message.MessageListBean;
import com.you07.util.route.FloorEdgeFilter;
import com.you07.util.route.GraphHopperHelper;
import com.you07.util.route.Routing;
import com.you07.vtp.model.LocationHistory;
import com.you07.vtp.model.LocationTrackManager;
import com.you07.vtp.service.LocationHitoryService;
import com.you07.vtp.service.LocationTrackManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.graphhopper.routing.util.TraversalMode.NODE_BASED;

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
    private MapRouteDataService mapRouteDataService;
    @Autowired
    private TeacherInfoService teacherInfoService;

    private static GeometryCollection LINES = null;

    private static Map<String, GraphHopperStorage> GRAPH_HOPPER_STROAGE_MAP = new HashMap<>();
    private static Map<String, LocationIndex> LOCATION_INDEX_MAP = new HashMap<>();


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
//            if(LINES == null){
//                List<MapRoutePolyline> mapRoutePolylineList = mapRoutePolylineService.selectAll();
//                Geometry[] geometries = new Geometry[mapRoutePolylineList.size()];
//                for(int i = 0; i < mapRoutePolylineList.size(); i++){
//                    geometries[i] = mapRoutePolylineList.get(i).getGeom();
//                }
//
//                GeometryFactory geometryFactory=new GeometryFactory();
//                LINES = geometryFactory.createGeometryCollection(geometries);
//            }

                ArrayList<Double[]> arrayList = new ArrayList<>();
//            for(LocationHistory locationHistory : list){
//                PointPairDistance pointPairDistance = new PointPairDistance();
//                DistanceToPoint.computeDistance(LINES,new Coordinate(locationHistory.getLng(),locationHistory.getLat()),pointPairDistance);
//                Coordinate coordinate = pointPairDistance.getCoordinate(0);
//
//                Double[] doubleArray = new Double[]{coordinate.x, coordinate.y};
//                Double[] doubleArray = new Double[]{locationHistory.getLng(),locationHistory.getLat()};
//                arrayList.add(doubleArray);
//            }

                for(int i = 1; i < list.size(); i++){

                    LocationHistory startLocation = list.get(i - 1);
                    LocationHistory endLocation = list.get(i);

                    if(!startLocation.getLng().equals(endLocation.getLng()) || !startLocation.getLat().equals(endLocation.getLat())
                            || !startLocation.getFloorid().equals(endLocation.getFloorid())){
                        String startLng = startLocation.getLng().toString();
                        String startLat = startLocation.getLat().toString();
                        String startFloorId = "0";
                        if(startLocation.getInDoor() == 1){
                            startFloorId = String.valueOf(Integer.parseInt(startLocation.getFloorid().substring(4)) - 9);
                        }


                        String endLng = endLocation.getLng().toString();
                        String endLat = endLocation.getLat().toString();
                        String endFloorId = "0";
                        if(endLocation.getInDoor() == 1){
                            endFloorId = String.valueOf(Integer.parseInt(endLocation.getFloorid().substring(4)) - 9);
                        }

                        String[] startLoc = {startLat, startLng, "0"};
                        String[] endLoc = {endLat, endLng, "0"};
//                    String[] startLoc = {list.get(i - 1).getLng().toString(), list.get(i - 1).getLat().toString(), list.get(i - 1).getFloorid()};
//                    String[] endLoc = {list.get(i).getLng().toString(), list.get(i).getLat().toString(), list.get(i).getFloorid()};
                        List<Routing> routings = findRoutePath(campusId.toString(), startLoc, endLoc);

                        if(routings != null && routings.size() > 0){
                            for(int j = 0; j < routings.size(); j++){
                                arrayList.addAll( routings.get(j).getPointList());
                            }
                        } else{
                            arrayList.add(new Double[] {list.get(i - 1).getLng(), list.get(i - 1).getLat()});
                            arrayList.add(new Double[] {list.get(i).getLng(), list.get(i).getLat()});
                        }
                    }
                }
                ArrayList<Double[]> pointList = new ArrayList<>();
                for(int k = 0; k < arrayList.size(); k++){
                    if(pointList.size() == 0 || !pointList.get(pointList.size() - 1).equals(arrayList.get(k))){
                        pointList.add(arrayList.get(k));
                    }
                }

                if(list.size() > 0){
                    messageListBean.setData(list);
                    messageListBean.setStatus(true);
                    messageListBean.setCode(200);
                    messageListBean.addPropertie("track", pointList.toArray());
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
     * 路径规划
     * @param mapid
     * @param startLoc
     * @param endLoc
     * @return
     */
    private List<Routing> findRoutePath(String mapid,
                                        String[] startLoc,
                                        String[] endLoc) {
        GraphHopperStorage graph = null;
        LocationIndex index = null;


        String realPath = getRootPath() + "/routing";
        if(GRAPH_HOPPER_STROAGE_MAP.containsKey(mapid)){
            graph = GRAPH_HOPPER_STROAGE_MAP.get(mapid);
        } else{
            NavigationInfo navigationInfo = mapRouteDataService.getRouteData(mapid);

            graph = GraphHopperHelper
                    .createGraph(realPath, navigationInfo, mapid, "foot");

            GRAPH_HOPPER_STROAGE_MAP.put(mapid, graph);
        }

        if(LOCATION_INDEX_MAP.containsKey(mapid)){
            index = LOCATION_INDEX_MAP.get(mapid);
        } else{
            index = GraphHopperHelper.createTree(realPath, graph, mapid);

            LOCATION_INDEX_MAP.put(mapid, index);
        }


        double[] StartLatLon = new double[]{Double.parseDouble(startLoc[0])
                , Double.parseDouble(startLoc[1]), Double.parseDouble(startLoc[2])};
        double[] EndLatLon = new double[]{Double.parseDouble(endLoc[0])
                , Double.parseDouble(endLoc[1]), Double.parseDouble(endLoc[2])};

        QueryResult startIndex = index.findClosest(StartLatLon[0], StartLatLon[1],
                new FloorEdgeFilter((int) StartLatLon[2], mapid));
        QueryResult endIndex = index.findClosest(EndLatLon[0], EndLatLon[1],
                new FloorEdgeFilter((int) EndLatLon[2], mapid));

        Weighting weighting = null;

        weighting = new ShortestWeighting(GraphHopperHelper.footEncoder);
        AStar aStar = new AStar(graph, weighting, NODE_BASED);

        if (startIndex.getClosestNode() == -1 || endIndex.getClosestNode() == -1) {
            return null;
        }

        Path path = aStar.calcPath(startIndex.getClosestNode(), endIndex.getClosestNode());
        final TranslationMap trMap = new TranslationMap().doImport();
        final Translation tr = trMap.getWithFallBack(Locale.SIMPLIFIED_CHINESE);

        InstructionList instructions = path.calcInstructions(tr);

        Map<String, String> nameToFloorMap = GraphHopperHelper.getMapToNameAndFloor().get(mapid);

        List<Routing> routings = new ArrayList<Routing>();

        for (int i = 0; i < instructions.size(); i++) {
            Routing routing = new Routing();
            if (instructions.size() - 1 == i) {
                routing.setPointList(instructions.get(i).getPoints().toGeoJson());
            } else {
                routing.setPointList(path.calcEdges().get(i).fetchWayGeometry(3).toGeoJson());
            }

            routing.setDistance(instructions.get(i).getDistance());
            routing.setSign(instructions.get(i).getSign());
            routing.setTime(instructions.get(i).getTime());

            String name = instructions.get(i).getName();

            if (StringUtils.isNotBlank(name)) {
                try {
                    Integer.parseInt(name.substring(5, 6));

                    routing.setName(name);
                } catch (Exception e) {
                    routing.setName(nameToFloorMap.get(name));
                }
            } else {
                routing.setName("");
            }

            routings.add(routing);
        }
        return routings;
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

    public String getRootPath(){
        return ClassUtils.getDefaultClassLoader().getResource("").getPath();
    }
}
