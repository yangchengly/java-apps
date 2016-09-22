package com.avaya.aps.httpclient.VxmlHttpClientSimulator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CustomStringUtil{
	
	public static boolean isNumeric(String s) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher matcher = pattern.matcher(s);
		
		if (matcher.matches())
			return true;
		
		return false;
	}
}
