package com.avaya.aps.httpclient.VxmlHttpClientSimulator;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class NameValueUtils {
	
	public static List<NameValuePair> getNameList(String module, String namelist,
			int threadNum) {
		String node = namelist.split(" ")[0].split("___")[0];

		/*
		 * As an session data is to be shared in each module for one call, but
		 * session should be shared between calls, so the ideal way is to create
		 * a unique session data for each call.
		 */
		if (node != null && node.equals("session"))
			return PropertiesUtil.getSession(threadNum);

		return PropertiesUtil.findNamelist(module, node);
	}
	
	public static List<NameValuePair> getMenuOption(List<NameValuePair> nvps) {
		/*
		 * In order to support defining more options in specified menu for more
		 * than one time.
		 */

		List<NameValuePair> list = new ArrayList<NameValuePair>();

		NameValuePair nvpToBeRemoved = null;
		NameValuePair nvpToBeAdded = null;

		for (NameValuePair nameValuePair : nvps) {

			String name = nameValuePair.getName();
			String value = nameValuePair.getValue();
			
			if(value == null){
				System.out.println(name);
				new RuntimeException("Didn't find value for the menu").printStackTrace();
				return null;
			}

			if (name.indexOf("___value") > 0 && name.startsWith("M")) {
				String[] values = value.split(",");

				if (values.length > 1) {
					list.add(new BasicNameValuePair(name, values[0]));

					nvpToBeRemoved = nameValuePair;
					nvpToBeAdded = new BasicNameValuePair(name,
							value.substring(value.indexOf(',') + 1));
				} 
				else {
					nvpToBeRemoved = nameValuePair;
					list.add(nameValuePair);
					nvpToBeAdded = new BasicNameValuePair(name,null);
				}

			} else {
				list.add(nameValuePair);
				
			}
		}

		if (nvpToBeRemoved != null) {
//			nvps.remove(nvpToBeRemoved);
			
		}
		if(nvpToBeAdded !=null){
//			nvps.add(nvpToBeAdded);
		}

		return list;
	}
	
	public static String getModuleName(String baseUrl) {
		return baseUrl.split("/")[3];
	}
}
