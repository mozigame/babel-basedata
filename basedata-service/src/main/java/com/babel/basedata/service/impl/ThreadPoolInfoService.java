package com.babel.basedata.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.babel.common.core.entity.IPoolInfoVO;
import com.babel.common.core.service.IThreadPoolInfoService;
import com.babel.common.core.util.ServerUtil;
import com.babel.basedata.model.ThreadPoolPO;
import com.babel.basedata.service.IThreadPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("basedataThreadPoolInfoService")
public class ThreadPoolInfoService implements IThreadPoolInfoService {
	@Autowired
	private IThreadPoolService threadPoolService;
	@Override
	public Map<String, Map<String, Object>> getPoolInfoMap() {
		return ServerUtil.getThreadPoolInfo();
	}
	@Transactional(propagation=Propagation.SUPPORTS)
	public int savePoolInfo(String sysType, TaskExecutor taskExecutor, IPoolInfoVO poolInfo){
		ThreadPoolPO threadPoolPO=new ThreadPoolPO();
		threadPoolPO.setAllowCoreThreadTimeOut(poolInfo.getAllowCoreThreadTimeOut());
		threadPoolPO.setCorePoolSize(poolInfo.getCorePoolSize());
		threadPoolPO.setMaxPoolSize(poolInfo.getMaxPoolSize());
		threadPoolPO.setKeepAliveSeconds(poolInfo.getKeepAliveSeconds());
		threadPoolPO.setQueueCapacity(poolInfo.getQueueCapacity());
		threadPoolPO.setCode(poolInfo.getCode());
		threadPoolPO.setName(poolInfo.getName());
		threadPoolPO.setSysType(sysType);
		threadPoolPO.setLimitTime(poolInfo.getLimitTime());
		if(taskExecutor instanceof ThreadPoolTaskExecutor){
			threadPoolPO.setThreadType(1);
//			ThreadPoolTaskExecutor threadPool=(ThreadPoolTaskExecutor)taskExecutor;
//			threadPoolPO.setCorePoolSize(threadPool.getCorePoolSize());
//			threadPoolPO.setMaxPoolSize(threadPool.getMaxPoolSize());
		}
		
		this.threadPoolService.create(threadPoolPO);
		
		return 0;
	}
	
	
	
	public List<IPoolInfoVO> findThreadPoolList(String sysType, Date modifyDate){
		int count=this.threadPoolService.countThreadPoolByModifyDate(sysType, modifyDate);
		if(count==0){
			return new ArrayList<>();
		}
		List<ThreadPoolPO> list= this.threadPoolService.findThreadPoolByModifyDate(sysType, modifyDate);
		List<IPoolInfoVO> rList=new ArrayList<>();
		rList.addAll(list);
		return rList;
	}

}
