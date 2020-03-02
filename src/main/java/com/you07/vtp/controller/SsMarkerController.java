package com.you07.vtp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.PageInfo;
import com.you07.util.message.MessageBean;
import com.you07.util.message.MessageListBean;
import com.you07.vtp.model.SsGroup;
import com.you07.vtp.model.SsMarker;
import com.you07.vtp.service.SsGroupService;
import com.you07.vtp.service.SsMarkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 人员对接配置
 *
 * @author cs
 * @version 1.0
 */

@RestController
@CrossOrigin
@RequestMapping("/marker")
public class SsMarkerController {
    @Autowired
    private SsMarkerService markerService;

    @GetMapping("/page")
    @ResponseBody
    public String page(@RequestParam(name = "campusCode")Integer campusCode,
                       @RequestParam(name = "markerName")String markerName,
                       @RequestParam(name = "page")Integer page,
                       @RequestParam(name = "pageSize")Integer pageSize) {

        MessageBean<PageInfo> messageBean = new MessageBean<PageInfo>(null);
        PageInfo pageInfo = markerService.queryAll(campusCode,markerName,page,pageSize);
        if (pageInfo.getList().size() > 0) {
            messageBean.setData(pageInfo);
            messageBean.setStatus(true);
            messageBean.setCode(200);
            messageBean.setMessage("获取成功");
        } else {
            messageBean.setStatus(false);
            messageBean.setCode(1002);
            messageBean.setMessage("没有查询到数据");
        }

        return JSON.toJSONString(messageBean);
    }

    @GetMapping("/listQuery")
    @ResponseBody
    public String listQuery(@RequestParam(name = "campusCode")Integer campusCode) {
        MessageListBean<SsMarker> messageBean = new MessageListBean<SsMarker>(null);
        List<SsMarker> list = markerService.listQuery(campusCode);
        if (list.size() > 0) {
            messageBean.setData(list);
            messageBean.setStatus(true);
            messageBean.setCode(200);
            messageBean.setMessage("获取成功");
        } else {
            messageBean.setStatus(false);
            messageBean.setCode(1002);
            messageBean.setMessage("没有查询到数据");
        }
        return JSON.toJSONString(messageBean,SerializerFeature.DisableCircularReferenceDetect);
    }

    @PostMapping("/add")
    @ResponseBody
    public String add(@RequestBody SsMarker ssMarker){
        try {
            MessageBean messageBean = new MessageBean<>(null);
            ssMarker.setUpdateTime(new Timestamp(new Date().getTime()));
            Integer add = markerService.add(ssMarker);

            if (add > 0) {
                messageBean.setStatus(true);
                messageBean.setData(add);
                messageBean.setCode(200);
                messageBean.setMessage("添加成功");
            } else {
                messageBean.setStatus(false);
                messageBean.setCode(1002);
                messageBean.setMessage("没有查询到数据");
            }
            return JSON.toJSONString(messageBean);
        }catch (Exception e) {
            return JSON.toJSONString("未知异常");
        }
    }

    @PostMapping("/update")
    @ResponseBody
    public String update(@RequestBody SsMarker ssMarker){
        try {
            MessageBean messageBean = new MessageBean<>(null);
            ssMarker.setUpdateTime(new Timestamp(new Date().getTime()));
            Integer update = markerService.update(ssMarker);

            if (update > 0) {
                messageBean.setStatus(true);
                messageBean.setData(update);
                messageBean.setCode(200);
                messageBean.setMessage("更新成功");
            } else {
                messageBean.setStatus(false);
                messageBean.setCode(1002);
                messageBean.setMessage("没有查询到数据");
            }
            return JSON.toJSONString(messageBean);
        }catch (Exception e) {
            return JSON.toJSONString("未知异常");
        }
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam(name = "markerId") Integer markerId){
        try {
            MessageBean messageBean = new MessageBean<>(null);
            Integer delete = markerService.delete(markerId);

            if (delete > 0) {
                messageBean.setStatus(true);
                messageBean.setData(delete);
                messageBean.setCode(200);
                messageBean.setMessage("删除成功");
            } else {
                messageBean.setStatus(false);
                messageBean.setCode(1002);
                messageBean.setMessage("没有查询到数据");
            }
            return JSON.toJSONString(messageBean);
        }catch (Exception e) {
            return JSON.toJSONString("未知异常");
        }
    }

    @DeleteMapping("/bulkDelete")
    @ResponseBody
    public String bulkDelete(@RequestParam(name = "markerIds") String markerIds){
        try {
            MessageBean messageBean = new MessageBean<>(null);
            Integer delete = markerService.bulkDelete(markerIds);

            if (delete > 0) {
                messageBean.setStatus(true);
                messageBean.setData(delete);
                messageBean.setCode(200);
                messageBean.setMessage("删除成功");
            } else {
                messageBean.setStatus(false);
                messageBean.setCode(1002);
                messageBean.setMessage("没有查询到数据");
            }
            return JSON.toJSONString(messageBean);
        }catch (Exception e) {
            return JSON.toJSONString("未知异常");
        }
    }

}
