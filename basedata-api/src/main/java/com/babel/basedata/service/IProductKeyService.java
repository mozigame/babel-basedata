package com.babel.basedata.service;

import java.util.List;

import com.babel.basedata.model.ProductKeyPO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;

public interface IProductKeyService  extends IBaseService<ProductKeyPO> {
	public List<ProductKeyPO> selectByProductKey(ProductKeyPO productKey);
	 
	public ProductKeyPO findProductKeyById(Long id);
	
	public List<ProductKeyPO> findProductKeyByIds(List<Long> ids);
	
	
	
	public ProductKeyPO getProductKeyByPpcode(String appid);
	
	public PageVO<ProductKeyPO> findPageByProductKey(ProductKeyPO productKey, PageVO<ProductKeyPO> page);
	
	public RetResult<Long> update(ProductKeyPO record);
	
	public RetResult<Long> create(ProductKeyPO record);
	
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid) throws RetException;
	
	
}
