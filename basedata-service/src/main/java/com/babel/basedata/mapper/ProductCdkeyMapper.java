package com.babel.basedata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.babel.common.core.page.PageVO;
import com.babel.basedata.model.ProductCdkeyPO;
import tk.mybatis.mapper.common.MapperMy;

public interface ProductCdkeyMapper extends MapperMy<ProductCdkeyPO> {
	List<ProductCdkeyPO> findProductCdkeyListByPage(@Param("param")ProductCdkeyPO param, @Param("page")PageVO<ProductCdkeyPO> page);
	int findProductCdkeyListByPageCount(@Param("param") ProductCdkeyPO record);
}