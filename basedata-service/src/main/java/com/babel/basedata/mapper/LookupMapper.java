package com.babel.basedata.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.babel.common.core.page.PageVO;
import com.babel.basedata.model.LookupPO;

import tk.mybatis.mapper.common.MapperMy;

public interface LookupMapper extends MapperMy<LookupPO> {
	 List<LookupPO> findLookupListByPage(@Param("param")LookupPO param, @Param("page")PageVO<LookupPO> page);
	 int findLookupListByPageCount(@Param("param") LookupPO record);
	 
	 int findLookupMapByPageCount(@Param("param") HashMap record);
	 
	 List<HashMap> findLookupMapByPage(@Param("param")HashMap param, @Param("page")PageVO<HashMap> page);
}