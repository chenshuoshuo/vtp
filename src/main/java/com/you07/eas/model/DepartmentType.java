package com.you07.eas.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 部门类别实例
 * @author RY
 * @version 1.0
 * @since 2018-8-6 09:50:37
 *
 */
@Table(name = "eas_zzjg_bmlb")
public class DepartmentType{
	/**
	 * 部门类别编码
	 */
	@Id
	@Column(name = "bmlbdm")
	private String bmlbdm;
	/**
	 * 部门类别名称
	 */
	@Column(name = "bmlbmc")
	private String bmlbmc;

	public String getBmlbdm() {
		return bmlbdm;
	}

	public void setBmlbdm(String bmlbdm) {
		this.bmlbdm = bmlbdm;
	}

	public String getBmlbmc() {
		return bmlbmc;
	}

	public void setBmlbmc(String bmlbmc) {
		this.bmlbmc = bmlbmc;
	}
}
