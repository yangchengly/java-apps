package com.avaya.aps.httpclient.VxmlHttpClientSimulator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

public class Log4jTester extends Thread{
	
	private int threadNum = 0;
	private Logger logger;
	
	public Log4jTester(int i) {		
		this.threadNum = i;
		this.logger = this.getLogger();
	}
	
	public void run() {		
		this.logger.info("This thread number is " + this.threadNum);
	}
	
	public Logger getLogger() {
		String appenderName = "StressTesting-" + this.threadNum;
		
		System.out.println("appenderName:" + appenderName);
		
		PatternLayout layout = new PatternLayout();
		layout.setConversionPattern("%d{MM/dd HH:mm:ss} %p  %m%n");
		
		RollingFileAppender appender = new RollingFileAppender();
		
		appender.setName(appenderName);		
		appender.setFile("logs/" + appenderName + ".log");
		appender.setThreshold(Level.DEBUG);
		appender.setEncoding("UTF-8");		
		
		appender.activateOptions();		
		appender.setLayout(layout);		
		
		Logger logger = Logger.getLogger(appenderName);
		logger.addAppender(appender);
		
		return logger;
	}

	public static void main(String[] args) {
//		for (int i = 0; i <10; i++) {
//			new Log4jTester(i).start();
//			System.out.println("Created thread:" + i);
//		}
		
		long l = System.currentTimeMillis();
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		long l2 = System.currentTimeMillis();
		System.out.println(l2 - l);
		
	}
}
