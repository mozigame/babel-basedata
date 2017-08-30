package com.babel.basedata.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.babel.basedata.model.FuncRetryPO;
import com.babel.basedata.model.RetryRuleDetailPO;
import com.babel.basedata.model.RetryRulePO;
import com.babel.common.core.service.IRetryRuleCheckService;
import com.babel.common.core.util.RedisUtil;
import com.babel.common.core.util.SpringContextUtil;

public class RetryRuleUtils {
	private static Logger logger = Logger.getLogger(RetryRuleUtils.class);
	
	public static Map<String, List<RetryRuleDetailPO>> retryRuleListMap=new HashMap<>();
	private static Map<String, RetryRulePO> retryRuleMap=new HashMap<>();
	private static Map<String, FuncRetryPO> funcRetryMap=new HashMap<>();
	private static Map<Long, Map<String, String>> funcRetryParamMap=new HashMap<Long, Map<String, String>>();
	
	public static final String REDIS_KEY_RETRY_RULE_LIST="redisRetryRuleListMap";
	public static final String REDIS_KEY_RETRY_RULE="redisRetryRuleMap";
	public static final String REDIS_KEY_FUNC_RETRY="redisFuncRetryMap";
	public static final String REDIS_KEY_FUNC_RETRY_PARAM="redisFuncRetryParamMap";
	
	public static String RETRY_FUNC_CODE_LOGINFILTER="f_loginFilter";
	
	
	public static void putRetryRuleList(String ruleCode, List<RetryRuleDetailPO> list){
		RedisUtil.putRedis(retryRuleListMap, REDIS_KEY_RETRY_RULE_LIST, ruleCode, list);
	}
	
	public static List<RetryRuleDetailPO> getRetryRuleList(String ruleCode){
		return (List<RetryRuleDetailPO>)RedisUtil.getRedis(retryRuleListMap, REDIS_KEY_RETRY_RULE_LIST, ruleCode);
	}
	
	public static void removeFuncRetryList(String code){
		retryRuleListMap.remove(code);
		RedisUtil.putRedis(retryRuleListMap, REDIS_KEY_RETRY_RULE_LIST, code, null);
	}
	
	
	
	public static void putRetryRule(String retryCode, RetryRulePO whiteType){
		RedisUtil.putRedis(retryRuleMap, REDIS_KEY_RETRY_RULE, retryCode, whiteType);
	}
	
	public static RetryRulePO getRetryRule(String retryCode){
		return (RetryRulePO)RedisUtil.getRedis(retryRuleMap, REDIS_KEY_RETRY_RULE, retryCode);
	}
	
	
	
	
	public static void putFuncRetry(String code, FuncRetryPO funcRetry){
		RedisUtil.putRedis(funcRetryMap, REDIS_KEY_FUNC_RETRY, code, funcRetry);
	}
	
	public static void removeFuncRetry(String code){
		funcRetryMap.remove(code);
		RedisUtil.putRedis(funcRetryMap, REDIS_KEY_FUNC_RETRY, code, null);
	}
	
	public static FuncRetryPO getFuncRetry(String code){
		return (FuncRetryPO)RedisUtil.getRedis(funcRetryMap, REDIS_KEY_FUNC_RETRY, code);
	}
	
	
	
	
	public static void putFuncRetryParam(Long cid, Map<String, String> funcRetry){
		RedisUtil.putRedis(funcRetryParamMap, REDIS_KEY_FUNC_RETRY_PARAM, cid, funcRetry);
	}
	
	public static Map<String, String> getFuncRetryParam(Long cid){
		return (Map<String, String>)RedisUtil.getRedis(funcRetryParamMap, REDIS_KEY_FUNC_RETRY_PARAM, cid);
	}
	public static void removeFuncRetryParam(Long cid){
		funcRetryParamMap.remove(cid);
		RedisUtil.putRedis(funcRetryParamMap, REDIS_KEY_FUNC_RETRY_PARAM, cid, new ArrayList<>());
	}
	
	
	
	public static IRetryRuleCheckService getCheckServiceByRuleCode(String ruleCode){
		IRetryRuleCheckService retryRuleCheckService=null;
		RetryRulePO retryRule=retryRuleMap.get(ruleCode);
		String serviceCode="retryRuleCheckEhcacheService";
		if(retryRule!=null && !StringUtils.isEmpty(retryRule.getServiceCode())){
			serviceCode=retryRule.getServiceCode().trim();
		}
		if (SpringContextUtil.containsBean(serviceCode)) {
			retryRuleCheckService = (IRetryRuleCheckService) SpringContextUtil
					.getBean(serviceCode);
		}
		else{
			logger.error("-----getCheckService--ruleCode="+ruleCode+" service not found!");
		}
		return retryRuleCheckService;
	}
	
	public static IRetryRuleCheckService getCheckServiceByFuncRuleCode(String funcRetryCode){
		FuncRetryPO funcRetry=funcRetryMap.get(funcRetryCode);
		if(funcRetry==null){
			logger.warn("----funcRetryCode="+funcRetryCode+" is not exist on funcRetryMap");
			return null;
		}
		String ruleCode=funcRetry.getRetryRule_code();
		return getCheckServiceByRuleCode(ruleCode);
	}
	
	
}
