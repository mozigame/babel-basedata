package com.babel.basedata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.babel.common.core.page.PageVO;
import com.babel.basedata.model.UserPO;

import tk.mybatis.mapper.common.MapperMy;

public interface UserMapper extends MapperMy<UserPO> {
	List<UserPO> findUserListByPage(@Param("param")UserPO param, @Param("page")PageVO<UserPO> page);
	 int findUserListByPageCount(@Param("param") UserPO record);
}