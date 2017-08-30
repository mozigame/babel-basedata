package com.babel.basedata.service.impl;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;

import com.babel.basedata.entity.LogInfoVO;
import com.babel.basedata.model.ModelPO;
import com.babel.basedata.util.Sysconfigs;
import com.babel.common.core.util.SpringContextUtil;

public class LogDataSaveThread extends Thread implements  Runnable {
	private static final Logger logger = Logger.getLogger(LogDataSaveThread.class);
	
	public LogDataSaveThread(LogSqlManagerService logSqlManager, final String logType, final Date startTime,final String sqlId, final String sql
			, final long runTime, final Throwable exp, final Map<String, Object> paramMap){
		this.logSqlManager=logSqlManager;
		this.logType=logType;
		this.startTime=startTime;
		this.sqlId=sqlId;
		this.sql=sql;
		this.runTime=runTime;
		this.exp=exp;
		this.paramMap=paramMap;
		super.setName("sql-"+sqlId);
	}
	public LogDataSaveThread(LogSqlManagerService logSqlManager, final String logType, final Date startTime,final String method
			, final long runTime, final Throwable exp, final Map<String, Object> paramMap){
		this.logSqlManager=logSqlManager;
		this.logType=logType;
		this.startTime=startTime;
		this.method=method;
		this.runTime=runTime;
		this.exp=exp;
		this.paramMap=paramMap;
		super.setName("method-"+method);
	}
	
	private String logType;
	private String sqlId;
	private String sql;
	private String method;
	private Long runTime;
	private Date startTime;
	private Map<String, Object> paramMap;
	private Throwable exp;
	private LogSqlManagerService logSqlManager;
	private RedisTemplate redisTemplate;
	private void initRedis(){
		if(redisTemplate==null){
			this.redisTemplate=(RedisTemplate)SpringContextUtil.getBean("redisTemplate");
		}
	}
	
	public void run() {
		try {
			Integer logInfo=0;
			Integer logWarn=2000;
			Integer level = 0;
			
			ModelPO model=null;
			String redisKey="logInfo";
			LogInfoVO logInfoVO=null;
			if("logSql".equals(logType)){
				redisKey="logSqls";
				logInfo=this.getConfigValue(Sysconfigs.env_sql.LOG_INFO.code, 0);
				logWarn=this.getConfigValue(Sysconfigs.env_sql.LOG_WARN.code, 2000);
				level = getLogLevelByConfigTime(runTime, logInfo, logWarn, exp);
				model=this.logSqlManager.prepareModel(sqlId);
				//是否按条件进行忽略sql日志到数据库处理
				if(exp==null && 
						(ignoreBySqlTypeConfig(sqlId, sql, runTime, paramMap) 
								|| this.ignoreByRunTime(sqlId, sql)
								|| this.ignoreBySql(sqlId, sql)
								|| this.ignoreBySqlId(sqlId, sql, model))){
					return;
				}
				logInfoVO=new LogInfoVO(startTime, sqlId, sql,level, runTime,exp,paramMap);
			}
			else if("logService".equals(logType)){
				redisKey="logServices";
				logInfo=this.getConfigValue(Sysconfigs.env_service.LOG_INFO.code, 0);
				logWarn=this.getConfigValue(Sysconfigs.env_service.LOG_WARN.code, 2000);
				level = getLogLevelByConfigTime(runTime, logInfo, logWarn, exp);
				model=this.logSqlManager.prepareModel(paramMap);
				//是否按条件进行忽略service日志到数据库处理
				if(exp==null && 
						(this.ignoreByRunTime(runTime)
								|| this.ignoreByServiceKey(runTime, model))){
					return;
				}
				logInfoVO=new LogInfoVO(startTime,method,level,runTime,exp,paramMap);
			}
			else{
				logger.info("----invalid logType:"+logType);
			}
			
			
			if(this.logSqlManager.getSaveLogBatchSize()>0 && logInfoVO!=null){
				initRedis();
				if(redisTemplate!=null){
					redisTemplate.boundListOps(redisKey).rightPush(logInfoVO);
					return;
				}
			}

			this.logSqlManager.insertOperatorLog(level, startTime, sqlId, sql, runTime, exp, paramMap, model);
		} catch (Exception e) {
			logger.error("----addLogServiceAsync--paramMap="+paramMap, e);
			if(exp!=null){
				logger.warn("----addLogServiceAsync--exp:"+exp.getMessage(), exp);
			}
		}
	}
	
	/**
	 * 按执行时间进行忽略
	 * @param rungTime
	 * @return
	 */
	private boolean ignoreByRunTime(final long runTime){
		boolean isIgnore=false;
		Integer logInfoRunTime=getConfigValue(Sysconfigs.env_service.LOG_INFO.code, 0);
		if(runTime<logInfoRunTime){
			logger.info("-----service.logInfomethod="+paramMap.get("method")+":"+runTime+"ms:("+logInfoRunTime+")");
			isIgnore=true;
			return isIgnore;
		}
		return isIgnore;
	}
	
	/**
	 * 按服务的类及方法名进行忽略
	 * @param model
	 * @return
	 */
	private boolean ignoreByServiceKey(final long runTime, ModelPO model){
		boolean isIgnore=false;
		Set<String> ignoreKey=(Set<String>)Sysconfigs.getEnvMap().get(Sysconfigs.env_service.IGNORE_SERVICE_KEY.code);
		if(ignoreKey.contains(model.getClassName()+"."+model.getFuncCode())){
			logger.info("-----service.ignoreIntfKey method="+model.getFuncCode()+":"+runTime+"ms:("+model.getClassName()+"."+model.getFuncCode()+")");
			isIgnore=true;
			return isIgnore;
		}
		return isIgnore;
	}
	
	/**
	 * 是否按sqlType进行忽略sql日志到数据库处理,sqlType:(select,update,delete)
	 * @param sqlId
	 * @param sql
	 * @param runTime
	 * @param paramMap
	 * @return
	 */
	private boolean ignoreBySqlTypeConfig(final String sqlId, final String sql, final long runTime,
			final Map<String, Object> paramMap) {
		boolean isIgnore=false;
		String sqlType=(String)paramMap.get("sqlType");
		String ignoreSqlType=(String)Sysconfigs.getEnvMap().get(Sysconfigs.env_sql.IGNORE_SQL_TYPE.code);
		if(ignoreSqlType!=null){
			if(ignoreSqlType.indexOf("sqlLogAll")>=0){
				logger.info("-----sql.ignoreSqlType sqlId="+sqlId+":"+runTime+"ms:(sqlLogAll)\n		sql="+sql);
				isIgnore=true;
				return isIgnore;
			}
			String[] ignores=ignoreSqlType.split(",");
			for(String ignore:ignores){
				ignore=ignore.trim();
				if(ignore.equals(sqlType)){
					logger.info("-----sql.ignoreSqlType sqlId="+sqlId+":"+runTime+"ms:("+ignore+")\n		sql="+sql);
					isIgnore=true;
					break;
				}
			}
		}
		return isIgnore;
	}
	
	/**
	 * 按执行时间进行忽略
	 * @param rungTime
	 * @return
	 */
	private boolean ignoreByRunTime(final String sqlId, final String sql){
		boolean isIgnore=false;
		Integer logInfoRunTime=getConfigValue(Sysconfigs.env_sql.LOG_INFO.code, 0);
		if(runTime<logInfoRunTime){
			logger.info("-----sql.logInfo sqlId="+sqlId+":"+runTime+"ms:("+logInfoRunTime+")\n		sql="+sql);
			isIgnore=true;
			return isIgnore;
		}
		return isIgnore;
	}
	
	/**
	 * 按sql包含进行忽略，即只要sql中含有ignoreSqlKey就忽略
	 * @param sqlId
	 * @param sql
	 * @return
	 */
	private boolean ignoreBySql(final String sqlId, final String sql){
		boolean isIgnore=false;
		Set<String> ignoreKey=(Set<String>)Sysconfigs.getEnvMap().get(Sysconfigs.env_sql.IGNORE_KEY.code);
		if(ignoreKey==null){
			return isIgnore;
		}
		for(String ignoreSqlKey:ignoreKey){
			if(ignoreSqlKey.length()>0 && sql.indexOf(ignoreSqlKey)>=0){
				logger.info("-----sql.ignoreKey sqlId="+sqlId+":"+runTime+"ms:("+ignoreSqlKey+")\n		sql="+sql);
				isIgnore=true;
				break;
			}
		}
		return isIgnore;
	}
	
	/**
	 * 按mapperId进行忽略,类名+方法名
	 * @param sqlId
	 * @param sql
	 * @param model
	 * @return
	 */
	private boolean ignoreBySqlId(final String sqlId, final String sql, ModelPO model){
		boolean isIgnore=false;
		Set<String> logSqlIdSet=(Set<String>)Sysconfigs.getEnvMap().get(Sysconfigs.env_sql.IGNORE_SQL_ID.code);
		if(logSqlIdSet.contains(model.getClassName()+"."+model.getFuncCode())){
			logger.info("-----sql.ignoreSqlId sqlId="+sqlId+":"+runTime+"ms:("+model.getClassName()+"."+model.getFuncCode()+")\n		sql="+sql);
			isIgnore=true;
			return isIgnore;
		}
		
		return isIgnore;
	}
	
	
	
	private Integer getConfigValue(final String key, final Integer defaultValue){
		Object v=Sysconfigs.getEnvMap().get(key);
		if(v==null){
//			logger.warn(" key="+key+" is null");
			return defaultValue;
		}
		
		Integer logInfo=0;
		if(v instanceof Integer){
			logInfo=(Integer)v;
		}
		else{
			logInfo=Integer.parseInt(""+v);
		}
		return logInfo;
		
	}
	
	/**
	 * @param runTime
	 * @param exp
	 * @return
	 */
	private Integer getLogLevelByConfigTime(final long runTime, Integer logInfo, Integer logWarn, final Throwable exp) {

		Integer logLevel=2;
		if(exp!=null){
			logLevel=4;
		}
		else if(runTime>=logWarn){
			logLevel=3;
		}
		else if(runTime>=logInfo){
			logLevel=2;
		}
		return logLevel;
	}
}
