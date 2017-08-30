package com.babel.basedata.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.data.RetResult.msg_codes;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.ISelfInject;
import com.babel.common.core.service.impl.BaseService;
import com.babel.common.core.util.ConfigUtils;
import com.babel.basedata.logger.LogService;
import com.babel.basedata.mapper.LookupItemMapper;
import com.babel.basedata.model.LookupItemPO;
import com.babel.basedata.model.LookupPO;
import com.babel.basedata.service.ILookupItemService;
import com.babel.basedata.service.ILookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

@Service("lookupItemService")
public class LookupItemServiceImpl extends BaseService<LookupItemPO> implements ILookupItemService, ISelfInject{
	 private static final Log logger = LogFactory.getLog(LookupItemServiceImpl.class);
	 @Autowired
	 private LookupItemMapper mapper;
	 
	 @Autowired
	 private ILookupService lookupService;
	 
	 private ILookupItemService _self;
	 public void setSelf(Object proxyBean) { //通过InjectBeanSelfProcessor注入自己（目标对象）的AOP代理对象  
	        this._self = (ILookupItemService) proxyBean;  
	    }  
	 
	@Override
	public LookupItemMapper getMapper() {
		return mapper;
	}

	 public List<LookupItemPO> selectByLookupItem(LookupItemPO item, int page, int rows){
		 Example example = new Example(LookupItemPO.class);
	        
       example.selectProperties("color","itemCode","itemName","orderCount","itemValue","lookupId","attr1","attr2","cid");
       example.setOrderByClause("lookup_Id,order_count");
       Example.Criteria criteria = example.createCriteria();
       criteria.andEqualTo("ifDel", 0);
       criteria.andEqualTo("status", 1);
       if (item.getCid() != null) {
           criteria.andEqualTo("cid", item.getCid());
       }
       if (item.getLanguage() != null) {
           criteria.andEqualTo("language", item.getLanguage());
       }
       if (item.getLookupId() != null) {
           criteria.andEqualTo("lookupId", item.getLookupId());
       }
       //分页查询
       PageHelper.startPage(page, rows);
       return selectByExample(example); 
	 }
	@Override
	public RetResult<LookupItemPO> findLookupItemById(Long id) {
		RetResult<LookupItemPO> ret = new RetResult<>();
		if(id==null||id.longValue()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "invalid id:"+id, null);
			return ret;
		}
		ret.setData(mapper.selectByPrimaryKey(id));
		return ret;
	}
	
	public List<LookupItemPO> findLookupItemByLookupId(Long lookupId) {
		logger.info("----findLookupItemByLookupId--lookupId="+lookupId);
		
		Example example = new Example(LookupItemPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("lookupId", lookupId);
		return mapper.selectByExample(example);
	}
	
	@CacheEvict(value = "basedata", key = "'getLookupItemByCode'+#language+'--'+#lookupCode" )
//	@CacheEvict("lookupItems")
	public void findLookupItemByLookupCode_flushCache(String language, String lookupCode){
		logger.info("----flushCache--language="+language+", lookupCode="+lookupCode);
	}
	
	@Cacheable(value = "basedata", key = "'getLookupItemByCode'+#language+'--'+#lookupCode" )
//	@Cacheable("lookupItems")
	public RetResult<LookupItemPO> findLookupItemByLookupCode(String language, String lookupCode) {
		logger.info("----findLookupItemByLookupCode--language="+language+", lookupCode="+lookupCode);
		RetResult<LookupItemPO> ret = new RetResult<>();
		
		
		if(language==null){
			language="CN";
		}

		if(lookupCode==null){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "invalid lookupCode:"+lookupCode, null);
			return ret;
		}
		else if(lookupCode.length()>200){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "lookupCode:"+lookupCode+" out of length:"+200, null);
			return ret;
		}
		
		String[] codes=lookupCode.split(",");
		List<String> codeList=new ArrayList<String>();
		for(String code:codes){
			codeList.add(code.trim());
		}
		
		try {
			List<LookupItemPO> list= this.getMapper().findLookupItemListByCode(language, codeList);
			ret.setDataList(list);
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "lookupCode:"+lookupCode, e);
			return ret;
		}
		return ret;
	}
	
	public RetResult<LookupItemPO> findLookupItemByDataId(String language, String lookupCode, String dataId) {
		if(StringUtils.isEmpty(language)){
			language="CN";
		}
		RetResult<LookupItemPO> itemRet=new RetResult<>();
		if(StringUtils.isEmpty(lookupCode)){
			return itemRet.initError(msg_codes.ERR_DATA_INPUT, "lookupCode is empty", null);
		}
		if(StringUtils.isEmpty(dataId)){
			return itemRet.initError(msg_codes.ERR_DATA_INPUT, "dataId is empty", null);
		}
		RetResult<LookupItemPO> itemsRet=this.findLookupItemByLookupCode(language, lookupCode);
		if(!itemsRet.isSuccess()){
			logger.warn("findLookupItemByDataId--language="+language+" lookupCode="+lookupCode+" dataId="+dataId);
			return itemsRet;
		}
    	Collection<LookupItemPO> itemList=itemsRet.getDataList();
    	LookupItemPO lookupItemPO=null;
    	for(LookupItemPO item:itemList){
    		if(dataId.equals(item.getItemCode())){
    			lookupItemPO=item;
    			break;
    		}
    	}
    	itemRet.setData(lookupItemPO);
    	return itemRet;
	}
	
	private void findLookupItemByLookupCode_flushCache(String language, Long lookupId){
		LookupPO lookup=this.lookupService.findLookupById(lookupId);
		if(lookup!=null){
			this._self.findLookupItemByLookupCode_flushCache(language, lookup.getCode());
		}
	}
	
	/**
     * 根据系统类型查数据字典中配置的appid
     * @param sysType
     * @return
     */
	public String getSysAppIdByLookupCode(String sysType) {
		String appId=null;
		if(StringUtils.isEmpty(sysType)){
			return appId;
		}
		String lookupCode="tf_sys_type";
    	String language="CN";
    	RetResult<LookupItemPO> itemRet=this.findLookupItemByDataId(language, lookupCode, sysType);
    	if(itemRet.isSuccess() && itemRet.getSize()>0){
    		appId= itemRet.getFirstData().getAttr2();
    	}
    	else{
    		logger.warn("----getSysAppIdByLookupCode--sysType="+sysType+" msgCode="+itemRet.getMsgCode()+" msgBody="+itemRet.getMsgBody());
    	}
    	return appId;
	}
	
	public RetResult<String> getAppSecretByConfig(String appId) {
		RetResult<String> retResult=new RetResult<>();
		String mchAppid=ConfigUtils.getConfigValue("mch_appId");
		String mchLhAppid=ConfigUtils.getConfigValue("mch_lh_appId");
		logger.info("-----getAppSecret--appId="+appId+" mchAppid="+mchAppid+" mchLhAppid="+mchLhAppid);
		String appsecret=null;
		if(appId.equals(mchAppid)){
			appsecret=ConfigUtils.getConfigValue("mch_appSecret");
		}
		else if(appId.equals(mchLhAppid)){
			appsecret=ConfigUtils.getConfigValue("mch_lh_appSecret");
		}
		else{
			return retResult.initError(msg_codes.ERR_DATA_INVALID, "appId="+appId+"无效", null);
		}
		
		if(appsecret==null){
			return retResult.initError(msg_codes.ERR_DATA_NOT_FOUND, "appId="+appId+"未找到对应的appSecret", null);
		}
		retResult.setData(appsecret);
		return retResult;
	}
	
	@Override
	public PageVO<LookupItemPO> findPageByLookupItem(LookupItemPO lookupItem, PageVO<LookupItemPO> page) {
		logger.info("----findPageByLookupItem--lookupId="+lookupItem.getLookupId());
		Example example = new Example(LookupItemPO.class);
//		  example.selectProperties("nameCn","nameEn","code","cid");
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		if (StringUtil.isNotEmpty(lookupItem.getItemName())) {
            criteria.andLike("itemName", "%" + lookupItem.getItemName() + "%");
        }
        if (StringUtil.isNotEmpty(lookupItem.getItemCode())) {
            criteria.andLike("itemCode", "%" + lookupItem.getItemCode() + "%");
        }
        if (lookupItem.getCid() != null) {
            criteria.andEqualTo("cid", lookupItem.getCid());
        }
        if (lookupItem.getLookupId() != null) {
            criteria.andEqualTo("lookupId", lookupItem.getLookupId());
        }
		
		// 分页查询
		PageHelper.startPage(page.getCurrentPage(), page.getPageSize());
		List<LookupItemPO> list = selectByExample(example);
		PageInfo<LookupItemPO> pageInfo = new PageInfo<LookupItemPO>(list);
		PageVO<LookupItemPO> pageRet = new PageVO<LookupItemPO>(pageInfo);
		return pageRet;
	}
	
	@LogService(title="lookupItem.create",author="cjh",calls="insert", descs="lookupId=#{0.lookupId}")
	public RetResult<Long> create(LookupItemPO record){
		logger.info("----create--");
		RetResult<Long> ret = new RetResult<>();
		if(StringUtils.isEmpty(record.getItemCode())||StringUtils.isEmpty(record.getItemName())){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "itemCode="+record.getItemCode()+" or itemName="+record.getItemName()+" is empty", null);
			logger.warn("create="+ret.getMsgBody());
			return ret;
		}
		record.initCreate();
		this.getMapper().insert(record);
		this.findLookupItemByLookupCode_flushCache(record.getLanguage(), record.getLookupId());
		ret.setData(record.getCid());
		return ret;
	}
	
	@LogService(title="lookupItem.update",author="cjh",calls="updateByPrimaryKeySelective", descs="lookupId=#{0.lookupId}")
	public RetResult<Long> update(LookupItemPO record){
		logger.info("----update--");
		RetResult<Long> ret = new RetResult<>();
		if(record.getCid()==null||record.getCid().longValue()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+record.getCid()+" is empty", null);
			return ret;
		}
		record.initUpdate();
		this.mapper.updateByPrimaryKeySelective(record);
		this.findLookupItemByLookupCode_flushCache(record.getLanguage(), record.getLookupId());
		ret.setData(record.getCid());
		
		return ret;
	}
	
	@LogService(title="lookupItem.deleteVirtual",author="cjh",calls="updateByPrimaryKeySelective")
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid){
		logger.info("----deleteVirtual--");
		RetResult<Integer> ret = new RetResult<>();
		if(cid==null||cid==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+cid+" is empty", null);
			return ret;
		}
		LookupItemPO lookupItem = new LookupItemPO();
		lookupItem.setCid(cid);
		lookupItem.setModifyUser(operUserId);
		int v=0;
		try {
			v = this.deleteVirtual(lookupItem);
			this.findLookupItemByLookupCode_flushCache(lookupItem.getLanguage(), lookupItem.getLookupId());
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "cid="+cid+",error="+e.getMessage(), e);
		}
		ret.setData(v);
		return ret;
	}

}
