package com.babel.basedata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.babel.common.core.page.PageVO;
import com.babel.basedata.model.WhiteListPO;

import tk.mybatis.mapper.common.MapperMy;

public interface WhiteListMapper extends MapperMy<WhiteListPO> {
	List<WhiteListPO> findWhiteListListByPage(@Param("param")WhiteListPO param, @Param("page")PageVO<WhiteListPO> page);
	int findWhiteListListByPageCount(@Param("param") WhiteListPO record);
}