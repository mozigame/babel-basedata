package com.babel.basedata.service;

import com.babel.basedata.model.LogLoginPO;
import com.babel.basedata.model.UserPO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;

public interface ILogLoginService extends IBaseService<LogLoginPO> {
	public void createLogLoginAsync(LogLoginPO record, RetResult<UserPO> loginRet);
	
	public RetResult<Long> create(LogLoginPO record, RetResult<UserPO> loginRet);
	
	public LogLoginPO findLogLoginById(Long id);
	
	public PageVO<LogLoginPO> findPageByLogLogin(LogLoginPO logLogin, PageVO<LogLoginPO> page);
}
