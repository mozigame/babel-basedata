package com.babel.basedata.service;

import java.util.List;

import com.babel.basedata.model.PassiveReplyPO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;

public interface IPassiveReplyService  extends IBaseService<PassiveReplyPO> {
	public List<PassiveReplyPO> selectByPassiveReply(PassiveReplyPO passiveReply);
	 
	public PassiveReplyPO findPassiveReplyById(Long id);
	
	public List<PassiveReplyPO> findPassiveReplyByIds(List<Long> ids);
	
	public PageVO<PassiveReplyPO> findPageByPassiveReply(PassiveReplyPO passiveReply, PageVO<PassiveReplyPO> page);
	
	public RetResult<Long> update(PassiveReplyPO record);
	
	public RetResult<Long> create(PassiveReplyPO record);
	
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid) throws RetException;
	
	
}
