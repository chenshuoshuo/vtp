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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TeacherInfoService {


    public TeacherInfo get(String teacherCode) {
        JSONObject jsonObject = null;
        jsonObject = RestTemplateUtil.getJSONObjectForCmIps("/os/teachingStaff/get/"+teacherCode);
        if (jsonObject.getJSONObject("data").getString("staffNumber") != null) {
            TeacherInfo teacherInfo = new TeacherInfo();
            String name = jsonObject.getJSONObject("data").getString("realName");
            String gender = jsonObject.getJSONObject("data").getString("gender");
            String organizationCode = jsonObject.getJSONObject("data").getString("organizationCode");
            //根据机构编号得到机构名
            JSONObject jsonObjectOrg = null;
            jsonObjectOrg = RestTemplateUtil.getJSONObjectForCmIps("/os/organization/get/" + organizationCode);
            String organizationName = jsonObjectOrg.getJSONObject("data").getString("organizationName");
            teacherInfo.setGender(gender);
            teacherInfo.setName(name);
            teacherInfo.setOrgCode(organizationCode);
            teacherInfo.setOrgName(organizationName);
            return teacherInfo;
        }else{
            return null;
        }
    }

    public List<TeacherInfo> searchWithCodeName(String keyword, String page, String pageSize) throws IOException {
        JSONObject jsonObject = null;
        String pattern = "^[0-9]\\d*$";
        Pattern patternSecond = Pattern.compile(pattern);
        Matcher matcher = patternSecond.matcher(keyword);
        boolean matches = matcher.matches();
        String staffNumber = null;
        String realName = null;
        if (matches == true && keyword != null) {
            staffNumber = keyword;
        } else {
            realName = keyword;
        }
        StringBuilder sb = new StringBuilder("/os/teachingStaff/pageQuery?page=" + page + "&pageSize="+ pageSize);
        if (staffNumber != null) {
            sb.append("&staffNumber=" + staffNumber);
        }
        else if (realName != null) {
            sb.append("&realName=" + realName);
        }
        jsonObject = RestTemplateUtil.getJSONObjectForCmIps(sb.toString());
        List<TeacherInfo> teacherInfoList = new ArrayList<>();
        Map<String, Object> objectMap = (Map<String, Object>) jsonObject.get("data");
        List<Object> objectList = (List<Object>) objectMap.get("content");
        for(int i = 0; i < objectList.size(); i++) {
            TeacherInfo teacherInfo = new TeacherInfo();
            Map<String, Object> map = new LinkedHashMap<>();
            map = (Map<String, Object>) objectList.get(i);
            teacherInfo.setTeachercode((String) map.get("staffNumber"));
            teacherInfo.setName((String) map.get("realName"));
            teacherInfo.setGender((String) map.get("gender"));
            teacherInfo.setOrgCode((String) map.get("organizationCode"));
            String code = (String) map.get("organizationCode");
            //根据机构编号得到机构名
            JSONObject jsonObjectOrg = null;
            jsonObjectOrg = RestTemplateUtil.getJSONObjectForCmIps("/os/organization/get/" + code);
            String organizationName = jsonObjectOrg.getJSONObject("data").getString("organizationName");
            teacherInfo.setOrgName(organizationName);
            teacherInfoList.add(teacherInfo);
        }
        return teacherInfoList;
    }
}
