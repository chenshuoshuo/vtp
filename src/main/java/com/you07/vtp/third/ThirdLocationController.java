package com.you07.vtp.third;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.you07.util.message.MessageBean;
import com.you07.util.message.MessageListBean;
import com.you07.util.route.Routing;
import com.you07.vtp.model.LocationHistory;
import com.you07.vtp.model.LocationTrackManager;
import com.you07.vtp.service.LocationHitoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName ThirdLocationController
 * @Description 第三方位置、轨迹controller
 * @Author wells
 * @Date 2019/4/29 10:54
 * @Version 1.0
 **/
@RestController
@CrossOrigin
@RequestMapping("/thirdLocation")
@Api(value="第三方位置、轨迹查询接口 controller",tags={"第三方位置、轨迹查询接口"})
public class ThirdLocationController {

    @Autowired
    private LocationHitoryService locationHitoryService;

    @ApiOperation("根据时间查询定位")
    @GetMapping("/locations")
    @ResponseBody
    public String locations(@ApiParam(name="startTime",value="开始时间，格式：'yyyy-MM-dd HH:mm:ss'",required=true) @RequestParam(name = "startTime",required = true) String startTime,
                            @ApiParam(name="endTime",value="结束时间，格式：'yyyy-MM-dd HH:mm:ss'",required=true) @RequestParam(name = "endTime", required = true, defaultValue = "") String endTime,
                            @ApiParam(name="inSchool",value="校内校外，1校内，2校外",required=true) @RequestParam(name = "inSchool",required = true) Integer inSchool,
                            @ApiParam(name="campusId",value="校区ID",required=true) @RequestParam(name = "campusId",required = true) Integer campusId,
                            @ApiParam(name="page",value="页码",required=true) @RequestParam(name = "page", required = true) Integer page,
                            @ApiParam(name="pageSize",value="每页数据条数",required=true) @RequestParam(name = "pageSize", required = true) Integer pageSize){
        MessageBean<PageInfo<LocationHistory>> messageBean = new MessageBean<>();
        try {
            Page<LocationHistory> pageBean = PageHelper.startPage(page, pageSize);
            List<LocationHistory> list = locationHitoryService.selectTrackWithTimeZone(startTime, endTime, inSchool, campusId);
            PageInfo<LocationHistory> pageInfo = new PageInfo<>(pageBean);
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
        return JSON.toJSONString(messageBean, SerializerFeature.DisableCircularReferenceDetect);
    }

}
