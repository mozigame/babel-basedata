package com.babel.basedata.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.data.RetResult.msg_codes;
import com.babel.common.core.entity.AccessToken;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.impl.BaseService;
import com.babel.common.core.tools.WeixinUtil;
import com.babel.common.core.util.ConfigUtils;
import com.babel.basedata.logger.LogService;
import com.babel.basedata.mapper.TemplateMapper;
import com.babel.basedata.model.LogMsgPO;
import com.babel.basedata.model.LookupItemPO;
import com.babel.basedata.model.TemplatePO;
import com.babel.basedata.service.ILogMsgService;
import com.babel.basedata.service.ILookupItemService;
import com.babel.basedata.service.ITemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

@Service("templateService")
public class TemplateServiceImpl extends BaseService<TemplatePO> implements ITemplateService{
	private static final Log logger = LogFactory.getLog(TemplateServiceImpl.class);
	
	@Autowired
	private TemplateMapper templateMapper;
	
	@Autowired
    private ILookupItemService lookupItemService;
	
	@Autowired
	private ILogMsgService logMsgService;
    
	
	 @Override
		public TemplateMapper getMapper() {
			return templateMapper;
		}
	
	@Override
	public TemplatePO findTemplateById(Long id) {
		logger.info("----TemplatePO findTemplateById----id = "+id);
		//SqlHelper.addIgnore(TemplatePO.class, "name");
		return templateMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public List<TemplatePO> findTemplateByIds(List<Long> ids) {
		logger.info("----findTemplateByIds----");
		Example example = new Example(TemplatePO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("cid", ids);
		return templateMapper.selectByExample(example);
	}
	
	public TemplatePO findTemplateByTplId(String sysType, String tplId) {
		logger.info("----findTemplateByTplId--sysType="+sysType+" tplId="+tplId);
		Example example = new Example(TemplatePO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("sysType", sysType);
		criteria.andEqualTo("tplId", tplId);
		criteria.andEqualTo("ifDel", 0);
		criteria.andEqualTo("status", 1);
		List<TemplatePO> list=templateMapper.selectByExample(example);
		if(list.size()>0){
			if(list.size()>1){
				logger.warn("-----findTemplateByTplId--tplId="+tplId+" list size="+list.size());
			}
			return list.get(0);
		}
		return null;
	}
	
	public TemplatePO findTemplateByCode(String sysType, String code) {
		logger.info("----findTemplateByCode--sysType="+sysType+" code="+code);
		Example example = new Example(TemplatePO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("sysType", sysType);
		criteria.andEqualTo("code", code);
		criteria.andEqualTo("ifDel", 0);
		criteria.andEqualTo("status", 1);
		List<TemplatePO> list=templateMapper.selectByExample(example);
		if(list.size()>0){
			if(list.size()>1){
				logger.warn("-----findTemplateByTplId--code="+code+" list size="+list.size());
			}
			return list.get(0);
		}
		return null;
	}
	
	@Override
    public List<TemplatePO> selectByTemplate(TemplatePO template) {
		logger.info("----selectByTemplate----template = "+template);
		if(template == null) {
			template = new TemplatePO();
		}
        return templateMapper.select(template);
    }
	
	public PageVO<TemplatePO> findPageByTemplate(TemplatePO template, PageVO<TemplatePO> page) {
		logger.info("----findPageByTemplate----template = "+template);
		Example example = new Example(TemplatePO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
  		if (!StringUtils.isEmpty(template.getSysType())) {
			criteria.andEqualTo("sysType", template.getSysType());
		}
  		if (template.getMsgType() != null) {
			criteria.andEqualTo("msgType", template.getMsgType());
		}
  		if (template.getCode() != null) {
			criteria.andEqualTo("code", template.getCode());
		}
  		if (template.getTplId() != null) {
			criteria.andEqualTo("tplId", template.getTplId());
		}
  		if (template.getStatus() != null) {
			criteria.andEqualTo("status", template.getStatus());
		}
		String orderClause = page.getOrderClause();
		if(StringUtil.isNotEmpty(orderClause)) {
			example.setOrderByClause(orderClause);
		}
		return selectPageByExample(example, page);
	}
	
//	  /**
//     * 根据系统类型查数据字典中配置的appid
//     * @param sysType
//     * @return
//     */
//	private LookupItemPO getSysItemByLookupCode(String sysType) {
//		LookupItemPO appItem=null;
//		if(StringUtils.isEmpty(sysType)){
//			return appItem;
//		}
//		String lookupCode="tf_sys_type";
//    	String language="CN";
//    	RetResult<LookupItemPO> itemRet=lookupItemService.findLookupItemByLookupCode(language, lookupCode);
//    	Collection<LookupItemPO> itemList=itemRet.getDataList();
//    	
//    	for(LookupItemPO item:itemList){
//    		if(sysType.equals(item.getItemCode())){
//    			appItem=item;
//    			break;
//    		}
//    	}
//    	return appItem;
//	}
	
	/**
	 * 按模板发送数据
	 * 发送的数据可以为空：
	 * 	a,为空时自动取tplExample的样例数据
	 *  b,不为空时仍会取tplExample数据，但会用paramMap去覆盖tplExample的数据
	 * @param tplId
	 * @param tos
	 * @param paramMap
	 * @return
	 */
	public RetResult<Long> sendTplMsg(String sysType, String code, String tos, LogMsgPO logMsg, Map<String, String> paramMap){
		RetResult<Long> ret = new RetResult<Long>();
		TemplatePO templatePO=this.findTemplateByCode(sysType, code);
		if(templatePO==null){
			return ret.initError(msg_codes.ERR_DATA_UNEXISTED, "code="+code+" not found", null);
		}
		ret= this.sendMsg(templatePO,tos, logMsg, paramMap);
		if(!ret.isSuccess()){
			logger.warn("-----sendTplMsg--code="+code+" msgCode="+ret.getMsgCode()+" msgBody="+ret.getMsgBody());
		}
		return ret;
	}
	
	/**
	 * 从微信公从号导入所有模板
	 */
	public RetResult<String> importWxTemplate(String sysType,Long operUserId){
		RetResult<String> retResult=new RetResult<String>();
		String url="https://api.weixin.qq.com/cgi-bin/template/get_all_private_template";
    	logger.info("-----importWxTemplate--sysType="+sysType);
    	String appId=this.lookupItemService.getSysAppIdByLookupCode(sysType);
    	
    	try {
    		RetResult<String> appsecRet = this.lookupItemService.getAppSecretByConfig(appId);
    		if(!appsecRet.isSuccess()){
    			return appsecRet;
    		}
    		String appsecret=appsecRet.getFirstData();
        	
			AccessToken accessToken=WeixinUtil.getAccessToken(appId, appsecret);
			if(accessToken==null){
				return retResult.initError(msg_codes.ERR_DATA_INVALID, "sysType="+sysType+" appId="+appId+" token未找到", null);
			}
			String data=WeixinUtil.wxApiGet(url, accessToken.getAccessToken());
			Map<String, List> bodyMap=gson.fromJson(data, new TypeToken<Map<String, List>>() {}.getType());
			List<Map<String, String>> tmpList=(List<Map<String, String>>)bodyMap.get("template_list");
			
			TemplatePO search = new TemplatePO();
			search.setIfDel(0);
			search.setSysType(sysType);
			List<TemplatePO> tplList=this.selectByTemplate(search);
			TemplatePO templatePO=null;
			boolean isExist=false;
			for(Map<String, String> tmp:tmpList){
				templatePO=getTplByData(tmp);
				templatePO.setSysType(sysType);
				isExist=false;
				for(TemplatePO tpl:tplList){
					if(templatePO.getTplId().equals(tpl.getTplId())){
						isExist=true;
						break;
					}
				}
				if(!isExist){
					templatePO.setCreateUser(operUserId);
					templatePO.setModifyUser(operUserId);
					this.create(templatePO);
				}
			}
		}  catch (Exception e) {
			return retResult.initError(msg_codes.ERR_UNKNOWN, "import", e);
		}
    	return retResult;
	}

	private TemplatePO getTplByData(Map<String, String> tmp) {
		TemplatePO templatePO;
		templatePO=new TemplatePO();
		templatePO.setTplId(tmp.get("template_id"));
		templatePO.setTitle(tmp.get("title"));
		templatePO.setPrimaryIndustry(tmp.get("primary_industry"));
		templatePO.setDeputyIndustry(tmp.get("deputy_industry"));
		templatePO.setTplData(tmp.get("content"));
		templatePO.setExample(tmp.get("example"));
		templatePO.setMsgType(5);//微信
		
		templatePO.initCreate();
		return templatePO;
	}
	
	

	private Gson gson = new Gson();
	@LogService(title="TemplateServiceImpl.sendMsg",author="Jinhe.chen")
	public RetResult<Long> sendMsg(TemplatePO template,  String tos, LogMsgPO logMsg, Map<String, String> paramMap){
		logger.info("----create----sendMsg = "+template+" tos="+tos);
		RetResult<Long> ret = new RetResult<Long>();
		if(StringUtils.isEmpty(template.getSysType())){
			return ret.initError(msg_codes.ERR_DATA_INPUT, "sysType empty", null);
		}
		if(StringUtils.isEmpty(template.getTplExample())){
			return ret.initError(msg_codes.ERR_DATA_INPUT, "tmpExample empty", null);
		}
		
		if(StringUtils.isEmpty(tos)){
			return ret.initError(msg_codes.ERR_DATA_INPUT, "tos empty", null);
		}
		
		String lookupCode="tf_sys_type";
    	String language="CN";
    	String dataId=template.getSysType();
		RetResult<LookupItemPO> itemRet=lookupItemService.findLookupItemByDataId(language, lookupCode, dataId);
		if(!itemRet.isSuccess()){
			return new RetResult<>(itemRet);
		}
		
		LookupItemPO sysItem=itemRet.getFirstData();
		if(sysItem==null){
			return ret.initError(msg_codes.ERR_DATA_INPUT, "sysType not found on lookupItem", null);
		}
		
		Map<String, String> dataMap=new HashMap<>();
		try {
			Map<String, String> exampleMap = gson.fromJson( template.getTplExample(), new TypeToken<Map<String, String>>() { }.getType());
			dataMap.putAll(exampleMap);
//			logger.info("-----paramMap="+paramMap);
		} catch (JsonSyntaxException e) {
			return ret.initError(msg_codes.ERR_DATA_INVALID, "tplExample data format", e);
		}
		
		if(paramMap!=null){
			dataMap.putAll(paramMap);
		}
		
		Integer msgType=template.getMsgType();
		if(msgType==null){
			msgType=5;//微信
		}
		String sysPrefix=sysItem.getAttr1();
		String appid=sysItem.getAttr2();
		String homeUrl=sysItem.getAttr3();
		
		String url=template.getUrl();
		if(!StringUtils.isEmpty(url) && !url.startsWith("http")){
			url=homeUrl+url;
		}
		
//		LogMsgPO logMsg=new LogMsgPO();
		if(logMsg==null){
			logMsg=new LogMsgPO();
		}
		logMsg.setMsgCode(template.getMsgCode());
		logMsg.setTemplate(template.getTplId());
		logMsg.setMsgType(msgType);//微信
		logMsg.setTos(tos);
		if(logMsg.getTitle()==null){
			logMsg.setTitle(template.getName());
		}
    	String tplMsg = WeixinUtil.getTplMsg(null, logMsg.getTemplate(), url, dataMap);
//    	logger.info("-----tplMsg="+tplMsg);
    	logMsg.setContent(tplMsg);
    	this.logMsgService.create(logMsg);
    	ret.setData(logMsg.getCid());

		return ret;
	}
	
	@LogService(title="TemplateServiceImpl.create",author="Jinhe.chen",calls="insert")
	public RetResult<Long> create(TemplatePO template){
		logger.info("----create----template = "+template);
		RetResult<Long> ret = new RetResult<Long>();
		save(template);
		ret.setData(template.getCid());
		return ret;
	}
	
	@LogService(title="TemplateServiceImpl.update",author="Jinhe.chen",calls="updateByPrimaryKey",descs="cid=#{0.cid}")
	public RetResult<Long> update(TemplatePO template){
		logger.info("----update----template = "+template);
		RetResult<Long> ret = new RetResult<Long>();
		if(template.getCid()==null || template.getCid().longValue()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid = "+template.getCid()+" is empty", null);
			return ret;
		}
		templateMapper.updateByPrimaryKey(template);
		ret.setData(template.getCid());
		return ret;
	}
	
	@LogService(title="TemplateServiceImpl.update",author="Jinhe.chen",calls="deleteVirtual",descs="cid=#{0.cid}")
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid) throws RetException{
		logger.info("----deleteVirtual----cid = "+cid);
		RetResult<Integer> ret = new RetResult<Integer>();
		if(cid == null || cid == 0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid = "+cid+" is empty", null);
			return ret;
		}
		TemplatePO template = new TemplatePO();
		template.setModifyUser(operUserId);
		template.setCid(cid);
		int v = 0;
		try {
			v = this.deleteVirtual(template);
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "cid = "+cid+",error = "+e.getMessage(), e);
			logger.error("-----deleteVirtual----", e);
			throw new RetException(ret);
		}
		ret.setData(v);
		return ret;
	}

}
