package com.avaya.aps.elasticsearch.es_java_tcpclient;

import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

public class SpreadsheetWriter {
	public static void writeSpreadsheet(List<Bucket> list, String index, String type) {

		try {
			Workbook workbook = new SXSSFWorkbook();

			Sheet sheet = workbook.createSheet("IVR Stress Testing Reporting");

			Row row = sheet.createRow(0);
			row.createCell(0).setCellValue("SN");
			row.createCell(1).setCellValue("Session ID");
			row.createCell(2).setCellValue("Sum");
			row.createCell(3).setCellValue("Start time");
			row.createCell(4).setCellValue("End time");
			row.createCell(5).setCellValue("Duration(seconds)");
			row.createCell(6).setCellValue("Duration(mills)");

			int sizeOfBucket = list.size();
			TcpClient client = new TcpClient();

			for (int i = 0; i < sizeOfBucket; i++) {
				Bucket bucket = list.get(i);

				String sessionID = (String) bucket.getKey();
				long docCount = bucket.getDocCount();

				String minDate = client.queryMinDate(index, type, sessionID);
				String maxDate = client.queryMaxDate(index, type, sessionID);

				DateTime minDt = new DateTime(minDate);
				DateTime maxDt = new DateTime(maxDate);

				Seconds sec = Seconds.secondsBetween(minDt, maxDt);
				int duration = sec.getSeconds();

				long mills = maxDt.getMillis() - minDt.getMillis();

//				System.out.println(sessionID + " | " + docCount + " | " + minDt.toString("yyyy-MM-dd HH:mm:ss.SSS")
//						+ " | " + maxDt.toString("yyyy-MM-dd HH:mm:ss.SSS") + " | " + duration + " | " + mills);

				row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(i + 1);
				row.createCell(1).setCellValue(sessionID);
				row.createCell(2).setCellValue(docCount);
				row.createCell(3).setCellValue(minDt.toString("yyyy-MM-dd HH:mm:ss.SSS"));
				row.createCell(4).setCellValue(maxDt.toString("yyyy-MM-dd HH:mm:ss.SSS"));
				row.createCell(5).setCellValue(duration);
				row.createCell(6).setCellValue(mills);
			}

			FileOutputStream fos = new FileOutputStream(Constants.INDEX + ".xlsx");
			workbook.write(fos);
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
