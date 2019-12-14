package com.you07.vtp.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.you07.util.VTPFileUtil;
import com.you07.util.message.MessageBean;
import com.you07.vtp.model.LocationTrackManager;
import com.you07.vtp.service.LocationTrackManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理接口
 * @version 1.0
 * @author RY
 * @since 2018-8-27 09:52:02
 */

@RestController
@CrossOrigin
@RequestMapping("/manager")
@Api(value="用户管理接口controller",tags={"用户管理接口"})
public class LocationTrackManagerController {
    @Autowired
    private LocationTrackManagerService locationTrackManagerService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ApiOperation("获取用户信息分页")
    @PostMapping("/pageQuery")
    @ResponseBody
    private String loadManagerPage(@ApiParam(name="userid",value="用户ID",required=false) @RequestParam(name = "userid", required = false, defaultValue = "") String userid,
                                   @ApiParam(name="username",value="用户名",required=false) @RequestParam(name = "username", required = false, defaultValue = "") String username,
                                   @ApiParam(name="page",value="页码",required=true) @RequestParam(name = "page", required = true) Integer page,
                                   @ApiParam(name="pageSize",value="每页数据条数",required=true) @RequestParam(name = "pageSize", required = true) Integer pageSize){
        MessageBean<PageInfo<LocationTrackManager>> messageBean = new MessageBean<>();

        try {
            Page<LocationTrackManager> pageBean = PageHelper.startPage(page, pageSize);
            List<LocationTrackManager> list = locationTrackManagerService.listQuery(userid, username);
            PageInfo<LocationTrackManager> pageInfo = new PageInfo<>(pageBean);
            if(pageInfo.getList().size() > 0){
                messageBean.setData(pageInfo);
                messageBean.setStatus(true);
                messageBean.setCode(200);
                messageBean.setMessage("获取成功");
            } else{
                messageBean.setStatus(false);
                messageBean.setCode(10002);
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

    /**
     * 获取详情
     * @param userid
     * @return
     */
    @ApiOperation("获取用户信息详情")
    @GetMapping("/detail/{id}")
    @ResponseBody
    public String loadEnrollmentDetail(@ApiParam(name="userid",value="用户信息ID",required=true) @PathVariable("userid") String userid){
        MessageBean<LocationTrackManager> messageBean = new MessageBean<LocationTrackManager>(null);

        try {
            LocationTrackManager locationTrackManager = locationTrackManagerService.get(userid);

            if(locationTrackManager != null){
                messageBean.setData(locationTrackManager);
                messageBean.setStatus(true);
                messageBean.setCode(200);
                messageBean.setMessage("获取成功");
            } else{
                messageBean.setStatus(false);
                messageBean.setCode(10002);
                messageBean.setMessage("没有查询到数据");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            messageBean.setStatus(false);
            messageBean.setCode(10001);
            messageBean.setMessage("接口错误");
        }

        return JSON.toJSONString(messageBean);
    }

    /**
     * 添加用户信息
     * @param  locationTrackManager 用户信息对象
     * @return
     */
    @ApiOperation("添加用户信息")
    @PostMapping("/add")
    @ResponseBody
    public String add(@ApiParam(name="locationTrackManager",value="用户信息对象",required=true) @RequestBody LocationTrackManager locationTrackManager){
        MessageBean<LocationTrackManager> messageBean = new MessageBean<LocationTrackManager>(null);

        try {
            if(locationTrackManagerService.get(locationTrackManager.getUserid()) == null){
                ShaPasswordEncoder sha = new ShaPasswordEncoder();
                locationTrackManager.setPassword(sha.encodePassword(locationTrackManager.getPassword(), locationTrackManager.getUserid()));
                locationTrackManager.setPosttime(new Date());

                int insertCount = locationTrackManagerService.add(locationTrackManager);
                if(insertCount > 0){
                    //updatePrivilege(locationTrackManager.getUserid());
                    messageBean.setStatus(true);
                    messageBean.setCode(200);
                    messageBean.setMessage("添加成功");
                } else{
                    messageBean.setStatus(false);
                    messageBean.setCode(10002);
                    messageBean.setMessage("添加失败");
                }
            } else{
                messageBean.setStatus(false);
                messageBean.setCode(10002);
                messageBean.setMessage("用户ID重复");
            }

        } catch (Exception e) {
            e.printStackTrace();
            messageBean.setStatus(false);
            messageBean.setCode(10001);
            messageBean.setMessage("接口错误");
        }

        return JSON.toJSONString(messageBean);
    }

    @ApiOperation("更新用户信息")
    @PostMapping("/update")
    @ResponseBody
    public String update(@ApiParam(name="locationTrackManager",value="用户信息对象",required=true) @RequestBody LocationTrackManager locationTrackManager){
        MessageBean<LocationTrackManager> messageBean = new MessageBean<LocationTrackManager>(null);

        try {
                /*ShaPasswordEncoder sha = new ShaPasswordEncoder();
                locationTrackManager.setPassword(sha.encodePassword(locationTrackManager.getPassword(), locationTrackManager.getUserid()));
                locationTrackManager.setPosttime(new Date());*/
            LocationTrackManager manager = locationTrackManagerService.get(locationTrackManager.getUserid());
            manager.setUsername(locationTrackManager.getUsername());
            manager.setIsManager(locationTrackManager.getIsManager());
            manager.setOrgCodes(locationTrackManager.getOrgCodes());
            manager.setOrgNames(locationTrackManager.getOrgNames());
            int updatecount = locationTrackManagerService.update(manager);
            if(updatecount > 0){
                updatePrivilege(locationTrackManager.getUserid());
                messageBean.setStatus(true);
                messageBean.setCode(200);
                messageBean.setMessage("更新成功");
            } else{
                messageBean.setStatus(false);
                messageBean.setCode(10002);
                messageBean.setMessage("更新失败");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);

            messageBean.setStatus(false);
            messageBean.setCode(10001);
            messageBean.setMessage("接口错误");
        }

        return JSON.toJSONString(messageBean);
    }

    /**
     * 删除用户信息
     * @param userid 用户信息ID
     * @return
     */
    @ApiOperation("根据ID删除用户信息")
    @PostMapping("/delete")
    @ResponseBody
    public String delete(@ApiParam(name="userid",value="用户信息ID",required=true) @RequestParam(name = "userid", required = true) String userid){
        MessageBean<LocationTrackManager> messageBean = new MessageBean<LocationTrackManager>(null);

        try {
            locationTrackManagerService.delete(userid);
            messageBean.setStatus(true);
            messageBean.setCode(200);
            messageBean.setMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            messageBean.setStatus(false);
            messageBean.setCode(10001);
            messageBean.setMessage("接口错误");
        }

        return JSON.toJSONString(messageBean);
    }

    /**
     * 批量删除用户信息
     * @param ids 用户信息ID，多个以','分隔
     * @return
     */
    @ApiOperation("批量删除用户信息")
    @PostMapping("/bulkDelete")
    @ResponseBody
    public String bulkDelete(@ApiParam(name="ids",value="用户信息ID，多个以','分隔",required=true) @RequestParam(name = "ids", required = true) String ids){
        MessageBean<LocationTrackManager> messageBean = new MessageBean<LocationTrackManager>(null);

        try {
            String[] idArray = ids.split(",");
            for(String str : idArray){
                locationTrackManagerService.delete(str);
            }
            messageBean.setStatus(true);
            messageBean.setCode(200);
            messageBean.setMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            messageBean.setStatus(false);
            messageBean.setCode(10001);
            messageBean.setMessage("接口错误");
        }

        return JSON.toJSONString(messageBean);
    }


    public void updatePrivilege(String userid) throws IOException, DocumentException {
        LocationTrackManager locationTrackManager = locationTrackManagerService.get(userid);

        String[] orgCodeArray = locationTrackManager.getOrgCodes().split(",");
        Map<String, String> orgCodeMap = new HashMap<>();
        for(String orgCode : orgCodeArray){
            orgCodeMap.put(orgCode, orgCode);
        }

        // 用户文件生成
        File userXML = new File(VTPFileUtil.getRootPath()
                + "user-xml" + System.getProperty("file.separator")
                +  locationTrackManager.getUserid() + ".xml");
        if(!userXML.exists()){
            userXML = new File(VTPFileUtil.getRootPath()
                    + "user-xml" + System.getProperty("file.separator")
                    +  locationTrackManager.getUserid() + ".xml");
        }

        // 满用户文件读取
        File allUserXML = new File(VTPFileUtil.getRootPath()
                + "user-xml" + System.getProperty("file.separator")
                +  "asXML.xml");

        SAXReader reader = new SAXReader();
        Document document = reader.read(allUserXML);
        Element root = document.getRootElement();
        Element teaElement = root.element("teacher");
        List<Element> deptList = teaElement.elements();
        Integer teacherCount = 0;
        for(Element deptElement : deptList){
            if(!orgCodeMap.containsKey(deptElement.attribute("id").getValue())){
                teaElement.remove(deptElement);
            } else {
                teacherCount += Integer.valueOf(teaElement.attribute("count").getValue());
            }
        }
        teaElement.attribute("count").setValue(teacherCount.toString());


        Element stuElement = root.element("student");
        List<Element> adList = stuElement.elements();
        Integer stuCount = 0;
        for(Element adElement : adList){
            List<Element> ciList = adElement.elements();
            Integer adStudentCount = 0;
            for(Element ciElement : ciList){
                if(!orgCodeMap.containsKey(ciElement.attribute("id").getValue())){
                    adElement.remove(ciElement);
                } else{
                    adStudentCount += Integer.valueOf(ciElement.attribute("count").getValue());
                }
            }
            if(adElement.elements().size() == 0){
                stuElement.remove(adElement);
            } else{
                adElement.attribute("count").setValue(adStudentCount.toString());
                stuCount += adStudentCount;
            }
        }
        stuElement.attribute("count").setValue(stuCount.toString());

        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(new FileOutputStream(userXML), format);
        writer.setEscapeText(false);
        writer.write(document);

        // 权限文件生成
        File privilegeXML = new File(VTPFileUtil.getRootPath()
                + "privilege-xml" + System.getProperty("file.separator")
                +  locationTrackManager.getUserid() + ".xml");
        if(!privilegeXML.exists()){
            privilegeXML = new File(VTPFileUtil.getRootPath()
                    + "privilege-xml" + System.getProperty("file.separator")
                    +  locationTrackManager.getUserid() + ".xml");
        }

        // 满权限文件读取
        File allPrivilegeXML = new File(VTPFileUtil.getRootPath()
                + "privilege-xml" + System.getProperty("file.separator")
                +  "all.xml");

        reader = new SAXReader();
        document = reader.read(allPrivilegeXML);
        root = document.getRootElement();
        teaElement = root.element("teacher");
        deptList = teaElement.elements();
        for(Element deptElement : deptList){
            if(!orgCodeMap.containsKey(deptElement.attribute("id").getValue())){
                deptElement.addAttribute("checked", "0");
            } else{
                deptElement.addAttribute("checked", "1");
            }
        }

        stuElement = root.element("student");
        adList = stuElement.elements();
        for(Element adElement : adList){
            List<Element> ciList = adElement.elements();
            int checkedCount = 0;
            for(Element ciElement : ciList){
                if(!orgCodeMap.containsKey(ciElement.attribute("id").getValue())){
                    ciElement.addAttribute("checked", "0");
                } else{
                    ciElement.addAttribute("checked", "1");
                    checkedCount += 1;
                }
            }
            if(adElement.elements().size() == checkedCount){
                adElement.addAttribute("checked", "1");
            } else{
                adElement.addAttribute("checked", "0");
            }
        }

        writer = new XMLWriter(new FileOutputStream(privilegeXML), format);
        writer.setEscapeText(false);
        writer.write(document);

    }


}
