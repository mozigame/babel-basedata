package com.babel.basedata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.babel.common.core.page.PageVO;
import com.babel.basedata.model.TemplatePO;
import tk.mybatis.mapper.common.MapperMy;

public interface TemplateMapper extends MapperMy<TemplatePO> {
	List<TemplatePO> findTemplateListByPage(@Param("param")TemplatePO param, @Param("page")PageVO<TemplatePO> page);
	int findTemplateListByPageCount(@Param("param") TemplatePO record);
}