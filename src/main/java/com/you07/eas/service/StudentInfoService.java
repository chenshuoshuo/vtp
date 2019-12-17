package com.you07.eas.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.you07.eas.model.ClassInfo;
import com.you07.eas.model.Result;
import com.you07.eas.model.StudentInfo;
import com.you07.eas.vo.AcademyVO;
import com.you07.eas.vo.ClassVO;
import com.you07.eas.vo.MajorVO;
import com.you07.eas.vo.StudentVO;
import com.you07.util.RestTemplateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class StudentInfoService {
    public StudentInfo get(String studentNo) {
        JSONObject studentJson = RestTemplateUtil.getJSONObjectForCmIps("/os/studentInfo/get/"+studentNo);

        StudentVO studentVO = studentJson.getObject("data", StudentVO.class);
        //根据学号编号得到班级编号
        JSONObject classJson = RestTemplateUtil.getJSONObjectForCmIps("/os/classInfo/get/"+studentVO.getClassCode());
        ClassVO classVO = classJson.getObject("data", ClassVO.class);
        //根据专业编号得到院系编号
        JSONObject majorJson = RestTemplateUtil.getJSONObjectForCmIps("/os/major/get/"+classVO.getMajorCode());
        MajorVO majorVO = majorJson.getObject("data", MajorVO.class);
        //根据院系编号得到院系名
        JSONObject academyJson = RestTemplateUtil.getJSONObjectForCmIps("/os/academy/get/"+majorVO.getAcademyCode());
        AcademyVO academyVO = academyJson.getObject("data", AcademyVO.class);

        return new StudentInfo(studentVO, academyVO);
    }

    public List<StudentInfo> searchWithCodeName(String keyword, String page, String pageSize) {
        JSONObject jsonObject = null;
        String pattern = "^[0-9]\\d*$";
        Pattern patternSecond = Pattern.compile(pattern);
        Matcher matcher = patternSecond.matcher(keyword);
        boolean matches = matcher.matches();
        String studentNo = null;
        String realName = null;
        if (matches == true && keyword != null) {
            studentNo = keyword;
        } else {
            realName = keyword;
        }
        StringBuilder sb = new StringBuilder("/os/studentInfo/pageQuery?page=" + page + "&pageSize=" + pageSize);
        if (studentNo != null) {
            sb.append("&studentNo=" + studentNo);
        } else if (realName != null) {
            sb.append("&realName=" + realName);
        }
        jsonObject = RestTemplateUtil.getJSONObjectForCmIps(sb.toString());
        Map<String, Object> objectMap = (Map<String, Object>) jsonObject.get("data");
        List<Object> objectList = (List<Object>) objectMap.get("content");
        List<StudentInfo> studentInfoList = new ArrayList<>();
        for (int i = 0; i < objectList.size(); i++) {
            StudentInfo studentInfo = new StudentInfo();
            Map<String, Object> map = new LinkedHashMap<>();
            map = (Map<String, Object>) objectList.get(i);
            studentInfo.setStudentno((String) map.get("studentNo"));
            studentInfo.setName((String) map.get("realName"));
            studentInfo.setGender((String) map.get("gender"));
            String classCode = (String) map.get("classCode");
            //根据班级号查询机构
            //根据学号编号得到班级编号
            JSONObject jsonObjectClass = null;
            jsonObjectClass = RestTemplateUtil.getJSONObjectForCmIps("/os/classInfo/get/" + classCode);
            String majorCode = jsonObjectClass.getJSONObject("data").getString("majorCode");
            String className = jsonObjectClass.getJSONObject("data").getString("className");
            String grade = jsonObjectClass.getJSONObject("data").getString("grade");
            studentInfo.setClassCode(classCode);
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
        List<String> orgs = Arrays.asList(privilegeOrgCodes.split(","));
        JSONObject jsonObject = null;
        StringBuilder sb = new StringBuilder();
        sb.append("/os/studentInfo/searchWithClassCodeAndKeyWord?page=0&pageSize=100");
        if (keyWord != null && StringUtils.isEmpty("")) {
            sb.append("&keyword=" + keyWord);
        }
        jsonObject = RestTemplateUtil.getJSONObjectForCmIps(sb.toString());
        List<StudentInfo> students = new ArrayList<>();
        List<StudentVO> studentVOS = jsonObject.getJSONObject("data").getJSONArray("content").toJavaList(StudentVO.class);
        studentVOS.forEach(s->students.add(new StudentInfo(s)));
        for (int i = 0; i < students.size(); i++) {
            StudentInfo studentInfo = students.get(i);
            if(!orgs.contains(studentInfo.getClassCode()))
                students.remove(i--);
        }
        return students;
    }

    public List<StudentInfo> loadWithClassCodes(String classCode) {

        String[] orgCodes = classCode.split(",");
        JSONObject jsonObject = null;
        StringBuilder sb = new StringBuilder();
        sb.append("/os/studentInfo/searchWithClassCodeAndKeyWord?page=0&pageSize=100000");
        for (int i = 0; i < orgCodes.length; i++) {
            sb.append("&classCodes=" + orgCodes[i]);
        }
        jsonObject = RestTemplateUtil.getJSONObjectForCmIps(sb.toString());

        List<StudentVO> studentVOS = jsonObject.getJSONObject("data").getJSONArray("content").toJavaList(StudentVO.class);
        List<StudentInfo> studentInfos = new ArrayList<>();
        studentVOS.forEach(s->studentInfos.add(new StudentInfo(s)));

        return studentInfos;
    }

}
