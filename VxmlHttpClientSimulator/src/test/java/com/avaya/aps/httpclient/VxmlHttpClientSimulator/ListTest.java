package com.avaya.aps.httpclient.VxmlHttpClientSimulator;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.avaya.aps.httpclient.VxmlHttpClientSimulator.actions.HttpAction;

public class ListTest extends TestCase {

	public void testRemoteList() {
		
		HttpAction httpAction = new HttpAction(1);
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("MenuNode___value", "1,2,3"));
		nvps.add(new BasicNameValuePair("n2", "v2"));
		nvps.add(new BasicNameValuePair("n3", "v3"));
		nvps.add(new BasicNameValuePair("n4", "v4"));

		List<NameValuePair> list = httpAction.getMenuOptionAndUpdate(nvps);
		System.out.println(httpAction.convertToString(list));
		System.out.println(httpAction.convertToString(nvps));

	}
}
