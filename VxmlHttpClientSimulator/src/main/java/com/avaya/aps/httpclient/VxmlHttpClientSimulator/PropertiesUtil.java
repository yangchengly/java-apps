package com.avaya.aps.httpclient.VxmlHttpClientSimulator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class PropertiesUtil {

	private static Map<Integer, List<NameValuePair>> sessionMap = new HashMap<Integer, List<NameValuePair>>();
	private static Map<String, Map<String, List<NameValuePair>>> map = new HashMap<String, Map<String, List<NameValuePair>>>();
	private static final ThreadLocal<Map<String, Map<String, List<NameValuePair>>>> threadLocal = new ThreadLocal<Map<String, Map<String, List<NameValuePair>>>>();

	public static void loadAllProperties(String path) {

		File sessionProperties = new File(path + "/" + "session.properties");
		List<NameValuePair> sList = readProperties(sessionProperties);

		File root = new File(path);
		File[] rootFiles = root.listFiles();

		for (File file : rootFiles) {
			if (file.isDirectory()) {
				String dir = file.getName();

				Map<String, List<NameValuePair>> m = new HashMap<String, List<NameValuePair>>();
				File[] subFiles = file.listFiles();
				for (File f : subFiles) {
					if (f.isDirectory())
						continue;

					String fName = f.getName();
					fName = fName.substring(0, fName.indexOf("."));
					m.put(fName, readProperties(f));
				}

				m.put("session", sList);
				map.put(dir, m);
			}
		}
		
//		threadLocal.set(map);
	}
	
	@SuppressWarnings("unchecked")
	public static void loadConfProperties() {


		
		try {
			threadLocal.set((Map<String, Map<String, List<NameValuePair>>>)clone(map));
//			System.out.println((Map<String, Map<String, List<NameValuePair>>>)threadLocal.get());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static List<NameValuePair> readProperties(File file) {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		FileInputStream fis = null;

		try {
			fis = new FileInputStream(file);

			Properties prop = new Properties();
			prop.load(fis);

			Enumeration<?> enu = prop.propertyNames();
			while (enu.hasMoreElements()) {
				String key = (String) enu.nextElement();
				String value = prop.getProperty(key);

				nvps.add(new BasicNameValuePair(key, value));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return nvps;
	}

	public static List<NameValuePair> findNamelist(String module, String node) {
//		return map.get(module).get(node);
		Map<String, Map<String, List<NameValuePair>>> threadMap = (Map<String, Map<String, List<NameValuePair>>>)threadLocal.get();
		if(threadMap == null){
			loadConfProperties();
			threadMap = (Map<String, Map<String, List<NameValuePair>>>)threadLocal.get();
		}
		return threadMap.get(module).get(node);
	}

	public static void putSession(int id, List<NameValuePair> session) {
		sessionMap.put(id, session);
	}

	public static List<NameValuePair> getSession(int id) {
		return sessionMap.get(id);
	}	
	
	
    public static Object clone(Object src) throws IOException, ClassNotFoundException{
        if(src==null) return src;
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ObjectOutputStream oos=new ObjectOutputStream(baos);
        oos.writeObject(src);
        ByteArrayInputStream bais=new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois=new ObjectInputStream(bais);        
        return ois.readObject();
    } 
    
}
