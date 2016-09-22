package com.avaya.aps.httpclient.VxmlHttpClientSimulator.logger;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

public class LoggerFactory {

	private static final String LOG_FILE_NAME = "avaya-cmb-ivr-";

	public static Logger getLogger(String name) {
		String fileName = LoggerFactory.LOG_FILE_NAME + name;

		PatternLayout layout = new PatternLayout();
		layout.setConversionPattern("%d{MM/dd HH:mm:ss} %p  %m%n");

		RollingFileAppender appender = new RollingFileAppender();
		appender.setName(fileName);
		appender.setFile("logs/" + fileName + ".log");
		appender.setThreshold(Level.DEBUG);
		appender.setEncoding("UTF-8");
		appender.activateOptions();
		appender.setLayout(layout);

		Logger logger = Logger.getLogger(fileName);
		logger.addAppender(appender);

		return logger;
	}
}
