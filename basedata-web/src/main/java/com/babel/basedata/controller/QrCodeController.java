package com.babel.basedata.controller;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.data.RetResult.msg_codes;
import com.babel.common.core.entity.AccessToken;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.logger.LogAudit;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.tools.WeixinUtil;
import com.babel.common.core.util.ConfigUtils;
import com.babel.common.core.util.RedisUtil;
import com.babel.common.web.context.AppContext;
import com.babel.common.web.controller.WebBaseController;
import com.babel.basedata.model.LookupItemPO;
import com.babel.basedata.model.QrCodePO;
import com.babel.basedata.service.ILookupItemService;
import com.babel.basedata.service.IQrCodeService;
//import com.babel.ins.base.model.PartnerPO;
//import com.babel.ins.base.service.IPartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author liuzh_3nofxnp
 * @since 2015-09-19 17:15
 */
@Controller
@RequestMapping("/basedata/qrCode")
public class QrCodeController extends WebBaseController {
	private static final Log logger = LogFactory.getLog(QrCodeController.class);
	
	public static String API_WX_QR_CODE="https://api.weixin.qq.com/cgi-bin/qrcode/create";

    @Autowired
    private IQrCodeService qrCodeService;
    
    @Autowired
    private ILookupItemService lookupItemService;
    
//    @Autowired
//    private IPartnerService partnerService;
    
		
    @RequestMapping(value = {"index", "index.html", ""})
    public ModelAndView index(QrCodePO qrCode){
    	ModelAndView result = new ModelAndView("basedata/ey_qrCode");
    	return result;
    }
    
    @RequestMapping(value = {"list"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public PageVO<QrCodePO> findByPage(QrCodePO qrCode,
                                @RequestParam(required = false, defaultValue = "1") int page,
                                @RequestParam(required = false, defaultValue = "10") int rows) {
    	logger.info("-------findByPage-------");
    	PageVO<QrCodePO> pageVO = new PageVO<QrCodePO>(page, rows);
    	pageVO = qrCodeService.findPageByQrCode(qrCode, pageVO);

    	this.initDisp(pageVO.getRows());
//    	this.initDispForPartner(pageVO.getRows());
    	
    	return pageVO;
    }

//    /**
//     * 显示partnerName处理
//     * @param pageVO
//     * @param partnerIdList
//     */
//	private void initDispForPartner(Collection<QrCodePO> qrCodeList) {
//		if(qrCodeList==null){
//			return;
//		}
//		List<Long> partnerIdList = new ArrayList<Long>();
//    	try {
//    		partnerIdList = ObjectToMapUtil.getDataPropListByName(qrCodeList, "partnerId");
//		} catch (Exception e) {
//			logger.warn("------initDispForPartner--"+qrCodeList.size(), e);
//		}
//		if(!CollectionUtils.isEmpty(partnerIdList)){
//    		List<PartnerPO> partnerPOList = partnerService.findPartnerByIds(partnerIdList);
//	    	for (QrCodePO qrCodePO : qrCodeList) {
//	    		if(qrCodePO.getPartnerId()==null){
//	    			continue;
//	    		}
//	    		for (PartnerPO partnerPO : partnerPOList) {
//					if(qrCodePO.getPartnerId().longValue() == partnerPO.getCid().longValue()) {
//						qrCodePO.setPartnerName(partnerPO.getName());
//						break;
//					}
//				}
//			}
//    	}
//	}
	 


    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<QrCodePO> view(QrCodePO qrCode) {
    	RetResult<QrCodePO> ret = new RetResult<QrCodePO>();
        if (qrCode.getCid() != null) {
            qrCode = qrCodeService.selectByKey(qrCode.getCid());
        }
        ret.setData(qrCode);
        return ret;
    }
    

    /**
     * save or update
     * @param qrCode
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="save",  descs="sysType=#{0.cid}")
    public RetResult<Long> save(QrCodePO qrCode) {
//    	logger.info("------qrCode--cid="+qrCode.getCid()+" code="+qrCode.getCode()+" nameCn="+qrCode.getNameCn());
    	RetResult<Long> ret=new RetResult<Long>();
        if (qrCode.getCid() != null) {
        	this.initUpdate(qrCode);
            qrCodeService.updateNotNull(qrCode);
        } else {
        	this.initCreate(qrCode);
            qrCodeService.create(qrCode);
        }
        ret.setData(qrCode.getCid());
        return ret;
    }
    
    /**
     * 重载载入token
     * @param sysType
     * @return
     */
    @RequestMapping(value = "reloadWxToken", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="reloadWxToken",  descs="sysType=#{0}")
    public RetResult<AccessToken> reloadWxToken(@RequestParam("sysType")String sysType) {
    	RetResult<AccessToken> retResult=new RetResult<>();
    	if(StringUtils.isEmpty(sysType) || "-1".equals(sysType)){
    		return retResult.initError(msg_codes.ERR_DATA_INPUT, "sysType can not empty!", null);
    	}
    	String appId=this.lookupItemService.getSysAppIdByLookupCode(sysType);
    	if(StringUtils.isEmpty(appId)){
    		return retResult.initError(msg_codes.ERR_DATA_INPUT, "sysType="+sysType+" 未找到对应的appid", null);
    	}
    	
    	String mchAppid=ConfigUtils.getConfigValue("mch_appId");
    	logger.info("-----reloadWxToken1--sysType="+sysType+" appId="+appId+" mchAppId="+mchAppid);
    	String mchPasswd=null;
    	if(appId.equals(mchAppid)){
    		mchPasswd=ConfigUtils.getConfigValue("mch_appSecret");
    	}
    	
    	mchAppid=ConfigUtils.getConfigValue("mch_lh_appId");
    	logger.info("-----reloadWxToken2--sysType="+sysType+" appId="+appId+" mchAppId="+mchAppid);
    	if(appId.equals(mchAppid)){
    		mchPasswd=ConfigUtils.getConfigValue("mch_lh_appSecret");
    	}
    	
    	
    	if(mchPasswd!=null){
    		try {
    			AccessToken token = WeixinUtil.getAccessTokenFromCache(mchAppid, mchPasswd);
    			retResult.setData(token);
    			if(token!=null){
    				logger.info("----reloadCacheToken--"+gson.toJson(token));
    			}
    			else{
    				logger.error("----reloadCacheToken--fail");
    			}
    		} catch (Exception e) {
    			logger.error("------reloadCacheToken--mchLhAppid="+mchAppid, e);
    			retResult.initError(msg_codes.ERR_UNKNOWN, "reloadCacheToken-mchAppId="+mchAppid, e);
    		}
    	}
    	else{
    		return retResult.initError(msg_codes.ERR_DATA_NOT_FOUND, "sysType="+sysType+",appId="+appId+"未找到对应的appSecret", null);
    	}
    	
    	
    	return retResult;
    }
    
    private Gson gson = new Gson();
    @RequestMapping(value = "genQrCodeWx", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="genQrCodeWx",  descs="sysType=#{0.sysType}")
    public RetResult<Long> genQrCodeWx(QrCodePO qrCode) {
    	RetResult<Long> retResult=new RetResult<>();
    	String appId=this.lookupItemService.getSysAppIdByLookupCode(qrCode.getSysType());
    	if(StringUtils.isEmpty(appId)){
    		return retResult.initError(msg_codes.ERR_DATA_INPUT, "sysType="+qrCode.getSysType()+" 在lookupItem未找到对应的appid", null);
    	}
    	
    	if(qrCode.getAgingType()==null){
    		return retResult.initError(msg_codes.ERR_DATA_INPUT, "agingType is empty", null);
    	}
    	
    	qrCode.setQrType(1);//微信二维码生成
    	
    	String actionName="";
    	if(qrCode.getAgingType().intValue()==1){//长久
    		actionName="QR_LIMIT_SCENE";
    	}
    	else if(qrCode.getAgingType().intValue()==2){//临时
    		actionName="QR_SCENE";
    	}
    	else{
    		return retResult.initError(msg_codes.ERR_DATA_INPUT, "agingType="+qrCode.getAgingType()+" invalid", null);
    	}
    	

    	try {
    		/**
        	 * 从缓存中获取token
        	 */
    		AccessToken accessToken=(AccessToken)RedisUtil.getRedis(WeixinUtil.REDIS_KEY_TOKEN_MAP, appId);
    		if(accessToken==null){
    			return retResult.initError(msg_codes.ERR_DATA_NOT_FOUND, "appId="+appId+" accessToken not found!", null);
    		}
    		
    		String data="{\"action_name\": \""+actionName+"\",\"expire_seconds\": 2592000"
    				+ ", \"action_info\": {\"scene\": {\"scene_id\": "+qrCode.getPartnerId()+",\"scene_str\": \""+qrCode.getPartnerId()+"\"}}}";
			String body=WeixinUtil.wxApiPost(API_WX_QR_CODE, data, accessToken.getAccessToken());
			logger.info("-----genQrCodeWx--accessToken="+accessToken.getAccessToken()+"\n body="+body);
			Map<String, Object> bodyMap=gson.fromJson(body, new TypeToken<Map<Object, String>>() {}.getType());
			qrCode.setData(data);
			if(bodyMap.get("errcode")!=null){
				retResult.setMsgCode((String)bodyMap.get("errcode"));
				retResult.setMsgBody((String)bodyMap.get("errmsg"));
				retResult.setFlag(3);//3远程调用异常
				return retResult;
			}
			else{
				qrCode.setTicket((String)bodyMap.get("ticket"));
				qrCode.setUrl((String)bodyMap.get("url"));
			}
			this.initCreate(qrCode);
            qrCodeService.create(qrCode);
		} catch (Exception e) {
			logger.warn("生成二维码", e);
			return retResult.initError(msg_codes.ERR_UNKNOWN, "生成二维码", e);
			
		}
    	
    	return retResult;
    	
    }
    
    /**
     * 普通二维码生成
     * @return
     */
    @RequestMapping(value = "genQrCode", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="genQrCode",  descs="sysType=#{0.sysType}")
    public RetResult<Long> genQrCode(QrCodePO qrCode) {
    	RetResult<Long> retResult=new RetResult<>();
    	if(qrCode.getQrType()==null){
    		qrCode.setQrType(2);//普通二维码生成
    	}
    	
    	this.initCreate(qrCode);
        qrCodeService.create(qrCode);
    	
    	return retResult;
    }

    

    @RequestMapping(value = {"delete"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<Integer> delete(@RequestParam("id") Long id) {
        RetResult<Integer> ret = new RetResult<Integer>();
		try {
			Long operUserId=AppContext.getCurrentUserId();
			ret =this.qrCodeService.deleteVirtual(operUserId, id);
		} catch (RetException e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN , e.getMessage(), e);
		}
        return ret;
    }
    
}
