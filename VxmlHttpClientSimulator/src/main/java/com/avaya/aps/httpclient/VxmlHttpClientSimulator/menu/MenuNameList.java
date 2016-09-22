package com.avaya.aps.httpclient.VxmlHttpClientSimulator.menu;

import java.util.HashMap;
import java.util.Map;

public class MenuNameList {
	
	public static Map<String, String> mapMenu = new HashMap<String, String>(7);
	
	static {
		mapMenu.put("MenuNode___value", "1");
		mapMenu.put("MenuNode___confidence", "1");
		mapMenu.put("MenuNode___utterance", "1");
		mapMenu.put("MenuNode___inputmode", "dtmf");
		mapMenu.put("MenuNode___interpretation", "1");
		mapMenu.put("MenuNode___noinputcount", "0");
		mapMenu.put("MenuNode___nomatchcount", "0");
	}
}
