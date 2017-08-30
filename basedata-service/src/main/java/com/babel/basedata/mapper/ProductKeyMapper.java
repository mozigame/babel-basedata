package com.babel.basedata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.babel.common.core.page.PageVO;
import com.babel.basedata.model.ProductKeyPO;
import tk.mybatis.mapper.common.MapperMy;

public interface ProductKeyMapper extends MapperMy<ProductKeyPO> {
	List<ProductKeyPO> findProductKeyListByPage(@Param("param")ProductKeyPO param, @Param("page")PageVO<ProductKeyPO> page);
	int findProductKeyListByPageCount(@Param("param") ProductKeyPO record);
}