package com.avaya.welljoint.importexceltodb.pachira;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class SunSpreadsheetReader {

	private Map<String, DataAttached> dataAttachedMap = new HashMap<String, DataAttached>();

	public void read2Record(String businessType) {
		String fileDir = "D:/Sunshine/Complain_Sample/2_Reord.xls";

		File file = new File(fileDir);
		Workbook workbook = null;

		try {
			workbook = WorkbookFactory.create(file);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Sheet sheet = workbook.getSheetAt(0);
		int numOfRow = sheet.getLastRowNum();
		numOfRow++;
		
		for (int i = 2; i < numOfRow; i++) {
			Row row = sheet.getRow(i);

			DataAttached da = new DataAttached();
			da.setId(row.getCell(0).getStringCellValue());
			da.setMobile(row.getCell(1).getStringCellValue());
			da.setCallingDate(row.getCell(2).getStringCellValue());
			da.setStartTime(row.getCell(3).getStringCellValue());
			da.setEndTime(row.getCell(4).getStringCellValue());
			da.setDuration(row.getCell(5).getStringCellValue());
			da.setCallingType(row.getCell(7).getStringCellValue());
			da.setBusinessType(businessType);

			dataAttachedMap.put(row.getCell(0).getStringCellValue(), da);
		}
		
		System.out.println("numOfRow:" + numOfRow);
	}

	public void read3RecordYW() {
		String fileDir = "D:/Sunshine/Complain_Sample/3_ReordYW.xls";

		File file = new File(fileDir);
		Workbook workbook = null;

		try {
			workbook = WorkbookFactory.create(file);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Sheet sheet = workbook.getSheetAt(0);
		int numOfRow = sheet.getLastRowNum();
		numOfRow++;
		
		for (int i = 2; i < numOfRow; i++) {
			Row row = sheet.getRow(i);

			String businessId = row.getCell(0).getStringCellValue();
			String id = row.getCell(1).getStringCellValue();
			String custNo = row.getCell(2).getStringCellValue();
			String comment = row.getCell(3).getStringCellValue();
			String callingType = row.getCell(4).getStringCellValue();
			String agentNo = row.getCell(5).getStringCellValue();
			String gender = row.getCell(6).getStringCellValue();
			String channelType = row.getCell(7).getStringCellValue();
			String ensureNo = row.getCell(8).getStringCellValue();

			DataAttached da = dataAttachedMap.get(id);
			if (da == null) {
				System.out.println("id:" + id);
				continue;
			}
			
			da.setBusinessId(businessId);
			da.setCustNo(custNo);
			da.setComment(comment);
			da.setCallingType(callingType);
			da.setAgentNo(agentNo);
			da.setGender(gender);
			da.setChannelType(channelType);
			da.setEnsureNo(ensureNo);

			dataAttachedMap.put(id, da);
		}
		System.out.println("numOfRow:" + numOfRow);
	}	

	public Map<String, DataAttached> getDataAttachedMap() {
		return dataAttachedMap;
	}

	public static void main(String[] args) {
		SunSpreadsheetReader reader = new SunSpreadsheetReader();
		reader.read2Record("投诉训练");
		
		reader.read3RecordYW();

		Iterator<Entry<String, DataAttached>> iter = reader.getDataAttachedMap().entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, DataAttached> entry = iter.next();
			System.out.println(entry);
		}
		
		System.out.println("Size:" + reader.getDataAttachedMap().size());
	}
}
