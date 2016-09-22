package com.avaya.welljoint.importexceltodb;

public class Cache {
	public static PropertyInfo propertyInfo;	
	
	static {
		propertyInfo = PropertyUtil.loadPropertyInfo();
		if (propertyInfo == null) {
			System.out.println("Couldn't load conf.properties properly!");
			System.out.println("Programming is going to exit!");
			System.exit(0);
		}
	}
}
