package com.babel.basedata.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.babel.basedata.model.SysconfigPO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;


/**
 * 
 * @author jinhe.chen
 *
 */
public interface ISysconfigService  extends IBaseService<SysconfigPO> {
	public SysconfigPO findSysconfigById(Long id);
	
	public PageVO<SysconfigPO> findPageBySysconfig(SysconfigPO search, PageVO<SysconfigPO> page);
	
	public List<SysconfigPO> findSysconfigByParentIds(Integer confType, List<Long> parentIds);
	
	public List<Map<String, Object>> findSysconfigByParentIdsCount(Integer confType, 
			List<Long> pidList);
	
	public List<SysconfigPO> findSysconfigByIds(List<Long> idList);
	
	public RetResult<Long> updateRel(Long operId, Long cid, Long oldParentId, Long newParentId);
	
	public RetResult<Long> update(SysconfigPO record);
	
	public RetResult<Long> create(SysconfigPO record);
	
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid);
	
	
	
	public RetResult<SysconfigPO> getSysconfigByCode(Integer confType, String code);
	public SysconfigPO getSysconfigByCodeCache(Integer confType, String code);
	public void getSysconfigByCodeCache_cacheFlush(Integer confType, String code);
	
	public RetResult<SysconfigPO> getSysconfigByName(Integer confType, String name);
	
	
	public Date findSysconfigAllMaxModifyDate(Integer confType, String parentCode);
	public RetResult<SysconfigPO> findSysconfigAll(Integer confType, String parentCode);
	public RetResult<SysconfigPO> findSysconfigAllCache(Integer confType, String parentCode);
	public void findSysconfigAll_cacheFlush(Integer confType, String parentCode);
	
	
//	public Map<String, Object> getEnvMap();
//	public void initSysconfigMapAsync(Integer confType);
	public  Map<String, Object> initSysconfigMap(Integer confType, String sysType);
	
	public SysconfigPO findByCode(String pcode);
	
	
}
