package com.you07.eas.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 班级信息
 * @author RY
 * @version 1.0
 * @since 2018-8-6 09:22:44
 */
@Table(name = "eas_classinfo")
public class ClassInfo{
	/**
	 * 班级代码
	 */
	@Id
	@Column(name = "classcode")
	private String classcode;
	/**
	 * 院系代码
	 */
	@Column(name = "major_code")
	private String majorCode;
	/**
	 * 班级名称
	 */
	@Column(name = "classname")
	private String classname;
	/**
	 * 学生人数
	 */
	@Column(name = "studentnumber")
	private Integer studentnumber;
	/**
	 * 辅导员职工号
	 */
	@Column(name = "teachercode")
	private String teachercode;
	/**
	 * 年级
	 */
	@Column(name = "grade")
	private String grade;
	/**
	 * 排序
	 */
	@Column(name = "orderid")
	private String orderid;
	/**
	 * 备注
	 */
	@Column(name = "memo")
	private String memo;

	public String getClasscode() {
		return classcode;
	}

	public void setClasscode(String classcode) {
		this.classcode = classcode;
	}

	public String getMajorCode() {
		return majorCode;
	}

	public void setMajorCode(String majorCode) {
		this.majorCode = majorCode;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public Integer getStudentnumber() {
		return studentnumber;
	}

	public void setStudentnumber(Integer studentnumber) {
		this.studentnumber = studentnumber;
	}

	public String getTeachercode() {
		return teachercode;
	}

	public void setTeachercode(String teachercode) {
		this.teachercode = teachercode;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}
