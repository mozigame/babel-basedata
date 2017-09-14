package com.babel.basedata.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.babel.basedata.entity.LogInfoVO;
import com.babel.basedata.model.LogDbPO;
import com.babel.basedata.model.ModelPO;
import com.babel.basedata.mongo.dao.ILogDbDao;
import com.babel.basedata.service.ILogDbService;
import com.babel.basedata.service.IModelService;
import com.babel.basedata.util.Sysconfigs;
import com.babel.common.core.logger.ILogSqlManager;
import com.babel.common.core.util.CommUtil;
import com.babel.common.core.util.RedisUtil;
import com.babel.common.core.util.ServerUtil;
import com.babel.common.core.util.SpringContextUtil;
import com.babel.common.core.util.TaskExecutorUtils;
import com.google.gson.Gson;

@Service("logSqlManager")
public class LogSqlManagerService implements ILogSqlManager {
	private static final Logger logger = Logger.getLogger(LogSqlManagerService.class);

	@Autowired
	private ILogDbService logDbService;
	
	@Autowired
	private ILogDbDao logDbDao;
	
	@Autowired
	@Lazy
	private IModelService modelService;


	
	private Properties properties=new Properties();
	/**
	 * 从redis缓存获取数据处理的批次数量,例如200
	 * 即如果redis的数量低于200时会全取，大于200时，则只取200进行处理
	 */
	private static int saveLogBatchSize=0;//批量从redis获取缓存的log数据
	private static String REDIS_KEY_LOG_SERVICES="logServices";
	private static String REDIS_KEY_LOG_SQLS="logSqls";
	private static Gson gson=new Gson();

	
	public void insertOperatorLog(final Integer logLevel, final Date startDate, final String sqlId, final String sql
			, final long runTime, final Throwable exp, final Map<String, Object> paramMap, ModelPO model){
		String expInfo=LogInfoVO.expToString(exp);
		LogDbPO logDb=this.operatorLog(logLevel, startDate, sqlId, sql, runTime, expInfo, paramMap, model);
		if(logDb!=null){
			this.logDbService.log(logDb);
		}
	}

	
	private void insertOperatorLogList(List<LogInfoVO> logList){
		List<LogDbPO> logDbList=new ArrayList<>();
		
		Date startDate=null;
		String sqlId=null;
		String sql=null;
		long runTime;
		Integer level=null;
		String exp;
		Map<String, Object> paramMap=null;
		LogDbPO logDb=null;
		ModelPO model=null;
		for(LogInfoVO log:logList){
			startDate=log.getStartTime();
			sqlId=log.getSqlId();
			sql=log.getSql();
			runTime=log.getRunTime();
			exp=log.getExp();
			paramMap=log.getParamMap();
			level=log.getLogLevel();
			if(sql!=null){
				model=this.prepareModel(sqlId);
			}
			else{
				model=this.prepareModel(paramMap);
			}
			logDb=this.operatorLog(level, startDate, sqlId, sql, runTime, exp, paramMap, model);
			if(logDb!=null){
				//System.out.println("-----log="+log+" logDb="+logDb);
				logDbList.add(logDb);
			}
		}
		
		if(logDbList.size()>0)
			this.logDbService.logList(logDbList);;
//			this.logDbDao.insertAll(logDbList);
		
	}

	
	private LogDbPO operatorLog(final Integer logLevel, final Date startDate, final String sqlId, final String sql
			, final long runTime, final String exp, final Map<String, Object> paramMap, ModelPO model){
		
		
		LogDbPO logDb=this.prepareLogDb(startDate, runTime, paramMap);
		logDb.setLogLevel(logLevel);
		logDb.setFlag("0");
		Object rows=paramMap.get("rows");
		if(rows!=null){
			logDb.setRows((Integer)rows);
		}
		if(sql!=null){
			logDb.setOperType((String)paramMap.get("sqlType"));
			logDb.setJsonRet(sql);
			logDb.setServiceId(-1l);
		}
		else{
			logDb.setServiceId(0l);
			logDb.setJsonRet((String)paramMap.get("returnValue"));
			//logService
			logDb.setTitle((String)paramMap.get("logService.title"));
			logDb.setAuthor((String)paramMap.get("logService.author"));
			Map<String, Object> argMap=(Map<String, Object>)paramMap.get("args");
			logDb.setDescs(CommUtil.replaceElByMap((String)paramMap.get("logService.descs"), argMap));
			logDb.setCalls((String)paramMap.get("logService.calls"));
		}

		
		if(exp!=null){
			logDb.setJsonRet(logDb.getJsonRet()+"\n"+exp);
			logDb.setFlag("3");
//			System.out.println("-----exceptions="+logDb.getJsonRet());
		}
		
		
		if(modelService!=null){
			Long modelId=initModelId(model, modelService);
			logDb.setModelId(modelId);
		}

		
//		this.logDbService.log(logDb);
		return logDb;
	}
	private static synchronized Long initModelId(ModelPO model, IModelService modelService) {
		return modelService.initModelId(model);
	}

	

	
	public ModelPO prepareModel(Map<String, Object> paramMap){
		String target=(String)paramMap.get("target");
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
		model.setFuncCode((String)paramMap.get("method"));
		model.setClassName(className);
		model.setPackageName(packageName);
		model.setCreateDate(new Date());
		return model;
	}
	
	public ModelPO prepareModel(final String sqlId){
		ModelPO model=new ModelPO();
		String funcCode=sqlId.substring(sqlId.lastIndexOf(".")+1);
		String className=sqlId.substring(0, sqlId.lastIndexOf("."));
		String packageName=className;
		int found=className.lastIndexOf(".");
		if(found>0){
			packageName=className.substring(0, found);
			className=className.substring(found+1);
		}
		model.setFuncCode(funcCode);
		model.setClassName(className);
		model.setPackageName(packageName);
		return model;
	}
	
	private LogDbPO prepareLogDb(final Date startDate, final long runTime, final Map<String, Object> paramMap){
		LogDbPO logDb = new LogDbPO();
//		if(request!=null){
//			logDb.setIp(AppContext.getIpAddr(request));
//		}
////		System.out.println("----prepareLogDb-userName="+AppContext.getUsername());
		
		if(paramMap.get("args")!=null){
			logDb.setJsonParam(gson.toJson(paramMap.get("args")));
		}
		logDb.setUserName((String)paramMap.get("userName"));
		Object obj=paramMap.get("userId");
		String userId=(obj == null) ? null : obj.toString();
		logDb.setUuid((String)paramMap.get("uuid"));
		if(null!=userId){
			logDb.setUserId(Long.parseLong(userId));
		}
		
		if(logDb.getUserName()==null){
			logDb.setUserName("");
		}
		
		
		String remoteHost=(String)paramMap.get("remoteHost");
		remoteHost=CommUtil.getHostShort(remoteHost);
		if("dubbo".equals(this.properties.get("run.type"))){
			String ip=this.getServerIP();
			ip=CommUtil.getHostShort(ip);
			if(remoteHost!=null){
				ip=remoteHost+"->"+ip;
			}
			logDb.setIp(ip);
		}
		else{
			logDb.setIp(remoteHost);
		}
		
		logDb.setCreateDate(startDate);
		logDb.setRunTime(runTime);
		
		
		
		obj=paramMap.get("threadId");
		String threadCode=(obj == null) ? null : obj.toString();
		if(null != threadCode){
			logDb.setThreadId(Long.parseLong(threadCode));
		}
		return logDb;
		
	}
	
	

	private static String ip=null;
	private static String getServerIP(){
		if(ip==null){
			ip=ServerUtil.getLocalIP();
		}
		return ip;
	}
	

	
	
	private RedisTemplate redisTemplate;
	private void initRedis(){
		if(redisTemplate==null){
			this.redisTemplate=(RedisTemplate)SpringContextUtil.getBean("redisTemplate");
		}
	}
	
	private static Properties propertiesLog=null;
	private  static Properties getPropertiesLog() {
		if(propertiesLog==null){
			 Properties properties=TaskExecutorUtils.getPoolInfo(1, 10, 1000);
			propertiesLog=properties;
		}
		return propertiesLog;
	}
	
	@Override
	public void addLogSqlAsync(final Date startTime, final String sqlId, final String sql
			, final long runTime, final Throwable exp, final HashMap<String, Object> paramMap){
		logger.debug("------addLogSqlAsync--saveLogBatchSize="+saveLogBatchSize+" sqlId="+sqlId+" runTime="+runTime);
		
		if(!RedisUtil.isHasRedisTemplate()){
			return;
		}
		final String className=this.getClass().getSimpleName();
		String logType="logSql";
		final Class clazz=this.getClass();
		final String key=null;
		Properties properties=this.getPropertiesLog();
		
		TaskExecutor taskExecutor=TaskExecutorUtils.getTaskExecutorInstance(Sysconfigs.getAppType(), clazz, key, properties);
		taskExecutor.execute(new LogDataSaveThread(this, logType, startTime, sqlId, sql, runTime, exp, paramMap));

	}






	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public void addLogServiceAsync(final Date startTime,  String method, final long runTime, final Throwable exp,
			final HashMap<String, Object> paramMap) {
		logger.info("------addLogServiceAsync--saveLogBatchSize="+saveLogBatchSize+" method="+method);

//		String ignoreServiceType=(String)Sysconfigs.getEnvMap().get("sql.ignoreServiceType");
//		if(ignoreServiceType!=null && ignoreServiceType.indexOf("serviceLogAll")>=0){
//			logger.info("-----service.ignoreServiceType serviceLogAll method="+method+":"+runTime+"ms"+"\n		paramMap="+paramMap);
//			return;
//		}
		
		if(!RedisUtil.isHasRedisTemplate()){
			return;
		}
		
		String logType="logService";
		final Class clazz=this.getClass();
		final String key=null;
		Properties properties=this.getPropertiesLog();
		
		TaskExecutor taskExecutor=TaskExecutorUtils.getTaskExecutorInstance(Sysconfigs.getAppType(), clazz, key, properties);
		taskExecutor.execute(new LogDataSaveThread(this, logType, startTime, method, runTime, exp, paramMap));
		
	}
	
	public void executeLogSql(){
		if(!RedisUtil.isHasRedisTemplate()){
			return;
		}
		initRedis();
		String keyList=REDIS_KEY_LOG_SQLS;
		this.saveLogDbBatch(keyList);
	}
	
	private synchronized void saveLogDbBatch(final String redisKey){
		int timeLimit=5;
		if(RedisUtil.isRunLimit(this.getClass(), timeLimit, redisKey+"saveLogDbBatch", false)){
			return;
		}
		if(!RedisUtil.tryLock(redisKey)){
			return;
		}
		TaskExecutor taskExecutor=TaskExecutorUtils.getExecutorSingle(Sysconfigs.getAppType(), this.getClass(), "saveLogDbBatch", timeLimit);
		taskExecutor.execute(new Runnable() {
			public void run() {
				saveLogDbBatch(redisKey);
				RedisUtil.unLock(redisKey);
			}

			/**
			 * 批量保存日志
			 * @param redisKey
			 */
			private  void saveLogDbBatch(final String redisKey) {
				int sizeGet=200;
				try {
					//批量获取数据并删除
					List<LogInfoVO> logList=null;
					Long size=redisTemplate.boundListOps(redisKey).size();
//					if(SpringContextUtil.containsBean("redisClusterConfiguration")){
//						logList=RedisListUtil.getListWithRemove(redisTemplate, redisKey, sizeGet);
//					}
//					else{
//						logList=RedisListUtil.getListWithPop(redisTemplate, redisKey, sizeGet);
//					}
					if(size>sizeGet){
						size=sizeGet+0l;
					}
					logList=new ArrayList<>();
					LogInfoVO logInfoVO=null;
					for(int i=0; i<size; i++){
						logInfoVO=(LogInfoVO)redisTemplate.boundListOps(redisKey).rightPop();
						if(logInfoVO!=null){
							logList.add(logInfoVO);
						}
					}
					
					String sqlId="";
					for(LogInfoVO log:logList){
						if(log.getSqlId()!=null)
							sqlId+=log.getSqlId()+",";
						else{
							sqlId+=log.getMethod()+",";
						}
					}
					
					if(logList.size()>0){
						logger.info("----saveLogDbBatch--redisKey="+redisKey+" size="+logList.size()+"/"+sizeGet+"\n	logMethod="+CommUtil.getStringLimit(sqlId, 1000));
						insertOperatorLogList(logList);
					}
				} catch (Exception e) {
					logger.error("----saveLogDbBatch--", e);
				}
			}
		});
	}
	
	
	public void executeLogService(){
		initRedis();
		String keyList=REDIS_KEY_LOG_SERVICES;
		this.saveLogDbBatch(keyList);
	}
	
	public void setProperties(Properties properties0) {
		this.properties = properties0;
	}


	public void setSaveLogBatchSize(int saveLogBatchSize) {
		LogSqlManagerService.saveLogBatchSize = saveLogBatchSize;
	}


	public  int getSaveLogBatchSize() {
		return LogSqlManagerService.saveLogBatchSize;
	}
	

}