package com.you07.vtp.schedule;

import com.you07.util.VTPFileUtil;
import com.you07.vtp.model.LocationTrackManager;
import com.you07.vtp.service.LocationTrackManagerService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@EnableAsync
public class RefreshManagerPrivilegeSchedule {
    @Autowired
    private LocationTrackManagerService locationTrackManagerService;


    @Scheduled(cron = "0 4 * * * ?")
    public void refreshPrivilege() throws IOException, DocumentException {
        List<LocationTrackManager> managerList = locationTrackManagerService.listQuery(null, null);
        for(LocationTrackManager locationTrackManager : managerList){
            String[] orgCodeArray = locationTrackManager.getOrgCodes().split(",");
            Map<String, String> orgCodeMap = new HashMap<>();
            for(String orgCode : orgCodeArray){
                orgCodeMap.put(orgCode, orgCode);
            }

            // 用户文件生成
            File userXML = new File(VTPFileUtil.getRootPath()
                    + "user-xml" + System.getProperty("file.separator")
                    +  locationTrackManager.getUserid() + ".xml");
            if(!userXML.exists()){
                userXML = new File(VTPFileUtil.getRootPath()
                        + "user-xml" + System.getProperty("file.separator")
                        +  locationTrackManager.getUserid() + ".xml");
            }

            // 满用户文件读取
            File allUserXML = new File(VTPFileUtil.getRootPath()
                    + "user-xml" + System.getProperty("file.separator")
                    +  "asXML.xml");

            SAXReader reader = new SAXReader();
            Document document = reader.read(allUserXML);
            Element root = document.getRootElement();
            Element teaElement = root.element("teacher");
            List<Element> deptList = teaElement.elements();
            Integer teacherCount = 0;
            for(Element deptElement : deptList){
                if(!orgCodeMap.containsKey(deptElement.attribute("id").getValue())){
                    teaElement.remove(deptElement);
                } else {
                    teacherCount += Integer.valueOf(teaElement.attribute("count").getValue());
                }
            }
            teaElement.attribute("count").setValue(teacherCount.toString());


            Element stuElement = root.element("student");
            List<Element> adList = stuElement.elements();
            Integer stuCount = 0;
            for(Element adElement : adList){
                List<Element> ciList = adElement.elements();
                Integer adStudentCount = 0;
                for(Element ciElement : ciList){
                    if(!orgCodeMap.containsKey(ciElement.attribute("id").getValue())){
                        adElement.remove(ciElement);
                    } else{
                        adStudentCount += Integer.valueOf(ciElement.attribute("count").getValue());
                    }
                }
                if(adElement.elements().size() == 0){
                    stuElement.remove(adElement);
                } else{
                    adElement.attribute("count").setValue(adStudentCount.toString());
                    stuCount += adStudentCount;
                }
            }
            stuElement.attribute("count").setValue(stuCount.toString());

            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(new FileOutputStream(userXML), format);
            writer.setEscapeText(false);
            writer.write(document);

            // 权限文件生成
            File privilegeXML = new File(VTPFileUtil.getRootPath()
                    + "privilege-xml" + System.getProperty("file.separator")
                    +  locationTrackManager.getUserid() + ".xml");
            if(!privilegeXML.exists()){
                privilegeXML = new File(VTPFileUtil.getRootPath()
                        + "privilege-xml" + System.getProperty("file.separator")
                        +  locationTrackManager.getUserid() + ".xml");
            }

            // 满权限文件读取
            File allPrivilegeXML = new File(VTPFileUtil.getRootPath()
                    + "privilege-xml" + System.getProperty("file.separator")
                    +  "all.xml");

            reader = new SAXReader();
            document = reader.read(allPrivilegeXML);
            root = document.getRootElement();
            teaElement = root.element("teacher");
            deptList = teaElement.elements();
            for(Element deptElement : deptList){
                if(!orgCodeMap.containsKey(deptElement.attribute("id").getValue())){
                    deptElement.addAttribute("checked", "0");
                } else{
                    deptElement.addAttribute("checked", "1");
                }
            }

            stuElement = root.element("student");
            adList = stuElement.elements();
            for(Element adElement : adList){
                List<Element> ciList = adElement.elements();
                int checkedCount = 0;
                for(Element ciElement : ciList){
                    if(!orgCodeMap.containsKey(ciElement.attribute("id").getValue())){
                        ciElement.addAttribute("checked", "0");
                    } else{
                        ciElement.addAttribute("checked", "1");
                        checkedCount += 1;
                    }
                }
                if(adElement.elements().size() == checkedCount){
                    adElement.addAttribute("checked", "1");
                } else{
                    adElement.addAttribute("checked", "0");
                }
            }

            writer = new XMLWriter(new FileOutputStream(privilegeXML), format);
            writer.setEscapeText(false);
            writer.write(document);

        }
    }

    public String getRootPath() throws FileNotFoundException {
        return ClassUtils.getDefaultClassLoader().getResource("").getPath();
    }
}
