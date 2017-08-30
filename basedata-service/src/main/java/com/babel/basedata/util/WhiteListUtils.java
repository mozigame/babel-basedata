package com.babel.basedata.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.babel.basedata.model.WhiteListPO;
import com.babel.basedata.model.WhiteTypePO;
import com.babel.common.core.service.IWhiteListCheckService;
import com.babel.common.core.util.RedisUtil;
import com.babel.common.core.util.SpringContextUtil;

/**
 * 白名单缓存
 * @author 金和
 *
 */
public class WhiteListUtils {
	private static Logger logger = Logger.getLogger(WhiteListUtils.class);
	private static Map<Long, WhiteTypePO> whiteTypeMap=new HashMap<>();//{cid, whiteType}
	private static Map<String, List<WhiteListPO>> whiteListMap=new HashMap<>();//{dataType_type, whiteList}
	public static final String REDIS_KEY_WHITE_TYPE="redisWhiteTypeMap";
	public static final String REDIS_KEY_WHITE_LIST="redisWhiteListMap";
	
	public static IWhiteListCheckService getCheckServiceByTypeCode(){
		IWhiteListCheckService whiteListCheckService=null;
//		WhiteTypePO retryRule=whiteTypeMap.get(whiteType);
		String serviceCode="whiteListCheckService";
		String serviceCodeSys=(String)Sysconfigs.getEnvMap().get("app.whitelist.serviceCode");
		if(!StringUtils.isEmpty(serviceCodeSys)){
			serviceCode=serviceCodeSys.trim();
		}
		if (SpringContextUtil.containsBean(serviceCode)) {
			whiteListCheckService = (IWhiteListCheckService) SpringContextUtil
					.getBean(serviceCode);
		}
		else{
			logger.error("-----getCheckService--serviceCode="+serviceCode+" service not found!");
		}
		return whiteListCheckService;
	}
	
	
	public static void putWhiteType(Long cid, WhiteTypePO whiteType){
		RedisUtil.putRedis(whiteTypeMap, REDIS_KEY_WHITE_TYPE, cid, whiteType);
	}
	
	public static WhiteTypePO getWhiteType(Long cid){
		return (WhiteTypePO)RedisUtil.getRedis(whiteTypeMap, REDIS_KEY_WHITE_TYPE, cid);
	}
	
	public static void putWhiteList(String key, List<WhiteListPO> whiteList){
		RedisUtil.putRedis(whiteListMap, REDIS_KEY_WHITE_LIST, key, whiteList);
	}
	
	public static void removeWhiteList(String key){
		whiteListMap.remove(key);
		
		RedisUtil.putRedis(whiteListMap, REDIS_KEY_WHITE_LIST, key, new ArrayList<>());
	}
	
	public static List<WhiteListPO> getWhiteList(String key){
		return (List<WhiteListPO>)RedisUtil.getRedis(whiteListMap, REDIS_KEY_WHITE_LIST, key);
	}
	
	
	
	
	
	
}
