package com.babel.basedata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.babel.common.core.page.PageVO;
import com.babel.basedata.model.PassiveReplyPO;
import tk.mybatis.mapper.common.MapperMy;

public interface PassiveReplyMapper extends MapperMy<PassiveReplyPO> {
	List<PassiveReplyPO> findPassiveReplyListByPage(@Param("param")PassiveReplyPO param, @Param("page")PageVO<PassiveReplyPO> page);
	int findPassiveReplyListByPageCount(@Param("param") PassiveReplyPO record);
}