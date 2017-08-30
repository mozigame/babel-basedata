package com.babel.basedata.service;

import java.util.List;
import java.util.Map;

import com.babel.basedata.model.RoleModulePO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;

public interface IRoleModuleService  extends IBaseService<RoleModulePO> {
	public RoleModulePO findRoleModuleById(Long id);
	
	public List<RoleModulePO> findRoleModuleByRoleId(Long roleId);
	
	public List<RoleModulePO> findRoleModuleByRoleIds(List<Long> roleIdList);
	
	public List<Map<String, Object>> getRoleModuleCountByParentId(List<Long> roleIdList, List<Long> parentIdList);
	
	public RetResult<String> saveRoleModules(Long roleId, String moduleIds, Long operUserId);
	
	public PageVO<RoleModulePO> findPageByRoleModule(RoleModulePO search, PageVO<RoleModulePO> page);
	
	public RetResult<Long> update(RoleModulePO record);
	
	public RetResult<Long> create(RoleModulePO record);
	
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid);
	
	
}
