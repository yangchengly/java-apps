package com.avaya.aps.elasticsearch.es_java_tcpclient;

import java.util.List;

import org.junit.Test;

public class IndexCreationTest {

	@Test
	public void testCreateIndex() {
		LogReader logReader = new LogReader();
		TcpClient client = new TcpClient();

		while (!logReader.getFlag()) {
			List<ReportLog> reportLogs = logReader.loadWithoutHandlingException(Constants.BULK_SIZE);
			client.bulkIndex(Constants.INDEX, Constants.TYPE, reportLogs);
		}

	}
}
