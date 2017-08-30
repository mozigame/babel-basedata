package com.babel.basedata.service;

import java.util.Collection;
import java.util.List;

import com.babel.basedata.model.UserPO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;

public interface IUserService  extends IBaseService<UserPO> {
	public List<UserPO> selectByUser(UserPO user);
	
	public RetResult<UserPO> findUserByIdList(List<Long> idList);
	 
	public UserPO findUserById(Long id);
	
	public List<UserPO> findUserByIds(List<Long> ids);
	
	public List<UserPO> findUserByUserNames(Collection<String> userNames);
	
	public PageVO<UserPO> findPageByUser(UserPO search, PageVO<UserPO> page);
	
	public RetResult<Long> update(UserPO record);
	
	public RetResult<Long> create(UserPO record);
	
	public RetResult<Integer> deleteVirtual(Long operId, Long cid);
	
	
}
