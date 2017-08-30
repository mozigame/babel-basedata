package com.babel.basedata.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.babel.basedata.logger.LogService;
import com.babel.basedata.mapper.RetryRuleDetailMapper;
import com.babel.basedata.model.RetryRuleDetailPO;
import com.babel.basedata.model.RetryRulePO;
import com.babel.basedata.service.IRetryRuleDetailService;
import com.babel.basedata.service.IRetryRuleService;
import com.babel.basedata.util.RetryRuleUtils;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.ISelfInject;
import com.babel.common.core.service.impl.BaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

@Service("retryRuleDetailService")
public class RetryRuleDetailServiceImpl extends BaseService<RetryRuleDetailPO> implements IRetryRuleDetailService, ISelfInject{
	 private static final Log logger = LogFactory.getLog(RetryRuleDetailServiceImpl.class);
	 @Autowired
	 private RetryRuleDetailMapper mapper;
	 @Autowired
	 private IRetryRuleService retryRuleService;
	 
	 private IRetryRuleDetailService _self;
	 public void setSelf(Object proxyBean) { //通过InjectBeanSelfProcessor注入自己（目标对象）的AOP代理对象  
	        this._self = (IRetryRuleDetailService) proxyBean;  
	    }  
	 
	@Override
	public RetryRuleDetailMapper getMapper() {
		return mapper;
	}
	 

	 public List<RetryRuleDetailPO> selectByRetryRuleDetail(RetryRuleDetailPO item, int page, int rows){
		 Example example = new Example(RetryRuleDetailPO.class);
	        
       example.selectProperties("color","code","name","orderCount","retryRuleId","cid");
       example.setOrderByClause("retryRuleId,order_count");
       Example.Criteria criteria = example.createCriteria();
       criteria.andEqualTo("ifDel", 0);
       criteria.andEqualTo("status", 1);
       if (item.getCid() != null) {
           criteria.andEqualTo("cid", item.getCid());
       }
       if (item.getRetryRuleId() != null) {
           criteria.andEqualTo("retryRuleId", item.getRetryRuleId());
       }
       //分页查询
       PageHelper.startPage(page, rows);
       return selectByExample(example); 
	 }
	@Override
	public RetResult<RetryRuleDetailPO> findRetryRuleDetailById(Long id) {
		RetResult<RetryRuleDetailPO> ret = new RetResult<>();
		if(id==null||id.longValue()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "invalid id:"+id, null);
			return ret;
		}
		ret.setData(mapper.selectByPrimaryKey(id));
		return ret;
	}
	
	public List<RetryRuleDetailPO> findRetryRuleDetailByRetryRuleId(Long retryRuleId) {
		logger.info("----findRetryRuleDetailByRetryRuleId--retryRuleId="+retryRuleId);
		Example example = new Example(RetryRuleDetailPO.class);
		Example.Criteria criteria = example.createCriteria();
		example.selectProperties("limitIp","limitUser","code","name","paramJson","tipsInfo","maxCount","period","retryType","retryRuleId","cid");
		criteria.andEqualTo("ifDel", 0);
		criteria.andEqualTo("status", 1);
		criteria.andEqualTo("retryRuleId", retryRuleId);
		example.setOrderByClause("period asc");
		return mapper.selectByExample(example);
	}
	
	@CacheEvict(value = "basedata", key = "'getRetryRuleDetailByCode'+#ruleCode" )
//	@CacheEvict("retryRuleDetails")
	public void findRetryRuleDetailByRuleCode_flushCache(String ruleCode){
		logger.info("----flushCache--ruleCode="+ruleCode);
	}
	

	
//	@Cacheable(value = "basedata", key = "'getRetryRuleDetailByCode'+#ruleCode" )
//	@Cacheable("retryRuleDetails")
	public RetResult<RetryRuleDetailPO> findRetryRuleDetailByRuleCode(String ruleCode) {
		logger.info("----findRetryRuleDetailByRetryRuleCode--ruleCode="+ruleCode);
		RetResult<RetryRuleDetailPO> ret = new RetResult<>();
		
		if(ruleCode==null){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "invalid ruleCode:"+ruleCode, null);
			return ret;
		}
		else if(ruleCode.length()>200){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "ruleCode:"+ruleCode+" out of length:"+200, null);
			return ret;
		}
		
		RetryRulePO retryRule=this.retryRuleService.findRetryRuleByCode(ruleCode);
		if(retryRule!=null){
			ret.setDataList(this.findRetryRuleDetailByRetryRuleId(retryRule.getCid()));
		}
		
		
//		String[] codes=ruleCode.split(",");
//		List<String> codeList=new ArrayList<String>();
//		for(String code:codes){
//			codeList.add(code.trim());
//		}
		
//		try {
//			List<RetryRuleDetailPO> list= this.getMapper().findRetryRuleDetailListByCode(language, codeList);
//			ret.setDataList(list);
//		} catch (Exception e) {
//			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "ruleCode:"+ruleCode, e);
//			return ret;
//		}
		return ret;
	}
	
	private void findRetryRuleDetailByRetryRuleCode_flushCache(Long retryRuleId){
//		RetryRulePO retryRule=this.retryRuleService.findRetryRuleById(retryRuleId);
//		if(retryRule!=null){
//			this._self.findRetryRuleDetailByRetryRuleCode_flushCache(retryRule.getCode());
//		}
	}
	
	@Override
	public PageVO<RetryRuleDetailPO> findPageByRetryRuleDetail(RetryRuleDetailPO retryRuleDetail, PageVO<RetryRuleDetailPO> page) {
		logger.info("----findPageByRetryRuleDetail--retryRuleId="+retryRuleDetail.getRetryRuleId());
		Example example = new Example(RetryRuleDetailPO.class);
//		  example.selectProperties("nameCn","nameEn","code","cid");
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		if (StringUtil.isNotEmpty(retryRuleDetail.getName())) {
            criteria.andLike("name", "%" + retryRuleDetail.getName() + "%");
        }
        if (StringUtil.isNotEmpty(retryRuleDetail.getCode())) {
            criteria.andLike("code", "%" + retryRuleDetail.getCode() + "%");
        }
        if (retryRuleDetail.getCid() != null) {
            criteria.andEqualTo("cid", retryRuleDetail.getCid());
        }
        if (retryRuleDetail.getRetryRuleId() != null) {
            criteria.andEqualTo("retryRuleId", retryRuleDetail.getRetryRuleId());
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
		List<RetryRuleDetailPO> list = selectByExample(example);
		PageInfo<RetryRuleDetailPO> pageInfo = new PageInfo<RetryRuleDetailPO>(list);
		PageVO<RetryRuleDetailPO> pageRet = new PageVO<RetryRuleDetailPO>(pageInfo);
		return pageRet;
	}
	
	@LogService(title="retryRuleDetail.create",author="cjh",calls="insert", descs="retryRuleId=#{0.retryRuleId}")
	public RetResult<Long> create(RetryRuleDetailPO record){
		logger.info("----create--");
		RetResult<Long> ret = new RetResult<>();
		if(StringUtils.isEmpty(record.getCode())||StringUtils.isEmpty(record.getName())){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "code="+record.getCode()+" or name="+record.getName()+" is empty", null);
			logger.warn("create="+ret.getMsgBody());
			return ret;
		}
		record.initCreate();
		this.getMapper().insert(record);
//		this.findRetryRuleDetailByRetryRuleCode_flushCache(record.getLanguage(), record.getRetryRuleId());
		ret.setData(record.getCid());
		return ret;
	}
	
	@LogService(title="retryRuleDetail.update",author="cjh",calls="updateByPrimaryKeySelective", descs="retryRuleId=#{0.retryRuleId}")
	public RetResult<Long> update(RetryRuleDetailPO record){
		logger.info("----update--");
		RetResult<Long> ret = new RetResult<>();
		if(record.getCid()==null||record.getCid().longValue()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+record.getCid()+" is empty", null);
			return ret;
		}
		record.initUpdate();
		this.mapper.updateByPrimaryKeySelective(record);
//		this.findRetryRuleDetailByRetryRuleCode_flushCache(record.getLanguage(), record.getRetryRuleId());
		ret.setData(record.getCid());
		refreshCache(record.getCid());
		
		return ret;
	}
	
	@LogService(title="retryRuleDetail.deleteVirtual",author="cjh",calls="updateByPrimaryKeySelective")
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid){
		logger.info("----deleteVirtual--");
		RetResult<Integer> ret = new RetResult<>();
		if(cid==null||cid==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+cid+" is empty", null);
			return ret;
		}
		RetryRuleDetailPO retryRuleDetail = new RetryRuleDetailPO();
		retryRuleDetail.setCid(cid);
		retryRuleDetail.setModifyUser(operUserId);
		int v=0;
		try {
			v = this.deleteVirtual(retryRuleDetail);
			refreshCache(cid);
//			this.findRetryRuleDetailByRetryRuleCode_flushCache(retryRuleDetail.getLanguage(), retryRuleDetail.getRetryRuleId());
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "cid="+cid+",error="+e.getMessage(), e);
		}
		ret.setData(v);
		return ret;
	}
	
	public void refreshCache(Long retryRuleId){
		RetryRulePO rule=this.retryRuleService.findRetryRuleById(retryRuleId);
		if(rule!=null){
			refreshCache(retryRuleId, rule.getCode());
			RetryRuleUtils.putRetryRule(rule.getCode(), rule);
		}
	}
	
	public void refreshCache(Long retryRuleId, String retryCode){
		List<RetryRuleDetailPO> list=this.findRetryRuleDetailByRetryRuleId(retryRuleId);
		RetryRuleUtils.putRetryRuleList(retryCode, list);
		RetryRuleUtils.putRetryRule(retryCode, this.retryRuleService.findRetryRuleById(retryRuleId));
	}

}
