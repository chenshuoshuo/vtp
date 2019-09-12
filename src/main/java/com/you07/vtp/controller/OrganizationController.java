package com.you07.vtp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.you07.eas.model.ClassInfo;
import com.you07.eas.model.StudentInfo;
import com.you07.eas.service.ClassInfoService;
import com.you07.eas.service.StudentInfoService;
import com.you07.util.message.MessageListBean;
import com.you07.vtp.model.LocationCampusInfo;
import com.you07.vtp.model.LocationHistory;
import com.you07.vtp.model.LocationSystemConfig;
import com.you07.vtp.model.LocationTrackManager;
import com.you07.vtp.service.LocationCampusInfoService;
import com.you07.vtp.service.LocationSystemConfigService;
import com.you07.vtp.service.LocationTrackManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * 组织机构信息接口controller
 * @version 1.0
 * @author RY
 * @since 2018-6-21 17:18:30
 */

@RestController
@CrossOrigin
@Api(value="组织机构信息 controller",tags={"组织机构信息接口"})
public class OrganizationController {

    @Autowired
    private ClassInfoService classInfoService;
    @Autowired
    private StudentInfoService studentInfoService;
    @Autowired
    private LocationTrackManagerService locationTrackManagerService;

    @GetMapping("/orgXml")
    @ApiOperation("获取组织机构信息")
    public String orgXml(HttpServletResponse httpServletResponse,
                            @ApiParam(name="userid",value="用户ID",required=true) @RequestParam("userid") String userid){
        try {
            // 设置响应头
            // 指定内容类型为 xml 格式
            httpServletResponse.setContentType("text/xml");
            // 防止中文乱码
            httpServletResponse.setCharacterEncoding("UTF-8");
            // 向响应中写入数据
            PrintWriter writer = httpServletResponse.getWriter();
            writer.write(getUserByFile(userid));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 读取XML文件
     * @return
     * @throws FileNotFoundException
     */
    private String getUserByFile(String usercode) throws FileNotFoundException {
        StringBuffer temp = new StringBuffer();
        File file = new File(getRootPath()
                + "user-xml" + System.getProperty("file.separator")
                +  usercode + ".xml");
        if(!file.exists()){
            return "";
        } else{
            String str;
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                while ((str = in.readLine()) != null)
                {
                    temp.append(str);
                }
                in.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return temp.toString();
        }

    }

    @GetMapping("/privilegeXml")
    @ApiOperation("获取权限结构信息")
    public String privilegeXml(HttpServletResponse httpServletResponse,
                            @ApiParam(name="userid",value="用户ID",required=true) @RequestParam("userid") String userid){
        try {
            // 设置响应头
            // 指定内容类型为 xml 格式
            httpServletResponse.setContentType("text/xml");
            // 防止中文乱码
            httpServletResponse.setCharacterEncoding("UTF-8");
            // 向响应中写入数据
            PrintWriter writer = httpServletResponse.getWriter();
            writer.write(getPrivilegeByFile(userid));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @ApiOperation("搜索班级")
    @PostMapping("/searchClassInfo")
    @ResponseBody
    public String searchClassInfo(@ApiParam(name="keyWord",value="关键词，班级代码/名称",required=true) @RequestParam("keyWord") String keyWord,
                                   @ApiParam(name="managerId",value="管理员ID",required=false) @RequestParam("managerId") String managerId){
        MessageListBean<ClassInfo> messageListBean = new MessageListBean<ClassInfo>();
        try {
            LocationTrackManager manager = locationTrackManagerService.get(managerId);
            String orgCodes = manager.getOrgCodes();
            List<ClassInfo> list = classInfoService.selectWithPrivilegeOrgCodes(keyWord, orgCodes);
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

    @ApiOperation("搜索学生")
    @PostMapping("/searchStudentInfo")
    @ResponseBody
    public String searchStudentInfo(@ApiParam(name="keyWord",value="关键词，学生学号/名称",required=true) @RequestParam("keyWord") String keyWord,
                                  @ApiParam(name="managerId",value="管理员ID",required=false) @RequestParam("managerId") String managerId){
        MessageListBean<StudentInfo> messageListBean = new MessageListBean<StudentInfo>();
        try {
            LocationTrackManager manager = locationTrackManagerService.get(managerId);
            String orgCodes = manager.getOrgCodes().replaceAll(",", "','");
            List<StudentInfo> list = studentInfoService.selectWithPrivilegeOrgCodes(keyWord, orgCodes);
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

    /**
     * 读取XML文件
     * @return
     * @throws FileNotFoundException
     */
    private String getPrivilegeByFile(String usercode) throws FileNotFoundException {
        StringBuffer temp = new StringBuffer();
        File file = new File(getRootPath()
                + "privilege-xml" + System.getProperty("file.separator")
                +  usercode + ".xml");
        if(!file.exists()){
            file = new File(getRootPath()
                    + "privilege-xml" + System.getProperty("file.separator")
                    +  "all.xml");
        }
        String str;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            while ((str = in.readLine()) != null)
            {
                temp.append(str);
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp.toString();

    }

    public String getRootPath() throws FileNotFoundException {
        return ClassUtils.getDefaultClassLoader().getResource("").getPath();
    }
}
