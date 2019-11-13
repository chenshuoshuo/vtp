package com.you07.eas.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.you07.config.datasource.DataBaseContextHolder;
import com.you07.config.datasource.annotation.DataSourceConnection;
import com.you07.eas.model.ClassInfo;
import com.you07.eas.model.Result;
import com.you07.eas.model.StudentInfo;
import com.you07.eas.model.TeacherInfo;
import com.you07.util.DateUtil;
import com.you07.util.RestTemplateUtil;
import javafx.beans.binding.StringBinding;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentInfoService {
    public StudentInfo get(String studentNo) {
        JSONObject jsonObject = null;
        jsonObject = RestTemplateUtil.getJSONObjectForCmIps("/os/studentInfo/get/" + studentNo);
        if (jsonObject.getJSONObject("data").getString("studentNo") != null) {
            StudentInfo studentInfo = new StudentInfo();
            String name = jsonObject.getJSONObject("data").getString("realName");
            String gender = jsonObject.getJSONObject("data").getString("gender");
            String classCode = jsonObject.getJSONObject("data").getString("classCode");
            //根据学号编号得到班级编号
            JSONObject jsonObjectClass = null;
            jsonObjectClass = RestTemplateUtil.getJSONObjectForCmIps("/os/classInfo/get/" + classCode);
            String majorCode = jsonObjectClass.getJSONObject("data").getString("majorCode");
            //根据专业编号得到院系编号
            JSONObject jsonObjectMajor = null;
            jsonObjectMajor = RestTemplateUtil.getJSONObjectForCmIps("/os/major/get/" + majorCode);
            String academyCode = jsonObjectMajor.getJSONObject("data").getString("academyCode");
            //根据院系编号得到院系名
            JSONObject jsonObjectAcade = null;
            jsonObjectAcade = RestTemplateUtil.getJSONObjectForCmIps("/os/academy/get/" + academyCode);
            String academyName = jsonObjectAcade.getJSONObject("data").getString("academyName");
            studentInfo.setGender(gender);
            studentInfo.setRealName(name);
            studentInfo.setOrgCode(academyCode);
            studentInfo.setOrgName(academyName);
            return studentInfo;
        } else {
            return null;
        }
    }

    public List<StudentInfo> searchWithCodeName(String keyword) {
        JSONObject jsonObject = null;
        jsonObject = RestTemplateUtil.getJSONObjectForCmIps("/os/studentInfo/search?keyWord=" + keyword);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        List<StudentInfo> studentInfoList = new ArrayList<>();
        Result<List<StudentInfo>> listResult = jsonObject.toJavaObject(Result.class);
        List<StudentInfo> studentInfoListFromDate = listResult.getData();
        for(StudentInfo s : studentInfoListFromDate){
            //根据班级号得到机构信息
            String classCode = s.getClassCode();
            JSONObject jsonObjectClass = null;
            jsonObjectClass = RestTemplateUtil.getJSONObjectForCmIps("/os/classInfo/get/" + classCode);
            String majorCode = jsonObjectClass.getJSONObject("data").getString("majorCode");
            String className = jsonObjectClass.getJSONObject("data").getString("className");
            String grade = jsonObjectClass.getJSONObject("data").getString("grade");
            ClassInfo classInfo = new ClassInfo();
            classInfo.setGrade(grade);
            classInfo.setClassname(className);
            classInfo.setClasscode(classCode);
            s.setClassInfo(classInfo);
            //根据专业编号得到院系编号
            JSONObject jsonObjectMajor = null;
            jsonObjectMajor = RestTemplateUtil.getJSONObjectForCmIps("/os/major/get/" + majorCode);
            String academyCode = jsonObjectMajor.getJSONObject("data").getString("academyCode");
            //根据院系编号得到院系名
            JSONObject jsonObjectAcade = null;
            jsonObjectAcade = RestTemplateUtil.getJSONObjectForCmIps("/os/academy/get/" + academyCode);
            String academyName = jsonObjectAcade.getJSONObject("data").getString("academyName");
            s.setOrgCode(academyCode);
            s.setOrgName(academyName);
            studentInfoList.add(s);
        }

        for (int i = 0; i < studentInfoListFromDate.size(); i++) {
            StudentInfo studentInfo = new StudentInfo();
            Map<String, Object> map = new LinkedHashMap<>();
            map = (Map<String, Object>) studentInfoListFromDate.get(i);
            studentInfo.setStudentno((String) map.get("studentNo"));
            studentInfo.setRealName((String) map.get("realName"));
            studentInfo.setGender((String) map.get("gender"));
            String classCode = (String) map.get("classCode");
            //根据班级号查询机构
            //根据学号编号得到班级编号
            JSONObject jsonObjectClass = null;
            jsonObjectClass = RestTemplateUtil.getJSONObjectForCmIps("/os/classInfo/get/" + classCode);
            String majorCode = jsonObjectClass.getJSONObject("data").getString("majorCode");
            String className = jsonObjectClass.getJSONObject("data").getString("className");
            String grade = jsonObjectClass.getJSONObject("data").getString("grade");
            ClassInfo classInfo = new ClassInfo();
            classInfo.setGrade(grade);
            classInfo.setClassname(className);
            classInfo.setClasscode(classCode);
            studentInfo.setClassInfo(classInfo);
            //根据专业编号得到院系编号
            JSONObject jsonObjectMajor = null;
            jsonObjectMajor = RestTemplateUtil.getJSONObjectForCmIps("/os/major/get/" + majorCode);
            String academyCode = jsonObjectMajor.getJSONObject("data").getString("academyCode");
            //根据院系编号得到院系名
            JSONObject jsonObjectAcade = null;
            jsonObjectAcade = RestTemplateUtil.getJSONObjectForCmIps("/os/academy/get/" + academyCode);
            String academyName = jsonObjectAcade.getJSONObject("data").getString("academyName");
            studentInfo.setOrgCode(academyCode);
            studentInfo.setOrgName(academyName);
            studentInfoList.add(studentInfo);
        }
        return studentInfoList;
    }

    public List<StudentInfo> selectWithPrivilegeOrgCodes(String keyWord, String privilegeOrgCodes) {
        String[] strings = privilegeOrgCodes.split(",");
        JSONObject jsonObject = null;
        StringBuilder sb = new StringBuilder();
        sb.append("/os/studentInfo/searchWithClassCodeAndKeyWord");
        if (keyWord != null && StringUtils.isEmpty("")) {
            sb.append("?keyword=" + keyWord);
        }
        if (privilegeOrgCodes != null && StringUtils.isEmpty("privilegeOrgCodes")) {
            for(int i = 0; i < strings.length; i++) {
                sb.append("&classcodes=" + strings[i]);
            }
        }
        jsonObject = RestTemplateUtil.getJSONObjectForCmIps(sb.toString());
        List<StudentInfo> studentInfoList = new ArrayList<>();
        Result<List<StudentInfo>> listResult = jsonObject.toJavaObject(Result.class);
        List<StudentInfo> studentInfoList1 = listResult.getData();
        for (int i = 0; i < studentInfoList1.size(); i++){
            StudentInfo studentInfo = new StudentInfo();
            List<String> rulerList = (List<String>) studentInfoList1.get(i);
            studentInfo.setStudentno(rulerList.get(0));
            studentInfo.setRealName(rulerList.get(2));
            studentInfo.setGender(rulerList.get(3));
            //通过班级编码拿到班级信息
            String classCode = rulerList.get(1);
            //根据班级号查询机构
            //根据学号编号得到班级编号
            JSONObject jsonObjectClass = null;
            jsonObjectClass = RestTemplateUtil.getJSONObjectForCmIps("/os/classInfo/get/" + classCode);
            String majorCode = jsonObjectClass.getJSONObject("data").getString("majorCode");
            //根据专业编号得到院系编号
            JSONObject jsonObjectMajor = null;
            jsonObjectMajor = RestTemplateUtil.getJSONObjectForCmIps("/os/major/get/" + majorCode);
            String academyCode = jsonObjectMajor.getJSONObject("data").getString("academyCode");
            //根据院系编号得到院系名
            JSONObject jsonObjectAcade = null;
            jsonObjectAcade = RestTemplateUtil.getJSONObjectForCmIps("/os/academy/get/" + academyCode);
            String academyName = jsonObjectAcade.getJSONObject("data").getString("academyName");
            studentInfo.setOrgCode(academyCode);
            studentInfo.setOrgName(academyName);
            studentInfoList.add(studentInfo);
        }
        return studentInfoList;
    }

    public List<StudentInfo> loadWithClassCodes(String classCode) {
        String[] orgCodes = classCode.split(",");
        JSONObject jsonObject = null;
        StringBuilder sb = new StringBuilder();
        sb.append("/os/studentInfo/searchWithClassCodeAndKeyWord?");
        for (int i = 0; i < orgCodes.length; i++) {
            sb.append("&classCodes="+orgCodes[i]);
        }
        jsonObject = RestTemplateUtil.getJSONObjectForCmIps(sb.toString());
        List<StudentInfo> studentInfoList = new ArrayList<>();
        Result<List<StudentInfo>> listResult = jsonObject.toJavaObject(Result.class);
        List<StudentInfo> studentInfoLists = listResult.getData();
        for (int i = 0; i < studentInfoLists.size(); i++) {
            StudentInfo studentInfo = new StudentInfo();
            List<String> rulerList = (List<String>) studentInfoLists.get(i);
            studentInfo.setStudentno(rulerList.get(0));
            studentInfo.setRealName(rulerList.get(2));
            studentInfo.setGender(rulerList.get(3));
            //通过班级编码拿到班级信息
            String classCodeClass = rulerList.get(1);
            //根据班级号查询机构
            //根据学号编号得到班级编号
            JSONObject jsonObjectClass = null;
            jsonObjectClass = RestTemplateUtil.getJSONObjectForCmIps("/os/classInfo/get/" + classCodeClass);
            String majorCode = jsonObjectClass.getJSONObject("data").getString("majorCode");
            //根据专业编号得到院系编号
            JSONObject jsonObjectMajor = null;
            jsonObjectMajor = RestTemplateUtil.getJSONObjectForCmIps("/os/major/get/" + majorCode);
            String academyCode = jsonObjectMajor.getJSONObject("data").getString("academyCode");
            //根据院系编号得到院系名
            JSONObject jsonObjectAcade = null;
            jsonObjectAcade = RestTemplateUtil.getJSONObjectForCmIps("/os/academy/get/" + academyCode);
            String academyName = jsonObjectAcade.getJSONObject("data").getString("academyName");
            studentInfo.setOrgCode(academyCode);
            studentInfo.setOrgName(academyName);
            studentInfoList.add(studentInfo);
        }
            return studentInfoList;
        }
    }
