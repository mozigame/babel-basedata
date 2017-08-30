package com.babel.basedata.service;

import java.util.List;

import com.babel.basedata.model.LookupItemPO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;

public interface ILookupItemService  extends IBaseService<LookupItemPO> {
	public List<LookupItemPO> selectByLookupItem(LookupItemPO item, int page, int rows);
	
	public RetResult<LookupItemPO> findLookupItemById(Long id);
	
	public PageVO<LookupItemPO> findPageByLookupItem(LookupItemPO search, PageVO<LookupItemPO> page);
	
	/**
     * 根据系统类型查数据字典中配置的appid
     * @param sysType
     * @return
     */
	public String getSysAppIdByLookupCode(String sysType);
	
	public RetResult<String> getAppSecretByConfig(String appId);
	
	public RetResult<Long> update(LookupItemPO record);
	
	public RetResult<Long> create(LookupItemPO record);
	
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid);
	
	public RetResult<LookupItemPO> findLookupItemByLookupCode(String language, String lookupCode);
	
	public RetResult<LookupItemPO> findLookupItemByDataId(String language, String lookupCode, String dataId);
	
	public void findLookupItemByLookupCode_flushCache(String language, String lookupCode);
}
