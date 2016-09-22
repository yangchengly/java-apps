package com.avaya.aps.httpclient.VxmlHttpClientSimulator.quartz;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.avaya.aps.httpclient.VxmlHttpClientSimulator.PropertiesUtil;
import com.avaya.aps.httpclient.VxmlHttpClientSimulator.actions.HttpAction;


/**
 * 调用任务
 * @ClassName: CallQuartz
 * TODO
 * @author Xingle
 * @date 2014-8-14 下午12:48:44
 */
public class CallQuartz {
    private static Logger logger = Logger.getLogger(CallQuartz.class);
    
    private static AtomicInteger threadNum = new AtomicInteger(1);
    
    private static String path = null;
    
    private static int maxStep = 25;
    private static String baseUrl = null;
    
    static {
    	try {
			path = CallQuartz.class.getClassLoader().getResource("").toURI().getPath();
			String filePath = path + "conf/task.properties";
			Properties pps = new Properties();
			InputStream in = new FileInputStream(filePath);
			//从输入流中读取属性列表（键和元素对） 
			pps.load(in);
			maxStep = Integer.parseInt(pps.getProperty("maxStep"));
			baseUrl = pps.getProperty("baseUrl");
			PropertiesUtil.loadAllProperties(path + "conf");
			
			logger.info("baseUrl:"+baseUrl);
			logger.info("maxStep:"+maxStep);
			logger.info("has load properties");

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
//    @Autowired 
//    @Qualifier("memcachedClient")
//    private MemCachedClient memClient;
        
    public void callAllQuartz(){
//        List<QuartzVo> list =new ArrayList<QuartzVo>();
//        Iterator<String> idIter = QuartzVo.quartzMap.keySet().iterator();
//        String idStr = "";
//        while(idIter.hasNext()){
//            idStr = idStr+idIter.next()+",";
//        }
//        logger.info("当前任务有： "+idStr);
//        String flag = "";
//        Map<String,List<QuartzVo> > map = this.getNewJobs();
//        Iterator<Entry<String, List<QuartzVo>>> iter = map.entrySet().iterator();
//        while(iter.hasNext()){
//            Entry<String, List<QuartzVo>> entry = iter.next();
//            flag = entry.getKey();
//            list =  entry.getValue();
//        }
//        //新增
//        if("1".equals(flag)){
//            logger.info("新增加定时任务的数量"+list.size()+"");
//            for(int i =0;i<list.size();i++){
//                QuartzVo vo = list.get(i);    
//                QuartzVo.quartzMap.put(vo.getJobTitle(), vo);    
//                QuartzManager.addJob(vo.getJobTitle(), vo, vo.getJobcron());
//                            
//            }
//        }
//        else if("2".equals(flag)){
//            logger.info("删除的定时任务的数量"+list.size()+"");
//            for(int i =0;i<list.size();i++){
//                QuartzVo vo = list.get(i);            
//                QuartzManager.removeJob(vo.getJobTitle());
//                QuartzVo.quartzMap.remove(vo.getJobTitle());
//            }
//            
//        }
    	
    	logger.info("start quartz");
    	int ani = 3000;
		String ucidPrefix = "0000100671";
		long ucid = 1467265209;
		int num = threadNum.getAndIncrement();
		
		ani += num;
		ucid += num;
		
		logger.info("ani:"+ani);
		logger.info("ucid:"+ucid);
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		
		List<NameValuePair> sesNameList = PropertiesUtil.findNamelist("Main",
				"session");

		for (int j = 0; j < sesNameList.size(); j++) {
			String key = sesNameList.get(j).getName();

			if (key.equalsIgnoreCase("session___ani")) {
				nvps.add(new BasicNameValuePair("session___ani", String
						.valueOf(ani)));
			} else if (key.equalsIgnoreCase("session___sessionlabel")) {
				nvps.add(new BasicNameValuePair("session___sessionlabel",
						ucidPrefix + ucid));
			} else if (key.equalsIgnoreCase("session___ucid")) {
				nvps.add(new BasicNameValuePair("session___ucid",
						ucidPrefix + ucid));
			} else {
				nvps.add(sesNameList.get(j));
			}
		}
		PropertiesUtil.putSession(num, nvps);
		HttpAction.maxStep = maxStep;
		new HttpAction(num, baseUrl).run();
		logger.info(num);
        
    }

}
