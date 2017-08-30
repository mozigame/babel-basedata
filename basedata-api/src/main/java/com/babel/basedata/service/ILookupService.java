package com.babel.basedata.service;

import java.util.HashMap;
import java.util.List;

import com.babel.basedata.model.LookupPO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;

public interface ILookupService  extends IBaseService<LookupPO> {
	public List<LookupPO> selectByLookup(LookupPO lookup, int page, int rows);
	 
	public LookupPO findLookupById(Long id);
	
	public PageVO<LookupPO> findPageByLookup(LookupPO search, PageVO<LookupPO> page);
	public PageVO<HashMap> findLookupMapByPage(HashMap search, PageVO<HashMap> page);
	
	public RetResult<Long> update(LookupPO record);
	
	public RetResult<Long> create(LookupPO record);
	
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid) throws RetException;
	
	
}
