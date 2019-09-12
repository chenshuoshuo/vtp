package com.you07.eas.service;

import com.alibaba.fastjson.JSONObject;
import com.you07.eas.model.ClassInfo;
import com.you07.eas.model.Result;
import com.you07.eas.model.StudentInfo;
import com.you07.util.RestTemplateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClassInfoService {

    public List<ClassInfo> selectWithPrivilegeOrgCodes(String keyWord, String privilegeOrgCodes){
        String[] strings = privilegeOrgCodes.split(",");
        JSONObject jsonObject = null;
        StringBuilder sb = new StringBuilder();
        sb.append("/os/classInfo/searchWithClassCodeAndKeyWord");
        if (keyWord != null && StringUtils.isEmpty("")) {
            sb.append("?keyword=" + keyWord);
        }
        if (privilegeOrgCodes != null && StringUtils.isEmpty("privilegeOrgCodes")) {
            for(int i = 0; i < strings.length; i++) {
                sb.append("&classcode=" + strings[i]);
            }
        }
        jsonObject = RestTemplateUtil.getJSONObjectForCmIps(sb.toString());
        List<ClassInfo> classInfoList = new ArrayList<>();
        Result<List<ClassInfo>> listResult = jsonObject.toJavaObject(Result.class);
        List<ClassInfo> classInfoList1 = listResult.getData();
        for (int i = 0; i < classInfoList1.size(); i++){
            ClassInfo classInfo = new ClassInfo();
            List<String> rulerList = (List<String>) classInfoList1.get(i);
            classInfo.setClasscode(rulerList.get(0));
            classInfo.setMajorCode(rulerList.get(1));
            classInfo.setClassname(rulerList.get(2));
            classInfo.setGrade(String.valueOf(rulerList.get(3)));
            classInfoList.add(classInfo);
        }
        return classInfoList;
    }
}
