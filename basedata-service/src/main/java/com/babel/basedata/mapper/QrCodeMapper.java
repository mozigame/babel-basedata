package com.babel.basedata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.babel.common.core.page.PageVO;
import com.babel.basedata.model.QrCodePO;

import tk.mybatis.mapper.common.MapperMy;

public interface QrCodeMapper extends MapperMy<QrCodePO> {
	List<QrCodePO> findQrCodeListByPage(@Param("param")QrCodePO param, @Param("page")PageVO<QrCodePO> page);
	int findQrCodeListByPageCount(@Param("param") QrCodePO record);
}