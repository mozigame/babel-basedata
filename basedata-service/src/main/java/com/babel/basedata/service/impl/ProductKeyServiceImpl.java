package com.babel.basedata.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.impl.BaseService;
import com.babel.common.core.util.TaskExecutorUtils;
import com.babel.basedata.logger.LogService;
import com.babel.basedata.mapper.ProductKeyMapper;
import com.babel.basedata.model.ProductKeyPO;
import com.babel.basedata.service.IProductKeyService;
import com.babel.basedata.util.Sysconfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

@Service("productKeyService")
public class ProductKeyServiceImpl extends BaseService<ProductKeyPO> implements IProductKeyService{
	private static final Log logger = LogFactory.getLog(ProductKeyServiceImpl.class);
	
	@Autowired
	private ProductKeyMapper productKeyMapper;
	
	private static Map<String, ProductKeyPO> prodctKeyMap=new HashMap<>();
	private Date asyncDate=null;
	
	 @Override
		public ProductKeyMapper getMapper() {
			return productKeyMapper;
		}
	 
	 public void loadProductKeyAll(){
		 ProductKeyPO search = new ProductKeyPO();
//		 search.setStatus(1);
		 search.setIfDel(0);
		 List<ProductKeyPO> list=this.selectByProductKey(search);
		 logger.info("-----loadProductKeyAll--list="+list.size());
		 for(ProductKeyPO pKey:list){
			 prodctKeyMap.put(pKey.getAppid(), pKey);
		 }
	 }
	 
	 public List<ProductKeyPO> findProductKeyByModifyDate(Date modifyDate){
			logger.info("----findProductKeyByModifyDate----modifyDate="+modifyDate);
			Example example = new Example(ProductKeyPO.class);
			Example.Criteria criteria = example.createCriteria();
			criteria.andEqualTo("ifDel", 0);
//			criteria.andEqualTo("status", 1);
			if(modifyDate!=null){
				criteria.andGreaterThanOrEqualTo("modifyDate", modifyDate);
			}
			return productKeyMapper.selectByExample(example);
		}
	 
	 private void reloadNewlyProductKeyAsync(){
		 String sysType=Sysconfigs.getAppType();
		 final Class clazz=this.getClass();
		 final String key=null;
		 
		 final int timeLimit=5;//5s
		 Date now = new Date();
		 if(asyncDate==null || 
				asyncDate.before(org.apache.commons.lang.time.DateUtils.addSeconds(now, -timeLimit))){//5秒后可重新加载
			return;
		 }
		 
		 final Properties properties=TaskExecutorUtils.getPoolInfo(1, 10, 10);
		 properties.put("limitTime", timeLimit);
		 TaskExecutor taskExecutor=TaskExecutorUtils.getTaskExecutorInstance(sysType, clazz, key, properties);
		 if(taskExecutor==null){
			 logger.warn("-----reloadNewlyProductKeyAsync--taskExecutor is null");
		 }
		 taskExecutor.execute(new Runnable() {
				public void run() {
					Date lastDate=asyncDate;
					asyncDate=now;
					synchronized (lastDate) {
						List<ProductKeyPO> list=findProductKeyByModifyDate(lastDate);
						logger.warn("-----reloadNewlyProductKeyAsync--list="+list.size());
						for(ProductKeyPO pKey:list){
							 prodctKeyMap.put(pKey.getAppid(), pKey);
						}
					}
				}
		 });
	 }
	
	@Override
	public ProductKeyPO findProductKeyById(Long id) {
		logger.info("----ProductKeyPO findProductKeyById----id = "+id);
		//SqlHelper.addIgnore(ProductKeyPO.class, "name");
		return productKeyMapper.selectByPrimaryKey(id);
	}
	
	public ProductKeyPO getProductKeyByPpcode(String appid){
		if(prodctKeyMap.isEmpty()){
			synchronized (appid) {
				this.loadProductKeyAll();
			}
		}
		reloadNewlyProductKeyAsync();
		return prodctKeyMap.get(appid);
	}
	
	@Override
	public List<ProductKeyPO> findProductKeyByIds(List<Long> ids) {
		logger.info("----findProductKeyByIds----");
		Example example = new Example(ProductKeyPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("cid", ids);
		return productKeyMapper.selectByExample(example);
	}
	
	@Override
    public List<ProductKeyPO> selectByProductKey(ProductKeyPO productKey) {
		logger.info("----selectByProductKey----productKey = "+productKey);
		if(productKey == null) {
			productKey = new ProductKeyPO();
		}
		productKey.setStatus(1);
		productKey.setIfDel(0);
        return productKeyMapper.select(productKey);
    }
	
	public PageVO<ProductKeyPO> findPageByProductKey(ProductKeyPO productKey, PageVO<ProductKeyPO> page) {
		logger.info("----findPageByProductKey----productKey = "+productKey);
		Example example = new Example(ProductKeyPO.class);
		Example.Criteria criteria = example.createCriteria();
  		if (productKey.getAppid() != null) {
			criteria.andEqualTo("appid", productKey.getAppid());
		}
  		if (productKey.getStatus() != null) {
			criteria.andEqualTo("status", productKey.getStatus());
		}
		String orderClause = page.getOrderClause();
		if(StringUtil.isNotEmpty(orderClause)) {
			example.setOrderByClause(orderClause);
		}
		return selectPageByExample(example, page);
	}
	
	
	@LogService(title="ProductKeyServiceImpl.create",author="Jinhe.chen",calls="insert")
	public RetResult<Long> create(ProductKeyPO productKey){
		logger.info("----create----productKey = "+productKey);
		RetResult<Long> ret = new RetResult<Long>();
		productKey.initCreate();
		save(productKey);
		ret.setData(productKey.getCid());
		return ret;
	}
	
	@LogService(title="ProductKeyServiceImpl.update",author="Jinhe.chen",calls="updateByPrimaryKey",descs="cid=#{0.cid}")
	public RetResult<Long> update(ProductKeyPO productKey){
		logger.info("----update----productKey = "+productKey);
		RetResult<Long> ret = new RetResult<Long>();
		if(productKey.getCid()==null || productKey.getCid().longValue()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid = "+productKey.getCid()+" is empty", null);
			return ret;
		}
		productKey.initUpdate();
		productKey.setIfDel(0);
		productKeyMapper.updateByPrimaryKey(productKey);
		ret.setData(productKey.getCid());
		return ret;
	}
	
	@LogService(title="ProductKeyServiceImpl.update",author="Jinhe.chen",calls="deleteVirtual",descs="cid=#{0.cid}")
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid) throws RetException{
		logger.info("----deleteVirtual----cid = "+cid);
		RetResult<Integer> ret = new RetResult<Integer>();
		if(cid == null || cid == 0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid = "+cid+" is empty", null);
			return ret;
		}
		ProductKeyPO productKey = new ProductKeyPO();
		productKey.setCid(cid);
		productKey.setModifyUser(operUserId);
		int v = 0;
		try {
			v = this.deleteVirtual(productKey);
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "cid = "+cid+",error = "+e.getMessage(), e);
			logger.error("-----deleteVirtual----", e);
			throw new RetException(ret);
		}
		ret.setData(v);
		return ret;
	}

}
