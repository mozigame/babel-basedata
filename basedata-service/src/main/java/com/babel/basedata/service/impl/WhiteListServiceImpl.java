package com.babel.basedata.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.mock.web.MockServletContext;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.babel.basedata.logger.LogService;
import com.babel.basedata.mapper.WhiteListMapper;
import com.babel.basedata.model.WhiteListPO;
import com.babel.basedata.model.WhiteTypePO;
import com.babel.basedata.service.IWhiteListService;
import com.babel.basedata.service.IWhiteTypeService;
import com.babel.basedata.util.WhiteListUtils;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.impl.BaseService;
import com.babel.common.core.util.SpringContextUtil;
import com.babel.common.web.loader.IContextTaskLoader;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

@Service("whiteListService")
public class WhiteListServiceImpl extends BaseService<WhiteListPO> implements IWhiteListService{
	private static final Log logger = LogFactory.getLog(WhiteListServiceImpl.class);
	
	@Autowired
	private WhiteListMapper whiteListMapper;
	@Autowired
	private IWhiteTypeService whiteTypeService;
	
	 @Override
		public WhiteListMapper getMapper() {
			return whiteListMapper;
		}
	
	@Override
	public WhiteListPO findWhiteListById(Long id) {
		logger.info("----WhiteListPO findWhiteListById----id = "+id);
		return whiteListMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public List<WhiteListPO> findWhiteListByIds(List<Long> ids) {
		logger.info("----findWhiteListByIds----");
		Example example = new Example(WhiteListPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("cid", ids);
		return whiteListMapper.selectByExample(example);
	}
	
	
	public List<WhiteListPO> findWhiteListByWhiteTypeIds(List<Long> whiteTypeIdList) {
		logger.info("----findWhiteListByWhiteTypeId--whiteTypeIdList="+whiteTypeIdList);
		Example example = new Example(WhiteListPO.class);
		Example.Criteria criteria = example.createCriteria();
		example.selectProperties("content","whiteTypeId","cid");
		criteria.andEqualTo("ifDel", 0);
		criteria.andEqualTo("status", 1);
		criteria.andIn("whiteTypeId", whiteTypeIdList);
//		example.setOrderByClause("period asc");
		return whiteListMapper.selectByExample(example);
	}
	
	public List<WhiteListPO> findWhiteListByDataType(Integer dataType, Integer whiteType){
		List<WhiteTypePO> whiteTypeList=this.whiteTypeService.findWhiteTypeByType(dataType, whiteType);
		if(whiteTypeList.size()==0){
			return new ArrayList<>();
		}
		List<Long> wtIdList=new ArrayList<>();
		for(WhiteTypePO wt:whiteTypeList){
			wtIdList.add(wt.getCid());
		}
		

		return this.findWhiteListByWhiteTypeIds(wtIdList);
	}
	
	@CacheEvict(value = "basedata", key = "'getWhiteListByCode'+#whiteTypeCode" )
//	@CacheEvict("whiteTypeDetails")
	public void findWhiteListByRuleCode_flushCache(String whiteTypeCode){
		logger.info("----flushCache--whiteTypeCode="+whiteTypeCode);
	}
	

	
//	@Cacheable(value = "basedata", key = "'getWhiteListByCode'+#whiteTypeCode" )
//	@Cacheable("whiteTypeDetails")
	public RetResult<WhiteListPO> findWhiteListByRuleCode(String whiteTypeCode) {
		logger.info("----findWhiteListByWhiteTypeCode--whiteTypeCode="+whiteTypeCode);
		RetResult<WhiteListPO> ret = new RetResult<>();
		
		if(whiteTypeCode==null){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "invalid whiteTypeCode:"+whiteTypeCode, null);
			return ret;
		}
		else if(whiteTypeCode.length()>200){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "whiteTypeCode:"+whiteTypeCode+" out of length:"+200, null);
			return ret;
		}
		
		WhiteTypePO whiteType=this.whiteTypeService.findWhiteTypeByCode(whiteTypeCode);
		if(whiteType!=null){
			ret.setDataList(this.findWhiteListByWhiteTypeIds(Arrays.asList(whiteType.getCid())));
		}
		
		

		return ret;
	}
	
	@Override
    public List<WhiteListPO> selectByWhiteList(WhiteListPO whiteList) {
		logger.info("----selectByWhiteList----whiteList = "+whiteList);
		if(whiteList == null) {
			whiteList = new WhiteListPO();
		}
		whiteList.setStatus(1);
		whiteList.setIfDel(0);
        return whiteListMapper.select(whiteList);
    }
	
	public PageVO<WhiteListPO> findPageByWhiteList(WhiteListPO whiteList, PageVO<WhiteListPO> page) {
		logger.info("----findPageByWhiteList----whiteList = "+whiteList);
		Example example = new Example(WhiteListPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		if (StringUtil.isNotEmpty(whiteList.getContent())) {
			criteria.andLike("content", "%" + whiteList.getContent() + "%");
		}
		if (whiteList.getWhiteTypeId() != null) {
			criteria.andEqualTo("whiteTypeId", whiteList.getWhiteTypeId());
		}
		String orderClause = page.getOrderClause();
		if(StringUtil.isNotEmpty(orderClause)) {
			example.setOrderByClause(orderClause);
		}
		return selectPageByExample(example, page);
	}
	
	public PageVO<WhiteListPO> findPageByMapperXML(WhiteListPO whiteList, PageVO<WhiteListPO> page) {
		logger.info("----findPageByMapperXML----");
		List<WhiteListPO> list=null;
		int totalSize=0;
		try {
			totalSize = whiteListMapper.findWhiteListListByPageCount(whiteList);
			list = whiteListMapper.findWhiteListListByPage(whiteList, page);
		} catch (Exception e) {
			logger.error("-----findWhiteListListByPage--", e);
		}
		PageVO<WhiteListPO> pageRet=new PageVO<WhiteListPO>(list, totalSize, page.getPageSize(), page.getCurrentPage());
		return pageRet;
	}
	
	@LogService(title="WhiteListServiceImpl.create",author="Jview",calls="insert")
	public RetResult<Long> create(WhiteListPO whiteList){
		logger.info("----create----whiteList = "+whiteList);
		RetResult<Long> ret = new RetResult<Long>();
		whiteList.initCreate();
		save(whiteList);
		ret.setData(whiteList.getCid());
		
		this.reloadCache(whiteList);
		
		return ret;
	}
	
	private void reloadCache(WhiteListPO whiteList){
		WhiteTypePO whiteType=WhiteListUtils.getWhiteType(whiteList.getWhiteTypeId());
		if(whiteType!=null)
			this.reloadCache(whiteType.getDataType(), whiteType.getType());
		else{
			logger.warn("----reloadCache--whiteTypeId="+whiteList.getWhiteTypeId());
		}
	}
	private void reloadCache(Integer dataType, Integer whiteType){
		logger.info("------reloadCache--dataType="+dataType+" whiteType="+whiteType);
		try {
			ServletContext context=new MockServletContext();
			context.setAttribute("dataType", dataType);
			context.setAttribute("whiteType", whiteType);
			ServletContextEvent event=new ServletContextEvent(context);
			if(SpringContextUtil.containsBean("whiteListLoader")){
				IContextTaskLoader load=(IContextTaskLoader)SpringContextUtil.getBean("whiteListLoader");
				load.execute(event);
			}
		} catch (BeansException e) {
			logger.warn("------reloadCache--dataType="+dataType+" whiteType="+whiteType, e);
		}
	}
	
	@LogService(title="WhiteListServiceImpl.update",author="Jview",calls="updateByPrimaryKey",descs="cid=#{0.cid}")
	public RetResult<Long> update(WhiteListPO whiteList){
		logger.info("----update----whiteList = "+whiteList.getWhiteTypeId());
		RetResult<Long> ret = new RetResult<Long>();
		if(whiteList.getCid()==null || whiteList.getCid().longValue()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid = "+whiteList.getCid()+" is empty", null);
			return ret;
		}
		whiteList.initUpdate();
		whiteList.setIfDel(0);
		whiteListMapper.updateByPrimaryKey(whiteList);
		ret.setData(whiteList.getCid());
		this.reloadCache(whiteList);
		return ret;
	}
	
	@LogService(title="WhiteListServiceImpl.update",author="Jview",calls="deleteVirtual",descs="cid=#{0.cid}")
	public RetResult<Integer> deleteVirtual(Long cid) throws RetException{
		logger.info("----deleteVirtual----cid = "+cid);
		RetResult<Integer> ret = new RetResult<Integer>();
		if(cid == null || cid == 0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid = "+cid+" is empty", null);
			return ret;
		}
		WhiteListPO whiteList = new WhiteListPO();
		whiteList.setCid(cid);
		int v = 0;
		try {
			v = this.deleteVirtual(whiteList);
			this.reloadCache(whiteList);
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "cid = "+cid+",error = "+e.getMessage(), e);
			logger.error("-----deleteVirtual----", e);
			throw new RetException(ret);
		}
		ret.setData(v);
		return ret;
	}

}
