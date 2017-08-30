package com.babel.basedata.service.impl;

import java.util.HashMap;
import java.util.List;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.impl.BaseService;
import com.babel.basedata.logger.LogService;
import com.babel.basedata.mapper.LookupMapper;
import com.babel.basedata.model.LookupPO;
import com.babel.basedata.service.ILookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.util.StringUtil;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("lookupService")
public class LookupServiceImpl extends BaseService<LookupPO> implements ILookupService{
	 private static final Log logger = LogFactory.getLog(LookupServiceImpl.class);

	 @Autowired
	 private LookupMapper mapper;
	 
	@Override
	public LookupMapper getMapper() {
		return mapper;
	}
	
	@Override
	public LookupPO findLookupById(Long id) {
		logger.info("----findLookupById--id="+id);
		return mapper.selectByPrimaryKey(id);
	}
	
	@Override
    public List<LookupPO> selectByLookup(LookupPO lookup, int page, int rows) {
		logger.info("----selectByLookup--lookup="+lookup);
		
        Example example = new Example(LookupPO.class);
        example.selectProperties("nameCn","nameEn","code","cid");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ifDel", 0);
        criteria.andEqualTo("status", 1);
        if (StringUtil.isNotEmpty(lookup.getNameCn())) {
            criteria.andLike("nameCn", "%" + lookup.getNameCn() + "%");
        }
        if (StringUtil.isNotEmpty(lookup.getCode())) {
            criteria.andLike("code", "%" + lookup.getCode() + "%");
        }
        if (lookup.getCid() != null) {
            criteria.andEqualTo("cid", lookup.getCid());
        }
        
        //分页查询
        PageHelper.startPage(page, rows);
        return selectByExample(example);
    }
	
	@Override
	@LogService(title="lookup",author="cjh",calls="findLookupListByPageCount,findLookupListByPage")
	public PageVO<LookupPO> findPageByLookup(LookupPO search, PageVO<LookupPO> page) {
		logger.info("----findLookupListByPage--");

		
		List<LookupPO> list=null;
		int totalSize=0;
		try {
			totalSize = this.getMapper().findLookupListByPageCount(search);
			list=this.getMapper().findLookupListByPage(search, page);
		} catch (Exception e) {
			logger.error("-----findLookupListByPage--", e);
		}
		
		
		PageVO<LookupPO> pageRet=new PageVO<LookupPO>(list, totalSize, page.getPageSize(), page.getCurrentPage());
		return pageRet;
		
	}
	
	@Override
	@LogService(title="lookupMap",author="cjh",calls="findLookupMapByPageCount,findLookupMapByPage")
	public PageVO<HashMap> findLookupMapByPage(HashMap search, PageVO<HashMap> page){
		List<HashMap> list=null;
		int totalSize=0;
		try {
			totalSize = this.getMapper().findLookupMapByPageCount(search);
			list=this.getMapper().findLookupMapByPage(search, page);
		} catch (Exception e) {
			logger.error("-----findLookupListByPage--", e);
		}
		
		
		PageVO<HashMap> pageRet=new PageVO<HashMap>(list, totalSize, page.getPageSize(), page.getCurrentPage());
		return pageRet;
	}
	public PageVO<LookupPO> findPageByLookup2(LookupPO lookup, PageVO<LookupPO> page) {
		Example example = new Example(LookupPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		if (StringUtil.isNotEmpty(lookup.getNameCn())) {
			example.or().andLike("nameCn", "%" + lookup.getNameCn() + "%");
			example.or().andLike("nameEn", "%" + lookup.getNameCn() + "%");
		}
		if (StringUtil.isNotEmpty(lookup.getCode())) {
			criteria.andLike("code", "%" + lookup.getCode() + "%");
		}
		if (lookup.getCid() != null) {
			criteria.andEqualTo("cid", lookup.getCid());
		}
		
		
		// 分页查询
		PageHelper.startPage(page.getCurrentPage(), page.getPageSize());
		List<LookupPO> list = selectByExample(example);
		PageInfo<LookupPO> pageInfo = new PageInfo<LookupPO>(list);
		PageVO<LookupPO> pageRet = new PageVO<LookupPO>(pageInfo);
		return pageRet;
	}
	
	@Override
	@LogService(title="lookup.create",author="cjh",calls="insert",descs="code=#{0.code}")
	public RetResult<Long> create(LookupPO record){
		logger.info("----create--");
		RetResult<Long> ret = new RetResult<>();
		if(StringUtils.isEmpty(record.getCode())||StringUtils.isEmpty(record.getNameCn())){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "code="+record.getCode()+" or nameCn="+record.getNameCn()+" is empty", null);
			logger.warn("create="+ret.getMsgBody());
			return ret;
		}
		record.initCreate();
		this.getMapper().insert(record);
		ret.setData(record.getCid());
		return ret;
	}
	
	@Override
	@LogService(title="lookup.update",author="cjh",calls="updateByPrimaryKey",descs="lookupId=#{0.cid},code=#{0.code}")
	public RetResult<Long> update(LookupPO record){
		logger.info("----update--");
		RetResult<Long> ret = new RetResult<>();
		if(record.getCid()==null||record.getCid().longValue()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+record.getCid()+" is empty", null);
			logger.error(ret.getMsgBody());
			return ret;
		}
		record.initUpdate();
		record.setIfDel(0);
		this.mapper.updateByPrimaryKey(record);
		ret.setData(record.getCid());
		return ret;
	}
	
	@Override
	@LogService(title="lookup.deleteVirtual",author="cjh",calls="updateByPrimaryKeySelective", descs="cid=#{1}")
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid)throws RetException{
		logger.info("----deleteVirtual--");
	
		RetResult<Integer> ret = new RetResult<>();
		if(cid==null||cid==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+cid+" is empty", null);
			return ret;
		}
		LookupPO lookup = new LookupPO();
		lookup.setCid(cid);
		lookup.setModifyUser(operUserId);
		int v=0;
		try {
			v = this.deleteVirtual(lookup);
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "cid="+cid+",error="+e.getMessage(), e);
			throw new RetException(ret);
		}
		ret.setData(v);
		return ret;
	}

}
