package com.you07.vtp.controller;

import com.alibaba.fastjson.JSON;
import com.you07.util.message.MessageBean;
import com.you07.util.message.MessageListBean;
import com.you07.vtp.model.LocationCampusInfo;
import com.you07.vtp.model.LocationSystemConfig;
import com.you07.vtp.model.SystemConfigVO;
import com.you07.vtp.service.LocationCampusInfoService;
import com.you07.vtp.service.LocationSystemConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 系统配置接口
 * @version 1.0
 * @author RY
 */

@RestController
@CrossOrigin
@Api(value="系统配置 controller",tags={"系统配置接口"})
@RequestMapping("/systemConfig")
public class SystemConfigVOController {

    @Autowired
    private LocationSystemConfigService locationSystemConfigService;
    @Autowired
    private LocationCampusInfoService locationCampusInfoService;

    @ApiOperation("获取产品配置信息")
    @GetMapping("/loadProductConfig")
    @ResponseBody
    public String loadProductConfig(){
        MessageBean<SystemConfigVO> messageBean = new MessageBean<SystemConfigVO>(null);

        try {
            SystemConfigVO systemConfigVO = new SystemConfigVO();
            systemConfigVO.setLocationSystemConfig(locationSystemConfigService.loadDefault());
            systemConfigVO.setCampusInfoList(locationCampusInfoService.queryAll());

            messageBean.setData(systemConfigVO);
            messageBean.setStatus(true);
            messageBean.setCode(200);
            messageBean.setMessage("获取成功");
        } catch (Exception e) {
            e.printStackTrace();
            messageBean.setStatus(false);
            messageBean.setCode(10001);
            messageBean.setMessage("接口错误");
        }

        return JSON.toJSONString(messageBean);
    }

    @ApiOperation("保存地图配置信息")
    @PutMapping("/saveMapConfig")
    @ResponseBody
    public String saveMapConfig(@ApiParam(name="locationSystemConfig",value="产品配置信息对象",required=true) @RequestBody LocationSystemConfig locationSystemConfig){
        MessageBean<LocationSystemConfig> messageBean = new MessageBean<LocationSystemConfig>(null);

        try {
            locationSystemConfigService.update(locationSystemConfig);

            messageBean.setData(locationSystemConfigService.loadDefault());
            messageBean.setStatus(true);
            messageBean.setCode(200);
            messageBean.setMessage("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            messageBean.setStatus(false);
            messageBean.setCode(10001);
            messageBean.setMessage("接口错误");
        }

        return JSON.toJSONString(messageBean);
    }

    @ApiOperation("保存校区配置信息")
    @PutMapping("/saveCampusConfig")
    @ResponseBody
    public String saveCampusConfig(@ApiParam(name="campusInfoList",value="校区信息列表",required=true) @RequestBody List<LocationCampusInfo> campusInfoList){
        MessageListBean<LocationCampusInfo> messageListBean = new MessageListBean<LocationCampusInfo>(null);

        try {
            for(LocationCampusInfo locationSystemConfig : campusInfoList){
                locationCampusInfoService.update(locationSystemConfig);
            }
            messageListBean.setData(locationCampusInfoService.queryAll());
            messageListBean.setStatus(true);
            messageListBean.setCode(200);
            messageListBean.setMessage("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            messageListBean.setStatus(false);
            messageListBean.setCode(10001);
            messageListBean.setMessage("接口错误");
        }

        return JSON.toJSONString(messageListBean);
    }

    @ApiOperation("获取综合配置信息")
    @GetMapping("/loadGeneralConfig")
    @ResponseBody
    public String loadGeneralConfig(){
        MessageBean<LocationSystemConfig> messageBean = new MessageBean<LocationSystemConfig>(null);

        try {
            LocationSystemConfig locationSystemConfig = locationSystemConfigService.loadDefault();

            messageBean.setData(locationSystemConfig);
            messageBean.setStatus(true);
            messageBean.setCode(200);
            messageBean.setMessage("获取成功");
        } catch (Exception e) {
            e.printStackTrace();
            messageBean.setStatus(false);
            messageBean.setCode(10001);
            messageBean.setMessage("接口错误");
        }

        return JSON.toJSONString(messageBean);
    }

    @ApiOperation("保存综合配置信息")
    @PutMapping("/saveGeneralConfig")
    @ResponseBody
    public String saveGeneralConfig(@ApiParam(name="locationSystemConfig",value="产品配置信息对象",required=true) @RequestBody LocationSystemConfig locationSystemConfig){
        MessageBean<LocationSystemConfig> messageBean = new MessageBean<LocationSystemConfig>(null);

        try {
            locationSystemConfig.setUpdateTime(new Date());
            locationSystemConfigService.update(locationSystemConfig);

            messageBean.setData(locationSystemConfig);
            messageBean.setStatus(true);
            messageBean.setCode(200);
            messageBean.setMessage("获取成功");
        } catch (Exception e) {
            e.printStackTrace();
            messageBean.setStatus(false);
            messageBean.setCode(10001);
            messageBean.setMessage("接口错误");
        }

        return JSON.toJSONString(messageBean);
    }
}
