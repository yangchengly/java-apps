package com.avaya.aps.httpclient.VxmlHttpClientSimulator.quartz;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


/**
 * 
 * @ClassName: QuartzVo
 * 
 * @author Xingle
 * @date 2014-8-14 下午12:49:25
 */
public class QuartzVo implements Job{
    
    public static int Id = 0;
    
    public static List<QuartzVo> quartzList = new ArrayList<QuartzVo>();
    
    public static Map<String, QuartzVo> quartzMap = new HashMap<String, QuartzVo>();
    
        
    /**
     * id
     */
    public String id ;
    /**
     * 
     */
    public String jobTitle;
    /**
     * 
     */
    public String  jcallpath;
    /**
     * 
     */
    public String jobcron;
    /**
    * @Fields s_date : 
    */
    public String s_date;
    /**
    * @Fields cycle : 
    */
    public String cycle;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getJobTitle() {
        return jobTitle;
    }
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    public String getJcallpath() {
        return jcallpath;
    }
    public void setJcallpath(String jcallpath) {
        this.jcallpath = jcallpath;
    }
    public String getJobcron() {
        return jobcron;
    }
    public void setJobcron(String jobcron) {
        this.jobcron = jobcron;
    }
    public String getCycle() {
        return cycle;
    }
    public void setCycle(String cycle) {
        this.cycle = cycle;
    }
    
    public String getS_date() {
        return s_date;
    }
    public void setS_date(String s_date) {
        this.s_date = s_date;
    }
    /**
     * 执行任务
     * @Description: 
     * @param arg0
     * @throws JobExecutionException
     * @author xingle
     * @data 2014-8-14 下午12:51:35
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String jobName = ((Category) context.getJobDetail()).getName();
//        logger.debug("" + jobName + " start！！");
        QuartzVo quartzVo = QuartzVo.quartzMap.get(jobName);
        String inurl = quartzVo.getJcallpath();
        URL url = null;
        HttpURLConnection conn = null;
        try {
            url = new URL(inurl);
            conn = (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
//            System.out.println("***************** 连接失败,程序地址 : " + inurl);
            e.printStackTrace();
        }
        try {
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
 //               System.out.println("****************** 调度失败!!,程序地址 : " + inurl);
            } else {
 //               System.out.println("" + jobName + "" + "已完成调度，程序地址: "
 //                       + inurl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

}