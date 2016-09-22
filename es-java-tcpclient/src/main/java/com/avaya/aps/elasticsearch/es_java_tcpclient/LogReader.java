package com.avaya.aps.elasticsearch.es_java_tcpclient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogReader {
	public static final String LOG_FILE_DIR = "D:/log/report.log";

	private BufferedReader br = null;
	private boolean flag = false;

	public LogReader() {
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(LOG_FILE_DIR)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public boolean getFlag() {
		return flag;
	}

	public List<ReportLog> loadWithoutHandlingException(int max) {
		List<ReportLog> list = new ArrayList<ReportLog>();

		try {

			String s = null;
			while ((s = br.readLine()) != null) {
				if (s.indexOf("session id") > 0) {
					ReportLog reportLog = this.getReportLog(s);

					list.add(reportLog);
					max--;
					if (max < 0) {
						return list;
					}

				}
			}

			flag = true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<ReportLog> load(int max) {

		List<ReportLog> list = new ArrayList<ReportLog>();

		try {

			String record = "";
			String s = null;
			while ((s = br.readLine()) != null) {

				if (s.indexOf("session id") > 0) {

					if (record != null && !record.equals("")) {

						

						ReportLog reportLog = this.getReportLog(record);

						list.add(reportLog);
						max--;
						if (max < 0) {

							System.out.println("list size:" + list.size());
							return list;
						}
					}

					record = s;
				} else {
					record += s;
				}
			}

			flag = true;

			// count++;
			// ReportLog reportLog = this.getReportLog(record);
			// System.out.println(String.valueOf(count) + ":" + reportLog);
			// list.add(reportLog);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public ReportLog getReportLog(String s) {
		ReportLog report = new ReportLog();
		try {

			
			String[] array = s.split("[|]");
			report.setDate(this.toDate(array[0].substring(0, 23).trim()));

			report.setLogLevel(array[0].substring(24, 29).trim());
			report.setStatus(array[0].substring(32).trim());

			String sessionId = array[1].split(":")[1].trim();
			sessionId = sessionId.replaceAll("[-]", "");
			report.setSessionID(sessionId);
			report.setModule(array[2].trim());
			report.setName(array[3].trim());
			report.setType(array[4].trim());
			report.setNode(array[5].trim());
			report.setInfo(array[6].trim());
			report.setMessage(array[7].trim());
		} catch (Exception ex) {
			return report;
		}

		return report;
	}

	public Date toDate(String sDate) {

		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");
			date = sdf.parse(sDate);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public void close() {
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void read() {
		File file = new File(LOG_FILE_DIR);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				System.out.println(f.getAbsolutePath() + "|" + f.getName());
			}
		}
	}

	// public static void main(String[] args) {
	// LogReader logReader = new LogReader();
	//
	// String s = "";
	// ReportLog reportLog = logReader.getReportLog(s);
	// System.out.println(reportLog);
	// }

	public static void main(String[] args) {

	}
}
