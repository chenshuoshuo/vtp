package com.you07.vtp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.PageInfo;
import com.you07.util.message.MessageBean;
import com.you07.util.message.MessageListBean;
import com.you07.vtp.model.SsGroup;
import com.you07.vtp.model.SsPersonDocking;
import com.you07.vtp.model.SsPersonDockingParameter;
import com.you07.vtp.service.SsGroupService;
import com.you07.vtp.service.SsPerSonDockingService;
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
@RequestMapping("/group")
public class SsGroupController {
    @Autowired
    private SsGroupService groupService;

    @GetMapping("/page")
    @ResponseBody
    public String page(@RequestParam(name = "groupName",required = false)String groupName,
                       @RequestParam(name = "page")Integer page,
                       @RequestParam(name = "pageSize")Integer pageSize) {

        MessageBean<PageInfo> messageBean = new MessageBean<PageInfo>(null);
        PageInfo pageInfo = groupService.queryAll(groupName,page,pageSize);
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
    public String listQuery() {
        MessageListBean<SsGroup> messageBean = new MessageListBean<SsGroup>(null);
        List<SsGroup> list = groupService.listQuery();
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
    public String add(@RequestBody SsGroup ssGroup){
        try {
            MessageBean messageBean = new MessageBean<>(null);
            int add = groupService.add(ssGroup);

            if (add > 0) {
                messageBean.setStatus(true);
                messageBean.setCode(200);
                messageBean.setMessage("添加成功");
            } else if(add == -1){
                messageBean.setStatus(false);
                messageBean.setCode(1001);
                messageBean.setMessage("英文名不能重复");
            }else {
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
    public String update(@RequestBody SsGroup ssGroup){
        try {
            MessageBean messageBean = new MessageBean<>(null);
            Integer update = groupService.update(ssGroup);

            if (update > 0) {
                messageBean.setStatus(true);
                messageBean.setData(update);
                messageBean.setCode(200);
                messageBean.setMessage("更新成功");
            } else if(update == -1){
                messageBean.setStatus(false);
                messageBean.setCode(1001);
                messageBean.setMessage("英文名不能重复");
            }else {
                messageBean.setStatus(false);
                messageBean.setCode(1002);
                messageBean.setMessage("没有查询到数据");
            }
            return JSON.toJSONString(messageBean);
        }catch (Exception e) {
            return JSON.toJSONString("未知异常");
        }
    }

    @GetMapping("/get")
    @ResponseBody
    public String get(@RequestParam(name = "groupId")Integer groupId){
        try {
            MessageBean messageBean = new MessageBean<>(null);
            SsGroup group = groupService.get(groupId);

            if (group != null) {
                messageBean.setStatus(true);
                messageBean.setData(group);
                messageBean.setCode(200);
                messageBean.setMessage("获取成功");
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

    @PutMapping("/blindPerson")
    @ResponseBody
    public String blindPerson(@RequestParam(name = "groupId")Integer groupId,
                              @RequestParam(name = "personIds") String personIds){
        try {
            MessageBean<SsGroup> messageBean = new MessageBean<SsGroup>(null);
            SsGroup ssGroup = groupService.blindPerson(groupId,personIds);

            if (ssGroup != null) {
                messageBean.setStatus(true);
                messageBean.setData(ssGroup);
                messageBean.setCode(200);
                messageBean.setMessage("绑定成功");
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
    public String delete(@RequestParam(name = "groupId") Integer groupId){
        try {
            MessageBean messageBean = new MessageBean<>(null);
            Integer delete = groupService.delete(groupId);

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
    public String bulkDelete(@RequestParam(name = "groupIds") String groupIds){
        try {
            MessageBean messageBean = new MessageBean<>(null);
            Integer delete = groupService.bulkDelete(groupIds);

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
