package com.babel.basedata.util;

import java.util.List;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.babel.basedata.model.FuncRetryPO;
import com.babel.basedata.model.RetryRulePO;
import com.babel.basedata.service.IFuncRetryService;
import com.babel.basedata.service.IRetryRuleDetailService;
import com.babel.basedata.service.IRetryRuleService;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.util.SpringContextUtil;
import com.babel.common.web.loader.IContextTaskLoader;

/**
 * 重试规则加载
 * @author 金和
 *
 */
@Service("retryRuleLoader")
public class RetryRuleLoader  implements IContextTaskLoader {
	private static Logger log = Logger.getLogger(RetryRuleLoader.class);
	@Autowired
	private IRetryRuleService retryRuleService;
	@Autowired
	private IRetryRuleDetailService retryRuleDetailService;
	
	@Autowired
	private IFuncRetryService funcRetryService;

	@Override
	public RetResult<String> execute(ServletContextEvent event) {
		RetResult<String> ret = new RetResult<String>();
		boolean isBasedata=SpringContextUtil.containsBean("dataSourceBasedata");
		log.debug("-----execute--isBasedata="+isBasedata);
		System.out.println("-----execute--isBasedata="+isBasedata);
		if(isBasedata){
			initFuncRetry();
			this.initRetryRule();
		}
		return ret;
	}
	
	@Scheduled(fixedRate = 180000)//3分钟加载一次
	public void reloadRetry(){
		boolean isBasedata=SpringContextUtil.containsBean("dataSourceBasedata");
		log.debug("-----reloadRetry--isBasedata="+isBasedata);
		System.out.println("-----reloadRetry--isBasedata="+isBasedata);
		if(isBasedata){
			initFuncRetry();
			this.initRetryRule();
		}
	}
	
	private void initFuncRetry(){
		this.log.info("-----load initFuncRetry--start--");
		List<FuncRetryPO> ruleList=this.funcRetryService.findFuncRetryAll();
		String ruleCode="";
		for(FuncRetryPO rule:ruleList){
			this.funcRetryService.refreshCache(rule.getCid(), rule.getCode());
			ruleCode+=rule.getCode()+",";
		}
		this.log.info("-----load initFuncRetry--ruleCodes="+ruleCode);
	}
	

	private void initRetryRule(){
		this.log.info("-----load retryRuleLoader--start--");
		List<RetryRulePO> ruleList=this.retryRuleService.findRetryRuleAll();
		String ruleCode="";
		for(RetryRulePO rule:ruleList){
			this.retryRuleDetailService.refreshCache(rule.getCid(), rule.getCode());
			ruleCode+=rule.getCode()+",";
		}
		this.log.info("-----load retryRuleLoader--ruleCodes="+ruleCode);
	}
}
