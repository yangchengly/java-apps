package com.avaya.aps.httpclient.VxmlHttpClientSimulator;

import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;

import junit.framework.TestCase;

public class PropertiesUtilTest extends TestCase {
	public void testMain() {

		PropertiesUtil.loadAllProperties("conf");

		List<NameValuePair> list = PropertiesUtil.findNamelist("Main",
				"session");
		for (Iterator<NameValuePair> iterator = list.iterator(); iterator
				.hasNext();) {
			NameValuePair nameValuePair = (NameValuePair) iterator.next();

			System.out.println("nameValuePair:" + nameValuePair.getName() + ";"
					+ nameValuePair.getValue());
		}
	}
}
