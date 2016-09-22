package com.avaya.welljoint.importexceltodb;

import junit.framework.TestCase;

public class PropertyUtilTest extends TestCase {
	public void testLoadPropertyInfo() {
		PropertyInfo propInfo = PropertyUtil.loadPropertyInfo();
		System.out.println(propInfo.getDriver());
	}
}
