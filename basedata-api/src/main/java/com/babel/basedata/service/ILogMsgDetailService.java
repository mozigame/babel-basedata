package com.babel.basedata.service;

import java.util.Date;
import java.util.List;

import com.babel.basedata.model.LogMsgDetailPO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;

public interface ILogMsgDetailService extends IBaseService<LogMsgDetailPO> {
	public PageVO<LogMsgDetailPO> findPageByLogMsgDetail(LogMsgDetailPO logDb, PageVO<LogMsgDetailPO> page);
	
	public RetResult<LogMsgDetailPO> findLogMsgDetailList(Date sendTime, Integer msgType, Integer sendFlag);
	
	public void insertBatch(List<LogMsgDetailPO> msgList)throws Exception;
	
	public void updateBatch(List<LogMsgDetailPO> msgList)throws Exception;
	
	public RetResult<Long> create(LogMsgDetailPO record);
	
	public RetResult<Long> update(LogMsgDetailPO record);
	
	
}
