package com.avaya.openjpa;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Phonearea {

	
	private int id;
	
	@Id
	private String mobile;	

	private String province;

	private String city;

	private String corp;

	private String areacode;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCorp() {
		return corp;
	}

	public void setCorp(String corp) {
		this.corp = corp;
	}

	public String getAreacode() {
		return areacode;
	}

	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}

}
