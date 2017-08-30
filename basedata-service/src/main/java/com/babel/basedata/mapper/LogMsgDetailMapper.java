package com.babel.basedata.mapper;

import java.util.List;

import com.babel.basedata.model.LogMsgDetailPO;

import tk.mybatis.mapper.common.MapperMy;

public interface LogMsgDetailMapper extends MapperMy<LogMsgDetailPO> {
	public void insertBatch(List<LogMsgDetailPO> list);
	
	public void updateBatch(List<LogMsgDetailPO> list);
	
	public void insertLogMsgDetail(LogMsgDetailPO logMsg);
	
}