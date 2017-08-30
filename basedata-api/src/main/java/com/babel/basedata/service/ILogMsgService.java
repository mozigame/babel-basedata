package com.babel.basedata.service;

import java.util.Date;
import java.util.List;

import com.babel.basedata.model.LogMsgPO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;

public interface ILogMsgService extends IBaseService<LogMsgPO> {
	public PageVO<LogMsgPO> findPageByLogMsg(LogMsgPO logDb, PageVO<LogMsgPO> page, String selectProperties, List<String> msgCodeList);
	
	public RetResult<LogMsgPO> findLogMsgList(Date sendTime, Integer msgType, Integer sendFlag);
	
	public void insertBatch(List<LogMsgPO> msgList)throws Exception;
	
	public void updateBatch(List<LogMsgPO> msgList)throws Exception;
	
	public RetResult<Long> create(LogMsgPO record);
	
	public RetResult<Long> update(LogMsgPO record);
	
	
}
