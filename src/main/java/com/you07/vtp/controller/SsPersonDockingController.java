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
import com.you07.vtp.form.UserLocationForm;
import com.you07.vtp.model.LocationTrackManager;
import com.you07.vtp.model.SsPersonDocking;
import com.you07.vtp.model.SsPersonDockingParameter;
import com.you07.vtp.model.UserInfo;
import com.you07.vtp.service.LocationTrackManagerService;
import com.you07.vtp.service.SsPerSonDockingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 人员对接配置
 *
 * @author cs
 * @version 1.0
 */

@RestController
@CrossOrigin
@RequestMapping("/docking")
public class SsPersonDockingController {
    @Autowired
    private SsPerSonDockingService perSonDockingService;

    @GetMapping("/queryAll")
    @ResponseBody
    public String queryAll() {
        MessageListBean<SsPersonDocking> messageListBean = new MessageListBean<SsPersonDocking>(null);
        List<SsPersonDocking> list = perSonDockingService.queryAll();
        if (list.size() > 0) {
            messageListBean.setData(list);
            messageListBean.setStatus(true);
            messageListBean.setCode(200);
            messageListBean.setMessage("获取成功");
        } else {
            messageListBean.setStatus(false);
            messageListBean.setCode(1002);
            messageListBean.setMessage("没有查询到数据");
        }

        return JSON.toJSONString(messageListBean, SerializerFeature.DisableCircularReferenceDetect);
    }

    @GetMapping("/queryAllById")
    @ResponseBody
    public String search(@RequestParam(name = "dockingId") Integer dockingId){
        try {
            MessageListBean<SsPersonDockingParameter> messageListBean = new MessageListBean<SsPersonDockingParameter>(null);
            List<SsPersonDockingParameter> list = perSonDockingService.queryAllParameterById(dockingId);

            if (list.size() > 0) {
                messageListBean.setStatus(true);
                messageListBean.setData(list);
                messageListBean.setCode(200);
                messageListBean.setMessage("获取成功");
            } else {
                messageListBean.setStatus(false);
                messageListBean.setCode(1002);
                messageListBean.setMessage("没有查询到数据");
            }
            return JSON.toJSONString(messageListBean, SerializerFeature.DisableCircularReferenceDetect);
        }catch (Exception e) {
            return JSON.toJSONString("未知异常");
        }
    }
}
