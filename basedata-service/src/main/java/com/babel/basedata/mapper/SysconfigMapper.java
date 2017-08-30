package com.babel.basedata.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.babel.basedata.model.SysconfigPO;

import tk.mybatis.mapper.common.MapperMy;

public interface SysconfigMapper extends MapperMy<SysconfigPO> {
public void insertSysconfig(SysconfigPO model);
	
	
	public List<SysconfigPO> findSysconfigAll(@Param("confType") Integer confType
			, @Param("parentCode") String parentCode);
	
	public List<SysconfigPO> findSysconfigByParentIds(@Param("confType") Integer confType
			, @Param("pidList") List<Long> pidList);
	public List<Map<String, Object>> findSysconfigByParentIdsCount(@Param("confType") Integer confType, 
			@Param("pidList") List<Long> pidList);
}