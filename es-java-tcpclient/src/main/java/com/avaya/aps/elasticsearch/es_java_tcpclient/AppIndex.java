package com.avaya.aps.elasticsearch.es_java_tcpclient;

import java.util.List;

public class AppIndex {

	public static final String INDEX = "reportlog-2016.08.24";
	public static final String TYPE = "logs";
	public static final int BULK_SIZE = 10000;

	public static void main(String[] args) {
		LogReader logReader = new LogReader();
		TcpClient client = new TcpClient();

		while (!logReader.getFlag()) {
			List<ReportLog> reportLogs = logReader.loadWithoutHandlingException(BULK_SIZE);
			client.bulkIndex(INDEX, TYPE, reportLogs);
		}

	}
}
