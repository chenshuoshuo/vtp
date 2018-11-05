package com.you07.eas.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 专业信息
 * @author RY
 * @version 1.0
 * @since 2018-8-6 09:22:28
 */
@Table(name = "eas_major")
public class Major implements java.io.Serializable{
	/**
	 * 专业代码
	 */
	@Id
	@Column(name = "major_code")
	private String majorCode;
	/**
	 * 所属院系
	 */
	@Column(name = "acadamey_code")
	private Academy acadameyCode;
	/**
	 * 专业名称
	 */
	@Column(name = "major_name")
	private String majorName;
	/**
	 * 学制
	 */
	@Column(name = "major_last")
	private String majorLast;
	/**
	 * 培养层次
	 */
	@Column(name = "pycc")
	private String pycc;
	/**
	 * 学科名称
	 */
	@Column(name = "xkmc")
	private String discipline;
	/**
	 * 开始时间
	 */
	@Column(name = "kssj")
	private String startTime;
	/**
	 * 排序字段
	 */
	@Column(name = "orderid")
	private String orderid;
	/**
	 * 备注
	 */
	@Column(name = "memo")
	private String memo;

	public String getMajorCode() {
		return majorCode;
	}

	public void setMajorCode(String majorCode) {
		this.majorCode = majorCode;
	}

	public Academy getAcadameyCode() {
		return acadameyCode;
	}

	public void setAcadameyCode(Academy acadameyCode) {
		this.acadameyCode = acadameyCode;
	}

	public String getMajorName() {
		return majorName;
	}

	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}

	public String getMajorLast() {
		return majorLast;
	}

	public void setMajorLast(String majorLast) {
		this.majorLast = majorLast;
	}

	public String getPycc() {
		return pycc;
	}

	public void setPycc(String pycc) {
		this.pycc = pycc;
	}

	public String getDiscipline() {
		return discipline;
	}

	public void setDiscipline(String discipline) {
		this.discipline = discipline;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
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
