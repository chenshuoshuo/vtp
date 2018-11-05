package com.you07.eas.schedule;

import com.you07.eas.model.*;
import com.you07.eas.service.*;
import com.you07.util.DateUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.Calendar;
import java.util.List;

/**
 * 学工系统用户XML定时更新
 * @author RY
 * @version 1.0
 * @since 2018-3-15 16:40:45
 *
 */
@Component
@EnableAsync
public class AcademicSystemXMLSchedule{
	@Autowired
	private DepartmentInfoService departmentInfoService;
	@Autowired
	private TeacherInfoService teacherInfoService;
	@Autowired
	private AcademyService academyService;
	@Autowired
	private ClassInfoService classInfoService;
	@Autowired
	private StudentInfoService studentInfoService;

	/**
	 * 启动任务
	 */
	@Scheduled(cron = "0 3 * * * ?")
	public void startJob(){
		System.out.println("start create Academic System XML");

		createAcademicSystemXML();
	}
	
	public void createAcademicSystemXML(){
		
		//1、新建Document对象  
		Document document = DocumentHelper.createDocument();  
		//2、添加根节点bookstore  
		Element root = document.addElement("root");

		//3、为根节点添加教师子节点teacher  
		Element teacher = root.addElement("teacher");  
		//4、添加属性  
		teacher.addAttribute("id", "1");  
		teacher.addAttribute("name", "教职工");
		
		//5、为根节点添加学生子节点student 
		Element student = root.addElement("student");  
		//6、添加属性  
		student.addAttribute("id", "2");
		student.addAttribute("name", "学生");
		
		Integer teacherCount = 0;
		Integer studentCount = 0;

		//1、新建Document对象
		Document privilegeDocument = DocumentHelper.createDocument();
		//2、添加根节点bookstore
		Element privilegeRoot = privilegeDocument.addElement("root");

		//3、为根节点添加教师子节点teacher
		Element privilegeTeacher = privilegeRoot.addElement("teacher");
		//4、添加属性
		privilegeTeacher.addAttribute("id", "1");
		privilegeTeacher.addAttribute("name", "教职工");

		//5、为根节点添加学生子节点student
		Element privilegeStudent = privilegeRoot.addElement("student");
		//6、添加属性
		privilegeStudent.addAttribute("id", "2");
		privilegeStudent.addAttribute("name", "学生");

		Integer privilegeTeacherCount = 0;
		Integer privilegeStudentCount = 0;
		
		
		//遍历写入教师节点

		// 组织机构
		List<DepartmentInfo> oList = departmentInfoService.listQuery(null);

		for(DepartmentInfo o : oList){
			Element oElement = teacher.addElement("dept");
			oElement.addAttribute("id", o.getXsbmdm());
			oElement.addAttribute("name", o.getXsbmmc());
			Integer oTeacherCount = 0;

			// 教师
			List<TeacherInfo> teaList = teacherInfoService.listQuery(o.getXsbmdm());

			for(TeacherInfo tea : teaList){
				Element teaElement = oElement.addElement("tea");
				teaElement.addAttribute("id", tea.getTeachercode());
				teaElement.addAttribute("name", tea.getName());
				teaElement.addAttribute("count", "1");

				oTeacherCount += 1;
			}
			if(oTeacherCount == 0){
				teacher.remove(oElement);
			} else{
				oElement.addAttribute("count", oTeacherCount + "");
				teacherCount += oTeacherCount;

				Element privilegeDeptElement = privilegeTeacher.addElement("dept");
				privilegeDeptElement.addAttribute("id", o.getXsbmdm());
				privilegeDeptElement.addAttribute("name", o.getXsbmmc());
				privilegeDeptElement.addAttribute("count", "1");
				privilegeTeacherCount += 1;
			}
		}
		teacher.addAttribute("count", teacherCount + "");
		privilegeTeacher.addAttribute("count", privilegeTeacherCount + "");
		

		//遍历写入学生节点
		// 院系
		List<Academy> adList = academyService.queryAll();
		
		for(Academy ad : adList){
			Element adElement = student.addElement("ad");
			adElement.addAttribute("id", ad.getAcadameyCode());
			adElement.addAttribute("name", ad.getAcadameyName());
			Integer adStudentCount = 0;

			Element privilegeAdElement = privilegeStudent.addElement("ad");
			privilegeAdElement.addAttribute("id", ad.getAcadameyCode());
			privilegeAdElement.addAttribute("name", ad.getAcadameyName());
			Integer privilegeAdStudentCount = 0;
			

			// 班级，只读取未毕业学生
			// 根据月份判断学年，上半年算上个学年
			// 年级 + 学制 > 当前学年
			List<ClassInfo> classList = classInfoService.queryInSchoolWithAcadamey(ad.getAcadameyCode(), DateUtil.getDefaultInstance().getSchoolYear());

			for(ClassInfo ci : classList){
				Element ciElement = adElement.addElement("ci");
				ciElement.addAttribute("id", ci.getClasscode());
				ciElement.addAttribute("name", ci.getClassname());
				Integer ciStudentCount = 0;

				// 学生
				List<StudentInfo> stuList = studentInfoService.loadWithClassCodes(ci.getClasscode());

				for(StudentInfo stu : stuList){
					Element stuElement = ciElement.addElement("stu");
					stuElement.addAttribute("id", stu.getStudentno());
					stuElement.addAttribute("name", stu.getName());
					stuElement.addAttribute("count", "1");

					ciStudentCount += 1;
				}
				if(ciStudentCount == 0){
					adElement.remove(ciElement);
				} else{
					ciElement.addAttribute("count", ciStudentCount + "");
					adStudentCount += ciStudentCount;

					Element privilegeCiElement = privilegeAdElement.addElement("ci");
					privilegeCiElement.addAttribute("id", ci.getClasscode());
					privilegeCiElement.addAttribute("name", ci.getClassname());
					privilegeAdStudentCount += 1;
				}
			}
			if(adStudentCount == 0){
				student.remove(adElement);
			} else{
				adElement.addAttribute("count", adStudentCount + "");
				studentCount += adStudentCount;
			}

			if(privilegeAdStudentCount == 0){
				privilegeStudent.remove(privilegeAdElement);
			} else{
				privilegeAdElement.addAttribute("count", privilegeAdStudentCount + "");
				privilegeStudentCount += privilegeAdStudentCount;
			}
			
		}
		
		student.addAttribute("count", studentCount + "");
		privilegeStudent.addAttribute("count", privilegeStudentCount + "");
		
		
		//7、创建输出格式的对象，规定输出的格式为带换行和缩进的格式  
		OutputFormat format = OutputFormat.createPrettyPrint();  
		try {
			File asXML = ResourceUtils.getFile("classpath:user-xml/asXML.xml");
			//8、创建输出对象
			XMLWriter writer = new XMLWriter(new FileOutputStream(asXML), format);
			//9、设置输出，这里设置输出的内容不将特殊字符转义，例如<符号就输出<，如果不设置，系统默认会将特殊字符转义  
			writer.setEscapeText(false);  

			//10、输出xml文件  
			writer.write(document);

			File privilegeXML = ResourceUtils.getFile("classpath:privilege-xml/all.xml");
			//8、创建输出对象
			XMLWriter privilegeWriter = new XMLWriter(new FileOutputStream(privilegeXML), format);
			//9、设置输出，这里设置输出的内容不将特殊字符转义，例如<符号就输出<，如果不设置，系统默认会将特殊字符转义
			privilegeWriter.setEscapeText(false);

			//10、输出xml文件
			privilegeWriter.write(privilegeDocument);
		} catch (UnsupportedEncodingException e) {  
			e.printStackTrace();  
		} catch (FileNotFoundException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}
	}
    

}
