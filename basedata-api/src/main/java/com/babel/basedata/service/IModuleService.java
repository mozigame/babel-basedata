package com.babel.basedata.service;

import java.util.List;
import java.util.Map;

import com.babel.basedata.entity.ModuleTreeVO;
import com.babel.basedata.model.ModulePO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;

public interface IModuleService  extends IBaseService<ModulePO> {
	public ModulePO findModuleById(Long id);
	
	public List<ModulePO> findModuleByParentIds(List<Long> moduleIds);
	
	public List<Map<String, Object>> getModuleCountByParentId(List<Long> parentIdList);
	
	public List<ModulePO> findRolesModuleByParentIds(List<Long> roleIdList, List<Long> moduleIds);
	public List<ModulePO> findUserModuleByParentIds(Long userId, List<Long> moduleIds);
	
	public List<ModuleTreeVO> findRoleModuleTreeByParentIds(Long roleId, List<Long> moduleIds);
	
	public List<ModuleTreeVO> findRolesModuleTreeByParentIds(List<Long> roleIdList, List<Long> moduleIds);
	
	public PageVO<ModulePO> findPageByModule(ModulePO search, PageVO<ModulePO> page);
	
	public RetResult<ModulePO> getModuleByName(String name);
	
	public RetResult<Long> update(ModulePO record);
	
	public RetResult<Long> updateRel(Long operId, Long cid, Long oldParentId, Long newParentId);
	
	public RetResult<Long> create(ModulePO record);
	
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid);
	
	
}
