package com.babel.basedata.service;

import java.util.List;

import com.babel.basedata.model.RolePO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;

public interface IRoleService  extends IBaseService<RolePO> {
	public List<RolePO> selectByRole(RolePO role, int page, int rows);
	 
	public RolePO findRoleById(Long id);
	
	public List<RolePO> findRoleByIds(List<Long> ids);
	
	public PageVO<RolePO> findPageByRole(RolePO search, PageVO<RolePO> page);
	
	public RetResult<Long> update(RolePO record);
	
	public RetResult<Long> create(RolePO record);
	
	public RetResult<Integer> deleteVirtual(Long operId, Long cid);
	
	
}
