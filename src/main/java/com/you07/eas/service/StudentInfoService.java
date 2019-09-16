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
            JSONObject jsonObject1 = null;
            jsonObject1 = RestTemplateUtil.getJSONObjectForCmIps("/os/classInfo/get/" + classCode);
            String majorCode = jsonObject1.getJSONObject("data").getString("majorCode");
            //根据专业编号得到院系编号
            JSONObject jsonObject2 = null;
            jsonObject2 = RestTemplateUtil.getJSONObjectForCmIps("/os/major/get/" + majorCode);
            String academyCode = jsonObject2.getJSONObject("data").getString("academyCode");
            //根据院系编号得到院系名
            JSONObject jsonObject3 = null;
            jsonObject3 = RestTemplateUtil.getJSONObjectForCmIps("/os/academy/get/" + academyCode);
            String academyName = jsonObject3.getJSONObject("data").getString("academyName");
            studentInfo.setGender(gender);
            studentInfo.setRealName(name);
            studentInfo.setOrgCode(academyCode);
            studentInfo.setOrgName(academyName);
            return studentInfo;
        } else {
            return null;
        }
    }

    /*public List<StudentInfo> searchWithCodeName(String keyword) {
        JSONObject jsonObject = null;
        jsonObject = RestTemplateUtil.getJSONObjectForCmIps("/os/studentInfo/search?keyWord=" + keyword);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        List<StudentInfo> studentInfoList1 = jsonArray.toJavaList(StudentInfo.class);
        List<StudentInfo> studentInfoList = new ArrayList<>();
        *//*Result<List<StudentInfo>> listResult = jsonObject.toJavaObject(Result.class);
        List<StudentInfo> studentInfoList1 = listResult.getData();*//*
        for(StudentInfo s : studentInfoList1){
            //根据班级号得到机构信息
            *//*String classCode = s.getClassCode();
            JSONObject jsonObject1 = null;
            jsonObject1 = RestTemplateUtil.getJSONObjectForCmIps("/os/classInfo/get/" + classCode);
            String majorCode = jsonObject1.getJSONObject("data").getString("majorCode");
            String className = jsonObject1.getJSONObject("data").getString("className");
            String grade = jsonObject1.getJSONObject("data").getString("grade");
            ClassInfo classInfo = new ClassInfo();
            classInfo.setGrade(grade);
            classInfo.setClassname(className);
            classInfo.setClasscode(classCode);
            s.setClassInfo(classInfo);
            //根据专业编号得到院系编号
            JSONObject jsonObject2 = null;
            jsonObject2 = RestTemplateUtil.getJSONObjectForCmIps("/os/major/get/" + majorCode);
            String academyCode = jsonObject2.getJSONObject("data").getString("academyCode");
            //根据院系编号得到院系名
            JSONObject jsonObject3 = null;
            jsonObject3 = RestTemplateUtil.getJSONObjectForCmIps("/os/academy/get/" + academyCode);
            String academyName = jsonObject3.getJSONObject("data").getString("academyName");
            s.setOrgCode(academyCode);
            s.setOrgName(academyName);*//*
            studentInfoList.add(s);
        }

        *//*for (int i = 0; i < studentInfoList1.size(); i++) {
            StudentInfo studentInfo1 = new StudentInfo();
            Map<String, Object> map = new LinkedHashMap<>();
            map = (Map<String, Object>) studentInfoList1.get(i);
            studentInfo1.setStudentno((String) map.get("studentNo"));
            studentInfo1.setName((String) map.get("realName"));
            studentInfo1.setGender((String) map.get("gender"));
            String classCode = (String) map.get("classCode");
            //根据班级号查询机构
            //根据学号编号得到班级编号
            JSONObject jsonObject1 = null;
            jsonObject1 = RestTemplateUtil.getJSONObjectForCmIps("/os/classInfo/get/" + classCode);
            String majorCode = jsonObject1.getJSONObject("data").getString("majorCode");
            String className = jsonObject1.getJSONObject("data").getString("className");
            String grade = jsonObject1.getJSONObject("data").getString("grade");
            ClassInfo classInfo = new ClassInfo();
            classInfo.setGrade(grade);
            classInfo.setClassname(className);
            classInfo.setClasscode(classCode);
            studentInfo1.setClassInfo(classInfo);
            //根据专业编号得到院系编号
            JSONObject jsonObject2 = null;
            jsonObject2 = RestTemplateUtil.getJSONObjectForCmIps("/os/major/get/" + majorCode);
            String academyCode = jsonObject2.getJSONObject("data").getString("academyCode");
            //根据院系编号得到院系名
            JSONObject jsonObject3 = null;
            jsonObject3 = RestTemplateUtil.getJSONObjectForCmIps("/os/academy/get/" + academyCode);
            String academyName = jsonObject3.getJSONObject("data").getString("academyName");
            studentInfo1.setOrgCode(academyCode);
            studentInfo1.setOrgName(academyName);
            studentInfoList.add(studentInfo1);
        }*//*
        return studentInfoList;
    }*/

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
            String classCode1 = rulerList.get(1);
            //根据班级号查询机构
            //根据学号编号得到班级编号
            JSONObject jsonObject1 = null;
            jsonObject1 = RestTemplateUtil.getJSONObjectForCmIps("/os/classInfo/get/" + classCode1);
            String majorCode = jsonObject1.getJSONObject("data").getString("majorCode");
            //根据专业编号得到院系编号
            JSONObject jsonObject2 = null;
            jsonObject2 = RestTemplateUtil.getJSONObjectForCmIps("/os/major/get/" + majorCode);
            String academyCode = jsonObject2.getJSONObject("data").getString("academyCode");
            //根据院系编号得到院系名
            JSONObject jsonObject3 = null;
            jsonObject3 = RestTemplateUtil.getJSONObjectForCmIps("/os/academy/get/" + academyCode);
            String academyName = jsonObject3.getJSONObject("data").getString("academyName");
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
        List<StudentInfo> studentInfoList1 = listResult.getData();
        for (int i = 0; i < studentInfoList1.size(); i++) {
            StudentInfo studentInfo = new StudentInfo();
            List<String> rulerList = (List<String>) studentInfoList1.get(i);
            studentInfo.setStudentno(rulerList.get(0));
            studentInfo.setRealName(rulerList.get(2));
            studentInfo.setGender(rulerList.get(3));
            //通过班级编码拿到班级信息
            String classCode1 = rulerList.get(1);
            //根据班级号查询机构
            //根据学号编号得到班级编号
            JSONObject jsonObject1 = null;
            jsonObject1 = RestTemplateUtil.getJSONObjectForCmIps("/os/classInfo/get/" + classCode1);
            String majorCode = jsonObject1.getJSONObject("data").getString("majorCode");
            //根据专业编号得到院系编号
            JSONObject jsonObject2 = null;
            jsonObject2 = RestTemplateUtil.getJSONObjectForCmIps("/os/major/get/" + majorCode);
            String academyCode = jsonObject2.getJSONObject("data").getString("academyCode");
            //根据院系编号得到院系名
            JSONObject jsonObject3 = null;
            jsonObject3 = RestTemplateUtil.getJSONObjectForCmIps("/os/academy/get/" + academyCode);
            String academyName = jsonObject3.getJSONObject("data").getString("academyName");
            studentInfo.setOrgCode(academyCode);
            studentInfo.setOrgName(academyName);
            studentInfoList.add(studentInfo);
        }
            return studentInfoList;
        }
    }
