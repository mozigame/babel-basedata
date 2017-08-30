package com.babel.basedata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.babel.basedata.model.LookupItemPO;

import tk.mybatis.mapper.common.MapperMy;

public interface LookupItemMapper extends MapperMy<LookupItemPO> {
	List<LookupItemPO> findLookupItemListByCode(@Param("language") String language
			, @Param("lookupCodeList") List<String> lookupCodeList);
}