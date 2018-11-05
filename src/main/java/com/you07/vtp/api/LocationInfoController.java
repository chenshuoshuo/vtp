package com.you07.vtp.api;

import com.alibaba.fastjson.JSON;
import com.you07.util.message.MessageBean;
import com.you07.vtp.model.LocationHistory;
import com.you07.vtp.model.LocationLatest;
import com.you07.vtp.service.LocationLatestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 位置信息插入接口
 * @version 1.0
 * @author RY
 * @since 2018-10-16 17:25:28
 */

@RestController
@CrossOrigin
@RequestMapping("/api")
@Api(value="位置信息插入接口controller",tags={"位置信息插入接口"})
public class LocationInfoController {

    @Autowired
    private LocationLatestService locationLatestService;

    @ApiOperation("根据用户插入位置信息")
    @PostMapping("/addLocation")
    @ResponseBody
    public String addLocation(@ApiParam(name="locationInfo",value="位置信息",required=true) @RequestBody LocationLatest locationInfo){
        MessageBean<LocationHistory> messageBean = new MessageBean<LocationHistory>();
        try {
            if(locationLatestService.saveLocation(locationInfo) == 1){
                messageBean.setStatus(true);
                messageBean.setCode(200);
                messageBean.setMessage("插入成功");
            } else{
                messageBean.setStatus(false);
                messageBean.setCode(10002);
                messageBean.setMessage("插入失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageBean.setStatus(false);
            messageBean.setCode(10001);
            messageBean.setMessage("接口错误");
        }
        return JSON.toJSONString(messageBean);
    }

}
