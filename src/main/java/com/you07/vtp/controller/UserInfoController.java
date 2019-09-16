package com.you07.vtp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.you07.eas.model.StudentInfo;
import com.you07.eas.model.TeacherInfo;
import com.you07.eas.service.StudentInfoService;
import com.you07.eas.service.TeacherInfoService;
import com.you07.util.message.MessageBean;
import com.you07.util.message.MessageListBean;
import com.you07.vtp.model.LocationTrackManager;
import com.you07.vtp.model.SystemConfigVO;
import com.you07.vtp.model.UserInfo;
import com.you07.vtp.service.LocationTrackManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public String detail(@ApiParam(name="userid",value="用户ID",required=false) @RequestParam(name = "userid", required = true) String userid){
        MessageBean<UserInfo> messageBean = new MessageBean<UserInfo>(null);
        try {
            LocationTrackManager locationTrackManager = locationTrackManagerService.get(userid);
            StudentInfo studentInfo = studentInfoService.get(userid);
            TeacherInfo teacherInfo = teacherInfoService.get(userid);
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
                    username = studentInfo.getName();
                    orgName = studentInfo.getOrgName();
                }

                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(userid);
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
    public String search(@ApiParam(name="keyword",value="学工号/姓名",required=false) @RequestParam(name = "keyword", required = true) String keyword){
        MessageListBean<UserInfo> messageListBean = new MessageListBean<UserInfo>(null);
        try {
            List<StudentInfo> studentInfoList = studentInfoService.searchWithCodeName(keyword);
            List<TeacherInfo> teacherInfoList = teacherInfoService.searchWithCodeName(keyword);

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
                    userInfo.setUsername(studentInfo.getName());
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
}
