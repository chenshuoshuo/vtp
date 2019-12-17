package com.you07.eas.service;

import com.alibaba.fastjson.JSONObject;
import com.you07.eas.model.ClassInfo;
import com.you07.eas.model.Result;
import com.you07.eas.model.StudentInfo;
import com.you07.util.RestTemplateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ClassInfoService {

    public List<ClassInfo> selectWithPrivilegeOrgCodes(String keyWord, String privilegeOrgCodes){
        List<String> orgs = Arrays.asList(privilegeOrgCodes.split(","));
        JSONObject jsonObject = null;
        StringBuilder sb = new StringBuilder();
        sb.append("/os/classInfo/searchWithClassCodeAndKeyWord?page=0&pageSize=100000");
        if (keyWord != null && StringUtils.isEmpty("")) {
            sb.append("&keyword=" + keyWord);
        }
        jsonObject = RestTemplateUtil.getJSONObjectForCmIps(sb.toString());
        List<ClassInfo> classInfoList = jsonObject.getJSONObject("data").getJSONArray("content").toJavaList(ClassInfo.class);
        for(int i=0;i<classInfoList.size(); i++){
            ClassInfo classInfo = classInfoList.get(i);
            if(!orgs.contains(classInfo.getClasscode()))
                classInfoList.remove(i--);
        }
        return classInfoList;
    }
}
