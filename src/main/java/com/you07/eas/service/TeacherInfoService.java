package com.you07.eas.service;

import com.alibaba.fastjson.JSONObject;
import com.you07.eas.model.Result;
import com.you07.eas.model.TeacherInfo;
import com.you07.util.RestTemplateUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class TeacherInfoService {


    public TeacherInfo get(String teacherCode){
        JSONObject jsonObject = null;
        jsonObject = RestTemplateUtil.getJSONObjectForCmIps("/os/teachingStaff/get/"+teacherCode);
        TeacherInfo teacherInfo = new TeacherInfo();
        String name = jsonObject.getJSONObject("data").getString("realName");
        String gender = jsonObject.getJSONObject("data").getString("gender");
        String organizationCode = jsonObject.getJSONObject("data").getString("organizationCode");
        //根据机构编号得到机构名
        JSONObject jsonObject1 = null;
        jsonObject1 = RestTemplateUtil.getJSONObjectForCmIps("/os/organization/get/"+organizationCode);
        String organizationName = jsonObject1.getJSONObject("data").getString("organizationName");
        teacherInfo.setGender(gender);
        teacherInfo.setName(name);
        teacherInfo.setOrgCode(organizationCode);
        teacherInfo.setOrgName(organizationName);
        return teacherInfo;
    }

    public List<TeacherInfo> searchWithCodeName(String keyword) throws IOException {
        JSONObject jsonObject = null;
        jsonObject = RestTemplateUtil.getJSONObjectForCmIps("/os/teacherInfo/search?keyword="+keyword);
        List<TeacherInfo> teacherInfoList = new ArrayList<>();
        Result<Map<String, Object>> listResult = jsonObject.toJavaObject(Result.class);
        Map<String, Object> map = listResult.getData();
        TeacherInfo teacherInfo1 = new TeacherInfo();
        teacherInfo1.setName((String) map.get("realName"));
        teacherInfo1.setGender((String) map.get("gender"));
        teacherInfo1.setOrgCode((String) map.get("organizationCode"));
        String code = (String) map.get("organizationCode");
        //根据机构编号得到机构名
        JSONObject jsonObject1 = null;
        jsonObject1 = RestTemplateUtil.getJSONObjectForCmIps( "/os/organization/get/" + code);
        String organizationName = jsonObject1.getJSONObject("data").getString("organizationName");
        teacherInfo1.setOrgName(organizationName);
        teacherInfoList.add(teacherInfo1);
        return teacherInfoList;
    }
}
