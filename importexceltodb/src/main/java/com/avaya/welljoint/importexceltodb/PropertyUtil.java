package com.avaya.welljoint.importexceltodb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {

	public static PropertyInfo loadPropertyInfo() {
		File file = new File("conf.properties");
		FileInputStream fis = null;
		Properties prop = null;
		try {
			fis = new FileInputStream(file);

			prop = new Properties();
			prop.load(fis);

			PropertyInfo pInfo = new PropertyInfo();
			pInfo.setDriver(prop.getProperty("driver"));
			pInfo.setUrl(prop.getProperty("url"));
			pInfo.setUsername(prop.getProperty("username"));
			pInfo.setPassword(prop.getProperty("password"));
			pInfo.setFileDir(prop.getProperty("fileDir"));

			return pInfo;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}
}
