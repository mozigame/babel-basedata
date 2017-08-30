package com.babel.basedata.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.babel.basedata.model.FuncRetryPO;
import com.babel.basedata.model.RetryRuleDetailPO;
import com.babel.basedata.util.RetryRuleUtils;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.service.IRetryRuleCheckService;
import com.babel.common.core.util.ServerUtil;
import com.babel.common.core.util.SpringContextUtil;

@Service
public class RetryRuleCheckRedisService implements IRetryRuleCheckService {
	private static final Log logger = LogFactory
			.getLog(RetryRuleCheckRedisService.class);

	private static RedisTemplate redisTemplate = null;
	
	private void initService() {
		if (SpringContextUtil.containsBean("redisTemplate")) {
			redisTemplate = (RedisTemplate) SpringContextUtil
					.getBean("redisTemplate");
		}
	}
	/**
	 * 
	 * @param retry
	 * @param code
	 * @param userName 用户限制:all所有用户,none不对用户进行限制,userName指定的用户,多个用户以“,”隔开'，如果是指定用户，那不在这里面的用户就没有限制了
	 * @param userIp ip限制，all所有IP，all_out所有外部用户，all_in所有内网用户，ip指定的IP地址，多个ip以“,”隔开，如果是指定IP，那不在这里面的IP就没有限制了
	 * @return
	 */
	private String getRetryKey(RetryRuleDetailPO retry, String code, String userName, String userIp) {
		String limitIp=retry.getLimitIp();
		String limitUser=retry.getLimitUser();
		String retryKey="_retry_" + code + "_" + retry.getCode();
		if(limitIp==null){
			limitIp="none";
		}
		if(limitUser==null){
			limitUser="none";
		}
		
		if("none".equals(limitIp) && "none".equals(limitUser)){
			return null;
		}
		
		boolean isExist=false;
		if("all".equals(limitUser)){
			retryKey+="_"+userName;
		}
		else if(!"none".equals(limitUser)){
			String[] users=limitUser.split(",");
			isExist=false;
			for(String user:users){
				if(user.equals(userName)){
					isExist=true;
					break;
				}
			}
			if(isExist){
				retryKey+="_"+userName;
			}
			else{
				return null;
			}
		}
		
		
		if("all".equals(limitIp)){
			retryKey+="_"+userIp;
		}
		else if(!"none".equals(limitIp)){
			String[] ips=limitIp.split(",");
			isExist=false;
			for(String ip:ips){
				if(ip.equals(userIp)){
					isExist=true;
					break;
				}
			}
			if(isExist){
				retryKey+="_"+userIp;
			}
			else{
				return null;
			}
		}
		return retryKey;
	}

	public RetResult<String> checkLoginRetryCount(String funcRetryCode
			, String userName, String ip) {
		RetResult<String> ret = new RetResult<String>();
		try {
			if(redisTemplate == null){
				this.initService();
			}
			if (redisTemplate == null) {
				return ret;
			}
			FuncRetryPO funcRetry=RetryRuleUtils.getFuncRetry(funcRetryCode);
			if(funcRetry==null){
				logger.warn("----funcRetryCode="+funcRetryCode+" is not exist on funcRetryMap");
				return ret;
			}
			String ruleCode=funcRetry.getRetryRule_code();
			Long retryCount = 0l;
			String retryKey = null;
			List<RetryRuleDetailPO> ruleDetailList = RetryRuleUtils.getRetryRuleList(ruleCode);
			if(ruleDetailList==null){
				logger.warn("----ruleCode="+ruleCode+" is not exist on retryRuleMap");
				return ret;
			}
			for (RetryRuleDetailPO ruleDetail : ruleDetailList) {
				retryKey = this.getRetryKey(ruleDetail, ruleCode, userName, ip);
				if(retryKey!=null){
					ServerUtil.logRunCount(retryKey);
					Integer period = ruleDetail.getPeriod();
					Integer maxCount = ruleDetail.getMaxCount();
					retryCount = redisTemplate.opsForValue().increment(retryKey, 1l);
//					System.out.println("----retryKey="+retryKey+" retryCount="+retryCount+" maxCount="+maxCount);
					if (retryCount == 1) {
						redisTemplate.expire(retryKey, period, TimeUnit.SECONDS);
					}
					ServerUtil.logRunCount(retryKey);
					if (retryCount.intValue() > maxCount) {
						redisTemplate.opsForValue().increment(retryKey, -1l);//扣除是因为，次数限制后不进行计数处理
						ret.initError(RetResult.msg_codes.ERR_PERMISSION_DENIED,
								ruleDetail.getTipsInfo() +",retryCount="+ retryCount + "/"	+ maxCount, null);
						return ret;
					}
				}
			}
		} catch (Exception e1) {
			logger.warn("passwordRetryLimit by redis:" + e1.getMessage(), e1);
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN,	"Retry count check,error:" + e1.getMessage(), e1);
		}
		return ret;
	}

	public void cleanRetry(String ruleCode, String userName, String ip) {
		if(redisTemplate == null){
			this.initService();
		}
		List<RetryRuleDetailPO> ruleDetailList = RetryRuleUtils.getRetryRuleList(ruleCode);
		if(ruleDetailList==null){
			return;
		}
		String retryKey = null;
		for (RetryRuleDetailPO ruleDetail : ruleDetailList) {
			retryKey = this.getRetryKey(ruleDetail, ruleCode, userName, ip);
			if(retryKey!=null){
				redisTemplate.delete(retryKey);
			}
		}
	}
}
