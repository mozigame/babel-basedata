package com.babel.basedata.service;

import java.util.List;

import com.babel.basedata.model.RetryRulePO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;

public interface IRetryRuleService  extends IBaseService<RetryRulePO> {
	public List<RetryRulePO> selectByRetryRule(RetryRulePO retryRule, int page, int rows);
	
	public List<RetryRulePO> findRetryRuleAll();
	
	public List<RetryRulePO> findRetryRuleByIds(List<Long> idList);
	 
	public RetryRulePO findRetryRuleById(Long id);
	
	public RetryRulePO findRetryRuleByCode(String code);
	
	public PageVO<RetryRulePO> findPageByRetryRule(RetryRulePO search, PageVO<RetryRulePO> page);
	
	public RetResult<Long> update(RetryRulePO record);
	
	public RetResult<Long> create(RetryRulePO record);
	
	public RetResult<Integer> deleteVirtual(Long operId, Long cid)  throws RetException;
	
	
}
