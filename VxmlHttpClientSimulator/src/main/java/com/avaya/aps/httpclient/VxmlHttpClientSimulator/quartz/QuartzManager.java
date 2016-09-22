package com.avaya.aps.httpclient.VxmlHttpClientSimulator.quartz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;




/**
 * 任务管理
 * @ClassName: QuartzManager
 * 
 * @author Xingle
 * @date 2014-8-14 下午2:34:16
 */
public class QuartzManager {
    private static Logger logger = Logger.getLogger(QuartzManager.class);
    
    //Create an uninitialized StdSchedulerFactory.
    private static SchedulerFactory sf = new StdSchedulerFactory();
    
    /**
     * 添加任务
     * @param jobName
     * @param job
     * @param time
     * @author xingle
     * @data 2014-8-14 下午7:45:09
     */
    public static void addJob(String jobName,QuartzVo job,String time){        
        try {
            Scheduler scheduler = sf.getScheduler();
            
            //
//            JobDetail jobDetail = new JobDetail(jobName, jobName, job.getClass());
            JobDetailImpl jobDetail = new JobDetailImpl();
            jobDetail.setName(jobName);
            jobDetail.setGroup(jobName);
            jobDetail.setJobClass(job.getClass());
            
            if("1".equals(job.getCycle())){//循环
//                CronTrigger trigger = new CronTrigger(jobName, jobName);
            	CronTriggerImpl trigger = new CronTriggerImpl();
            	trigger.setName(jobName);
            	trigger.setGroup(jobName);
                trigger.setCronExpression(time);
                scheduler.scheduleJob(jobDetail, trigger);
            }
            else{//单次
                String s_Date = job.getS_date(); 
                logger.debug("*****"+s_Date);
                
                SimpleDateFormat formate= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
                Date startTime = formate.parse(s_Date);    
                logger.debug("*****"+startTime);
//                SimpleTrigger trigger = new SimpleTrigger(jobName, jobName, startTime);
                
                SimpleTriggerImpl trigger = new SimpleTriggerImpl();
                trigger.setName(jobName);
                trigger.setGroup(jobName);
                trigger.setStartTime(startTime);
                scheduler.scheduleJob(jobDetail, trigger);    
            }
            if(!scheduler.isShutdown()){
                scheduler.start();
            }
            logger.debug("*********定时任务"+jobName+" 加载完成*****************");
            
            
        } catch (SchedulerException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 删除任务
     * @param jobName
     * @author xingle
     * @data 2014-8-14 下午7:45:16
     */
    public static void removeJob(String jobName){
         try {
        	StdScheduler sched = (StdScheduler)sf.getScheduler();
        	
            sched.pauseTrigger(new TriggerKey(jobName,jobName));//
            sched.unscheduleJob(new TriggerKey(jobName,jobName));//
            sched.deleteJob(new JobKey(jobName, jobName));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
         logger.debug("*********定时任务"+jobName+" 已删除完成！*****************");
    }
     
}

