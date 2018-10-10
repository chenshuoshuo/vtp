package com.you07.eas.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 学校部门信息
 * @author RY
 * @version 1.0
 * @since 2018-8-6 09:52:52
 */
@Table(name = "eas_zzjg_xsbm")
public class DepartmentInfo{
	/**
	 * 部门代码
	 */
	@Id
	@Column(name = "xsbmdm")
	private String xsbmdm;
	/**
	 * 部门类别代码
	 */
	@Column(name = "bmlbdm")
	private String bmlbdm;
	/**
	 * 上级部门代码
	 */
	@Column(name = "sjbmdm")
	private String sjbmdm;
	/**
	 * 部门名称
	 */
	@Column(name = "xsbmmc")
	private String xsbmmc;

	public String getXsbmdm() {
		return xsbmdm;
	}

	public void setXsbmdm(String xsbmdm) {
		this.xsbmdm = xsbmdm;
	}

	public String getBmlbdm() {
		return bmlbdm;
	}

	public void setBmlbdm(String bmlbdm) {
		this.bmlbdm = bmlbdm;
	}

	public String getSjbmdm() {
		return sjbmdm;
	}

	public void setSjbmdm(String sjbmdm) {
		this.sjbmdm = sjbmdm;
	}

	public String getXsbmmc() {
		return xsbmmc;
	}

	public void setXsbmmc(String xsbmmc) {
		this.xsbmmc = xsbmmc;
	}
}
