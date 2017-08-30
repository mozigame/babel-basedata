package com.babel.basedata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.babel.common.core.page.PageVO;
import com.babel.basedata.model.WhiteTypePO;

import tk.mybatis.mapper.common.MapperMy;

public interface WhiteTypeMapper extends MapperMy<WhiteTypePO> {
	List<WhiteTypePO> findWhiteTypeListByPage(@Param("param")WhiteTypePO param, @Param("page")PageVO<WhiteTypePO> page);
	int findWhiteTypeListByPageCount(@Param("param") WhiteTypePO record);
}