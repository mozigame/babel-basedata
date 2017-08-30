package com.babel.basedata.service;

import java.util.List;

import com.babel.basedata.model.ProductCdkeyPO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;

public interface IProductCdkeyService  extends IBaseService<ProductCdkeyPO> {
	public List<ProductCdkeyPO> selectByProductCdkey(ProductCdkeyPO productCdkey);
	 
	public ProductCdkeyPO findProductCdkeyById(Long id);
	
	public List<ProductCdkeyPO> findProductCdkeyByIds(List<Long> ids);
	
	public PageVO<ProductCdkeyPO> findPageByProductCdkey(ProductCdkeyPO productCdkey, PageVO<ProductCdkeyPO> page);
	
	public RetResult<Long> update(ProductCdkeyPO record);
	
	public RetResult<Long> create(ProductCdkeyPO record);
	
	public RetResult<Integer> deleteVirtual(Long cid) throws RetException;


	public RetResult<ProductCdkeyPO> generateCdKey(ProductCdkeyPO productCdkey, String signValue
			, String timestamp, String nonce_str, String remoteIp);
	
	public RetResult<ProductCdkeyPO> findByOrderNO(String orderNo, String appid, String signValue
			, String timestamp, String nonce_str, String remoteIp);
	
	public ProductCdkeyPO findByCdkey(String cdkey);

	
}
