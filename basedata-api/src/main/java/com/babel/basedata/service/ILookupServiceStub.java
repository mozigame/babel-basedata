package com.babel.basedata.service;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.babel.basedata.model.LookupPO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.impl.BaseService;

import tk.mybatis.mapper.common.MapperMy;

public class ILookupServiceStub  extends BaseService<LookupPO> implements ILookupService{
	private static Logger logger = Logger.getLogger(ILookupServiceStub.class);
	private final ILookupService lookupService;

	// 构造函数传入真正的远程代理对象
	public ILookupServiceStub(ILookupService lookupService) {
		this.lookupService = lookupService;
	}

	@Override
	public List<LookupPO> selectByLookup(LookupPO lookup, int page, int rows) {
		return this.lookupService.selectByLookup(lookup, page, rows);
	}

	@Override
	public LookupPO findLookupById(Long id) {
		return this.lookupService.findLookupById(id);
	}

	@Override
	public PageVO<LookupPO> findPageByLookup(LookupPO search, PageVO<LookupPO> page) {
		logger.info("------stub--findPageByLookup");
		return this.lookupService.findPageByLookup(search, page);
	}

	@Override
	public PageVO<HashMap> findLookupMapByPage(HashMap search, PageVO<HashMap> page) {
		return this.lookupService.findLookupMapByPage(search, page);
	}

	@Override
	public RetResult<Long> update(LookupPO record) {
		return this.lookupService.update(record);
	}

	@Override
	public RetResult<Long> create(LookupPO record) {
		return this.lookupService.create(record);
	}

	@Override
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid) throws RetException {
		return this.lookupService.deleteVirtual(operUserId, cid);
	}

	@Override
	public MapperMy<LookupPO> getMapper() {
		return null;
	}


	
}
