package com.babel.basedata.util;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.task.TaskExecutor;

import com.babel.basedata.model.SysconfigPO;
import com.babel.basedata.service.ISysconfigService;
import com.babel.common.core.util.CommUtil;
import com.babel.common.core.util.RedisUtil;
import com.babel.common.core.util.TaskExecutorUtils;

/**
 * 限服务层使用
 * 混合sysconfigs的spring配置及后台sysconfig表的配置
 * @author jinhe.chen
 *
 */
public class Sysconfigs {
	private static Logger logger = Logger.getLogger(Sysconfigs.class);
	private Properties properties;
	/**
	 * 从启动加载的配置 ，如sql.ignoreSqlId,sql.ignoreKey
	 * 见spring-context.xml的id="sysconfigs"
	 */
	private static Map envMap=new HashMap();
	/**
	 * 从数据库加载的配置(可覆盖envMap的配置)
	 */
	private static Map envSysconfigMap=new HashMap();
	private static Date envSysMaxDate=null;
	private static String sysType;
	private static String appType;
	
	public void setSysType(String sysType) {
		Sysconfigs.sysType = sysType;
	}
	/**
	 * @return the appType
	 */
	public static String getAppType() {
		return appType;
	}

	/**
	 * @param appType the appType to set
	 */
	public void setAppType(String appType) {
		Sysconfigs.appType = appType;
	}

	

//	public static Map<String, Object> getEnvMap(){
//		return envMap;
//	}



	public void setProperties(Properties properties) {
		this.properties = properties;
		Set<Map.Entry<Object,Object>> sets=this.properties.entrySet();
		String key=null;
		for(Map.Entry<Object,Object> entry:sets){
			key=(String)entry.getKey();
			Sysconfigs.putMapSetByKey(Sysconfigs.envMap, key, entry.getValue());
		}
		logger.info("sysconfigs.envMap="+envMap);
	}
	
	public static void putMapSetByKey(Map<String, Object> configMap, String code, Object value) {
		List<String> list=new CommUtil().newList(Sysconfigs.env_sql.IGNORE_SQL_ID.code
				,Sysconfigs.env_sql.IGNORE_KEY.code
				,Sysconfigs.env_service.IGNORE_SERVICE_KEY.code);
		if(list.contains(code)){
			configMap.put(code, getString2Set(code, (String)value));
		}
		else{
			configMap.put(code, value);
		}
	}
	
	/**
	 * ModelMapper:create,insert 转成 ModelMapper.create,ModelMapper.insert
	 * @param lines
	 * @return
	 */
	private static Set<String> getString2Set(String key, String lines){
		Set<String> sets=new HashSet<String>();
		if(!StringUtils.isEmpty(lines)){
			lines=lines.trim();
			String[] sqlIds=null;
			if(lines.indexOf(";")>0){
				sqlIds=lines.split(";");
			}
			else{
				sqlIds=lines.split("\n");
			}
			for(String sqlId:sqlIds){
				sqlId=sqlId.trim();
				if(!"".equals(sqlId)){
					addAllSqlIds(key, sqlId, sets);
				}
			}
		}
		
		return sets;
	}


	/**
	 * 根据配置的信息生成对应的每个方法的sqlId配置
	 * ModelMapper:create,insert 转成 ModelMapper.create,ModelMapper.insert
	 * @param sets
	 * @param sqlId
	 * @return
	 */
	private static void addAllSqlIds(String key, String sqlId, Set<String> sets) {
		String className="";
		if((Sysconfigs.env_sql.IGNORE_SQL_ID.code.equals(key) || Sysconfigs.env_service.IGNORE_SERVICE_KEY.code.equals(key))
				&& sqlId.indexOf(":")>0){
			className=sqlId.substring(0, sqlId.indexOf(":"));
			sqlId=sqlId.substring(sqlId.indexOf(":")+1);
		}
		String[] methods=sqlId.split(",");
		for(String method:methods){
			method=method.trim();
			if(className.isEmpty()){
				sets.add(method);
			}
			else{
				sets.add(className+"."+method);
			}
		}
	}

	private static ISysconfigService sysconfigService;
//	private static TaskExecutor taskExecutor;

	public void setSysconfigService(ISysconfigService sysconfigService) {
		Sysconfigs.sysconfigService = sysconfigService;
	}
//	public  void setTaskExecutor(TaskExecutor taskExecutor) {
//		Sysconfigs.taskExecutor = taskExecutor;
//	}
	private static Date lastInitDate=null;

	public static enum env_sql{
		IGNORE_SQL_ID("sql.ignoreSqlId", "mybatis id忽略,如LogDbMapper.insert"),
		IGNORE_KEY("sql.ignoreKey", "查询关键字忽略:sql中包含就忽略"),
		IGNORE_SQL_TYPE("sql.ignoreSqlType", "查询类型忽略，select_seq,select_count"),
		LOG_INFO("sql.logInfo", "INFO日志级别执行时间，单位ms"),
		LOG_WARN("sql.logWarn", "WARN日志级别执行时间，单位ms");
		public String code;
		public String name;
		env_sql(String code, String name){
			this.code=code;
			this.name=name;
		}
		
	}
	public static enum env_service{
		IGNORE_SERVICE_KEY("service.ignoreIntfKey", "mybatis id忽略,如LogDbService.insert"),
		LOG_INFO("service.logInfo", "INFO日志级别执行时间，单位ms"),
		LOG_WARN("service.logWarn", "WARN日志级别执行时间，单位ms");
		public String code;
		public String name;
		env_service(String code, String name){
			this.code=code;
			this.name=name;
		}
		
	}
	
//	private static Date asyncEnvDate=null;
	public static synchronized Map<String, Object> getEnvMap(){
		if(sysconfigService==null){
			logger.warn("----disabled envMap by Sysconfigs.sysconfigService is null");
			return new HashMap<>();
		}
		Integer confType=1;//系统参数配置
		Date now = new Date();
		Map<String, Object> envMap=envSysconfigMap;
		if(envMap.isEmpty() || lastInitDate==null || 
				lastInitDate.before(DateUtils.addSeconds(now, -5))){//5秒后可重新加载
//			System.out.println("-------getEnvMap1--");
			if(lastInitDate==null){//第一次用同步
				lastInitDate=now;
				envMap=initSysconfigMap(confType);
			}
			else if(lastInitDate!=null){//后面用异步
				lastInitDate=now;
//				System.out.println("-------getEnvMap2--");
				initSysconfigMapAsync(confType);
			}
			
			
		}
//		envMap.putAll(Sysconfigs.envMap);
		Map<String, Object> map=new HashMap<>();
		map.putAll(Sysconfigs.envMap);
		map.putAll(envMap);
		map.put("lastInitDate", lastInitDate);
//		System.out.println("---getEnvMap2="+envMap+" "+lastInitDate+"/"+DateUtils.addSeconds(new Date(), -5));
		return map;
	}
	
	public static <T> T getValue(String key, T defaul) {
		return getValue(key, defaul, null, null);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getValue(String key, T defaul, String descript, String pcode) {
		if(StringUtils.isEmpty(key)) {
			return defaul;
		}
		Map<String, Object> envMap = getEnvMap();
		Object obj = envMap.get(key);
		if(obj != null) {
			try {
				if(defaul instanceof Integer){
					T t= (T)Integer.valueOf(""+obj);
					return t;
				} else if(defaul instanceof Long){
					T t= (T)Long.valueOf(""+obj);
					return t;
				} else if(defaul instanceof Double){
					T t= (T)Double.valueOf(""+obj);
					return t;
				}
				return (T)obj;
			} catch(Exception e) {
				logger.warn("--Sysconfigs--getValue, key="+key+" error="+e.getMessage(), e);
				return defaul;
			}
		}
		else{
			logger.warn("--Sysconfigs--getValue, key="+key+" not found");
			if(!StringUtils.isEmpty(descript) && !StringUtils.isEmpty(pcode)) {
				if(!RedisUtil.isRunLimit(Sysconfigs.class, 60, "sysconfigService.save"+key)){
					SysconfigPO keyPO = sysconfigService.findByCode(key);
					if(keyPO == null) {
						SysconfigPO pPO = sysconfigService.findByCode(pcode);
						if(pPO != null) {
							SysconfigPO entity1 = new SysconfigPO();
							entity1.setName(descript);
							entity1.setPid(pPO.getCid());
							entity1.setCode(key);
							entity1.setConfType(pPO.getConfType());
							entity1.setCanModify(pPO.getCanModify());
							entity1.setOrderCount(pPO.getOrderCount());
							if(defaul != null) {
								entity1.setValue(defaul.toString());
								entity1.setValueDefault(defaul.toString());
							}
							sysconfigService.create(entity1);
							logger.warn("--Sysconfigs--sysconfigService.create success, key="+key+" cid="+entity1.getCid());
						}
					}
				}
			}
		}
		return defaul;
	}
	
	
	public static Map<String, Object> initSysconfigMap(Integer confType){
		Date maxDate=sysconfigService.findSysconfigAllMaxModifyDate(confType, null);
		if(envSysMaxDate==null){
			envSysMaxDate=maxDate;
		}
		else if(envSysMaxDate.compareTo(maxDate)==0){//如果数据的最后个性时间未发生变化就不进行查询
			return envSysconfigMap;
		}
		Map<String, Object>  envMap=sysconfigService.initSysconfigMap(confType,sysType);
		if(!envMap.isEmpty()){
			envSysconfigMap=envMap;
		}
		else{
			envMap=envSysconfigMap;
		}
		return envMap;
	}
	
	public static void initSysconfigMapAsync(final Integer confType){
		TaskExecutor taskExecutor=TaskExecutorUtils.getExecutorSingle(Sysconfigs.getAppType(), Sysconfigs.class, "initSysconfigMapAsync", null);
		taskExecutor.execute(new Runnable() {
			public void run() {
//				Integer confType=1;//系统参数配置
				initSysconfigMap(confType);
			}
		});
	}
}
