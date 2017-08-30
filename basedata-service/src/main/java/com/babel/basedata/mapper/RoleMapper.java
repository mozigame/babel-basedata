package com.babel.basedata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.babel.common.core.page.PageVO;
import com.babel.basedata.model.RolePO;

import tk.mybatis.mapper.common.MapperMy;

public interface RoleMapper extends MapperMy<RolePO> {
	List<RolePO> findRoleListByPage(@Param("param")RolePO param, @Param("page")PageVO<RolePO> page);
	 int findRoleListByPageCount(@Param("param") RolePO record);
}