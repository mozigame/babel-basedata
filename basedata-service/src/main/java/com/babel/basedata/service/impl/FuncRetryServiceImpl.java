package com.babel.basedata.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.babel.basedata.mapper.FuncRetryMapper;
import com.babel.basedata.model.FuncRetryPO;
import com.babel.basedata.model.RetryRulePO;
import com.babel.basedata.service.IFuncRetryService;
import com.babel.basedata.service.IRetryRuleService;
import com.babel.basedata.util.RetryRuleUtils;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.impl.BaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

/**
 * 功能重试-功能与规则对应关系管理
 * @author cjh
 *
 */
@Service("funcRetryService")
public class FuncRetryServiceImpl extends BaseService<FuncRetryPO> implements IFuncRetryService{
	 private static final Log logger = LogFactory.getLog(FuncRetryServiceImpl.class);
	 //@Autowired
	 //private ILogDbService logDbService;
	
	@Autowired
	private FuncRetryMapper funcRetryMapper;
	
	@Autowired
	private IRetryRuleService retryRuleService;
	
	 @Override
		public FuncRetryMapper getMapper() {
			return funcRetryMapper;
		}
	
	@Override
	public FuncRetryPO findFuncRetryById(Long id) {
		logger.info("----findFuncRetryById--id="+id);
		return funcRetryMapper.selectByPrimaryKey(id);
	}
	
	public List<FuncRetryPO> findFuncRetryAll(){
		Example example = new Example(FuncRetryPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		criteria.andEqualTo("status", 1);
		List<FuncRetryPO> list=this.selectByExample(example);
		return list;
	}
	
	private void refreshCache(Long retryFuncId, Long retryRuleId){
		logger.info("------refreshCache--retryRuleId="+retryRuleId);
		RetryRulePO rule=this.retryRuleService.findRetryRuleById(retryRuleId);
		if(rule!=null){
			refreshCache(retryFuncId, rule.getCode());
		}
		else{
			logger.warn("------refreshCache--retryRuleId="+retryRuleId+" rule not found");
		}
	}
	
	/**
	 * 刷新缓存
	 */
	public void refreshCache(Long funcRetryId, String ruleCode){
		logger.info("------refreshCache--funcRetryId="+funcRetryId+" ruleCode="+ruleCode);
		FuncRetryPO funcRetry=this.findFuncRetryById(funcRetryId);
		if(funcRetry==null){
			logger.warn("------refreshCache--funcRetryId="+funcRetryId+" ruleCode="+ruleCode+" not found");	
			return;
		}
		
	
//		String paramDefine=funcRetry.getParamDefine();
		String paramJson=funcRetry.getParamJson();
		if(StringUtils.isEmpty(paramJson)){
			//do nothing
		}
		else if(paramJson.startsWith("{") && paramJson.endsWith("}")){
			try {
				Gson gson = new Gson();
				Map<String, String> paramMap = gson.fromJson(paramJson,  
				        new TypeToken<Map<String, String>>() {  
				        }.getType());  
//				RetryRuleUtils.funcRetryParamMap.put(funcRetry.getCid(), paramMap);
				RetryRuleUtils.putFuncRetryParam(funcRetry.getCid(), paramMap);
			} catch (JsonSyntaxException e) {
				logger.warn("--refreshCache--ruleCode="+ruleCode+" invalid paramJson:"+paramJson+" error:"+e.getMessage());
			}
		}
		else {
			logger.warn("--refreshCache--ruleCode="+ruleCode+" invalid json format for paramJson:"+paramJson);
		}
		
		
		RetryRulePO rule=this.retryRuleService.findRetryRuleById(funcRetry.getRetryRuleId());
		if(rule!=null){
			funcRetry.setRetryRule_code(rule.getCode());
			funcRetry.setRetryRule_disp(rule.getName());
		}
//		RetryRuleUtils.funcRetryMap.put(funcRetry.getCode(), funcRetry);
		RetryRuleUtils.putFuncRetry(funcRetry.getCode(), funcRetry);
	}
	
	@Override
    public List<FuncRetryPO> selectByFuncRetry(FuncRetryPO funcRetry, int page, int rows) {
		logger.info("----selectByFuncRetry--funcRetry="+funcRetry);
		
        Example example = new Example(FuncRetryPO.class);
        example.selectProperties("name","code","cid");
        /*Example.Criteria criteria = example.createCriteria();
        if (StringUtil.isNotEmpty(funcRetry.getXxx())) {
            criteria.andLike("xxx", "%" + funcRetry.getXxx() + "%");
        }*/

        //分页查询
        PageHelper.startPage(page, rows);
        return selectByExample(example);
    }
	
	public PageVO<FuncRetryPO> findPageByFuncRetry(FuncRetryPO funcRetry,
			PageVO<FuncRetryPO> page) {
		logger.info("----findFuncRetryListByPage--");

		Example example = new Example(FuncRetryPO.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtil.isNotEmpty(funcRetry.getCode())) {
			criteria.andLike("code", "%" + funcRetry.getCode() + "%");
		}
		if (StringUtil.isNotEmpty(funcRetry.getName())) {
			criteria.andLike("name", "%" + funcRetry.getName() + "%");
		}

		if (funcRetry.getCreateDate() != null) {
			criteria.andGreaterThanOrEqualTo("createDate",
					funcRetry.getCreateDate());
		}

		// order default
		if (StringUtils.isEmpty(page.getSort())) {
			page.setSort("createDate");
		}
		if (StringUtils.isEmpty(page.getOrder())) {
			page.setOrder("desc");
		}
		example.setOrderByClause(page.getOrderClause());

		// 分页查询
		PageHelper.startPage(page.getCurrentPage(), page.getPageSize());
		List<FuncRetryPO> list = selectByExample(example);
		this.initDispOfRetryRule(list);
		PageInfo<FuncRetryPO> pageInfo = new PageInfo<FuncRetryPO>(list);
		PageVO<FuncRetryPO> pageRet = new PageVO<FuncRetryPO>(pageInfo);
		return pageRet;

	}
	
	/**
	 * 根据modelId转换className,methodName
	 * @param list
	 */
	private void initDispOfRetryRule(List<FuncRetryPO> list) {
		List<Long> retryIdList=new ArrayList<>();
		for(FuncRetryPO log:list){
			if(log.getRetryRuleId()!=null)
				retryIdList.add(log.getRetryRuleId());
		}
		if(retryIdList.size()>0){
			List<RetryRulePO> mList=this.retryRuleService.findRetryRuleByIds(retryIdList);
			for(FuncRetryPO log:list){
				if(log.getRetryRuleId()!=null){
					for(RetryRulePO m:mList){
						if(m.getCid().longValue()==log.getRetryRuleId().longValue()){
							log.setRetryRule_disp(m.getName());
							log.setRetryRule_code(m.getCode());
							break;
						}
					}
				}
			}
		}
	}
	
//	@LogService(title="funcRetry.create",author="cjh",calls="insert", descs="cid=#{0.cid},code=#{0.code}")
	public RetResult<Long> create(FuncRetryPO record){
		logger.info("----create--");
//		this.logDbService.info(record, "create", "start", 0l);
		RetResult<Long> ret = new RetResult<Long>();
//		if(StringUtils.isEmpty(record.getXxx())){
//			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "xxx is empty", null);
//			logger.warn("create="+ret.getMsgBody());
//			return ret;
//		}
		record.initCreate();
		funcRetryMapper.insert(record);
		this.refreshCache(record.getCid(), record.getRetryRuleId());
		ret.setData(record.getCid());
		return ret;
	}
	
//	@LogService(title="funcRetry.update",author="cjh",calls="updateByPrimaryKey", descs="cid=#{0.cid},code=#{0.code}")
	public RetResult<Long> update(FuncRetryPO record){
		logger.info("----update--");
		RetResult<Long> ret = new RetResult<Long>();
		if(record.getCid()==null||record.getCid().longValue()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+record.getCid()+" is empty", null);
			return ret;
		}
		FuncRetryPO rule=this.findFuncRetryById(record.getCid());
		if(!rule.getCode().equals(record.getCode())){
			ret.initError(RetResult.msg_codes.ERR_DATA_OPERATE, "cid="+record.getCid()+" code can not change", null);
			return ret;
		}
		record.initUpdate();
		record.setIfDel(0);
		funcRetryMapper.updateByPrimaryKey(record);
		this.refreshCache(record.getCid(), record.getRetryRuleId());
		ret.setData(record.getCid());
		return ret;
	}
	
//	@LogService(title="funcRetry.deleteVirtual",author="cjh",calls="updateByPrimaryKeySelective", descs="cid=#{1}")
	public RetResult<Integer> deleteVirtual(Long operId, Long cid){
		logger.info("----update--");
//		this.logDbService.info(null, "update", "start", 0l);
		RetResult<Integer> ret = new RetResult<Integer>();
		if(cid==null||cid==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+cid+" is empty", null);
			return ret;
		}
		FuncRetryPO funcRetry = new FuncRetryPO();
		funcRetry.setCid(cid);
		funcRetry.setModifyUser(operId);
		int v=0;
		try {
			v = this.deleteVirtual(funcRetry);
//			RetryRuleUtils.funcRetryMap.remove(funcRetry.getCode());
			RetryRuleUtils.removeFuncRetry(funcRetry.getCode());
			RetryRuleUtils.removeFuncRetryParam(funcRetry.getCid());
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "cid="+cid+",error="+e.getMessage(), e);
		}
		ret.setData(v);
		return ret;
	}

}
