package com.babel.basedata.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Service;

import com.babel.basedata.model.WhiteListPO;
import com.babel.basedata.model.WhiteTypePO;
import com.babel.basedata.util.WhiteListUtils;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.service.IWhiteListCheckService;

@Service
public class WhiteListCheckService implements IWhiteListCheckService{
	private static Logger logger = Logger.getLogger(WhiteListCheckService.class);
	@Override
	public RetResult<Boolean> check(int dataType, String data) {
		RetResult<Boolean> ret=new RetResult<Boolean>();
		String whiteKey=dataType+"_"+1;
		String blackKey=dataType+"_"+2;
		
		List<WhiteListPO> whiteList=WhiteListUtils.getWhiteList(whiteKey);//白名单
		List<WhiteListPO> blackList=WhiteListUtils.getWhiteList(blackKey);//黑名单
		
		if(CollectionUtils.isEmpty(whiteList) && CollectionUtils.isEmpty(blackList)){
			ret.setMsgBody("NO_DATA");
			ret.setData(true);
		}
		else if(!CollectionUtils.isEmpty(whiteList) && CollectionUtils.isEmpty(blackList)){
//			System.out.println("----check--whiteList="+whiteList.size());
			ret.setData(this.checkWhite(data, whiteList));
//			ret.setData(true);
			ret.setMsgBody("CHECK_EQ_WHITE");
		}
		else if(CollectionUtils.isEmpty(whiteList) && !CollectionUtils.isEmpty(blackList)){
			ret.setData(this.checkBlack(data, blackList));
			ret.setMsgBody("CHECK_EQ_BLACK");
		}
		else{
			ret.setData(this.checkEqualBoth(data, whiteList, blackList));
			ret.setMsgBody("CHECK_EQ_BOTH");
		}
		logger.debug("------check--dataType="+dataType+" data="+data+" isPass="+ret.getFirstData()+" msgBody="+ret.getMsgBody());
		return ret;
	}
	
	private CheckResult isExist(String data, List<WhiteListPO> list){
		CheckResult check=null;
		for(WhiteListPO white:list){
			if(white.getContent().equals(data)){
				check=new CheckResult(white);
				break;
			}
		}
		return check;
	}
	
	/**
	 * 检查黑名单，即默认可通过
	 * 场景：没有白名单，只有黑名单
	 * @param data
	 * @param whiteList
	 * @return
	 */
	private boolean checkBlack(String data, List<WhiteListPO> list){
		CheckResult exist=this.isExist(data, list);
		if(exist!=null){
			logger.debug("------checkBlack--false--"+exist);
			return false;
		}
		return true;
	}
	

	/**
	 * 检查白名单，即默认不通过
	 * 场景：只有名单，没有黑名单
	 * @param data
	 * @param whiteList
	 * @return
	 */
	private boolean checkWhite(String data, List<WhiteListPO> list){
		CheckResult exist=this.isExist(data, list);
		if(exist!=null){
			logger.debug("------checkWhite--true--"+exist);
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * 同时有黑名单与白名单
	 * 
	 * 有白名单时，未加白名单的视为拒绝
	 * 
	 * 严格匹配，黑名单优先，发现是黑名单直接拒绝，否则检查看是否白名单，如果是白名单才通过
	 * 
	 * @param data
	 * @param whiteList
	 * @param blackList
	 * @return
	 */
	private boolean checkEqualBoth(String data, List<WhiteListPO> whiteList, List<WhiteListPO> blackList){
		CheckResult exist=this.isExist(data, blackList);
		if(exist!=null){
			logger.debug("------checkEqualBoth--false--"+exist);
			return false;
		}
		exist=this.isExist(data, whiteList);
		if(exist!=null){
			logger.debug("------checkEqualBoth--true--"+exist);
			return true;
		}
		return false;
	}

}

class CheckResult{
	public CheckResult(){
		
	}
	public CheckResult(WhiteListPO white){
		this.whiteListId=white.getCid();
		this.whiteTypeId=white.getWhiteTypeId();
		WhiteTypePO type=WhiteListUtils.getWhiteType(this.whiteTypeId);
		if(type!=null){
			this.type=type.getType();
			this.dataType=type.getDataType();
		}
	}
	public String toString(){
		return "dataType:"+dataType+",type:"+type+",whiteTypeId:"+whiteTypeId+",whiteListId="+whiteListId;
	}
	private Long whiteListId;
	private Long whiteTypeId;
	private Integer dataType;
	private Integer type;
	private boolean isPass;
	
	public Long getWhiteListId() {
		return whiteListId;
	}
	public void setWhiteListId(Long whiteListId) {
		this.whiteListId = whiteListId;
	}
	public Long getWhiteTypeId() {
		return whiteTypeId;
	}
	public void setWhiteTypeId(Long whiteTypeId) {
		this.whiteTypeId = whiteTypeId;
	}
	public boolean isPass() {
		return isPass;
	}
	public void setPass(boolean isPass) {
		this.isPass = isPass;
	}
	public Integer getDataType() {
		return dataType;
	}
	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
}
