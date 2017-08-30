package com.babel.basedata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.babel.basedata.model.SysconfigUserPO;

import tk.mybatis.mapper.common.MapperMy;

public interface SysconfigUserMapper extends MapperMy<SysconfigUserPO> {
	List<SysconfigUserPO> findSysconfigUserListByCode(@Param("userId") Long userId,
			@Param("sysconfigCodeList") List<String> sysconfigCodeList);
}