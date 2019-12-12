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
import com.you07.vtp.model.LocationTrackManager;
import com.you07.vtp.model.UserInfo;
import com.you07.vtp.service.LocationTrackManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息接口
 * @version 1.0
 * @author RY
 */

@RestController
@CrossOrigin
@Api(value="用户信息 controller",tags={"用户信息接口"})
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
    public String detail(@ApiParam(name="userCode",value="用户名",required=false) @RequestParam(name = "userCode", required = true) String userCode){
        MessageBean<UserInfo> messageBean = new MessageBean<UserInfo>(null);
        try {
            LocationTrackManager locationTrackManager = locationTrackManagerService.get(userCode);
            StudentInfo studentInfo = studentInfoService.get(userCode);
            TeacherInfo teacherInfo = teacherInfoService.get(userCode);
            Integer isManager =  0;
            if(locationTrackManager != null || teacherInfo != null || studentInfo != null){
                String username, orgName;
                username = orgName = "";
                if(locationTrackManager != null){
                    username = locationTrackManager.getUsername();
                    orgName = locationTrackManager.getDeptname();
                    isManager = locationTrackManager.getIsManager();
                } else if(teacherInfo != null){
                    username = teacherInfo.getName();
                    orgName = teacherInfo.getOrgName();
                } else{
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
            } else{
                messageBean.setStatus(false);
                messageBean.setCode(1002);
                messageBean.setMessage("没有查询到数据");
            }

        } catch (Exception e) {
            e.printStackTrace();
            messageBean.setStatus(false);
            messageBean.setCode(10001);
            messageBean.setMessage("接口错误");
        }

        return JSON.toJSONString(messageBean);
    }

    @ApiOperation("搜索用户信息")
    @PostMapping("/search")
    @ResponseBody
    public String search(@ApiParam(name="keyword",value="学工号/姓名",required=false) @RequestParam(name = "keyword", required = true) String keyword,
                         @ApiParam(name = "page", value = "当前页", required = true) @RequestParam("page") String page,
                         @ApiParam(name = "pageSize", value = "每页显示条数", required = true) @RequestParam("pageSize") String pageSize){
        MessageListBean<UserInfo> messageListBean = new MessageListBean<UserInfo>(null);
        try {
            List<StudentInfo> studentInfoList = studentInfoService.searchWithCodeName(keyword, page, pageSize);
            List<TeacherInfo> teacherInfoList = teacherInfoService.searchWithCodeName(keyword, page, pageSize);

            if(teacherInfoList.size() > 0 || studentInfoList.size() > 0){
                List<UserInfo> userInfoList = new ArrayList<>();

                for(TeacherInfo teacherInfo : teacherInfoList){
                    UserInfo userInfo = new UserInfo();
                    userInfo.setUserId(teacherInfo.getTeachercode());
                    userInfo.setAvatar("");
                    if(teacherInfo.getDepartmentInfo() != null){
                        userInfo.setOrgName(teacherInfo.getOrgName());
                    }
                    userInfo.setUsername(teacherInfo.getName());
                    userInfo.setUserType("教职工");

                    userInfoList.add(userInfo);
                }

                for(StudentInfo studentInfo : studentInfoList){
                    UserInfo userInfo = new UserInfo();
                    userInfo.setUserId(studentInfo.getStudentno());
                    userInfo.setAvatar("");
                    if(studentInfo.getClassInfo() != null){
                        userInfo.setOrgName(studentInfo.getClassInfo().getClassname());
                    }
                    userInfo.setUsername(studentInfo.getRealName());
                    userInfo.setUserType("学生");

                    userInfoList.add(userInfo);
                }

                messageListBean.setStatus(true);
                messageListBean.setData(userInfoList);
                messageListBean.setCode(200);
                messageListBean.setMessage("获取成功");
            } else{
                messageListBean.setStatus(false);
                messageListBean.setCode(1002);
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

    @ApiOperation("从Cmips得到权限xml")
    @GetMapping("/getPrivilegeXml")
    public MessageBean getPrivilegeXml() throws SAXException {
        JSONObject objectForCmIps = RestTemplateUtil.getJSONObjectForCmIps("/os/json/student");
        JSONArray jsonArray = objectForCmIps.getJSONArray("data");
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("root");
        Element student= root.addElement("student");
        student.addAttribute("id","1");
        student.addAttribute("name","学生");
        student.addAttribute("count",String.valueOf(jsonArray.size()));
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject adJsonObj = jsonArray.getJSONObject(i);
            String acadeName = adJsonObj.getString("name");
            String adCode = adJsonObj.getString("code");
            Element ad = student.addElement("ad");
            ad.addAttribute("name",acadeName);
            ad.addAttribute("id",adCode);
            JSONArray mjJsonArr = adJsonObj.getJSONArray("children");
           for (int j = 0; j < mjJsonArr.size(); j++) {
               JSONObject mjJsonObj = mjJsonArr.getJSONObject(j);
               JSONArray classJsonArr = mjJsonObj.getJSONArray("children");
               ad.addAttribute("count",String.valueOf(classJsonArr.size()));
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


    @ApiOperation("从Cmips得到能查看人员xml")
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
            Element student= root.addElement("student");
            student.addAttribute("id","1");
            student.addAttribute("name","学生");
            student.addAttribute("count",String.valueOf(jsonArray.size()));
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject adJsonObj = jsonArray.getJSONObject(i);
                String acadeName = adJsonObj.getString("name");
                String adCode = adJsonObj.getString("code");
                Element ad = student.addElement("ad");
                ad.addAttribute("name",acadeName);
                ad.addAttribute("id",adCode);
                JSONArray mjJsonArr = adJsonObj.getJSONArray("children");
                ad.addAttribute("count",String.valueOf(mjJsonArr.size()));
                for (int j = 0; j < mjJsonArr.size(); j++) {
                    JSONObject mjJsonObj = mjJsonArr.getJSONObject(i);
                    JSONArray classJsonArr = mjJsonObj.getJSONArray("children");
                    for (int k = 0; k < classJsonArr.size(); k ++) {
                        JSONObject claJsonObj = classJsonArr.getJSONObject(k);
                        String claCode = claJsonObj.getString("code");
                        String claName = claJsonObj.getString("name");
                        for (int l = 0; l < splitOrgCodes.length; l++) {
                            if (claCode.equals(splitOrgCodes[l])){
                                //continue loopThird;
                                Element ci = ad.addElement("ci");
                                ci.addAttribute("name", claName);
                                ci.addAttribute("id", claCode);
                                JSONArray stuJsonArr = claJsonObj.getJSONArray("children");
                                ci.addAttribute("count", String.valueOf(stuJsonArr.size()));
                                for (int m = 0; m < stuJsonArr.size(); m ++) {
                                    JSONObject stuJsonObj = stuJsonArr.getJSONObject(m);
                                    String stuCode = stuJsonObj.getString("code");
                                    String stuName = stuJsonObj.getString("name");
                                    Element stu = ci.addElement("stu");
                                    stu.addAttribute("name", stuName);
                                    stu.addAttribute("id", stuCode);
                                }
                            }
                        }

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

}
