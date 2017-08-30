package com.babel.basedata.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.babel.basedata.model.ModulePO;

import tk.mybatis.mapper.common.MapperMy;

public interface ModuleMapper extends MapperMy<ModulePO> {
	public void insertModule(ModulePO model);
	/**
	 * 根据角色及模块id查下面的数量
	 * @param roleIdList
	 * @param parentId module
	 * @return
	 */
	public List<Map<String, Object>> getModuleCountByParentId(@Param("parentIdList") List<Long> parentIdList);
}