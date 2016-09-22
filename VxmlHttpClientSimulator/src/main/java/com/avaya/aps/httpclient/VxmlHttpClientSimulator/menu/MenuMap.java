package com.avaya.aps.httpclient.VxmlHttpClientSimulator.menu;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MenuMap {

	private static MenuMap mm;
	private static Map<String, Menu> menuMap;

	private MenuMap() {
		menuMap = Collections.synchronizedMap(new HashMap<String, Menu>());
	}

	public synchronized static MenuMap getInstance() {
		if (mm == null)
			mm = new MenuMap();
		return mm;
	}

	public void put(String menuId, Menu menu) {
		menuMap.put(menuId, menu);
	}

	public Menu get(String menuId) {
		return menuMap.get(menuId);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		Iterator<Entry<String, Menu>> iterator = menuMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Menu> entry = iterator.next();
			sb.append("MenuId:" + entry.getKey() + ";");
			sb.append(entry.getValue());
		}
		sb.append("\n");

		return sb.toString();
	}
}
