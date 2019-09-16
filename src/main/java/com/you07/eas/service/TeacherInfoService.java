package com.you07.eas.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.you07.eas.model.Result;
import com.you07.eas.model.StudentInfo;
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
        if (jsonObject.getJSONObject("data").getString("staffNumber") != null) {
            TeacherInfo teacherInfo = new TeacherInfo();
            String name = jsonObject.getJSONObject("data").getString("realName");
            String gender = jsonObject.getJSONObject("data").getString("gender");
            String organizationCode = jsonObject.getJSONObject("data").getString("organizationCode");
            //根据机构编号得到机构名
            JSONObject jsonObject1 = null;
            jsonObject1 = RestTemplateUtil.getJSONObjectForCmIps("/os/organization/get/" + organizationCode);
            String organizationName = jsonObject1.getJSONObject("data").getString("organizationName");
            teacherInfo.setGender(gender);
            teacherInfo.setName(name);
            teacherInfo.setOrgCode(organizationCode);
            teacherInfo.setOrgName(organizationName);
            return teacherInfo;
        }else{
            return null;
        }
    }

   /* public List<TeacherInfo> searchWithCodeName(String keyword) throws IOException {
        JSONObject jsonObject = null;
        jsonObject = RestTemplateUtil.getJSONObjectForCmIps("/os/teachingStaff/search?keyWord="+keyword);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        List<TeacherInfo> teacherInfos = jsonArray.toJavaList(TeacherInfo.class);
        List<TeacherInfo> teacherInfoList = new ArrayList<>();
        *//*Result<List<TeacherInfo>> listResult = jsonObject.toJavaObject(Result.class);
        List<TeacherInfo> teacherInfos = listResult.getData();*//*
        for (TeacherInfo t : teacherInfos) {
            teacherInfoList.add(t);
        }
        *//*for(int i = 0; i < teacherInfos.size(); i++) {
            TeacherInfo teacherInfo = new TeacherInfo();
            Map<String, Object> map = new LinkedHashMap<>();
            map = (Map<String, Object>) teacherInfos.get(i);
            teacherInfo.setTeachercode((String) map.get("staffNumber"));
            teacherInfo.setName((String) map.get("realName"));
            teacherInfo.setGender((String) map.get("gender"));
            teacherInfo.setOrgCode((String) map.get("organizationCode"));
            String code = (String) map.get("organizationCode");
            //根据机构编号得到机构名
            JSONObject jsonObject1 = null;
            jsonObject1 = RestTemplateUtil.getJSONObjectForCmIps("/os/organization/get/" + code);
            String organizationName = jsonObject1.getJSONObject("data").getString("organizationName");
            teacherInfo.setOrgName(organizationName);
            teacherInfoList.add(teacherInfo);
        }*//*
        return teacherInfoList;
    }*/
}
