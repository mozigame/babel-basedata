package com.babel.basedata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.babel.common.core.page.PageVO;
import com.babel.basedata.model.ShortLinkPO;
import tk.mybatis.mapper.common.MapperMy;

public interface ShortLinkMapper extends MapperMy<ShortLinkPO> {
	List<ShortLinkPO> findShortLinkListByPage(@Param("param")ShortLinkPO param, @Param("page")PageVO<ShortLinkPO> page);
	int findShortLinkListByPageCount(@Param("param") ShortLinkPO record);
}