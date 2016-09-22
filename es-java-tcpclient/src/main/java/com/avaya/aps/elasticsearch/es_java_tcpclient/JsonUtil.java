package com.avaya.aps.elasticsearch.es_java_tcpclient;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

public class JsonUtil {
	public static String convertObjectToJson(Object value) {

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);  
			mapper.getSerializationConfig().withDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS"));
			
			StringWriter sw = new StringWriter();
			JsonGenerator gen = new JsonFactory().createJsonGenerator(sw);
			
			
			mapper.writeValue(gen, value);
			gen.close();

			return sw.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		String s = "08/08/2035 19:25:41:203  INFO - In Progress | session id:scccaepmpp02103-2016231024810-14 | Main:sltCallModule | MainTest |  testmessage | fuctionid | 4 | 00001101961471488500";
		
		LogReader logReader = new LogReader();
		ReportLog reportLog = logReader.getReportLog(s);
		
		String json = JsonUtil.convertObjectToJson(reportLog);
		System.out.println(json);
	}
}
