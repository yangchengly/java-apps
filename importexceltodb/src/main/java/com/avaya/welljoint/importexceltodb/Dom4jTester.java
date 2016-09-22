package com.avaya.welljoint.importexceltodb;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class Dom4jTester {

	public static void main(String[] args) {
		File file = new File("dom4j.xml");
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(file);
//			System.out.println(document.asXML());

			Node node = document.selectSingleNode("//students/form/block/submit");

			System.out.println(node.asXML());
			if (node != null) {

				String name = node.valueOf("@next");
				System.out.println("next:" + name);
				
				String namelist = node.valueOf("@namelist");
				System.out.println("namelist:" + namelist);
			}

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
