package com.avaya.aps.httpclient.VxmlHttpClientSimulator.menu;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Menu {

	public static Map<String, Menu> menuMap = Collections
			.synchronizedMap(new HashMap<String, Menu>());

	private String id;
	private String parentId;
	private Map<String, Choice> choices = new HashMap<String, Choice>();

	public Menu(String menuId) {
		this.id = menuId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public void addChoice(String dtmf, Choice choice) {
		choices.put(dtmf, choice);
	}

	public Choice getChoice(String dtmf) {
		return choices.get(dtmf);
	}

	public Choice next() {
		return choices.entrySet().iterator().next().getValue();
	}

	public Map<String, Choice> getChoices() {
		return choices;
	}

	public void setChoices(Map<String, Choice> choices) {
		this.choices = choices;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer(100);

		sb.append("id:" + this.id + ";");
		sb.append("parentId:" + this.parentId + ";");
		sb.append("\n");

		Iterator<Entry<String, Choice>> it = this.choices.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Choice> en = it.next();
			Choice choice = en.getValue();
			String dtmf = choice.getDtmf();
			String expr = choice.getExpr();

			sb.append("dtmf:" + dtmf + ";");
			sb.append("expr:" + expr + ";");
			sb.append("\n");
		}		

		return sb.toString();
	}
}
