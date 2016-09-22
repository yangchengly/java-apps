package com.avaya.aps.httpclient.VxmlHttpClientSimulator;

import junit.framework.TestCase;

public class HttpActionTest extends TestCase {
	public void testGetModuleFromBaseUrl() {
		String baseUrl = "http://99.48.237.25:8081/Main/";

		String module = baseUrl.split("/")[3];
		System.out.println("Module:" + module);
	}
}
