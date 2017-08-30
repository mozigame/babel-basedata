package com.babel.basedata.service;

import java.util.List;

import com.babel.basedata.model.WhiteTypePO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;

public interface IWhiteTypeService  extends IBaseService<WhiteTypePO> {
	public List<WhiteTypePO> selectByWhiteType(WhiteTypePO whiteType);
	 
	public WhiteTypePO findWhiteTypeById(Long id);
	
	public WhiteTypePO findWhiteTypeByCode(String code);
	
	public List<WhiteTypePO> findWhiteTypeAll();
	
	public List<WhiteTypePO> findWhiteTypeByType(Integer dataType, Integer whiteType);
	
	public List<WhiteTypePO> findWhiteTypeByIds(List<Long> ids);
	
	public PageVO<WhiteTypePO> findPageByWhiteType(WhiteTypePO whiteType, PageVO<WhiteTypePO> page);
	
//	public PageVO<WhiteTypePO> findPageByMapperXML(WhiteTypePO whiteType, PageVO<WhiteTypePO> page);
	
	public RetResult<Long> update(WhiteTypePO record);
	
	public RetResult<Long> create(WhiteTypePO record);
	
	public RetResult<Integer> deleteVirtual(Long cid) throws RetException;
	
	
}
