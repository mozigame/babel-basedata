package com.babel.basedata.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.babel.basedata.entity.LogInfoVO;
import com.babel.basedata.model.LogDbPO;
import com.babel.basedata.model.ModelPO;
import com.babel.basedata.mongo.dao.ILogDbDao;
import com.babel.basedata.service.ILogDbService;
import com.babel.basedata.service.IModelService;
import com.babel.basedata.util.Sysconfigs;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.logger.ILogManager;
import com.babel.common.core.logger.LogAudit;
import com.babel.common.core.util.CommUtil;
import com.babel.common.core.util.RedisListUtil;
import com.babel.common.core.util.RedisUtil;
import com.babel.common.core.util.ServerUtil;
import com.babel.common.core.util.SpringContextUtil;
import com.babel.common.core.util.TaskExecutorUtils;
import com.babel.common.web.context.AppContext;
import com.google.gson.Gson;

@Service("logManager")
public class LogManagerImpl implements ILogManager {
	private static final Logger logger = Logger.getLogger(LogManagerImpl.class);

	@Autowired
	private ILogDbService logDbService;
	
	@Autowired
	private IModelService modelService;
	@Autowired
	private ILogDbDao logDbDao;
	
	private RedisTemplate redisTemplate;
	private void initRedis(){
		if(redisTemplate==null){
			this.redisTemplate=(RedisTemplate)SpringContextUtil.getBean("redisTemplate");
		}
	}

	
	private static int saveLogBatchSize=0;
	private static String REDIS_KEY_LOG_AUDIT="logAudit";
	
	private static Properties propertiesLog=null;
	private  static Properties getPropertiesLog() {
		if(propertiesLog==null){
			 Properties properties=TaskExecutorUtils.getPoolInfo(1, 10, 1000);
			propertiesLog=properties;
		}
		return propertiesLog;
	}



	public void addLogErrorAsync(final String method, final Map<String, Object> pointMap,final long runTime, final Throwable exp, final HttpServletRequest request) {
		logger.debug("----addLogErrorAsync---method="+pointMap.get("method"));
		final Integer logLevel=4;
		final String ip=CommUtil.getHostShort(AppContext.getIpAddr(request));
		pointMap.put("User-Agent", request.getHeader("User-Agent"));
//		final String className=this.getClass().getSimpleName();
		Date startTime=new Date();
		final Class clazz=this.getClass();
		final String key=null;
		final Properties properties=this.getPropertiesLog();
		TaskExecutor taskExecutor=TaskExecutorUtils.getTaskExecutorInstance(Sysconfigs.getAppType(), clazz, key, properties);
		taskExecutor.execute(new Runnable() {
			public void run() {
				LogDbPO logDb=null;
				try {
					if(saveLogBatchSize>0){
						initRedis();
						if(redisTemplate!=null){
							LogInfoVO logInfo=new LogInfoVO(startTime,null, null,logLevel, runTime,exp,(HashMap)pointMap);
							logInfo.setMethod(method);
							logInfo.setIp(ip);
							Long value=redisTemplate.boundListOps(REDIS_KEY_LOG_AUDIT).rightPush(logInfo);
//							System.out.println("---"+value);
							return;
						}
					}
//					ServerUtil.logPool(className+".taskExecutorLog", taskExecutor);
					logDb=insertOperatorLog(logLevel, method, pointMap, runTime, exp, ip);
				} catch (Exception e) {
					logger.warn("----addLogErrorAsync pointMap="+pointMap, e);
				}
			}
		});
	}
//	@Override
	public void addLogInfoAsync(final String method, final Map<String, Object> pointMap,final long runTime, final Object retObj, final HttpServletRequest request) {
		logger.debug("----addLogInfoAsync----method="+pointMap.get("method"));
		final Integer logLevel=2;
		final String ip=(String)pointMap.get("remoteIp");
		if(request!=null){
			pointMap.put("User-Agent", request.getHeader("User-Agent"));
		}
		final String className=this.getClass().getSimpleName();
		Date startTime=new Date();
		final Class clazz=this.getClass();
		final String key=null;
		final Properties properties=this.getPropertiesLog();
		TaskExecutor taskExecutor=TaskExecutorUtils.getTaskExecutorInstance(Sysconfigs.getAppType(), clazz, key, properties);
		taskExecutor.execute(new Runnable() {
			public void run() {
				LogDbPO logDb=null;
				try {
					ServerUtil.logPool(className+".taskExecutorLog", taskExecutor);
					if(saveLogBatchSize>0){
						initRedis();
						if(redisTemplate!=null){
							LogInfoVO logInfo=new LogInfoVO(startTime,null, null,logLevel,runTime,null,(HashMap)pointMap);
							logInfo.setMethod(method);
							logInfo.setIp(ip);
							String descs=(String)pointMap.get("logAudit.descs");
							RetResult<String> retStr=getRetObj(descs, retObj);
							logInfo.setRetStr(retStr);
							Long value=redisTemplate.boundListOps(REDIS_KEY_LOG_AUDIT).rightPush(logInfo);
//							System.out.println("---"+value);
							return;
						}
					}
					logDb=insertOperatorLog(logLevel, method, pointMap, runTime, retObj, ip);
				} catch (Exception e) {
					logger.warn("----addLogInfoAsync pointMap="+pointMap, e);
				}
			}
		});
	}
	
	private static Gson gson =new Gson();
	
	public LogDbPO insertOperatorLog(Integer logLevel, String method, Map<String, Object> pointMap, long runTime, Object retObj, String ip) throws Exception{
		String descs=(String)pointMap.get("logAudit.descs");
		RetResult<String> retStr=this.getRetObj(descs, retObj);
		String exp=null;
		LogDbPO logDb=this.operatorLog(logLevel, method, exp, pointMap, runTime, retStr.getFirstData());
		logDb.setFlag(String.valueOf(retStr.getFlag()));
		logDb.setIp(ip);
		this.logDbService.log(logDb);
		return logDb;
	}
	
	private void insertOperatorLogList(List<LogInfoVO> logList) throws Exception{
		List<LogDbPO> logDbList=new ArrayList<>();
		
		Date startDate=null;
		String method=null;
		
		long runTime;
		Integer level=null;
		String exp;
		Map<String, Object> paramMap=null;
		LogDbPO logDb=null;
		
		RetResult<String> retStr=null;
		String retData=null;
		String flag=null;
		for(LogInfoVO log:logList){
			retData=null;
			flag=null;
			
			startDate=log.getStartTime();
			method=log.getMethod();
			runTime=log.getRunTime();
			exp=log.getExp();
			paramMap=log.getParamMap();
			level=log.getLogLevel();
			retStr=log.getRetStr();
			if(retStr!=null){
				retData=retStr.getFirstData();
				flag=String.valueOf(retStr.getFlag());
			}
			logDb=this.operatorLog(level, method, exp, paramMap, runTime, retData);
			logDb.setCreateDate(startDate);
			
			logDb.setFlag(flag);
			if(logDb!=null){
				logDb.setIp(log.getIp());
				//System.out.println("-----log="+log+" logDb="+logDb);
				logDbList.add(logDb);
			}
		}
		
		if(logDbList.size()>0)
			this.logDbService.logList(logDbList);
			logger.info("------insertAll----size="+logDbList.size());
//			this.logDbDao.insertAll(logDbList);
		
	}
	
	/**
	 * 
	 * @param descs
	 * @param retObj
	 * @return 返回数据信息
	 */
	private RetResult<String> getRetObj(String descs, Object retObj){
		RetResult<String> ret=new RetResult();
		if(retObj instanceof Throwable){
//			logDb.setJsonRet(ExceptionUtils.getFullStackTrace((Throwable)retObj));
			ret.setData(LogInfoVO.expToString((Throwable)retObj));
			ret.setFlag(2);
		}
		else if(!LogAudit.noRet.equals(descs)){//descs为noRet时，不保存返回值
			RetResult retData=null;
			if(retObj instanceof RetResult){
				retData=(RetResult)retObj;
//				logDb.setFlag(""+ret.getFlag());
				ret.setFlag(retData.getFlag());
			}
			if(LogAudit.noDatas.equals(descs)&& retData!=null){
				RetResult ret2=new RetResult(retData);
				ret2.setDataList(new ArrayList());
				ret2.setMsgBody("size:"+retData.getSize());
				retObj=ret2;
				ret.setFlag(ret2.getFlag());
			}
			String json=gson.toJson(retObj);
			json=CommUtil.getStringLimit(json, 2000);
			ret.setData(json);
			
		}
		return ret;
	}
	
	private LogDbPO operatorLog(Integer logLevel, String method, String exp, Map<String, Object> pointMap, long runTime, String jsonRet) throws Exception{
		LogDbPO logDb=this.prepareLogDb(pointMap);
		logDb.setLogLevel(logLevel);
		logDb.setRunTime(runTime);
		logDb.setFlag("0");
		if(exp!=null){
			logDb.setJsonRet(logDb.getJsonRet()+"\n"+exp);
			logDb.setFlag("3");
//			System.out.println("-----exceptions="+logDb.getJsonRet());
		}
		
		ModelPO model=this.prepareModel(pointMap);
		if(modelService!=null){
			Long modelId=initModelId(model, modelService);
			logDb.setModelId(modelId);
		}
		

		return logDb;
	}
	
	/**
	 * @param logDb
	 * @param model
	 */
	private static synchronized Long initModelId(ModelPO model, IModelService modelService) {
		return modelService.initModelId(model);
	}
	
	private ModelPO prepareModel(Map<String, Object> pointMap){
		String target=(String)pointMap.get("target");
		int find=target.indexOf("@");
		String className=target;
		if(find>0){
			className=target.substring(0,  find);
		}
		String packageName=null;
		find=className.lastIndexOf(".");
		if(find>0){
			packageName=className.substring(0, find);
			className=className.substring(find+1);
		}
		ModelPO model=new ModelPO();
		model.setFuncCode((String)pointMap.get("method"));
		model.setClassName(className);
		model.setPackageName(packageName);
		model.setCreateDate(new Date());
		return model;
	}
	
	private LogDbPO prepareLogDb(Map<String, Object> pointMap){
		LogDbPO logDb = new LogDbPO();
//		System.out.println("----prepareLogDb-userName="+AppContext.getUsername());
		logDb.setUserName((String)pointMap.get("userName"));
		logDb.setUuid((String)pointMap.get("uuid"));
		logDb.setUserId((Long)pointMap.get("userId"));
		
		
		logDb.setServiceId(0l);
		logDb.setTitle((String)pointMap.get("logAudit.title"));
		Map<String, Object> argMap=(Map<String, Object>)pointMap.get("args");
		logDb.setDescs(CommUtil.replaceElByMap((String)pointMap.get("logAudit.descs"), argMap));
		logDb.setCalls((String)pointMap.get("logAudit.calls"));
		
		logDb.setJsonParam(gson.toJson(argMap));
//		logDb.setRemark("a:"+log.author());
		logDb.setAuthor((String)pointMap.get("logAudit.author"));
//		logDb.setModelId(modelId);
		logDb.setThreadId((Long)pointMap.get("threadId"));
		logDb.setHeaderInfo((String)pointMap.get("User-Agent"));
		
		return logDb;
		
	}
	
	
	public void executeLogAudit(){
		initRedis();
		String keyList=REDIS_KEY_LOG_AUDIT;
		this.saveLogAuditBatch(keyList);
	}
	
	private synchronized void saveLogAuditBatch(final String redisKey){
		int timeLimit=3;
		if(RedisUtil.isRunLimit(this.getClass(), timeLimit, redisKey)){
			return;
		}
		if(!RedisUtil.tryLock(redisKey)){
			return;
		}
		TaskExecutor taskExecutor=TaskExecutorUtils.getExecutorSingle(Sysconfigs.getAppType(), this.getClass(), "saveLogAuditBatch", timeLimit);
		taskExecutor.execute(new Runnable() {
			public void run() {
				saveLogBatch(redisKey);
				RedisUtil.unLock(redisKey);
			}
			private void saveLogBatch(final String redisKey){
				List<LogInfoVO> logList=null;
				int sizeGet=200;
				try {
					//批量获取数据并删除
//					if(SpringContextUtil.containsBean("redisClusterConfiguration")){
//						logList=RedisListUtil.getListWithRemove(redisTemplate, redisKey, sizeGet);
//					}
//					else{
//						logList=RedisListUtil.getListWithPop(redisTemplate, redisKey, sizeGet);
//					}
					Long size=redisTemplate.boundListOps(redisKey).size();
					if(size>sizeGet){
						size=sizeGet+0l;
					}
					logList=new ArrayList<>();
					LogInfoVO logInfoVO=null;
					for(int i=0; i<size; i++){
						logInfoVO=(LogInfoVO)redisTemplate.boundListOps(redisKey).leftPop();
						if(logInfoVO!=null){
							logList.add(logInfoVO);
						}
					}
					
					String sqlId="";
					for(LogInfoVO log:logList){
						sqlId+=log.getMethod()+",";
					}
					
					if(logList.size()>0){
						logger.info("----saveLogAuditBatch--redisKey="+redisKey+" size="+logList.size()+"/"+sizeGet+"\n	logMethod="+CommUtil.getStringLimit(sqlId, 300));
						insertOperatorLogList(logList);
					}
				} catch (Exception e) {
					logger.error("----saveLogAuditBatch--", e);
				}
			}
		});
	}

	


	public  void setSaveLogBatchSize(int saveLogBatchSize) {
		LogManagerImpl.saveLogBatchSize = saveLogBatchSize;
	}
	
}