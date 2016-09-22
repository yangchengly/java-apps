package com.avaya.welljoint.importexceltodb;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class SpreadsheetReader {

	public FileInputStream getFileInputStream() {
		String fileDir = Cache.propertyInfo.getFileDir();

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fileDir);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fis;
	}

	public void readXlsFile() {

		try {
			Workbook workbook = WorkbookFactory.create(this.getFileInputStream());
			int numberOfSheets = workbook.getNumberOfSheets();

			List<MobileOwnership> list = new ArrayList<MobileOwnership>(65535);

			DBManager dbManager = new DBManager();

			for (int i = 0; i < numberOfSheets; i++) {
				Sheet sheet = workbook.getSheetAt(i);

				int lastRowNum = sheet.getLastRowNum();
				System.out.println("lastRowNum:" + lastRowNum);
				for (int j = 1; j <= lastRowNum; j++) {
					Row row = sheet.getRow(j);

					MobileOwnership mo = new MobileOwnership();
					mo.setId(String.valueOf(row.getCell(0).getNumericCellValue()));
					mo.setMobile(row.getCell(1).getStringCellValue());
					mo.setProvince(row.getCell(2).getStringCellValue());
					mo.setCity(row.getCell(3).getStringCellValue());
					mo.setCorp(row.getCell(4).getStringCellValue());
					mo.setAreacode(row.getCell(5).getStringCellValue());
					mo.setPostcode(row.getCell(6).getStringCellValue());

					list.add(mo);
				}

				dbManager.batchInsert(list);
				list = new ArrayList<MobileOwnership>(65535);

			}

		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
