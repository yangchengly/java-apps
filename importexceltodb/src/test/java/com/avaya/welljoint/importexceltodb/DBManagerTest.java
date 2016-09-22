package com.avaya.welljoint.importexceltodb;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class DBManagerTest extends TestCase {

	public void testBatchInsert() {
		DBManager dbManager = new DBManager();

		List<MobileOwnership> list = new ArrayList<MobileOwnership>();

		MobileOwnership mo = new MobileOwnership();
		mo.setId("1");
		mo.setMobile("1869862");
		mo.setProvince("Liaoning");
		mo.setCity("Dalian");
		mo.setCorp("ChinaMobile");
		mo.setAreacode("0411");
		mo.setPostcode("600010");
		
		list.add(mo);
		
		MobileOwnership mo2 = new MobileOwnership();
		mo2.setId("2");
		mo2.setMobile("1850243");
		mo2.setProvince("Liaoning");
		mo2.setCity("Dalian");
		mo2.setCorp("ChinaMobile");
		mo2.setAreacode("0411");
		mo2.setPostcode("600010");
		
		list.add(mo2);

		dbManager.batchInsert(list);
	}
}
