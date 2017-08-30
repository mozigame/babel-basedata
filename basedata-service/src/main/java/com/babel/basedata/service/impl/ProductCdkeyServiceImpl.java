package com.babel.basedata.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.data.RetResult.msg_codes;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.impl.BaseService;
import com.babel.common.core.tools.WeixinUtil;
import com.babel.common.core.util.DateUtils;
import com.babel.common.core.util.ObjectToMapUtil;
import com.babel.basedata.logger.LogService;
import com.babel.basedata.mapper.ProductCdkeyMapper;
import com.babel.basedata.model.ProductCdkeyPO;
import com.babel.basedata.model.ProductKeyPO;
import com.babel.basedata.service.IProductCdkeyService;
import com.babel.basedata.service.IProductKeyService;
import com.babel.basedata.util.Sysconfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.google.gson.Gson;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.util.StringUtil;

@Service("productCdkeyService")
public class ProductCdkeyServiceImpl extends BaseService<ProductCdkeyPO> implements IProductCdkeyService{
	private static final Log logger = LogFactory.getLog(ProductCdkeyServiceImpl.class);
	
	@Autowired
	private ProductCdkeyMapper productCdkeyMapper;
	
	@Autowired
    private IProductKeyService productKeyService;
	
	 @Override
		public ProductCdkeyMapper getMapper() {
			return productCdkeyMapper;
		}
	
	@Override
	public ProductCdkeyPO findProductCdkeyById(Long id) {
		logger.info("----ProductCdkeyPO findProductCdkeyById----id = "+id);
		//SqlHelper.addIgnore(ProductCdkeyPO.class, "name");
		return productCdkeyMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public List<ProductCdkeyPO> findProductCdkeyByIds(List<Long> ids) {
		logger.info("----findProductCdkeyByIds----");
		Example example = new Example(ProductCdkeyPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("cid", ids);
		return productCdkeyMapper.selectByExample(example);
	}
	
	public List<ProductCdkeyPO> findProductByCdkey(List<String>cdkeyList ){
		Example example = new Example(ProductCdkeyPO.class);
		example.selectProperties("cdkey");
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("cdkey", cdkeyList);
		criteria.andEqualTo("status", 1);
		return productCdkeyMapper.selectByExample(example);
	}
	
	@Override
    public List<ProductCdkeyPO> selectByProductCdkey(ProductCdkeyPO productCdkey) {
		logger.info("----selectByProductCdkey----productCdkey = "+productCdkey);
		if(productCdkey == null) {
			productCdkey = new ProductCdkeyPO();
		}
		productCdkey.setStatus(1);
		productCdkey.setIfDel(0);
        return productCdkeyMapper.select(productCdkey);
    }
	
	public PageVO<ProductCdkeyPO> findPageByProductCdkey(ProductCdkeyPO productCdkey, PageVO<ProductCdkeyPO> page) {
		logger.info("----findPageByProductCdkey----productCdkey = "+productCdkey);
		Example example = new Example(ProductCdkeyPO.class);
		Example.Criteria criteria = example.createCriteria();
  		if (productCdkey.getAppid() != null) {
			criteria.andEqualTo("appid", productCdkey.getAppid());
		}
  		if (productCdkey.getCdkey() != null) {
			criteria.andEqualTo("cdkey", productCdkey.getCdkey());
		}
  		if (productCdkey.getStatus() != null) {
			criteria.andEqualTo("status", productCdkey.getStatus());
		}
		String orderClause = page.getOrderClause();
		if(StringUtil.isNotEmpty(orderClause)) {
			example.setOrderByClause(orderClause);
		}
		return selectPageByExample(example, page);
	}
	
	@LogService(title="ProductCdkeyServiceImpl.create",author="Jinhe.chen",calls="insert")
	public RetResult<Long> create(ProductCdkeyPO productCdkey){
		logger.info("----create----productCdkey = "+productCdkey);
		RetResult<Long> ret = new RetResult<Long>();
		productCdkey.initCreate();
		save(productCdkey);
		ret.setData(productCdkey.getCid());
		return ret;
	}
	
	@LogService(title="ProductCdkeyServiceImpl.update",author="Jinhe.chen",calls="updateByPrimaryKey",descs="cid=#{0.cid}")
	public RetResult<Long> update(ProductCdkeyPO productCdkey){
		logger.info("----update----productCdkey = "+productCdkey);
		RetResult<Long> ret = new RetResult<Long>();
		if(productCdkey.getCid()==null || productCdkey.getCid().longValue()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid = "+productCdkey.getCid()+" is empty", null);
			return ret;
		}
		productCdkey.initUpdate();
		productCdkey.setIfDel(0);
		productCdkeyMapper.updateByPrimaryKey(productCdkey);
		ret.setData(productCdkey.getCid());
		return ret;
	}
	
	@LogService(title="ProductCdkeyServiceImpl.update",author="Jinhe.chen",calls="deleteVirtual",descs="cid=#{0.cid}")
	public RetResult<Integer> deleteVirtual(Long cid) throws RetException{
		logger.info("----deleteVirtual----cid = "+cid);
		RetResult<Integer> ret = new RetResult<Integer>();
		if(cid == null || cid == 0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid = "+cid+" is empty", null);
			return ret;
		}
		ProductCdkeyPO productCdkey = new ProductCdkeyPO();
		productCdkey.setCid(cid);
		int v = 0;
		try {
			v = this.deleteVirtual(productCdkey);
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "cid = "+cid+",error = "+e.getMessage(), e);
			logger.error("-----deleteVirtual----", e);
			throw new RetException(ret);
		}
		ret.setData(v);
		return ret;
	}

	private boolean checkExistByOrderNo(String appid, String orderNo, Date createDate){
		Example example = new Example(ProductCdkeyPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("appid", appid);
		criteria.andEqualTo("orderNo", orderNo);
		if(createDate!=null){
			criteria.andGreaterThanOrEqualTo("createDate", createDate);
		}
		int count=this.productCdkeyMapper.selectCountByExample(example);
		if(count>0){
			return true;
		}
		return false;
	}
	

	/**
	 * 生成兑换码
	 * @param appid 合作伙伴编码
	 * @param signValue 参数签名
	 * @param timestamp 时间戳
	 * @param nonceStr 随机码
	 */
	public RetResult<ProductCdkeyPO> generateCdKey(ProductCdkeyPO productCdkey, String signValue
			, String timestamp, String nonceStr, String remoteIp) {
		RetResult<ProductCdkeyPO> ret=new RetResult<>();
		RetResult<ProductKeyPO> retValid = this.checkValid(productCdkey, signValue, timestamp, nonceStr, remoteIp);
		
		Date now = new Date();
		if (!retValid.isSuccess()) {
			ret= new RetResult<>(retValid);
			return ret;
		}
		ProductKeyPO productKeyPO=retValid.getFirstData();
		productCdkey.setStatus(1);
		productCdkey.setIfDel(0);
		productCdkey.setIfUse(0);
//		productCdkey.setCdkey(this.getCdkeyCode(appid, "DH", 8));
		
		
		String pcode="lawHelpConfig";
		Integer cdKeyEffectDay=Sysconfigs.getValue("lawHelp.cdkeyEffectDay", 720, "兑换码有效期(天数)", pcode);
		productCdkey.setPrice(productKeyPO.getPrice());
		productCdkey.setDueDate(org.apache.commons.lang3.time.DateUtils.addDays(now, cdKeyEffectDay));//有效期一个月
		
		try {
			Date checkDate=org.apache.commons.lang3.time.DateUtils.addDays(now, -cdKeyEffectDay);
			if(this.checkExistByOrderNo(productCdkey.getAppid(), productCdkey.getOrderNo(), checkDate)){
				ret.initError(msg_codes.ERR_DATA_EXISTED, "orderNo="+productCdkey.getOrderNo()+" existed", null);
				return ret;
			}
			
			//一次生成5个兑换码,任选一个未使用的来用 
			String cdkey = getNewCdkey("DH", 6);
			productCdkey.setCdkey(cdkey);
			productCdkey.initCreate();
			create(productCdkey);
		} catch (Exception e) {
			ret.initError(msg_codes.ERR_UNKNOWN, "generate save", e);
			logger.error("----generateCdKey--"+ret.getMsgBody(), e);
			return new RetResult<>(ret); 
		}
		
		
		this.clearIntfProperties(productCdkey);
		
		
        
		RetResult<ProductCdkeyPO> retCdKey= new RetResult<>();
		retCdKey.setMsgBody(ret.getMsgBody());
		retCdKey.setMsgCode(ret.getMsgCode());
		retCdKey.setFlag(ret.getFlag());
		retCdKey.setData(productCdkey);;
		return retCdKey;
	}

	/**
	 * 一次生成多个cdkey兑换码，任选一个未使用的来用 
	 * @param prefix
	 * @param count
	 * @return
	 */
	private String getNewCdkey(String prefix, int count){
		List<String>cdkeyList=this.getCdkeyCode(prefix, count);
		List<ProductCdkeyPO> existList=this.findProductByCdkey(cdkeyList);
		logger.info("-----getNewCdkey--list="+new Gson().toJson(existList));
		String cdkey=null;
		boolean isExist=false;
		for(String cur:cdkeyList){
			isExist=false;
			for(ProductCdkeyPO exist:existList){
				if(cur.equals(exist.getCdkey())){
					isExist=true;
					break;
				}
			}
			if(!isExist){
				cdkey=cur;
				break;
			}
		}
		return cdkey;
	}
	
	public static String RANDOM_KEY_STR="0123456789ABCDEFGHJKLMNPQRSTUVWXYZ";
	private static char[] RANDOM_KEY_CHAR=null;
	private List<String> getCdkeyCode(String prefix, int count){
		List<String> list=new ArrayList<>(count);
		for(int i=0; i<count; i++){
			list.add(this.getCdkeyCode(prefix));
		}
		return list;
	}
	private String getCdkeyCode(String prefix){
		if(RANDOM_KEY_CHAR==null){
			RANDOM_KEY_CHAR=RANDOM_KEY_STR.toCharArray();
		}
		
		Long char_index=Math.round(Math.random()*(RANDOM_KEY_CHAR.length-1));
		String code=prefix+RANDOM_KEY_CHAR[char_index.intValue()]+this.getNumRandom();
		return code;
	}
	
	
	private String getNumRandom(){
		Long vLong=Math.round(Math.random()*(1000-1));
		if(vLong.longValue()<10){
			return "0000"+vLong;
		}
		else if(vLong.longValue()<100){
			return "000"+vLong;
		}
		else if(vLong.longValue()<1000){
			return "00"+vLong;
		}
		else if(vLong.longValue()<10000){
			return "0"+vLong;
		}
		return ""+vLong;
	}
	
//	private String getCdkeyCode(String appid, String prefix, int size){
//		String appType=Sysconfigs.getAppType();
//		Date now = new Date();
//		String code=appType+"_"+appid+"_"+now.getTime()+"_"+(1000*Math.random());
//		String sign = prefix+MD5Util.MD5Encode(code, "UTF-8").toUpperCase();
//		sign = CommUtil.getStringLimit(sign, size);
//		return sign;  
//	}
		
	 private RetResult<ProductKeyPO> checkValid(ProductCdkeyPO productCdkey, String signValue
			 , String timestamp, String nonceStr, String remoteIp) {
	    	RetResult<ProductKeyPO> ret=new RetResult<ProductKeyPO>();
	    	Date now = new Date();
	    	String appid=productCdkey.getAppid();
	    	if(StringUtils.isEmpty(appid)){
	    		return ret.initError(msg_codes.ERR_DATA_INPUT, "appid not found on request header", null);
	    	}
	    	
	    	if(StringUtils.isEmpty(signValue)){
	    		return ret.initError(msg_codes.ERR_DATA_INPUT, "signValue not found on request header", null);
	    	}
	    	if(StringUtils.isEmpty(productCdkey.getOrderNo())){
	    		return ret.initError(msg_codes.ERR_DATA_INPUT, "orderNo can not empty", null);
	    	}
	    	ProductKeyPO productKeyPO=this.productKeyService.getProductKeyByPpcode(appid);
	    	if(productKeyPO==null){
	    		return ret.initError(msg_codes.ERR_DATA_UNEXISTED, "appid="+appid+" invalid", null);
	    	}
	    	else if(productKeyPO.getStatus().intValue()!=1){
	    		return ret.initError(msg_codes.ERR_DATA_INVALID, "appid key status invalid", null);
	    	}
	    	else if(productKeyPO.getBeginDate()!=null && productKeyPO.getBeginDate().after(now)){
	    		String beginDates=DateUtils.format(productKeyPO.getBeginDate(), DateUtils.SDF_FORMAT_DATETIME);
	    		return ret.initError(msg_codes.ERR_DATA_INVALID, "beginDate="+beginDates+" after now", null);
	    	}
	    	else if(productKeyPO.getEndDate()!=null && productKeyPO.getEndDate().before(now)){
	    		String endDates=DateUtils.format(productKeyPO.getEndDate(), DateUtils.SDF_FORMAT_DATETIME);
	    		return ret.initError(msg_codes.ERR_DATA_INVALID, "endDate="+endDates+" by expired", null);
	    	}
	    	//如果白名单不为空，则检查白名单ip，基于包含关系
	    	if(!StringUtils.isEmpty(productKeyPO.getWhitelist()) 
	    			&& productKeyPO.getWhitelist().indexOf(remoteIp)<0){
	    		return ret.initError(msg_codes.ERR_PERMISSION_DENIED, "whitelist limit:"+remoteIp+" not found!", null);
	    	}
	    	String secretKey=productKeyPO.getSecretKey();
	    	//验证数据签名
	    	try {
				Map<String, Object> paramMap=ObjectToMapUtil.getDataMapByPropName(productCdkey, null, null);
				paramMap.put("timestamp", timestamp);
				paramMap.put("nonceStr", nonceStr);
				SortedMap<String, Object> paramSortMap=new TreeMap(paramMap);
				String paramSignValue=WeixinUtil.createWxSign(secretKey, "utf-8", paramSortMap);
				if(!paramSignValue.equals(signValue)){
					logger.warn("-----checkValid--secretKey="+secretKey+" paramMap="+paramMap+" paramSignValue="+paramSignValue);
					return ret.initError(msg_codes.ERR_DATA_INVALID, "data sign invalid", null);
				}
			} catch (Exception e) {
				ret.initError(msg_codes.ERR_UNKNOWN, "generate save", e);
				logger.error("----checkValid--secretKey="+secretKey+" error=" +ret.getMsgBody(), e);
			}
	    	
	    	ret.setData(productKeyPO);
	    	return ret;
	    }
	 

	@Override
	public ProductCdkeyPO findByCdkey(String cdkey) {
		ProductCdkeyPO productCdkeyPO = new ProductCdkeyPO();
		productCdkeyPO.setCdkey(cdkey);
		
		Example example = new Example(ProductCdkeyPO.class);
		Example.Criteria criteria = example.createCriteria();
  		if (!StringUtils.isEmpty(cdkey)) {
			criteria.andEqualTo("cdkey", cdkey.toUpperCase());
		}
  		criteria.andGreaterThan("dueDate", new Date());//有效期大于当前时间
  		List<ProductCdkeyPO> list = this.selectByExample(example);
		if(list != null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}
	
	public RetResult<ProductCdkeyPO> findByOrderNO(String orderNo, String appid, String signValue
			, String timestamp, String nonceStr, String remoteIp){ 
		RetResult<ProductCdkeyPO> ret=new RetResult<>();
		ProductCdkeyPO productCdkey=new ProductCdkeyPO();
		productCdkey.setOrderNo(orderNo);
		productCdkey.setAppid(appid);
		RetResult<ProductKeyPO> retResult=this.checkValid(productCdkey, signValue, timestamp, nonceStr, remoteIp);
		if(!retResult.isSuccess()){
			ret= new RetResult<>(retResult);
			return ret;
		}
		
		Example example = new Example(ProductCdkeyPO.class);
		String selectProperties=SqlHelper.getAllProperties(ProductCdkeyPO.class, "createDate,modifyDate,createUser,modifyUser,ifDel,status,useDate,ifUse,cid,appid");
		logger.info("-----findByOrderNO--orderNo="+orderNo+" selectProperties="+selectProperties);
		Example.Criteria criteria = example.createCriteria();
  		if (!StringUtils.isEmpty(orderNo)) {
			criteria.andEqualTo("orderNo", orderNo);
		}
  		example.selectProperties(selectProperties);
  		List<ProductCdkeyPO> list = this.productCdkeyMapper.selectByExample(example);
//  		System.out.println("----"+new Gson().toJson(list));
		if(!CollectionUtils.isEmpty(list)) {
			productCdkey=list.get(0);
			clearIntfProperties(productCdkey);
			ret.setData(productCdkey);
		
		}
		return ret;
	}

	/**
	 * 用于对外接口时，清除不必要的数据属性
	 * @param productCdkey
	 */
	private void clearIntfProperties(ProductCdkeyPO productCdkey) {
		productCdkey.setIfDel(null);
		productCdkey.setStatus(null);
		productCdkey.setAppid(null);
		productCdkey.setCid(null);
		productCdkey.setCreateDate(null);
		productCdkey.setModifyDate(null);
		productCdkey.setIfUse(null);
		String pcode="lawHelpConfig";
		String qrCodeDefault="";
		String cdKeyQrCodeUrl=Sysconfigs.getValue("lawHelp.cdKeyQrCodeUrl", qrCodeDefault, "兑换码对应二维码", pcode);
		productCdkey.setQrCodeUrl(cdKeyQrCodeUrl);//对应二维码地址
	}


}
