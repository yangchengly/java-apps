package com.avaya.aps.httpclient.VxmlHttpClientSimulator;

import java.util.List;

import org.apache.http.NameValuePair;

public class SubmitAttributes {
	
	private String next;
	private List<NameValuePair> list;
	private String namelist;
	private String method;
	
	// false means that this object is not
	// assigned with any values.
	private boolean flag = false; 

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public String getNamelist() {
		return namelist;
	}

	public void setNamelist(String namelist) {
		this.namelist = namelist;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public List<NameValuePair> getList() {
		return list;
	}

	public void setList(List<NameValuePair> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "next:" + next + ";\nnamelist:" + namelist + ";\nmethod:" + method;
	}

}
