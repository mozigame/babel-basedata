package com.babel.fwork.schedule.task;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.entity.BaseEntity;
import com.babel.common.core.util.ServerUtil;
import com.babel.fwork.basedata.model.LogMsgPO;
import org.quartz.JobExecutionException;
import org.springframework.core.task.TaskExecutor;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;

/**
 * 
 * date 2016-07-01
 * @author cjh
 *
 */
//@Service("msgSendTask")
public class ScheduleManager{
	private static final Log logger = LogFactory.getLog(ScheduleManager.class);

	private Integer runningCount=20;
	private String msgType="";
	private String taskId="";
	
	private IScheduleTask scheuduleTask;
	
//	@Autowired
//	private ILogDbService logDbService;

	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutorSchedule;
	
	public static enum TASK_TYPES{
		MSG_SMS("MSG_SMS", 1, "短信"),
		MSG_MAIL("MSG_MAIL", 2, "郵件"),
		INSURE_PAY("INSURE_PAY", 10, "赔付"),
		INSURE_REFUND("INSURE_REFUND", 13, "退款");
		private String key;
		private String name;
		private Integer type;
		TASK_TYPES(String key, Integer type, String name){
			this.key=key;
			this.type=type;
			this.name=name;
		}
		public String getKey() {
			return key;
		}
		public String getName() {
			return name;
		}
		public Integer getType(){
			return type;
		}
		
		public static boolean isExist(String key){
			ScheduleManager.TASK_TYPES[] types=ScheduleManager.TASK_TYPES.values();
			for(ScheduleManager.TASK_TYPES type:types){
				if(type.getKey().equals(key)){
					return true;
				}
			}
			return false;
		}
		
	}
	
	//待處理的數據
	private static Map<String, Map<Long, Object>> toDoMap=new ConcurrentHashMap<>();
	//正在處理的數據，處理一個，扣除一個，直到處理完成
	private static Map<String, Map<Long, Object>> runningMap=new ConcurrentHashMap<>();
	
	
	public static void addData(String key, Object obj){
		Map<Long, Object> dataMap=toDoMap.get(key);
		Map<Long, Object> runningDataMap=runningMap.get(key);
		if(dataMap==null){
			dataMap=new ConcurrentHashMap<Long, Object>();
		}
		if(obj instanceof BaseEntity){
			BaseEntity entity=(BaseEntity)obj;
			logger.info("---addData-key="+key+" "+obj.getClass().getSimpleName()+".cid="+entity.getCid()+" runContain="+runningDataMap.containsKey(entity.getCid()));
			if(!runningDataMap.containsKey(entity.getCid())){//如果任务已正在运行，则忽略
				dataMap.put(entity.getCid(), entity);
			}
		}
		else{
			logger.warn("----addData not class of BaseEntity:"+obj.getClass().getName());
		}
//		System.out.println("---addData-obj="+obj);
		toDoMap.put(key, dataMap);
	}
	
	public static void addDataList(String key, Collection list){
		for(Object obj:list){
			addData(key, obj);
		}
	}

	public void setTaskExecutorSchedule(TaskExecutor taskExecutor) {
		this.taskExecutorSchedule = taskExecutor;
	}
	
	public ScheduleManager() {
		
	}
	
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
//	public void execute(JobExecutionContext context) throws JobExecutionException {
//		logger.info(simpleDateFormat.format(new Date()) + ":" 
//				+ context.getTrigger().getKey().getGroup() + "," 
//				+ context.getTrigger().getKey().getName());
//		
//	}
	
	private void initMap(String key){
		Map<Long, Object> dataMap=toDoMap.get(key);
		if(dataMap==null){
			dataMap=new ConcurrentHashMap<Long, Object>();
			toDoMap.put(key, dataMap);
		}
		
		dataMap=runningMap.get(key);
		if(dataMap==null){
			dataMap=new ConcurrentHashMap<Long, Object>();
			runningMap.put(key, dataMap);
		}
	}
	
	
	/**
	 * 截入數據功能，一般5分鐘一次
	 */
	public void loadData(){
		String key=this.taskId+"_";
		Collection list=null;
		if(TASK_TYPES.isExist(msgType)){
			key+=msgType;
		}
		else{
			logger.warn("----loadData invalid msgType:"+msgType);
			return;
		}
		//Refund_INSURE_REFUND
		ServerUtil.logRunCount(key+"_execute");
		list=scheuduleTask.loadData();
		logger.info("----loadMsgData--key="+key+" toDoMap="+this.toDoMap+" list="+list);
		
		this.initMap(key);
		
		if(list!=null){
			addDataList(key, list);
		}
		logger.info("-----loadMsgData key="+key+" toDoMap="+toDoMap.get(key).size());
		
	}


	
	/**
	 * 執行郵件處理功能，一般10秒一次，使功能以接近準實時的方式執行
	 * 
	 */
	public void execute() throws JobExecutionException {
//		logger.info("----execute msgType:"+msgType);
		
		String key=this.taskId+"_";
		if(TASK_TYPES.isExist(msgType)){
			key+=msgType;
		}
		else{
			logger.warn("----loadData invalid msgType:"+msgType);
			return;
		}
		if(!ServerUtil.scheduleMap.containsKey(key)){//控制第一次加载，以提高启动时的执行处理时间
			ServerUtil.scheduleMap.put(key, new Date());
			loadData();
		}
		ServerUtil.logRunCount(key+"_execute");
		ServerUtil.scheduleMap.put("toDoCount", toDoMap.size());
		ServerUtil.scheduleMap.put("runningCount", runningMap.size());
//		logger.info("------execute-----msgType="+msgType+" key="+key+" runningCount="+runningCount);
		
		this.initMap(key);
		Map<Long, Object> toDoDataMap=toDoMap.get(key);
		Map<Long, Object> runningDataMap=runningMap.get(key);
		ServerUtil.scheduleMap.put("toDoDataCount", toDoDataMap.size());
		ServerUtil.scheduleMap.put("runningDataCount", runningDataMap.size());
	
		if(toDoDataMap.size()>0 || runningDataMap.size()>0){
			logger.info("-----execute key="+key+" progress="+runningDataMap.size()+"/"+toDoDataMap.size());
		}

		logger.debug("------execute-----msgType="+msgType+" key="+key+" runningCount="+runningCount+" runningDataMap="+runningDataMap.size()+" toDoDataMap="+toDoDataMap.size());
		if(runningDataMap.size()<this.runningCount.intValue()){
			if(toDoDataMap==null||toDoDataMap.isEmpty()){//如果沒有待處理,就不處理了
				return;
			}
			Iterator<Long> it=toDoDataMap.keySet().iterator();;
			Object obj=null;
			int count=0;
			Long cid=null;
			while(it.hasNext()){
				cid=it.next();
				obj=toDoDataMap.get(cid);
				count+=this.executeTask(obj);
				runningDataMap.put(cid, obj);//加到正在处理的列表中
				it.remove();//从待处理列表移除
				if(count+runningDataMap.size()>this.runningCount){//正在执行的任务超出限制时，不处理，由下次处理
					break;
				}
			}
			runningMap.put(key, runningDataMap);
		}
	}
	
	private int executeTask(Object obj){
		int count=0;
		if(obj instanceof LogMsgPO){
			LogMsgPO logMsg=(LogMsgPO)obj;
			if(logMsg.getMsgType().intValue()==2){
				this.sendLogMsg(logMsg);
			}
			count++;
		}
//		else if(obj instanceof BtlOrderPO){
//			this.insurePayBtl((BtlOrderPO)obj);
//			count++;
//		}
		return count;
	}
	
	private void sendLogMsg(final LogMsgPO logMsg) {
		final String key=this.taskId+"_"+TASK_TYPES.MSG_MAIL.key;
		taskExecutorSchedule.execute(new Runnable() {
			public void run() {
				long time=System.currentTimeMillis();
				Map<Long, Object> runningDataMap=runningMap.get(key);
				Object info=null;
				try {
					Object obj=scheuduleTask.execute(logMsg);
					if(obj instanceof RetResult){
						RetResult ret=(RetResult)obj;
						info=ret.getFirstData();
					}
				} catch (Exception e) {
					logger.error("-----execute error", e);
				}
				runningDataMap.remove(logMsg.getCid());
//				runningMap.put(key, runningDataMap);
				logger.info("----sendMsg--cid="+logMsg.getCid()+" tos="+logMsg.getTos()+" runningSize="+runningDataMap.size()+" executeResult="+info);
			}
		});
	}
	
//	private void insurePayBtl(final BtlOrderPO btl){
//		String keys=this.taskId+"_";
//		if(TASK_TYPES.isExist(msgType)){
//			keys+=msgType;
//		}
//		else{
//			logger.warn("----insurePayBtl--loadData invalid msgType:"+msgType);
//			return;
//		}
//		final String key=keys;
//		taskExecutorSchedule.execute(new Runnable() {
//			public void run() {
//				long time=System.currentTimeMillis();
//				Map<Long, Object> runningDataMap=runningMap.get(key);
//				Object info=null;
//				try {
//					Object obj=scheuduleTask.execute(btl);
//					if(obj instanceof RetResult){
//						RetResult ret=(RetResult)obj;
//						info=ret.getFirstData();
//					}
//				} catch (Exception e) {
//					logger.error("-----execute error", e);
//				}
//				runningDataMap.remove(btl.getCid());
////				runningMap.put(key, runningDataMap);
//				logger.info("----insurePayBtl--cid="+btl.getCid()+" orderNo="+btl.getOrderNo()+" runningSize="+runningDataMap.size()+" executeResult="+info);
//			}
//		});
//	}

	public void setRunningCount(Integer runningCount) {
		this.runningCount = runningCount;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public void setScheuduleTask(IScheduleTask scheuduleTask) {
		this.scheuduleTask = scheuduleTask;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
}
