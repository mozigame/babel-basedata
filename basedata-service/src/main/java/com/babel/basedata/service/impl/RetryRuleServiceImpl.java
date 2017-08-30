package com.babel.basedata.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.babel.basedata.logger.LogService;
import com.babel.basedata.mapper.RetryRuleMapper;
import com.babel.basedata.model.RetryRulePO;
import com.babel.basedata.service.IRetryRuleService;
import com.babel.basedata.util.RetryRuleUtils;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.impl.BaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;

@Service("retryRuleService")
public class RetryRuleServiceImpl extends BaseService<RetryRulePO> implements IRetryRuleService{
	 private static final Log logger = LogFactory.getLog(RetryRuleServiceImpl.class);
	 //@Autowired
	 //private ILogDbService logDbService;
	
	@Autowired
	private RetryRuleMapper retryRuleMapper;
	
	 @Override
	public RetryRuleMapper getMapper() {
		return retryRuleMapper;
	}
	
	@Override
	public RetryRulePO findRetryRuleById(Long id) {
		logger.info("----findRetryRuleById--id="+id);
		return retryRuleMapper.selectByPrimaryKey(id);
	}
	
	public List<RetryRulePO> findRetryRuleByIds(List<Long> idList){
		Example example = new Example(RetryRulePO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		criteria.andEqualTo("status", 1);
		criteria.andIn("cid", idList);
		List<RetryRulePO> list=this.selectByExample(example);
		return list;
	}
	
	@Override
	public RetryRulePO findRetryRuleByCode(String code) {
		Example example = new Example(RetryRulePO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		criteria.andEqualTo("status", 1);
		criteria.andEqualTo("code", code);
		List<RetryRulePO> list=this.selectByExample(example);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public List<RetryRulePO> findRetryRuleAll() {
		Example example = new Example(RetryRulePO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		criteria.andEqualTo("status", 1);
		List<RetryRulePO> list=this.selectByExample(example);
		return list;
	}
	
	@Override
    public List<RetryRulePO> selectByRetryRule(RetryRulePO retryRule, int page, int rows) {
		logger.info("----selectByRetryRule--retryRule="+retryRule);
		
        Example example = new Example(RetryRulePO.class);
        example.selectProperties("nameCn","nameEn","code","cid");
        /*Example.Criteria criteria = example.createCriteria();
        if (StringUtil.isNotEmpty(retryRule.getXxx())) {
            criteria.andLike("xxx", "%" + retryRule.getXxx() + "%");
        }*/

        //分页查询
        PageHelper.startPage(page, rows);
        return selectByExample(example);
    }
	
	public PageVO<RetryRulePO> findPageByRetryRule(RetryRulePO retryRule, PageVO<RetryRulePO> page) {
		logger.info("----findPageByRetryRule--");
		Example example = new Example(RetryRulePO.class);
		Example.Criteria criteria = example.createCriteria();
		
		
		criteria.andEqualTo("ifDel", 0);
        if (!StringUtils.isEmpty(retryRule.getName())) {
            criteria.andEqualTo("name", retryRule.getName());
        }
        if (!StringUtils.isEmpty(retryRule.getCode())) {
            criteria.andEqualTo("code", retryRule.getCode());
        }
   
        
        if (retryRule.getRuleType() != null) {
            criteria.andEqualTo("ruleType", retryRule.getRuleType());
        }

        //order default
        if(StringUtils.isEmpty(page.getSort())){
        	page.setSort("createDate");
        }
        if(StringUtils.isEmpty(page.getOrder())){
        	page.setOrder("desc");
        }
        example.setOrderByClause(page.getOrderClause());
		
		// 分页查询
		PageHelper.startPage(page.getCurrentPage(), page.getPageSize());
		List<RetryRulePO> list = selectByExample(example);
		PageInfo<RetryRulePO> pageInfo = new PageInfo<RetryRulePO>(list);
		PageVO<RetryRulePO> pageRet = new PageVO<RetryRulePO>(pageInfo);
		return pageRet;
	}
//	public PageVO<RetryRulePO> findPageByRetryRule(RetryRulePO search, PageVO<RetryRulePO> page) {
//		logger.info("----findRetryRuleListByPage--");
//
//		
//		List<RetryRulePO> list=null;
//		int totalSize=0;
//		try {
//			totalSize = retryRuleMapper.findRetryRuleListByPageCount(search);
//			list = retryRuleMapper.findRetryRuleListByPage(search, page);
//		} catch (Exception e) {
//			logger.error("-----findRetryRuleListByPage--", e);
//		}
//		
//		
//		PageVO<RetryRulePO> pageRet=new PageVO<RetryRulePO>(list, totalSize, page.getPageSize(), page.getCurrentPage());
//		return pageRet;
//		
//	}
	
	@LogService(title="retryRule.create",author="cjh",calls="insert", descs="cid=#{0.cid},code=#{0.code}")
	public RetResult<Long> create(RetryRulePO record){
		logger.info("----create--");
//		this.logDbService.info(record, "create", "start", 0l);
		RetResult<Long> ret = new RetResult<Long>();
//		if(StringUtils.isEmpty(record.getXxx())){
//			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "xxx is empty", null);
//			logger.warn("create="+ret.getMsgBody());
//			return ret;
//		}
		record.initCreate();
		retryRuleMapper.insert(record);
		ret.setData(record.getCid());
		return ret;
	}
	
	@LogService(title="retryRule.update",author="cjh",calls="updateByPrimaryKey", descs="cid=#{0.cid},code=#{0.code}")
	public RetResult<Long> update(RetryRulePO record){
		logger.info("----update--");
		RetResult<Long> ret = new RetResult<Long>();
		if(record.getCid()==null||record.getCid().longValue()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+record.getCid()+" is empty", null);
			return ret;
		}
		RetryRulePO rule=this.findRetryRuleById(record.getCid());
		if(!rule.getCode().equals(record.getCode())){
			ret.initError(RetResult.msg_codes.ERR_DATA_OPERATE, "cid="+record.getCid()+" rule code can not change", null);
			return ret;
		}
		record.initUpdate();
		record.setIfDel(0);
		retryRuleMapper.updateByPrimaryKey(record);
		ret.setData(record.getCid());
		return ret;
	}
	
	@LogService(title="retryRule.deleteVirtual",author="cjh",calls="updateByPrimaryKeySelective", descs="cid=#{1}")
	public RetResult<Integer> deleteVirtual(Long operId, Long cid) throws RetException{
		logger.info("----update--");
//		this.logDbService.info(null, "update", "start", 0l);
		RetResult<Integer> ret = new RetResult<Integer>();
		if(cid==null||cid==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+cid+" is empty", null);
			return ret;
		}
		RetryRulePO retryRule = new RetryRulePO();
		retryRule.setCid(cid);
		retryRule.setModifyUser(operId);
		int v=0;
		try {
			v = this.deleteVirtual(retryRule);
			RetryRulePO rule=this.findRetryRuleById(cid);
			RetryRuleUtils.removeFuncRetryList(rule.getCode());
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "cid="+cid+",error="+e.getMessage(), e);
			throw new RetException(ret);
		}
		ret.setData(v);
		return ret;
	}

}
