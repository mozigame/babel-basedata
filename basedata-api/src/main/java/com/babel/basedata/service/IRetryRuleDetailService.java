package com.babel.basedata.service;

import java.util.List;

import com.babel.basedata.model.RetryRuleDetailPO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;

public interface IRetryRuleDetailService  extends IBaseService<RetryRuleDetailPO> {
	public List<RetryRuleDetailPO> selectByRetryRuleDetail(RetryRuleDetailPO item, int page, int rows);
	
	public RetResult<RetryRuleDetailPO> findRetryRuleDetailById(Long id);
	
	public PageVO<RetryRuleDetailPO> findPageByRetryRuleDetail(RetryRuleDetailPO search, PageVO<RetryRuleDetailPO> page);
	
	public RetResult<Long> update(RetryRuleDetailPO record);
	
	public RetResult<Long> create(RetryRuleDetailPO record);
	
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid);
	
	public RetResult<RetryRuleDetailPO> findRetryRuleDetailByRuleCode(String ruleCode);
	
	public List<RetryRuleDetailPO> findRetryRuleDetailByRetryRuleId(Long retryRuleId);
	
	public void findRetryRuleDetailByRuleCode_flushCache(String ruleCode);
	
	public void refreshCache(Long retryRuleId);
	
	public void refreshCache(Long retryRuleId, String retryCode);
}
