package com.you07.eas.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 学生信息
 * @author RY
 * @version 1.0
 * @since 2018-8-6 09:39:01
 */
public class StudentInfo{
	/**
	 * 学号
	 */
	private String studentno;
	/**
	 * 班级信息
	 */
	private ClassInfo classInfo;
	/**
	 * 姓名
	 */
	private String realName;
	/**
	 * 性别
	 */
	private String gender;
	/**
	 * 生日
	 */
	private String birth;
	/**
	 * 年级
	 */
	private String nj;
	/**
	 * 微信号
	 */
	private String wechat;
	/**
	 * 手机号
	 */
	private String tel;
	/**
	 * 身份证号
	 */
	private String sfzh;
	/**
	 * 考生号
	 */
	private String ksh;
	/**
	 * 籍贯
	 */
	private String jg;
	/***
	 * 组织码
	 **/
	private String orgCode;

	/***
	 * 组织码
	 **/
	private String orgName;
	/**
	 * 班级编号
	 */
	private String classCode;

	public String getStudentno() {
		return studentno;
	}

	public void setStudentno(String studentno) {
		this.studentno = studentno;
	}

	public ClassInfo getClassInfo() {
		return classInfo;
	}

	public void setClassInfo(ClassInfo classInfo) {
		this.classInfo = classInfo;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getNj() {
		return nj;
	}

	public void setNj(String nj) {
		this.nj = nj;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getSfzh() {
		return sfzh;
	}

	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}

	public String getKsh() {
		return ksh;
	}

	public void setKsh(String ksh) {
		this.ksh = ksh;
	}

	public String getJg() {
		return jg;
	}

	public void setJg(String jg) {
		this.jg = jg;
	}
	@Transient
	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	@Transient
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
}
