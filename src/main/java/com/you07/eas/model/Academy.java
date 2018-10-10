package com.you07.eas.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 院系信息
 * @author RY
 * @version 1.0
 * @since 2018-8-6 09:22:15
 */
@Entity
@Table(name = "eas_acdamey")
public class Academy{
	/**
	 * 院系代码
	 */
	@Id
	@Column(name = "acadamey_code")
	private String acadameyCode;
	/**
	 * 院系名称
	 */
	@Column(name = "acadamey_name")
	private String acadameyName;
	/**
	 * 排序字段，数据库是char类型，只能尽量不用
	 */
	@Column(name = "orderid")
	private String orderid;
	/**
	 * 备注
	 */
	@Column(name = "memo")
	private String memo;

	public String getAcadameyCode() {
		return acadameyCode;
	}

	public void setAcadameyCode(String acadameyCode) {
		this.acadameyCode = acadameyCode;
	}

	public String getAcadameyName() {
		return acadameyName;
	}

	public void setAcadameyName(String acadameyName) {
		this.acadameyName = acadameyName;
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
