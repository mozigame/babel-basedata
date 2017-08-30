package com.babel.basedata.service;

import java.util.List;

import com.babel.basedata.model.FuncRetryPO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;

public interface IFuncRetryService  extends IBaseService<FuncRetryPO> {
	public List<FuncRetryPO> selectByFuncRetry(FuncRetryPO funcRetry, int page, int rows);
	 
	public FuncRetryPO findFuncRetryById(Long id);
	
	public void refreshCache(Long retryRuleId, String retryCode);
	
	public List<FuncRetryPO> findFuncRetryAll();
	
	public PageVO<FuncRetryPO> findPageByFuncRetry(FuncRetryPO search, PageVO<FuncRetryPO> page);
	
	public RetResult<Long> update(FuncRetryPO record);
	
	public RetResult<Long> create(FuncRetryPO record);
	
	public RetResult<Integer> deleteVirtual(Long operId, Long cid);
	
	
}
