package com.avaya.aps.httpclient.VxmlHttpClientSimulator;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.avaya.aps.httpclient.VxmlHttpClientSimulator.actions.HttpAction;

/**
 * Hello world!
 * 
 */
public class App {
	
	public static void main(String[] args) throws InterruptedException {

		/*
		 * args[0]: thread count
		 */

		if (!CustomStringUtil.isNumeric(args[0])) {
			System.out.println("You must enter valid number for thread count!");
			System.exit(0);
		}

		if (args[1] == null) {
			System.out.println("You must enter valid base url!");
		}
		
		if (!CustomStringUtil.isNumeric(args[2])) {
			System.out.println("You must enter valid number for maximum steps!");
			System.exit(0);
		}
		
		// String baseUrl = "http://99.48.237.25:8081/Main/";		

		int threadCount = Integer.parseInt(args[0]);
		String baseUrl = args[1];
		int maxStep = Integer.parseInt(args[2]);
		
		HttpAction.maxStep = maxStep;

		System.out
				.println("threadCount:" + threadCount + ";baseUrl:" + baseUrl);

		// System.exit(0);

		int ani = 3000;
		String ucidPrefix = "0000100671";
		long ucid = 1467265209;

		PropertiesUtil.loadAllProperties("conf");

		List<NameValuePair> sesNameList = PropertiesUtil.findNamelist("Main",
				"session");
		

		
		for (int i = 0; i < threadCount; i++) {

			ani += 1;
			ucid += 1;
			
			
			
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();

			for (int j = 0; j < sesNameList.size(); j++) {
				String key = sesNameList.get(j).getName();

				if (key.equalsIgnoreCase("session___ani")) {
					nvps.add(new BasicNameValuePair("session___ani", String
							.valueOf(ani)));
				} else if (key.equalsIgnoreCase("session___sessionlabel")) {
					nvps.add(new BasicNameValuePair("session___sessionlabel",
							ucidPrefix + ucid));
				} else if (key.equalsIgnoreCase("session___ucid")) {
					nvps.add(new BasicNameValuePair("session___ucid",
							ucidPrefix + ucid));
				} else {
					nvps.add(sesNameList.get(j));
				}
			}
			
			PropertiesUtil.putSession(i, nvps);

//			new Thread(new HttpAction(i), baseUrl).start();
			new Thread(new HttpAction(i, baseUrl)).start();
			Thread.sleep(50);
		}
	}
	
	
	
	
}
