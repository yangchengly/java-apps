package com.avaya.aps.httpclient.VxmlHttpClientSimulator.actions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

	public static void print(String content ,int threadNum,int step) {
	
		File file = new File("logs\\content" + "-" + threadNum + "-" + step);
		
		BufferedWriter bufferedWriter = null;
		try {


			bufferedWriter = new BufferedWriter(new FileWriter(file,true));
			bufferedWriter.write(content);
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			// 将缓冲区中的数据全部写出
	        try {
	        	bufferedWriter.flush();

				
	        	bufferedWriter.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
	        System.out.println("复制完成");
		}

      

        

		
		
	}

}
