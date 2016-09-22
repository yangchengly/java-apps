package com.avaya.welljoint.importexceltodb.pachira;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map;

public class DimWriter {
	public static final String DIM_NEW_DIR = "D:\\temp\\2016-07-23.dim.final";
	public static final String DIM_DIR = "D:\\temp\\2016-07-23.dim";

	public static void main(String[] args) {

		SunSpreadsheetReader reader = new SunSpreadsheetReader();
		reader.read2Record("投诉训练");
		reader.read3RecordYW();
		reader.getDataAttachedMap();
		Map<String, DataAttached> dataAttachedMap = reader.getDataAttachedMap();

		int i = 0;

		BufferedWriter bw = null;
		BufferedReader br = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(DIM_NEW_DIR)));
			bw.write("任务流水号|录音列表|业务id|呼叫类型|渠道名称|坐席工号|话务小结|保单号|客户号码|客户性别|客户手机号|通话日期|通话开始时间|通话结束时间|业务类型");
			bw.newLine();

			br = new BufferedReader(new InputStreamReader(new FileInputStream(DIM_DIR)));

			String line = br.readLine();
			line = br.readLine();

			while (line != null) {
				i++;

				String id = line.substring(0, line.indexOf('|'));
				String wavFile = line.substring(line.indexOf('/') + 1, line.indexOf('.'));

				DataAttached da = dataAttachedMap.get(wavFile);
				bw.write(id + "|" + id + "|" + da.getBusinessId() + "|" + da.getCallingType() + "|"
						+ da.getChannelType() + "|" + da.getAgentNo() + "|" + da.getComment() + "|" + da.getEnsureNo()
						+ "|" + da.getCustNo() + "|" + da.getGender() + "|" + da.getMobile() + "|" + da.getCallingDate()
						+ "|" + da.getStartTime() + "|" + da.getEndTime() + "|" + da.getBusinessType());
				bw.newLine();
				bw.flush();

				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("i:" + i);
	}
}
