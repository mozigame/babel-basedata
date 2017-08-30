package com.babel.basedata.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.babel.basedata.model.UserRolePO;

import tk.mybatis.mapper.common.MapperMy;

public interface UserRoleMapper extends MapperMy<UserRolePO> {
	/**
	 * 有效用户角色查询，只查询findDate对应的生效时间
	 * @param jobTypes	为空表示不限
	 * @param findDate 为空表示不限
	 * @return
	 */
	public List<UserRolePO> findUserRoleByJobTypes(@Param("jobTypes")List<Integer> jobTypes
			,@Param("dataDate") Date findDate, @Param("searchDate") Date searchDate);
}