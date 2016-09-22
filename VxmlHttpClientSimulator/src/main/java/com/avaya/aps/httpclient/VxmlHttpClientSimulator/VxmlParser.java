package com.avaya.aps.httpclient.VxmlHttpClientSimulator;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.avaya.aps.httpclient.VxmlHttpClientSimulator.actions.FileUtils;
import com.avaya.aps.httpclient.VxmlHttpClientSimulator.menu.Choice;
import com.avaya.aps.httpclient.VxmlHttpClientSimulator.menu.Menu;
import com.avaya.aps.httpclient.VxmlHttpClientSimulator.menu.MenuMap;

public class VxmlParser {

	public static List<SubmitAttributes> getSubmitAttribute(
			InputStream inputStream, Menu currentMenu,int threadNum, int step) throws Exception {

		List<SubmitAttributes> list = new ArrayList<SubmitAttributes>();

		String parentId = null;
		if (currentMenu != null) {
			parentId = currentMenu.getId();
		}

		String next = "";
		String method = "get";
		String namelist = "";

		try {
			SAXReader saxReader = new SAXReader();
			Document doc;
			doc = saxReader.read(inputStream);
			
			FileUtils.print(doc.asXML(),threadNum,step);

			String xmlns = doc.getRootElement().getNamespaceURI();
			Map<String, String> map = new HashMap<String, String>();
			map.put("h", xmlns);
			saxReader.getDocumentFactory().setXPathNamespaceURIs(map);

			Node submit = doc
					.selectSingleNode("//h:vxml/h:form/h:block/h:submit");

			// Submit is null when server responds exception normally;
			if (submit == null) {
				return list;
			}

			method = submit.valueOf("@method");
			namelist = submit.valueOf("@namelist").trim();

			// Parse XML with namespace.
			Node menuNode = doc.selectSingleNode("//h:vxml/h:menu");
			if (menuNode != null) {
				Map<String, String> choiceUrlMap = new HashMap<String, String>();

				List<?> listForm = doc.selectNodes("//h:vxml/h:form");
				for (Object object : listForm) {
					Node form = (Node) object;
					String formId = form.valueOf("@id");
					if (formId.startsWith("choice")) {
						String expr = form.selectSingleNode(
								"./h:block/h:assign").valueOf("@expr");

						choiceUrlMap.put(formId, expr);
					}
				}

				String menuId = menuNode.valueOf("@id");
				Menu objMenu = new Menu(menuId);
				objMenu.setParentId(parentId);

				List<?> listChoice = menuNode.selectNodes("./h:choice");
				for (int i = 0; i < listChoice.size(); i++) {
				if(listChoice.size() > 0) {
//					int i = 0;
					Node node = ((Node) listChoice.get(i));
					String dtmf = node.valueOf("@dtmf");
					String choiceNext = node.valueOf("@next");

					choiceNext = choiceNext.substring(1);
					String choiceUrl = choiceUrlMap.get(choiceNext);
					choiceUrl = choiceUrl.replaceAll("'", "");

					System.out.println("dtmf:" + dtmf + ";next:" + next
							+ ";choiceUrl:" + choiceUrl);

					Choice choice = new Choice(dtmf, choiceUrl);
					objMenu.addChoice(dtmf, choice);

					SubmitAttributes attrs = new SubmitAttributes();

					attrs.setFlag(true);
					attrs.setNext(choiceUrl);
					attrs.setNamelist(namelist);
					attrs.setMethod(method);
					attrs.setList(Choice.getNvps(menuId, dtmf));

					list.add(attrs);
				}
				}	
				currentMenu = objMenu;
				MenuMap.getInstance().put(objMenu.getId(), objMenu);
				
//				System.out.println(MenuMap.getInstance());
				
			} else {
				next = submit.valueOf("@next");
				method = method != null && method.equalsIgnoreCase("post") ? "post"
						: "get";

				System.out.println("next:" + next);
				SubmitAttributes attrs = new SubmitAttributes();
				attrs.setFlag(true);
				attrs.setNext(next);
				attrs.setNamelist(namelist);
				attrs.setMethod(method);
				
				list.add(attrs);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return list;
	}

	public static void main(String[] args) {
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new File("test2.xml"));
			String xmlns = document.getRootElement().getNamespaceURI();

			System.out.println(xmlns);

			// String xmlns =
			// document.getRootElement().attribute("xmlns").getStringValue();
			// Map<String, String> map = new HashMap<String, String>();
			// map.put("h", xmlns);
			//
			// reader.getDocumentFactory().setXPathNamespaceURIs(map);
			//
			//
			// List list =
			// document.selectNodes("//h:vxml/h:form/h:block/h:submit");
			// System.out.println(list.size());

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static boolean parseMenuOption(String url, String namelist,
			SubmitAttributes attrs, String method) throws Exception{
		
		if ("post".equalsIgnoreCase(method)) {
			
			List<NameValuePair> nvps = NameValueUtils.getNameList(
					NameValueUtils.getModuleName(url), namelist, 1);	
			
			if (nvps == null) {
				throw new RuntimeException("Didn't find related properties for the namelist");
				
			}
		
			List<NameValuePair> list = NameValueUtils.getMenuOption(nvps);
			
			if(attrs.getList()==null){
				return true;
			}
			
			String option = attrs.getList().get(0).getValue();
			
			for (NameValuePair nameValuePair : list) {
				String name = nameValuePair.getName();
				String value = nameValuePair.getValue();
				
				if(name.indexOf("__value")>0 && value.equals(option))
					return true;
			}
			return false;
			
		}
	
		return true;
	}
}
