package com.babel.basedata.mapper;

import java.util.List;

import com.babel.basedata.model.LogMsgPO;

import tk.mybatis.mapper.common.MapperMy;

public interface LogMsgMapper extends MapperMy<LogMsgPO> {
	public void insertBatch(List<LogMsgPO> list);
	
	public void updateBatch(List<LogMsgPO> list);
	
	public void insertLogMsg(LogMsgPO logMsg);
}