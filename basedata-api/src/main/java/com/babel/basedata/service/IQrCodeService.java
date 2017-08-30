package com.babel.basedata.service;

import java.util.List;

import com.babel.basedata.model.QrCodePO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;

public interface IQrCodeService  extends IBaseService<QrCodePO> {
	public List<QrCodePO> selectByQrCode(QrCodePO qrCode);
	 
	public QrCodePO findQrCodeById(Long id);
	
	public List<QrCodePO> findQrCodeByIds(List<Long> ids);
	
	public PageVO<QrCodePO> findPageByQrCode(QrCodePO qrCode, PageVO<QrCodePO> page);
	
	public PageVO<QrCodePO> findPageByMapperXML(QrCodePO qrCode, PageVO<QrCodePO> page);
	
	public RetResult<Long> update(QrCodePO record);
	
	public RetResult<Long> create(QrCodePO record);
	
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid) throws RetException;
	
	
}
