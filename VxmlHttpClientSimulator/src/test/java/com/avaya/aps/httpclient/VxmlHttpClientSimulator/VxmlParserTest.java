package com.avaya.aps.httpclient.VxmlHttpClientSimulator;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class VxmlParserTest {

	public static Map<String, String> choiceUrlMap = new HashMap<String, String>();

	public static void main(String[] args) {
		File file = new File("menu.xml");

		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read(file);

			String xmlns = document.getRootElement().getNamespaceURI();
			Map<String, String> map = new HashMap<String, String>();
			map.put("h", xmlns);
			saxReader.getDocumentFactory().setXPathNamespaceURIs(map);

			Node menu = document.selectSingleNode("//h:vxml/h:menu");
			if (menu != null) {
				Map<String, String> choiceUrlMap = new HashMap<String, String>();

				List<?> listForm = document.selectNodes("//h:vxml/h:form");
				for (Object object : listForm) {
					Node form = (Node) object;
					String formId = form.valueOf("@id");
					if (formId.startsWith("choice")) {
						String expr = form.selectSingleNode(
								"./h:block/h:assign").valueOf("@expr");

						choiceUrlMap.put(formId, expr);
					} 
				}
				
//				Node submit = document.selectSingleNode("//h:vxml/h:form/h:block/h:submit");

				// String id = menu.valueOf("@id");
				List<?> listChoice = menu.selectNodes("./h:choice");

				for (int i = 0; i < listChoice.size(); i++) {
					Node node = ((Node) listChoice.get(i));
					String choice = node.valueOf("@dtmf");
					String next = node.valueOf("@next");

					next = next.substring(1);
					String choiceUrl = choiceUrlMap.get(next);
					choiceUrl = choiceUrl.replaceAll("'", "");

					System.out.println("Choice:" + choice + ";next:" + next
							+ ";choiceUrl:" + choiceUrl);
				}
			} else {
				Node collect = document
						.selectSingleNode("//h:vxml/h:form/h:block/h:submit");
				String next = collect.valueOf("@next");

				System.out.println("next:" + next);
			}

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
