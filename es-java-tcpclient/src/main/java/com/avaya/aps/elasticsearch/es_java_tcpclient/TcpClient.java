package com.avaya.aps.elasticsearch.es_java_tcpclient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.max.MaxBuilder;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.min.MinBuilder;
import org.joda.time.DateTime;

public class TcpClient {

	private Client client;
	
	private List<Bucket> list = new ArrayList<Bucket>();

	public TcpClient() {
		try {
			client = TransportClient.builder().build()
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public void searchMatchAll(String index, String type) {
		SearchResponse response = (SearchResponse) client.prepareSearch(new String[] { index })
				.setTypes(new String[] { type }).setSearchType(SearchType.DFS_QUERY_THEN_FETCH).execute().actionGet();

		System.out.println(response);

	}

	public List<Bucket> aggregateBySessionId(String index, String type, long termSize) {
		TermsBuilder termBuilder = AggregationBuilders.terms("reportlog").field("sessionID");
		termBuilder.size((int) termSize);

		SearchRequestBuilder srb = client.prepareSearch(new String[] { index }).setTypes(new String[] { type });
		srb.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		srb.setSize(1);
		srb.addAggregation(termBuilder);

		SearchResponse response = (SearchResponse) srb.execute().actionGet();
		// System.out.println(response);

		Terms terms = response.getAggregations().get("reportlog");
		List<Bucket> lBuckets = terms.getBuckets();
		for (Bucket bucket : lBuckets) {
			list.add(bucket);
		}

		long sumOfCurDocCount = lBuckets.size();
		System.out.println("sumOfCurDocCount:" + sumOfCurDocCount);
		
		long sumOfOtherDocCount = terms.getSumOfOtherDocCounts();
		System.out.println("sumOfOtherDocCount:" + sumOfOtherDocCount);
		
		sumOfOtherDocCount = sumOfOtherDocCount - lBuckets.size();

		if (sumOfOtherDocCount > 0) {
//			termSize = sumOfCurDocCount + sumOfOtherDocCount;
			lBuckets = this.aggregateBySessionId1(index, type, termSize, sumOfOtherDocCount);
//			return lBuckets;
		}

		return list;
	}
	
	public List<Bucket> aggregateBySessionId1(String index, String type, long termSize, long sumOfOtherDocCount) {
		TermsBuilder termBuilder = AggregationBuilders.terms("reportlog").field("sessionID");
		termBuilder.size((int) termSize);

		SearchRequestBuilder srb = client.prepareSearch(new String[] { index }).setTypes(new String[] { type });
		srb.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		srb.setSize(1);
		srb.addAggregation(termBuilder);

		SearchResponse response = (SearchResponse) srb.execute().actionGet();
		// System.out.println(response);

		Terms terms = response.getAggregations().get("reportlog");
		List<Bucket> lBuckets = terms.getBuckets();
		for (Bucket bucket : lBuckets) {
			list.add(bucket);
		}
		
		sumOfOtherDocCount = sumOfOtherDocCount - lBuckets.size();

		long sumOfCurDocCount = lBuckets.size();
		System.out.println("sumOfCurDocCount:" + sumOfCurDocCount);

		if (sumOfOtherDocCount > 0) {
//			termSize = sumOfCurDocCount + sumOfOtherDocCount;
			lBuckets = this.aggregateBySessionId1(index, type, termSize, sumOfOtherDocCount);
//			return lBuckets;
		}

		return list;
	}

	public String queryMaxDate(String index, String type, String sessionID) {
		String aggrName = "queryMinDate";

		MaxBuilder maxBuilder = AggregationBuilders.max(aggrName).field("date");

		SearchRequestBuilder srb = client.prepareSearch(new String[] { index }).setTypes(new String[] { type });
		srb.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		srb.setQuery(QueryBuilders.termQuery("sessionID", sessionID));
		srb.setSize(0);
		srb.addAggregation(maxBuilder);

		SearchResponse response = (SearchResponse) srb.execute().actionGet();

		Max max = response.getAggregations().get(aggrName);
		// System.out.println(max.getValueAsString());

		return max.getValueAsString();
	}

	public String queryMinDate(String index, String type, String sessionID) {
		String aggrName = "queryMinDate";

		MinBuilder minBuilder = AggregationBuilders.min(aggrName).field("date");

		SearchRequestBuilder srb = client.prepareSearch(new String[] { index }).setTypes(new String[] { type });
		srb.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		srb.setQuery(QueryBuilders.termQuery("sessionID", sessionID));
		srb.setSize(0);
		srb.addAggregation(minBuilder);

		SearchResponse response = (SearchResponse) srb.execute().actionGet();

		Min min = response.getAggregations().get(aggrName);
		// System.out.println(min.getValueAsString());

		return min.getValueAsString();
	}

	public List<ReportLog> queryErrorMessage(String index, String type) {
		SearchRequestBuilder srb = client.prepareSearch(new String[] { index }).setTypes(new String[] { type });
		srb.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		srb.setQuery(QueryBuilders.termQuery("logLevel", "error"));

		SearchResponse response = (SearchResponse) srb.execute().actionGet();
		SearchHits sHits = response.getHits();

		SearchHit[] arrHit = sHits.getHits();

		List<ReportLog> list = new ArrayList<ReportLog>();
		for (SearchHit searchHit : arrHit) {
			ReportLog reportLog = new ReportLog();

			reportLog.setDate(new DateTime((String) searchHit.getSource().get("date")).toDate());
			reportLog.setInfo((String) searchHit.getSource().get("info"));
			reportLog.setMessage((String) searchHit.getSource().get("message"));
			reportLog.setModule((String) searchHit.getSource().get("module"));
			reportLog.setName((String) searchHit.getSource().get("name"));
			reportLog.setNode((String) searchHit.getSource().get("node"));
			reportLog.setSessionID((String) searchHit.getSource().get("sessionID"));
			reportLog.setStatus((String) searchHit.getSource().get("status"));
			reportLog.setType((String) searchHit.getSource().get("type"));
			reportLog.setLogLevel((String) searchHit.getSource().get("logLevel"));
			list.add(reportLog);
		}

		return list;
	}

	public void bulkIndex(String index, String type, List<ReportLog> reportLogs) {

		BulkRequestBuilder brb = client.prepareBulk();

		Iterator<ReportLog> iter = reportLogs.iterator();
		while (iter.hasNext()) {
			ReportLog reportLog = iter.next();

			IndexRequestBuilder request = client.prepareIndex(index, type);
			request.setSource(JsonUtil.convertObjectToJson(reportLog));

			brb.add(request);
//			
//			System.out.println("HashCode:" + reportLog.hashCode());
		}

		BulkResponse response = brb.get();
		System.out.println(response);

	}

//	public static void main(String[] args) {
////		String index = "reportlog-2016.08.23";
//		String index = "test2";
//		String type = "logs";
//
//		TcpClient client = new TcpClient();
//		List<Bucket> lBuckets = client.aggregateBySessionId(index, type, 10);
//		System.out.println("Session ID | sum | start time | end time | duration(seconds) | duration(mills)");
//
//		for (Bucket bucket : lBuckets) {
//
//			String sessionID = (String) bucket.getKey();
//			long docCount = bucket.getDocCount();
//
//			String minDate = client.queryMinDate(index, type, sessionID);
//			String maxDate = client.queryMaxDate(index, type, sessionID);
//
//			DateTime minDt = new DateTime(minDate);
//			DateTime maxDt = new DateTime(maxDate);
//
//			Seconds sec = Seconds.secondsBetween(minDt, maxDt);
//			int duration = sec.getSeconds();
//
//			long mills = maxDt.getMillis() - minDt.getMillis();
//
//			System.out.println(sessionID + " | " + docCount + " | " + minDt.toString("yyyy-MM-dd HH:mm:ss.SSS") + " | "
//					+ maxDt.toString("yyyy-MM-dd HH:mm:ss.SSS") + " | " + duration + " | " + mills);
//		}
//
//		List<ReportLog> list = client.queryErrorMessage(index, type);
//		for (ReportLog reportLog : list) {
//			System.out
//					.println(reportLog.getLogLevel() + " | " + reportLog.getSessionID() + " | " + reportLog.getInfo());
//
//			// String info = reportLog.getInfo();
//			// String[] pieces = info.split(" ");
//			// for (String string : pieces) {
//			// System.out.println(string);
//			// }
//		}
//	}
}
