package com.you07.vtp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.you07.eas.model.StudentInfo;
import com.you07.eas.model.TeacherInfo;
import com.you07.eas.service.StudentInfoService;
import com.you07.eas.service.TeacherInfoService;
import com.you07.util.RestTemplateUtil;
import com.you07.util.message.MessageBean;
import com.you07.util.message.MessageListBean;
import com.you07.vtp.form.UserLocationForm;
import com.you07.vtp.model.LocationTrackManager;
import com.you07.vtp.model.UserInfo;
import com.you07.vtp.service.LocationTrackManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用户信息接口
 *
 * @author RY
 * @version 1.0
 */

@RestController
@CrossOrigin
@Api(value = "用户信息 controller", tags = {"用户信息接口"})
@RequestMapping("/user")
public class UserInfoController {
    @Autowired
    private LocationTrackManagerService locationTrackManagerService;
    @Autowired
    private TeacherInfoService teacherInfoService;
    @Autowired
    private StudentInfoService studentInfoService;

    @ApiOperation("获取用户信息")
    @GetMapping("/detail")
    @ResponseBody
    public String detail(@ApiParam(name = "userCode", value = "用户名", required = false) @RequestParam(name = "userCode", required = true) String userCode) {
        MessageBean<UserInfo> messageBean = new MessageBean<UserInfo>(null);
        LocationTrackManager locationTrackManager = locationTrackManagerService.get(userCode);
        StudentInfo studentInfo = studentInfoService.get(userCode);
        TeacherInfo teacherInfo = teacherInfoService.get(userCode);
        Integer isManager = 0;
        if (locationTrackManager != null || teacherInfo != null || studentInfo != null) {
            String username, orgName;
            username = orgName = "";
            if (locationTrackManager != null) {
                username = locationTrackManager.getUsername();
                orgName = locationTrackManager.getDeptname();
                isManager = locationTrackManager.getIsManager();
            } else if (teacherInfo != null) {
                username = teacherInfo.getName();
                orgName = teacherInfo.getOrgName();
            } else {
                username = studentInfo.getRealName();
                orgName = studentInfo.getOrgName();
            }

            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(userCode);
            userInfo.setAvatar("");
            userInfo.setOrgName(orgName);
            userInfo.setUsername(username);

            messageBean.setData(userInfo);
            messageBean.setStatus(true);
            messageBean.setCode(200);
            messageBean.setMessage("获取成功");
            messageBean.addPropertie("isManager", isManager);
        } else {
            messageBean.setStatus(false);
            messageBean.setCode(1002);
            messageBean.setMessage("没有查询到数据");
        }


        return JSON.toJSONString(messageBean);
    }

    @ApiOperation("搜索用户信息")
    @PostMapping("/search")
    @ResponseBody
    public String search(@ApiParam(name = "keyword", value = "学工号/姓名", required = false) @RequestParam(name = "keyword", required = true) String keyword,
                         @ApiParam(name = "page", value = "当前页", required = true) @RequestParam("page") String page,
                         @ApiParam(name = "pageSize", value = "每页显示条数", required = true) @RequestParam("pageSize") String pageSize) throws IOException {
        try {
            MessageListBean<UserInfo> messageListBean = new MessageListBean<UserInfo>(null);
            List<StudentInfo> studentInfoList = studentInfoService.searchWithCodeName(keyword, page, pageSize);
            List<TeacherInfo> teacherInfoList = teacherInfoService.searchWithCodeName(keyword, page, pageSize);

            if (teacherInfoList.size() > 0 || studentInfoList.size() > 0) {
                List<UserInfo> userInfoList = new ArrayList<>();

                for (TeacherInfo teacherInfo : teacherInfoList) {
                    UserInfo userInfo = new UserInfo();
                    userInfo.setUserId(teacherInfo.getTeachercode());
                    userInfo.setAvatar("");
                    if (teacherInfo.getDepartmentInfo() != null) {
                        userInfo.setOrgName(teacherInfo.getOrgName());
                    }
                    userInfo.setUsername(teacherInfo.getName());
                    userInfo.setUserType("教职工");

                    userInfoList.add(userInfo);
                }

                for (StudentInfo studentInfo : studentInfoList) {
                    UserInfo userInfo = new UserInfo();
                    userInfo.setUserId(studentInfo.getStudentno());
                    userInfo.setAvatar("");
                    if (studentInfo.getClassCode() != null) {
                        userInfo.setOrgName(studentInfo.getOrgName());
                    }
                    userInfo.setUsername(studentInfo.getRealName());
                    userInfo.setUserType("学生");

                    userInfoList.add(userInfo);
                }

                messageListBean.setStatus(true);
                messageListBean.setData(userInfoList);
                messageListBean.setCode(200);
                messageListBean.setMessage("获取成功");
            } else {
                messageListBean.setStatus(false);
                messageListBean.setCode(1002);
                messageListBean.setMessage("没有查询到数据");
            }


            return JSON.toJSONString(messageListBean, SerializerFeature.DisableCircularReferenceDetect);
        }catch (Exception e) {
            return JSON.toJSONString("未知异常");
        }
    }

    @ApiOperation("从Cmips得到权限xml")
    @GetMapping("/getPrivilegeXml")
    public MessageBean getPrivilegeXml() throws SAXException {
        JSONObject objectForCmIps = RestTemplateUtil.getJSONObjectForCmIps("/os/json/student");
        JSONArray jsonArray = objectForCmIps.getJSONArray("data");
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("root");
        Element student = root.addElement("student");
        student.addAttribute("id", "1");
        student.addAttribute("name", "学生");
        student.addAttribute("count", String.valueOf(jsonArray.size()));
        for (int i = 0, count = 0; i < jsonArray.size(); i++) {
            JSONObject adJsonObj = jsonArray.getJSONObject(i);
            String acadeName = adJsonObj.getString("name");
            String adCode = adJsonObj.getString("code");
            Element ad = student.addElement("ad");
            ad.addAttribute("name", acadeName);
            ad.addAttribute("id", adCode);
            JSONArray mjJsonArr = adJsonObj.getJSONArray("children");
            for (int j = 0; j < mjJsonArr.size(); j++) {
                JSONObject mjJsonObj = mjJsonArr.getJSONObject(j);
                JSONArray classJsonArr = mjJsonObj.getJSONArray("children");
                ad.addAttribute("count", String.valueOf(classJsonArr.size()));
                for (int k = 0; k < classJsonArr.size(); k++) {
                    JSONObject claObject = classJsonArr.getJSONObject(k);
                    String claName = claObject.getString("name");
                    String claCode = claObject.getString("code");
                    Element ci = ad.addElement("ci");
                    ci.addAttribute("name", claName);
                    ci.addAttribute("id", claCode);
                }
            }
        }
        MessageBean<Object> objectMessageBean = new MessageBean<>();
        objectMessageBean.setCode(0);
        objectMessageBean.setStatus(true);
        objectMessageBean.setData(document.asXML());
        objectMessageBean.setMessage("操作成功");
        return objectMessageBean;
    }


    @ApiOperation("从Cmips得到能查看机构xml")
    @GetMapping("/getPersonInfoXml")
    public MessageBean getPersonInfoXml(@ApiParam(name = "userCode", value = "用户ID", required = true) @RequestParam String userCode) throws SAXException {
        //根据userId得到能够查看的组织结构编码
        Document document = null;
        try {
            LocationTrackManager locationTrackManager = locationTrackManagerService.get(userCode);
            String orgCodes = null;
            if (null != locationTrackManager) {
                orgCodes = locationTrackManager.getOrgCodes();
            }
            String[] splitOrgCodes = null;
            if (null != orgCodes) {
                splitOrgCodes = orgCodes.split(",");
            }
            JSONObject objectForCmIps = RestTemplateUtil.getJSONObjectForCmIps("/os/json/student");
            JSONArray jsonArray = objectForCmIps.getJSONArray("data");
            document = DocumentHelper.createDocument();
            Element root = document.addElement("root");
            Element student = root.addElement("student");
            student.addAttribute("id", "1");
            student.addAttribute("name", "学生");
            student.addAttribute("count", String.valueOf(jsonArray.size()));
            //院系
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject adJsonObj = jsonArray.getJSONObject(i);
                String acadeName = adJsonObj.getString("name");
                String adCode = adJsonObj.getString("code");
                Element ad = student.addElement("ad");
                ad.addAttribute("name", acadeName);
                ad.addAttribute("id", adCode);
                JSONArray mjJsonArr = adJsonObj.getJSONArray("children");
                ad.addAttribute("count", String.valueOf(mjJsonArr.size()));
                //专业
                for (int j = 0; j < mjJsonArr.size(); j++) {
                    JSONObject mjJsonObj = mjJsonArr.getJSONObject(j);
                    JSONArray classJsonArr = mjJsonObj.getJSONArray("children");
                    //班级
                    for (int k = 0, cnt = 0; k < classJsonArr.size(); k++) {
                        JSONObject claJsonObj = classJsonArr.getJSONObject(k);
                        String claCode = claJsonObj.getString("code");
                        String claName = claJsonObj.getString("name");
                        for (int l = 0; l < splitOrgCodes.length; l++) {
                            //判断是否有班级权限
                            if (claCode.equals(splitOrgCodes[l])) {
                                cnt++;
                                //continue loopThird;
                                Element ci = ad.addElement("ci");
                                ci.addAttribute("name", claName);
                                ci.addAttribute("id", claCode);
                                //JSONArray stuJsonArr = claJsonObj.getJSONArray("children");
                                //ci.addAttribute("count", String.valueOf(stuJsonArr.size()));
                                //学生
                                /*for (int m = 0; m < stuJsonArr.size(); m++) {
                                    JSONObject stuJsonObj = stuJsonArr.getJSONObject(m);
                                    String stuCode = stuJsonObj.getString("code");
                                    String stuName = stuJsonObj.getString("name");
                                    Element stu = ci.addElement("stu");
                                    stu.addAttribute("name", stuName);
                                    stu.addAttribute("id", stuCode);
                                }*/
                            }
                        }
                        //如果没有班级，删除该院系
                        if (cnt == 0)
                            student.remove(ad);
                        cnt = 0;

                    }
                }

            }
        } catch (Exception e) {
            MessageBean<Object> objectMessageBean = new MessageBean<>();
            objectMessageBean.setCode(200);
            objectMessageBean.setStatus(true);
            objectMessageBean.setData("没有权限查看或无学生");
            return objectMessageBean;
        }
        MessageBean<Object> objectMessageBean = new MessageBean<>();
        objectMessageBean.setCode(0);
        objectMessageBean.setStatus(true);
        objectMessageBean.setData(document.asXML());
        objectMessageBean.setMessage("操作成功");
        return objectMessageBean;
    }

    @ApiOperation("获取选择用户信息JSON数据")
    @GetMapping("/getPersonInfoJson")
    public MessageBean getPersonInfoJson(@ApiParam(name = "userCode", value = "用户ID", required = true) @RequestParam String userCode) throws SAXException {
        //根据userId得到能够查看的组织结构编码
        JSONArray result = new JSONArray();
        try {
            LocationTrackManager locationTrackManager = locationTrackManagerService.get(userCode);
            String orgCodes = null;
            if (null != locationTrackManager) {
                orgCodes = locationTrackManager.getOrgCodes();
            }
            String[] splitOrgCodes = null;
            if (null != orgCodes) {
                splitOrgCodes = orgCodes.split(",");
            }
            JSONObject objectForCmIps = RestTemplateUtil.getJSONObjectForCmIps("/os/json/student");
            JSONArray jsonArray = objectForCmIps.getJSONArray("data");
            result.addAll(jsonArray);
            JSONObject fontStyle = new JSONObject();
            fontStyle.put("color", "White");
            //院系
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject adJsonObj = jsonArray.getJSONObject(i);
                jsonArray.getJSONObject(i).put("type", 1);
                jsonArray.getJSONObject(i).put("font", fontStyle);
                jsonArray.getJSONObject(i).put("icon", "zTree_v3/css/zTreeStyle/img/diy/1_open.png");
                jsonArray.getJSONObject(i).put("iconSkin", "../img/tree_close.png");
                JSONArray mjJsonArr = adJsonObj.getJSONArray("children");
                //专业
                for (int j = 0; j < mjJsonArr.size(); j++) {
                    JSONObject mjJsonObj = mjJsonArr.getJSONObject(j);
                    jsonArray.getJSONObject(i).getJSONArray("children").getJSONObject(j).put("type", 2);
                    jsonArray.getJSONObject(i).getJSONArray("children").getJSONObject(j).put("font", fontStyle);
//                    jsonArray.getJSONObject(i).getJSONArray("children").getJSONObject(j).put("icon", "zTree_v3/css/zTreeStyle/img/diy/1_open.png");
//                    jsonArray.getJSONObject(i).getJSONArray("children").getJSONObject(j).put("iconSkin", "../img/tree_close.png");
                    JSONArray classJsonArr = mjJsonObj.getJSONArray("children");
                    //班级
                    for (int k = 0, cnt = 0; k < classJsonArr.size(); k++) {
                        JSONObject claJsonObj = classJsonArr.getJSONObject(k);
                        jsonArray.getJSONObject(i).getJSONArray("children").getJSONObject(j).getJSONArray("children").getJSONObject(k).put("type", 3);
                        jsonArray.getJSONObject(i).getJSONArray("children").getJSONObject(j).getJSONArray("children").getJSONObject(k).put("font", fontStyle);
                        String claCode = claJsonObj.getString("code");
                        JSONArray stuJsonArr = claJsonObj.getJSONArray("children");
                        /*for (int l = 0; l < splitOrgCodes.length; l++) {
                            //判断是否有班级权限
                            if (claCode.equals(splitOrgCodes[l])) {
                                cnt++;
                                //continue loopThird;
                                JSONArray stuJsonArr = claJsonObj.getJSONArray("children");
                                jsonArray.getJSONObject(i).getJSONArray("children").getJSONObject(j).getJSONArray("children").getJSONObject(k).getJSONArray("children").getJSONObject(l)
                                //学生
                                for (int m = 0; m < stuJsonArr.size(); m++) {
                                    JSONObject stuJsonObj = stuJsonArr.getJSONObject(m);
                                    String stuCode = stuJsonObj.getString("code");
                                    String stuName = stuJsonObj.getString("name");
                                }
                            }
                        }*/
                        // 判断是否有班级权限
                        if (Arrays.asList(splitOrgCodes).contains(claCode)) {
                            for (int m = 0; m < stuJsonArr.size(); m++) {
                                jsonArray.getJSONObject(i).getJSONArray("children").getJSONObject(j).getJSONArray("children").getJSONObject(k).getJSONArray("children").getJSONObject(m).put("type", 4);
                                jsonArray.getJSONObject(i).getJSONArray("children").getJSONObject(j).getJSONArray("children").getJSONObject(k).getJSONArray("children").getJSONObject(m).put("font", fontStyle);
                            }
                        } else {
                            result.getJSONObject(i).getJSONArray("children").getJSONObject(j).getJSONArray("children").remove(k);
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            MessageBean<Object> objectMessageBean = new MessageBean<>();
            objectMessageBean.setCode(200);
            objectMessageBean.setStatus(true);
            objectMessageBean.setData("没有权限查看或无学生");
            return objectMessageBean;
        }
        MessageBean<Object> objectMessageBean = new MessageBean<>();
        objectMessageBean.setCode(0);
        objectMessageBean.setStatus(true);
        objectMessageBean.setData(result);
        objectMessageBean.setMessage("操作成功");
        return objectMessageBean;
    }


    @ApiOperation("获取选择班级信息JSON数据")
    @GetMapping("/getClassInfoJson")
    public MessageBean getClassInfoJson(@ApiParam(name = "userCode", value = "用户ID", required = true) @RequestParam String userCode) throws SAXException {
        //根据userId得到能够查看的组织结构编码
        JSONArray result = new JSONArray();
        try {
            LocationTrackManager locationTrackManager = locationTrackManagerService.get(userCode);
            String orgCodes = null;
            if (null != locationTrackManager) {
                orgCodes = locationTrackManager.getOrgCodes();
            }
            String[] splitOrgCodes = null;
            if (null != orgCodes) {
                splitOrgCodes = orgCodes.split(",");
            }
            JSONObject objectForCmIps = RestTemplateUtil.getJSONObjectForCmIps("/os/json/student");
            JSONArray jsonArray = objectForCmIps.getJSONArray("data");
            result.addAll(jsonArray);
            JSONObject fontStyle = new JSONObject();
            fontStyle.put("color", "White");
            //院系
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject adJsonObj = jsonArray.getJSONObject(i);
                jsonArray.getJSONObject(i).put("type", 1);
                jsonArray.getJSONObject(i).put("font", fontStyle);
                jsonArray.getJSONObject(i).put("icon", "zTree_v3/css/zTreeStyle/img/diy/1_open.png");
                jsonArray.getJSONObject(i).put("iconSkin", "../img/tree_close.png");
                JSONArray mjJsonArr = adJsonObj.getJSONArray("children");
                //专业
                for (int j = 0; j < mjJsonArr.size(); j++) {
                    JSONObject mjJsonObj = mjJsonArr.getJSONObject(j);
                    jsonArray.getJSONObject(i).getJSONArray("children").getJSONObject(j).put("type", 2);
                    jsonArray.getJSONObject(i).getJSONArray("children").getJSONObject(j).put("font", fontStyle);
                    JSONArray classJsonArr = mjJsonObj.getJSONArray("children");
                    //班级
                    for (int k = 0, cnt = 0; k < classJsonArr.size(); k++) {
                        JSONObject claJsonObj = classJsonArr.getJSONObject(k);
                        jsonArray.getJSONObject(i).getJSONArray("children").getJSONObject(j).getJSONArray("children").getJSONObject(k).put("type", 3);
                        jsonArray.getJSONObject(i).getJSONArray("children").getJSONObject(j).getJSONArray("children").getJSONObject(k).put("font", fontStyle);
                        String claCode = claJsonObj.getString("code");
                        JSONArray stuJsonArr = claJsonObj.getJSONArray("children");

                        // 判断是否有班级权限
                        if (Arrays.asList(splitOrgCodes).contains(claCode)) {
                            for (int m = 0; m < stuJsonArr.size(); m++) {
                                result.getJSONObject(i).getJSONArray("children").getJSONObject(j).getJSONArray("children").getJSONObject(k).remove("children");
                            }
                        } else {
                            result.getJSONObject(i).getJSONArray("children").getJSONObject(j).getJSONArray("children").remove(k);
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            MessageBean<Object> objectMessageBean = new MessageBean<>();
            objectMessageBean.setCode(200);
            objectMessageBean.setStatus(true);
            objectMessageBean.setData("没有权限查看或无学生");
            return objectMessageBean;
        }
        MessageBean<Object> objectMessageBean = new MessageBean<>();
        objectMessageBean.setCode(0);
        objectMessageBean.setStatus(true);
        objectMessageBean.setData(result);
        objectMessageBean.setMessage("操作成功");
        return objectMessageBean;
    }

    @ApiOperation("获取选择教师信息JSON数据")
    @GetMapping("/getTeacherInfoJson")
    public MessageBean getTeacherInfoJson(@ApiParam(name = "userCode", value = "用户ID", required = true) @RequestParam String userCode){
        //根据userId得到能够查看的组织结构编码
        JSONArray result = new JSONArray();
        try {
            LocationTrackManager locationTrackManager = locationTrackManagerService.get(userCode);
            String orgCodes = null;
            if (null != locationTrackManager) {
                orgCodes = locationTrackManager.getOrgCodes();
            }
            String[] splitOrgCodes = null;
            if (null != orgCodes) {
                splitOrgCodes = orgCodes.split(",");
            }
            JSONObject objectForCmIps = RestTemplateUtil.getJSONObjectForCmIps("/os/json/teachingStaff");
            JSONArray jsonArray = objectForCmIps.getJSONArray("data");
            result.addAll(jsonArray);
            JSONObject fontStyle = new JSONObject();
            fontStyle.put("color", "White");
            //院系、部门
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject adJsonObj = jsonArray.getJSONObject(i);
                jsonArray.getJSONObject(i).put("type", 1);
                jsonArray.getJSONObject(i).put("font", fontStyle);
                jsonArray.getJSONObject(i).put("icon", "zTree_v3/css/zTreeStyle/img/diy/1_open.png");
                jsonArray.getJSONObject(i).put("iconSkin", "../img/tree_close.png");
                JSONArray mjJsonArr = adJsonObj.getJSONArray("children");
                //学院、科室
                for (int j = 0; j < mjJsonArr.size(); j++) {
                    JSONObject mjJsonObj = mjJsonArr.getJSONObject(j);
                    jsonArray.getJSONObject(i).getJSONArray("children").getJSONObject(j).put("type", 2);
                    jsonArray.getJSONObject(i).getJSONArray("children").getJSONObject(j).put("font", fontStyle);
                    JSONArray teacherJsonArr = mjJsonObj.getJSONArray("children");
                    //人
                    for (int k = 0, cnt = 0; k < teacherJsonArr.size(); k++) {
                        JSONObject personObj = teacherJsonArr.getJSONObject(k);
                        jsonArray.getJSONObject(i).getJSONArray("children").getJSONObject(j).getJSONArray("children").getJSONObject(k).put("type", 3);
                        jsonArray.getJSONObject(i).getJSONArray("children").getJSONObject(j).getJSONArray("children").getJSONObject(k).put("font", fontStyle);
                        //String claCode = personObj.getString("code");
                        /*JSONArray stuJsonArr = claJsonObj.getJSONArray("children");

                        // 判断是否有班级权限
                        if (Arrays.asList(splitOrgCodes).contains(claCode)) {
                            for (int m = 0; m < stuJsonArr.size(); m++) {
                                result.getJSONObject(i).getJSONArray("children").getJSONObject(j).getJSONArray("children").getJSONObject(k).remove("children");
                            }
                        } else {
                            result.getJSONObject(i).getJSONArray("children").getJSONObject(j).getJSONArray("children").remove(k);
                        }*/
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            MessageBean<Object> objectMessageBean = new MessageBean<>();
            objectMessageBean.setCode(200);
            objectMessageBean.setStatus(true);
            objectMessageBean.setData("没有权限查看或无学生");
            return objectMessageBean;
        }
        MessageBean<Object> objectMessageBean = new MessageBean<>();
        objectMessageBean.setCode(0);
        objectMessageBean.setStatus(true);
        objectMessageBean.setData(result);
        objectMessageBean.setMessage("操作成功");
        return objectMessageBean;
    }

    /**
     * 根据条件动态生成json文件
     */
    @ApiOperation("根据条件动态生成json文件")
    @GetMapping("/getJsonDynamic")
    public MessageBean getJsonDynamic(@Valid @RequestBody UserLocationForm form) {
        studentInfoService.getJsonDynamic(form);
        return null;
    }

    @ApiOperation("获取授权树列表信息")
    @GetMapping("/getAuthorizedJson")
    @ResponseBody
    public MessageBean getAuthorizedJson(@ApiParam(name = "userCode", value = "用户ID", required = true) @RequestParam String userCode) {
        JSONObject result = new JSONObject();
        //根据userId得到能够查看的组织结构编码
        LocationTrackManager locationTrackManager = locationTrackManagerService.get(userCode);
        String orgCodes = null;
        if (null != locationTrackManager) {
            orgCodes = locationTrackManager.getOrgCodes();
        }
        String[] splitOrgCodes = null;
        if (null != orgCodes) {
            splitOrgCodes = orgCodes.split(",");
        }
        JSONObject objectForCmIps = RestTemplateUtil.getJSONObjectForCmIps("/os/json/student");
        JSONArray jsonArray = objectForCmIps.getJSONArray("data");
        // 组装院系信息
        JSONArray collegeJsonArray = new JSONArray();
        // 组装教职工信息
        JSONArray teacherJsonArray = new JSONArray();

        //院系
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject adJsonObj = jsonArray.getJSONObject(i);
            JSONArray mjJsonArr = adJsonObj.getJSONArray("children");
            JSONObject college = new JSONObject();
            /*构建返回学院信息*/
            college.put("id", adJsonObj.getString("code"));
            college.put("label", adJsonObj.getString("name"));
            JSONArray childJsonArray = new JSONArray();
            //专业
            for (int j = 0; j < mjJsonArr.size(); j++) {
                JSONObject mjJsonObj = mjJsonArr.getJSONObject(j);
                JSONArray classJsonArr = mjJsonObj.getJSONArray("children");

                //班级
                for (int k = 0; k < classJsonArr.size(); k++) {
                    JSONObject claJsonObj = classJsonArr.getJSONObject(k);
                    String claCode = claJsonObj.getString("code");

                    /*构建返回学院信息(班级信息)*/
                    JSONObject classJsonObject = new JSONObject();
                    classJsonObject.put("id", claCode);
                    classJsonObject.put("label", claJsonObj.getString("name"));
                    // 判断是否有班级权限
                    /*if (Arrays.asList(splitOrgCodes).contains(claCode)) {
                        classJsonObject.put("checked", true);
                    }*/
                    childJsonArray.add(classJsonObject);
                }
            }
            college.put("children", childJsonArray);
            collegeJsonArray.add(college);
        }
        result.put("student", collegeJsonArray);

        JSONObject teachingStaffForCmIps = RestTemplateUtil.getJSONObjectForCmIps("/os/json/teachingStaff");
        JSONArray teachingStaffJsonArray = teachingStaffForCmIps.getJSONArray("data");
        //一级
        for (int i = 0; i < teachingStaffJsonArray.size(); i++) {
            JSONObject adJsonObj = teachingStaffJsonArray.getJSONObject(i);
            JSONArray mjJsonArr = adJsonObj.getJSONArray("children");
            /*构建返回一级部门信息*/
            JSONObject firstLevelDepart = new JSONObject();
            firstLevelDepart.put("id", adJsonObj.getString("code"));
            firstLevelDepart.put("label", adJsonObj.getString("name"));
            JSONArray firstLevelChildArray = new JSONArray();
            //二级
            for (int j = 0; j < mjJsonArr.size(); j++) {
                JSONObject mjJsonObj = mjJsonArr.getJSONObject(j);
                String claCode = mjJsonObj.getString("code");
                JSONObject secondLevelDepart = new JSONObject();
                secondLevelDepart.put("id", mjJsonObj.getString("code"));
                secondLevelDepart.put("label", mjJsonObj.getString("name"));
                // 判断是否有班级权限
                /*if (Arrays.asList(splitOrgCodes).contains(claCode)) {
                    secondLevelDepart.put("checked", true);
                }*/
                firstLevelChildArray.add(secondLevelDepart);
            }
            firstLevelDepart.put("children", firstLevelChildArray);
            teacherJsonArray.add(firstLevelDepart);
        }
        result.put("teacher", teacherJsonArray);
        MessageBean<Object> objectMessageBean = new MessageBean<>();
        objectMessageBean.setCode(0);
        objectMessageBean.setStatus(true);
        objectMessageBean.setData(result);
        objectMessageBean.setMessage("操作成功");
        return objectMessageBean;
    }
}
