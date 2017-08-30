package com.babel.basedata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.babel.common.core.page.PageVO;
import com.babel.basedata.model.ThreadPoolPO;
import tk.mybatis.mapper.common.MapperMy;

public interface ThreadPoolMapper extends MapperMy<ThreadPoolPO> {
	List<ThreadPoolPO> findThreadPoolListByPage(@Param("param")ThreadPoolPO param, @Param("page")PageVO<ThreadPoolPO> page);
	int findThreadPoolListByPageCount(@Param("param") ThreadPoolPO record);
}