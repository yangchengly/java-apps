package com.avaya.aps.elasticsearch.es_java_tcpclient;

import java.io.File;

public class FileReader {
	
	public static final String LOG_FILE_DIR = "D:/log/";
	
//	private BufferedReader br;
	
	public void read() {
		File file = new File(LOG_FILE_DIR);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				System.out.println(f.getAbsolutePath() + "|" + f.getName());
				
//				br = new BufferedReader
			}
		}
	}
	
	public static void main(String[] args) {
		FileReader reader = new FileReader();
		reader.read();
	}
}
