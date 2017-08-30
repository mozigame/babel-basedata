package com.babel.basedata.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.babel.basedata.model.UserRolePO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;

public interface IUserRoleService  extends IBaseService<UserRolePO> {
	public List<UserRolePO> selectByUserRole(UserRolePO userRole);
	
	public RetResult<UserRolePO> findUserRoleByIdList(List<Long> idList);
	
	public List<UserRolePO> findUserRoleByUserId(Long userId);
	 
	public UserRolePO findUserRoleById(Long id);
	
	public List<UserRolePO> findUserRoleByIds(List<Long> ids);
	
	/**
	 * 按jobType查询，只返回状态有效的，且数据时段为有效的，即findDate所在时段内的数据
	 * @param jobTypes
	 * @return
	 */
	public List<UserRolePO> findUserRoleByJobTypes(List<Integer> jobTypes, Date findDate, Date searchDate);
	
	public List<UserRolePO> findUserRoleByUserRoleNames(Collection<String> userRoleNames);
	
	public PageVO<UserRolePO> findPageByUserRole(UserRolePO search, PageVO<UserRolePO> page);
	
	public RetResult<Long> update(UserRolePO record);
	
	public RetResult<Long> create(UserRolePO record);
	
	public RetResult<Integer> deleteVirtual(Long operId, Long cid);
	
	
}
