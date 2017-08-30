package com.babel.basedata.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.core.task.TaskExecutor;

import com.babel.basedata.service.ISysconfigUserService;
import com.babel.common.core.util.TaskExecutorUtils;

/**
 * 限服务层使用
 * @author jinhe.chen
 *
 */
public class SysconfigUsers {
	private static Logger logger = Logger.getLogger(SysconfigUsers.class);
	private static Map<String, Object> baseUserConfigMap=new HashMap<>();//用户通用系统参数配置
	private static Map<Long, Map<String, Object>> userConfigMap=new HashMap<>();//用户个性化修改过的系统参数信息
	private static Date baseUserMaxDate=null;

	
	private static ISysconfigUserService sysconfigUserService;

	public void setSysconfigUserService(ISysconfigUserService sysconfigUserService) {
		SysconfigUsers.sysconfigUserService = sysconfigUserService;
	}
	private static Date lastInitDate=null;

	
	
	public static synchronized Map<Long, Map<String, Object>> getUserEnvMap(){
		if(sysconfigUserService==null){
			logger.warn("----disabled envMap by SysconfigUsers.sysconfigUserService is null");
			return new HashMap<>();
		}
		Map<Long, Map<String, Object>> envMap=userConfigMap;
		if(envMap.isEmpty() || lastInitDate==null || 
				lastInitDate.before(DateUtils.addSeconds(new Date(), -3600))){//5秒后可重新加载
//			System.out.println("-------getEnvMap1--");
			if(lastInitDate==null){//第一次用同步
				lastInitDate=new Date();
				baseUserConfigMap=initSysconfigBaseUserMap();
				envMap=initSysconfigUserMap();
			}
			else if(lastInitDate!=null){//后面用异步
				lastInitDate=new Date();
//				System.out.println("-------getEnvMap2--");
				initSysconfigMapAsync();
			}
		}
		return envMap;
	}
	
	public static Map<String, Object> getUserEnvMap(Long userId){
		Map<String, Object> map=new HashMap<>();
		map.putAll(baseUserConfigMap);
		Map<String, Object> userMap= userConfigMap.get(userId);
		if(userMap!=null)
			map.putAll(userMap);
		return map;
	}
	
	public static Map<String, Object> getUserMap(Long userId){
		Map<String, Object> userMap= userConfigMap.get(userId);
		if(userMap==null){
			userMap=new HashMap<String, Object>();
			userConfigMap.put(userId, userMap);
		}
		return userMap;
	}
	
	/**
	 * 加载默认设置，仅启动加载一次
	 * @return
	 */
	public static Map<String, Object> initSysconfigBaseUserMap(){
		Map<String, Object>  envMap=sysconfigUserService.initSysconfigBaseUserMap();
		if(!envMap.isEmpty()){
			baseUserConfigMap=envMap;
		}
		else{
			envMap=baseUserConfigMap;
		}
		return envMap;
	}
	
	/**
	 * 加载用户设置
	 * @return
	 */
	public static Map<Long, Map<String, Object>> initSysconfigUserMap(){
		Map<Long, Map<String, Object>>  envMap=sysconfigUserService.initSysconfigUserMapAll(null);
		if(!envMap.isEmpty()){
			userConfigMap=envMap;
		}
		else{
			envMap=userConfigMap;
		}
		return envMap;
	}
	
	public static void initSysconfigMapAsync(){
		TaskExecutor taskExecutor=TaskExecutorUtils.getExecutorSingle(Sysconfigs.getAppType(), SysconfigUsers.class, "initSysconfigMapAsync", null);
		taskExecutor.execute(new Runnable() {
			public void run() {
//				initSysconfigBaseUserMap();
				initSysconfigUserMap();
			}
		});
	}
}
