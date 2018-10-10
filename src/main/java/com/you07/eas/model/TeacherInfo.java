package com.you07.eas.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 教职工信息
 * @author Administrator
 * @version 1.0
 * @since 2016-4-19 13:54:24
 */
@Entity
@Table(name = "eas_teacherinfo")
public class TeacherInfo implements java.io.Serializable{
	/**
	 * 教职工号
	 */
	@Id
	@Column(name = "teachercode")
	private String teachercode;
	/**
	 * 所属部门信息
	 */
	private DepartmentInfo departmentInfo;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 性别
	 */
	private String gender;
	/**
	 * 生日
	 */
	private String birth;
	/**
	 * 邮箱
	 */
	private String mail;
	/**
	 * 职务
	 */
	private String resign;
	/**
	 * 职称
	 */
	private String professionalTitle;
	/**
	 * 微信号
	 */
	private String wechat;
	/**
	 * 手机号
	 */
	private String tel;
	/**
	 * 民族
	 */
	private String mz;
	/**
	 * 籍贯
	 */
	private String jg;
	/**
	 * 政治面貌
	 */
	private String zzmm;
	/**
	 * 所学专业
	 */
	private String sxzy;
	/**
	 * 学历
	 */
	private String xl;
	/**
	 * 学位
	 */
	private String xw;

	public String getTeachercode() {
		return teachercode;
	}

	public void setTeachercode(String teachercode) {
		this.teachercode = teachercode;
	}

	public DepartmentInfo getDepartmentInfo() {
		return departmentInfo;
	}

	public void setDepartmentInfo(DepartmentInfo departmentInfo) {
		this.departmentInfo = departmentInfo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getResign() {
		return resign;
	}

	public void setResign(String resign) {
		this.resign = resign;
	}

	public String getProfessionalTitle() {
		return professionalTitle;
	}

	public void setProfessionalTitle(String professionalTitle) {
		this.professionalTitle = professionalTitle;
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

	public String getMz() {
		return mz;
	}

	public void setMz(String mz) {
		this.mz = mz;
	}

	public String getJg() {
		return jg;
	}

	public void setJg(String jg) {
		this.jg = jg;
	}

	public String getZzmm() {
		return zzmm;
	}

	public void setZzmm(String zzmm) {
		this.zzmm = zzmm;
	}

	public String getSxzy() {
		return sxzy;
	}

	public void setSxzy(String sxzy) {
		this.sxzy = sxzy;
	}

	public String getXl() {
		return xl;
	}

	public void setXl(String xl) {
		this.xl = xl;
	}

	public String getXw() {
		return xw;
	}

	public void setXw(String xw) {
		this.xw = xw;
	}
}
