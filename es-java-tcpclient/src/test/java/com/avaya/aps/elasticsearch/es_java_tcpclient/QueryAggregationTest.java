package com.avaya.aps.elasticsearch.es_java_tcpclient;

import java.util.List;

import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.junit.Test;

public class QueryAggregationTest {

	// @Test
	// public void testBulkIndex() {
	// LogReader logReader = new LogReader();
	// List<ReportLog> reportLogs = logReader.load();
	//
	// String index = "test2";
	// String type = "logs";
	//
	// TcpClient client = new TcpClient();
	// client.bulkIndex(index, type, reportLogs);
	// }

	@Test
	public void testAggregation() {

		TcpClient client = new TcpClient();
		List<Bucket> lBuckets = client.aggregateBySessionId(Constants.INDEX, Constants.TYPE, 1000000);
		System.out.println("Size of lBuckets:" + lBuckets.size());

		SpreadsheetWriter.writeSpreadsheet(lBuckets, Constants.INDEX, Constants.TYPE);
	}
}
