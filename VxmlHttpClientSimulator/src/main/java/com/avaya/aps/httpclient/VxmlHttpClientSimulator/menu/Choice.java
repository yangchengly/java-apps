package com.avaya.aps.httpclient.VxmlHttpClientSimulator.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class Choice {

	private String dtmf;
	private String expr;
	
	public Choice(String dtmf, String expr) {
		this.dtmf = dtmf;
		this.expr = expr;
	}

	public String getDtmf() {
		return dtmf;
	}

	public void setDtmf(String dtmf) {
		this.dtmf = dtmf;
	}

	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}
	
	public static List<NameValuePair> getNvps(String menuId, String dtmf) {		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>(7);
		
		nvps.add(new BasicNameValuePair(menuId + "___value", dtmf));
		nvps.add(new BasicNameValuePair(menuId + "___confidence", "1"));
		nvps.add(new BasicNameValuePair(menuId + "___utterance", "1"));
		nvps.add(new BasicNameValuePair(menuId + "___inputmode", "dtmf"));
		nvps.add(new BasicNameValuePair(menuId + "___interpretation", "1"));
		nvps.add(new BasicNameValuePair(menuId + "___noinputcount", "0"));
		nvps.add(new BasicNameValuePair(menuId + "___nomatchcount", "0"));
		
		return nvps;
	}
}
