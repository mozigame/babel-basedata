package com.babel.basedata.service;

import java.util.List;

import com.babel.basedata.model.WhiteListPO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;

public interface IWhiteListService  extends IBaseService<WhiteListPO> {
	public List<WhiteListPO> selectByWhiteList(WhiteListPO whiteList);
	 
	public WhiteListPO findWhiteListById(Long id);
	
	public void findWhiteListByRuleCode_flushCache(String whiteTypeCode);
	
	public List<WhiteListPO> findWhiteListByIds(List<Long> ids);
	
	public List<WhiteListPO> findWhiteListByWhiteTypeIds(List<Long> whiteTypeIdList);
	
	public List<WhiteListPO> findWhiteListByDataType(Integer dataType, Integer whiteType);
	
	public PageVO<WhiteListPO> findPageByWhiteList(WhiteListPO whiteList, PageVO<WhiteListPO> page);
	
	public PageVO<WhiteListPO> findPageByMapperXML(WhiteListPO whiteList, PageVO<WhiteListPO> page);
	
	public RetResult<Long> update(WhiteListPO record);
	
	public RetResult<Long> create(WhiteListPO record);
	
	public RetResult<Integer> deleteVirtual(Long cid) throws RetException;
	
	
}
