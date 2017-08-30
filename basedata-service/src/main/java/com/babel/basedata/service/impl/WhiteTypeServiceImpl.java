package com.babel.basedata.service.impl;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.babel.basedata.logger.LogService;
import com.babel.basedata.mapper.WhiteTypeMapper;
import com.babel.basedata.model.WhiteTypePO;
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

@Service("whiteTypeService")
public class WhiteTypeServiceImpl extends BaseService<WhiteTypePO> implements IWhiteTypeService{
	private static final Log logger = LogFactory.getLog(WhiteTypeServiceImpl.class);
	
	@Autowired
	private WhiteTypeMapper whiteTypeMapper;
	
	 @Override
		public WhiteTypeMapper getMapper() {
			return whiteTypeMapper;
		}
	 
	 @Override
		public WhiteTypePO findWhiteTypeByCode(String code) {
			Example example = new Example(WhiteTypePO.class);
			Example.Criteria criteria = example.createCriteria();
			criteria.andEqualTo("ifDel", 0);
			criteria.andEqualTo("status", 1);
			criteria.andEqualTo("code", code);
			List<WhiteTypePO> list=this.selectByExample(example);
			if(list.size()>0){
				return list.get(0);
			}
			return null;
		}
		
		public List<WhiteTypePO> findWhiteTypeAll() {
			Example example = new Example(WhiteTypePO.class);
			Example.Criteria criteria = example.createCriteria();
			criteria.andEqualTo("ifDel", 0);
			criteria.andEqualTo("status", 1);
			List<WhiteTypePO> list=this.selectByExample(example);
			return list;
		}
		
		public List<WhiteTypePO> findWhiteTypeByType(Integer dataType, Integer whiteType) {
			Example example = new Example(WhiteTypePO.class);
			Example.Criteria criteria = example.createCriteria();
			criteria.andEqualTo("ifDel", 0);
			criteria.andEqualTo("status", 1);
			criteria.andEqualTo("dataType",dataType);
			criteria.andEqualTo("type", whiteType);
			List<WhiteTypePO> list=this.selectByExample(example);
			return list;
		}
	
	@Override
	public WhiteTypePO findWhiteTypeById(Long id) {
		logger.info("----WhiteTypePO findWhiteTypeById----id = "+id);
		return whiteTypeMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public List<WhiteTypePO> findWhiteTypeByIds(List<Long> ids) {
		logger.info("----findWhiteTypeByIds----");
		Example example = new Example(WhiteTypePO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("cid", ids);
		return whiteTypeMapper.selectByExample(example);
	}
	
	@Override
    public List<WhiteTypePO> selectByWhiteType(WhiteTypePO whiteType) {
		logger.info("----selectByWhiteType----whiteType = "+whiteType);
		if(whiteType == null) {
			whiteType = new WhiteTypePO();
		}
		whiteType.setStatus(1);
		whiteType.setIfDel(0);
        return whiteTypeMapper.select(whiteType);
    }
	
	public PageVO<WhiteTypePO> findPageByWhiteType(WhiteTypePO whiteType, PageVO<WhiteTypePO> page) {
		logger.info("----findPageByWhiteType----whiteType = "+whiteType);
		Example example = new Example(WhiteTypePO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
  		if (whiteType.getType() != null && whiteType.getType()>=0) {
			criteria.andEqualTo("type", whiteType.getType());
		}
  		if (whiteType.getDataType() != null && whiteType.getDataType()>=0) {
			criteria.andEqualTo("dataType", whiteType.getDataType());
		}
  		if (whiteType.getStatus() != null && whiteType.getStatus()>=0) {
			criteria.andEqualTo("status", whiteType.getStatus());
		}
		if (StringUtil.isNotEmpty(whiteType.getName())) {
			criteria.andLike("name", "%" + whiteType.getName() + "%");
		}
		String orderClause = page.getOrderClause();
		if(StringUtil.isNotEmpty(orderClause)) {
			example.setOrderByClause(orderClause);
		}
		return selectPageByExample(example, page);
	}
	
//	public PageVO<WhiteTypePO> findPageByMapperXML(WhiteTypePO whiteType, PageVO<WhiteTypePO> page) {
//		logger.info("----findPageByMapperXML----");
//		List<WhiteTypePO> list=null;
//		int totalSize=0;
//		try {
//			totalSize = whiteTypeMapper.findWhiteTypeListByPageCount(whiteType);
//			list = whiteTypeMapper.findWhiteTypeListByPage(whiteType, page);
//		} catch (Exception e) {
//			logger.error("-----findWhiteTypeListByPage--", e);
//		}
//		PageVO<WhiteTypePO> pageRet=new PageVO<WhiteTypePO>(list, totalSize, page.getPageSize(), page.getCurrentPage());
//		return pageRet;
//	}
	
	@LogService(title="WhiteTypeServiceImpl.create",author="Jview",calls="insert")
	public RetResult<Long> create(WhiteTypePO whiteType){
		logger.info("----create----whiteType = "+whiteType);
		RetResult<Long> ret = new RetResult<Long>();
		whiteType.initCreate();
		save(whiteType);
		WhiteListUtils.putWhiteType(whiteType.getCid(), whiteType);
		ret.setData(whiteType.getCid());
		this.reloadCache(whiteType.getDataType(), whiteType.getType());
		return ret;
	}
	
	@LogService(title="WhiteTypeServiceImpl.update",author="Jview",calls="updateByPrimaryKey",descs="cid=#{0.cid}")
	public RetResult<Long> update(WhiteTypePO whiteType){
		logger.info("----update----whiteType = "+whiteType);
		RetResult<Long> ret = new RetResult<Long>();
		if(whiteType.getCid()==null || whiteType.getCid().longValue()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid = "+whiteType.getCid()+" is empty", null);
			return ret;
		}
		whiteType.initUpdate();
		whiteType.setIfDel(0);
		whiteTypeMapper.updateByPrimaryKey(whiteType);
		ret.setData(whiteType.getCid());
		this.reloadCache(whiteType.getDataType(), whiteType.getType());
		WhiteListUtils.putWhiteType(whiteType.getCid(), whiteType);
		return ret;
	}
	
	@LogService(title="WhiteTypeServiceImpl.update",author="Jview",calls="deleteVirtual",descs="cid=#{0.cid}")
	public RetResult<Integer> deleteVirtual(Long cid) throws RetException{
		logger.info("----deleteVirtual----cid = "+cid);
		RetResult<Integer> ret = new RetResult<Integer>();
		if(cid == null || cid == 0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid = "+cid+" is empty", null);
			return ret;
		}
		WhiteTypePO whiteType = new WhiteTypePO();
		whiteType.setCid(cid);
		int v = 0;
		try {
			v = this.deleteVirtual(whiteType);
			this.reloadCache(whiteType.getDataType(), whiteType.getType());
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "cid = "+cid+",error = "+e.getMessage(), e);
			logger.error("-----deleteVirtual----", e);
			throw new RetException(ret);
		}
		ret.setData(v);
		return ret;
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

}
