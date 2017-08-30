package com.babel.basedata.service;

import java.util.Date;
import java.util.List;

import com.babel.basedata.model.ThreadPoolPO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;

public interface IThreadPoolService  extends IBaseService<ThreadPoolPO> {
	public List<ThreadPoolPO> selectByThreadPool(ThreadPoolPO threadPool);
	 
	public ThreadPoolPO findThreadPoolById(Long id);
	
	public ThreadPoolPO findThreadPoolByCode(String code);
	
	public int countThreadPoolByModifyDate(String sysType, Date modifyDate);
	
	public List<ThreadPoolPO> findThreadPoolByModifyDate(String sysType, Date modifyDate);
	
	public List<ThreadPoolPO> findThreadPoolByIds(List<Long> ids);
	
	public PageVO<ThreadPoolPO> findPageByThreadPool(ThreadPoolPO threadPool, PageVO<ThreadPoolPO> page);
	
	public PageVO<ThreadPoolPO> findPageByMapperXML(ThreadPoolPO threadPool, PageVO<ThreadPoolPO> page);
	
	public RetResult<Long> update(ThreadPoolPO record);
	
	public RetResult<Long> create(ThreadPoolPO record);
	
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid) throws RetException;
	
	
}
