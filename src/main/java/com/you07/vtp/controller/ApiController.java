package com.you07.vtp.controller;

import com.you07.vtp.model.LocationCampusInfo;
import com.you07.vtp.model.LocationSystemConfig;
import com.you07.vtp.service.LocationCampusInfoService;
import com.you07.vtp.service.LocationSystemConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

/**
 * 配置信息接口controller
 * @version 1.0
 * @author RY
 * @since 2018-6-21 17:18:30
 */

@RestController
@CrossOrigin
@Api(value="配置信息 controller",tags={"配置信息接口"})
public class ApiController {
    @Autowired
    private LocationSystemConfigService locationSystemConfigService;
    @Autowired
    private LocationCampusInfoService locationCampusInfoService;

    @GetMapping("/vtpConfig")
    @ApiOperation("获取配置信息")
    public String vtpConfig(HttpServletResponse httpServletResponse){
        try {
            // 设置响应头
            // 指定内容类型为 JSON 格式
            httpServletResponse.setContentType("text/javascript");
            // 防止中文乱码
            httpServletResponse.setCharacterEncoding("UTF-8");
            // 向响应中写入数据
            PrintWriter writer = httpServletResponse.getWriter();
            writer.write("var vtpConfig = " + loadVtpConfig() + ";");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 从数据库获取配置
     * @return
     */
    public String loadVtpConfig(){

        // 配置信息对象
        StringBuffer configJson = new StringBuffer();
        LocationSystemConfig locationSystemConfig = locationSystemConfigService.loadDefault();
        configJson.append("{");
        configJson.append("lqMapGisUrl:'" + locationSystemConfig.getLqMapGisUrl() + "',");
        configJson.append("amapKey:'" + locationSystemConfig.getAmapKey() + "',");
        configJson.append("topBgColor:'" + locationSystemConfig.getTopBgColor() + "',");
        configJson.append("rightTopBgColor:'" + locationSystemConfig.getRightTopBgColor() + "',");
        configJson.append("rightBottomBgColor:'" + locationSystemConfig.getRightBottomBgColor() + "',");
        configJson.append("logo:'" + locationSystemConfig.getLogo() + "',");
        configJson.append("waterMarkLogo:'" + locationSystemConfig.getWaterMarkLogo() + "',");
        configJson.append("gateImg:'" + locationSystemConfig.getGateImg() + "',");
        configJson.append("trackIcon:'" + locationSystemConfig.getTrackIcon() + "',");
        configJson.append("locationIcon:'" + locationSystemConfig.getLocationIcon() + "',");
        configJson.append("safetyIcon:'" + locationSystemConfig.getSafetyIcon() + "',");
        configJson.append("campusSwitchIcon:'" + locationSystemConfig.getCampusSwitchIcon() + "',");
        configJson.append("contact:'" + locationSystemConfig.getContact() + "',");


        // 校区信息对象
        List<LocationCampusInfo> locationCampusInfoList = locationCampusInfoService.queryAllDisplay();
        configJson.append("campustList:[");
        for(LocationCampusInfo locationCampusInfo : locationCampusInfoList){
            configJson.append("{");
            configJson.append("campusId:" + locationCampusInfo.getCampusId() + ",");
            configJson.append("campusName:'" + locationCampusInfo.getCampusName() + "',");
            configJson.append("campusLogo:'" + locationCampusInfo.getCampusLogo() + "',");
            configJson.append("isDefault:" + locationCampusInfo.getIsDefault() + ",");
            configJson.append("amapLngLat:'" + locationCampusInfo.getAmapLngLat() + "'");
            configJson.append("},");
        }
        if(locationCampusInfoList.size() > 0){
            configJson.deleteCharAt(configJson.length() - 1);
        }
        configJson.append("]}");

        return configJson.toString().replaceAll("null", "");
    }
}
