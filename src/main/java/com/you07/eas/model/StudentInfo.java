package com.you07.eas.model;

import com.you07.eas.vo.AcademyVO;
import com.you07.eas.vo.StudentVO;

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

/**
 * 学生信息
 * @author RY
 * @version 1.0
 * @since 2018-8-6 09:39:01
 */
@Table(name = "eas_studentinfo")
public class StudentInfo{
	/**
	 * 学号
	 */
	@Id
	@Column(name = "studentno")
	private String studentno;
	/**
	 * 班级信息
	 */
	private String classCode;
	/**
	 * 姓名
	 */
	@Column(name = "name")
	private String name;
	/**
	 * 性别
	 */
	@Column(name = "gender")
	private String gender;
	/**
	 * 生日
	 */
	@Column(name = "birth")
	private String birth;
	/**
	 * 年级
	 */
	@Column(name = "nj")
	private String nj;
	/**
	 * 微信号
	 */
	@Column(name = "wechat")
	private String wechat;
	/**
	 * 手机号
	 */
	@Column(name = "tel")
	private String tel;
	/**
	 * 身份证号
	 */
	@Column(name = "sfzh")
	private String sfzh;
	/**
	 * 考生号
	 */
	@Column(name = "ksh")
	private String ksh;
	/**
	 * 籍贯
	 */
	@Column(name = "jg")
	private String jg;

	private String orgCode;

	private String orgName;

	public StudentInfo() {
	}

	public StudentInfo(StudentVO studentVO) {
		setStudentno(studentVO.getStudentNo());
		setGender(studentVO.getGender());
		setName(studentVO.getRealName());
		setTel(studentVO.getTelephone());
		setClassCode(studentVO.getClassCode());
	}

	public StudentInfo(StudentVO studentVO, AcademyVO academyVO) {
		setStudentno(studentVO.getStudentNo());
		setGender(studentVO.getGender());
		setName(studentVO.getRealName());
		setTel(studentVO.getTelephone());
		setClassCode(studentVO.getClassCode());
		setOrgCode(academyVO.getAcademyCode());
		setOrgName(academyVO.getAcademyName());
	}


	public String getStudentno() {
		return studentno;
	}

	public void setStudentno(String studentno) {
		this.studentno = studentno;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getRealName() {
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
}
