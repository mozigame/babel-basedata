package com.babel.basedata.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.logger.LogAudit;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.impl.BaseService;
import com.babel.basedata.logger.LogService;
import com.babel.basedata.mapper.ThreadPoolMapper;
import com.babel.basedata.model.ThreadPoolPO;
import com.babel.basedata.service.IThreadPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

@Service("threadPoolService")
public class ThreadPoolServiceImpl extends BaseService<ThreadPoolPO> implements IThreadPoolService{
	private static final Log logger = LogFactory.getLog(ThreadPoolServiceImpl.class);
	
	@Autowired
	private ThreadPoolMapper threadPoolMapper;
	
	 @Override
		public ThreadPoolMapper getMapper() {
			return threadPoolMapper;
		}
	
	@Override
	public ThreadPoolPO findThreadPoolById(Long id) {
		logger.info("----ThreadPoolPO findThreadPoolById----id = "+id);
		//SqlHelper.addIgnore(ThreadPoolPO.class, "name");
		return threadPoolMapper.selectByPrimaryKey(id);
	}
	
	public ThreadPoolPO findThreadPoolByCode(String code){
		Example example = new Example(ThreadPoolPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("code", code);
		criteria.andEqualTo("ifDel", 0);
		List<ThreadPoolPO> list=threadPoolMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		return list.iterator().next();
	}
	
	@Override
	public List<ThreadPoolPO> findThreadPoolByIds(List<Long> ids) {
		logger.info("----findThreadPoolByIds----");
		Example example = new Example(ThreadPoolPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("cid", ids);
		return threadPoolMapper.selectByExample(example);
	}
	
	@Override
    public List<ThreadPoolPO> selectByThreadPool(ThreadPoolPO threadPool) {
		logger.info("----selectByThreadPool----threadPool = "+threadPool);
		if(threadPool == null) {
			threadPool = new ThreadPoolPO();
		}
		threadPool.setStatus(1);
		threadPool.setIfDel(0);
        return threadPoolMapper.select(threadPool);
    }
	
	public int countThreadPoolByModifyDate(String sysType, Date modifyDate){
//		logger.info("----countThreadPoolByModifyDate----sysType="+sysType+" modifyDate="+modifyDate);
		Example example = new Example(ThreadPoolPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		criteria.andEqualTo("status", 1);
		criteria.andEqualTo("sysType", sysType);
		if(modifyDate!=null){
			criteria.andGreaterThanOrEqualTo("modifyDate", modifyDate);
		}
		return threadPoolMapper.selectCountByExample(example);
	}
	
	public List<ThreadPoolPO> findThreadPoolByModifyDate(String sysType, Date modifyDate){
		logger.info("----findThreadPoolByModifyDate----sysType="+sysType+" modifyDate="+modifyDate);
		Example example = new Example(ThreadPoolPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		criteria.andEqualTo("status", 1);
		criteria.andEqualTo("sysType", sysType);
		if(modifyDate!=null){
			criteria.andGreaterThanOrEqualTo("modifyDate", modifyDate);
		}
		return threadPoolMapper.selectByExample(example);
	}
	
	@LogAudit(title="thread")
	public PageVO<ThreadPoolPO> findPageByThreadPool(ThreadPoolPO threadPool, PageVO<ThreadPoolPO> page) {
		logger.info("----findPageByThreadPool----threadPool = "+threadPool);
//		System.out.println(this.threadPoolMapper.nextSeqId(threadPool));
		Example example = new Example(ThreadPoolPO.class);
		Example.Criteria criteria = example.createCriteria();
  		if (threadPool.getSysType() != null) {
			criteria.andEqualTo("sysType", threadPool.getSysType());
		}
  		if (threadPool.getThreadType() != null) {
			criteria.andEqualTo("threadType", threadPool.getThreadType());
		}
  		if (threadPool.getCode() != null) {
			criteria.andEqualTo("code", threadPool.getCode());
		}
  		if (threadPool.getName() != null) {
			criteria.andEqualTo("name", threadPool.getName());
		}
  		if (threadPool.getStatus() != null) {
			criteria.andEqualTo("status", threadPool.getStatus());
		}
		String orderClause = page.getOrderClause();
		if(StringUtil.isNotEmpty(orderClause)) {
			example.setOrderByClause(orderClause);
		}
		return selectPageByExample(example, page);
	}
	
	public PageVO<ThreadPoolPO> findPageByMapperXML(ThreadPoolPO threadPool, PageVO<ThreadPoolPO> page) {
		logger.info("----findPageByMapperXML----");
		List<ThreadPoolPO> list=null;
		int totalSize=0;
		try {
			totalSize = threadPoolMapper.findThreadPoolListByPageCount(threadPool);
			list = threadPoolMapper.findThreadPoolListByPage(threadPool, page);
		} catch (Exception e) {
			logger.error("-----findThreadPoolListByPage--", e);
		}
		PageVO<ThreadPoolPO> pageRet=new PageVO<ThreadPoolPO>(list, totalSize, page.getPageSize(), page.getCurrentPage());
		return pageRet;
	}
	
	@LogService(title="ThreadPoolServiceImpl.create",author="Jinhe.chen",calls="insert")
	public RetResult<Long> create(ThreadPoolPO threadPool){
		logger.info("----create----threadPool = "+threadPool.getCode());
		RetResult<Long> ret = new RetResult<Long>();
		threadPool.initCreate();
		int saveStatus=this.threadPoolMapper.insert(threadPool);
		logger.info("----create----threadPool = "+threadPool.getCode()+" saveStatus="+saveStatus);
		ret.setData(threadPool.getCid());
		return ret;
	}
	
	@LogService(title="ThreadPoolServiceImpl.update",author="Jinhe.chen",calls="updateByPrimaryKey",descs="cid=#{0.cid}")
	public RetResult<Long> update(ThreadPoolPO threadPool){
		logger.info("----update----threadPool = "+threadPool.getCode());
		RetResult<Long> ret = new RetResult<Long>();
		if(threadPool.getCid()==null || threadPool.getCid().longValue()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid = "+threadPool.getCid()+" is empty", null);
			return ret;
		}
		threadPool.initUpdate();
		threadPool.setIfDel(0);
		int saveStatus=threadPoolMapper.updateByPrimaryKey(threadPool);
		logger.info("----update----threadPool = "+threadPool.getCode()+" saveStatus="+saveStatus);
		ret.setData(threadPool.getCid());
		return ret;
	}
	
	@LogService(title="ThreadPoolServiceImpl.update",author="Jinhe.chen",calls="deleteVirtual",descs="cid=#{0.cid}")
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid) throws RetException{
		logger.info("----deleteVirtual----cid = "+cid);
		RetResult<Integer> ret = new RetResult<Integer>();
		if(cid == null || cid == 0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid = "+cid+" is empty", null);
			return ret;
		}
		ThreadPoolPO threadPool = new ThreadPoolPO();
		threadPool.setCid(cid);
		threadPool.setModifyUser(operUserId);
		int v = 0;
		try {
			v = this.deleteVirtual(threadPool);
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "cid = "+cid+",error = "+e.getMessage(), e);
			logger.error("-----deleteVirtual----", e);
			throw new RetException(ret);
		}
		ret.setData(v);
		return ret;
	}

}
