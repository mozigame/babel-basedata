package com.babel.basedata.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.time.DateUtils;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.impl.BaseService;
import com.babel.basedata.logger.LogService;
import com.babel.basedata.mapper.LogMsgMapper;
import com.babel.basedata.model.LogMsgPO;
import com.babel.basedata.service.ILogMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

/**
 * 
 * 消息日志服务
 * @author cjh
 *
 */
@Service("logMsgService")
public class LogMsgService extends BaseService<LogMsgPO> implements ILogMsgService {
	private static final Log logger = LogFactory.getLog(LogMsgService.class);
	// logLevel:1 debug, 2 info, 3 warn, 4 error, 5 fatal.

	@Autowired
	private LogMsgMapper mapper;
	
//	@Autowired
//	private IScheduleToDoService scheduleToDoService;
//	
//
//	public void setScheduleToDoService(IScheduleToDoService scheduleToDoService) {
//		this.scheduleToDoService = scheduleToDoService;
//	}

	@Override
	public LogMsgMapper getMapper() {
		return mapper;
	}
	
	@LogService(title="logMsg.findLogMsgList", author="cjh", calls="LogMsgMapper.selectByExample", descs=LogService.noDatas)
	public RetResult<LogMsgPO> findLogMsgList(Date sendTime, Integer msgType, Integer sendFlag) {
		logger.info("----findLogMsgList--sendTime="+sendTime+" msgType="+msgType+" sendFlag="+sendFlag);
		RetResult<LogMsgPO> ret = new RetResult<LogMsgPO>();
		if (msgType == null) {
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "msgType is null", null);
			return ret;
		}
		if (sendFlag == null) {
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "sendFlag is null", null);
			return ret;
		}
		if (sendTime == null) {
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "sendTime is null", null);
			return ret;
		}
		Example example = new Example(LogMsgPO.class);
		Example.Criteria criteria = example.createCriteria();

		criteria.andEqualTo("msgType", msgType);
		criteria.andEqualTo("sendFlag", sendFlag);
		criteria.andGreaterThanOrEqualTo("sendTime", sendTime);
		
		Date date = new Date();
		date=DateUtils.addMinutes(date, 1);//当前时间往后一分种内
		criteria.andLessThanOrEqualTo("sendTime", date);//小于等于
		
		List<LogMsgPO> list = selectByExample(example);
		ret.setDataList(list);
		return ret;

	}

	@Override
	@LogService(title="logMsg.findPageByLogMsg", author="cjh", calls="LogMsgMapper.selectByExample", descs=LogService.noDatas)
	public PageVO<LogMsgPO> findPageByLogMsg(LogMsgPO logMsg, PageVO<LogMsgPO> page, String selectProperties, List<String> msgCodeList) {
		logger.info("----findPageByLogMsg--");
		Example example = new Example(LogMsgPO.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtil.isNotEmpty(logMsg.getTitle())) {
            criteria.andLike("title", "%" + logMsg.getTitle() + "%");
        }
		if (StringUtil.isNotEmpty(logMsg.getContent())) {
            criteria.andLike("content", "%" + logMsg.getContent() + "%");
        }
		
		if (StringUtil.isNotEmpty(logMsg.getTos())) {
            criteria.andLike("tos", "%" + logMsg.getTos() + "%");
        }

		if(!StringUtils.isEmpty(selectProperties)){
			example.selectProperties(selectProperties.split(","));
		}
		if(msgCodeList!=null && msgCodeList.size()>0){
			criteria.andIn("msgCode", msgCodeList);
		}
		else if (StringUtil.isNotEmpty(logMsg.getMsgCode())) {
            criteria.andLike("msgCode", "%" + logMsg.getMsgCode() + "%");
        }
        if (logMsg.getUserId() != null) {
            criteria.andEqualTo("userId", logMsg.getUserId());
        }
        if (logMsg.getMsgType() != null) {
            criteria.andEqualTo("msgType", logMsg.getMsgType());
        }
        if (logMsg.getEndTime() != null) {
            criteria.andGreaterThanOrEqualTo("endTime", logMsg.getEndTime());
        }
        if (logMsg.getCreateDate() != null) {
            criteria.andGreaterThanOrEqualTo("createDate", logMsg.getCreateDate());
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
		List<LogMsgPO> list = selectByExample(example);
		PageInfo<LogMsgPO> pageInfo = new PageInfo<LogMsgPO>(list);
		PageVO<LogMsgPO> pageRet = new PageVO<LogMsgPO>(pageInfo);
		return pageRet;
	}
	
	public boolean checkEmail(String email){
        boolean flag = false;
        try{
                String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(email);
                flag = matcher.matches();
            }catch(Exception e){
                flag = false;
            }
        return flag;
    }
     
    /**
     * 验证手机号码
     * @param mobiles
     * @return
     */
    public boolean checkMobileNumber(String mobileNumber){
        boolean flag = false;
        try{
                Pattern regex = Pattern.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
                Matcher matcher = regex.matcher(mobileNumber);
                flag = matcher.matches();
            }catch(Exception e){
                flag = false;
            }
        return flag;
    }
    
    private RetResult<Long> checkByMsgType(LogMsgPO record){
    	RetResult<Long> ret = new RetResult<>();
    	if(record.getMsgType()==null){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "msgType="+record.getMsgType()+" is empty", null);
			return ret;
		}
		else if(record.getMsgType().intValue()==1){//手機號檢查
			if(!this.checkMobileNumber(record.getTos())){
				ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "tos="+record.getTos()+" invalid mail format", null);
				return ret;
			}
		}
		else  if(record.getMsgType().intValue()==2){//郵箱檢查
			if(!this.checkEmail(record.getTos())){
				ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "tos="+record.getTos()+" invalid mail format", null);
				return ret;
			}
			if(record.getCcs()!=null && !this.checkEmail(record.getCcs())){
				ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cc="+record.getTos()+" invalid mail format", null);
				return ret;
			}
			if(record.getBccs()!=null && !this.checkEmail(record.getBccs())){
				ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "bcc="+record.getTos()+" invalid mail format", null);
				return ret;
			}
		}
    	return ret;
    }
	
    @LogService(title="logMsg.create", author="cjh", calls="LogMsgMapper.insertLogMsg")
	public RetResult<Long> create(LogMsgPO record){
		logger.info("----create--");
		long time=System.currentTimeMillis();
		RetResult<Long> ret = new RetResult<>();
		if(StringUtils.isEmpty(record.getContent())){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "content="+record.getContent()+" is empty", null);
			logger.warn("create="+ret.getMsgBody());
			return ret;
		}
		if(StringUtils.isEmpty(record.getTos())){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "tos="+record.getTos()+" is empty", null);
			logger.warn("create="+ret.getMsgBody());
			return ret;
		}
		
		
		Date date_after_5 = DateUtils.addMinutes(new Date(), 5);//5分钟后
		Date date_before_5 = DateUtils.addMinutes(new Date(), -5);//5分钟前
		
		if(record.getSendTime()!=null && record.getSendTime().before(date_before_5)){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "sendTime is expired, no need to add", null);
			logger.warn("create="+ret.getMsgBody());
			return ret;
		}
		
		ret=this.checkByMsgType(record);
		if(!ret.isSuccess()){
			logger.warn("create="+ret.getMsgBody());
			return ret;
		}
		
		record.setMailCount(this.getMailCount(record));
		boolean isSend=false;
		
		
		if(record.getSendTime()==null||
				record.getSendTime().after(date_before_5) && record.getSendTime().before(date_after_5)){//sendTime为空或sendTime在5分钟以内就立即发送
			isSend=true;
			if(record.getSendTime()==null){
				record.setSendTime(new Date());
			}
		}
		record.initCreate();
		this.getMapper().insertLogMsg(record);
		ret.setData(record.getCid());
		if(isSend){//马上发送
			this.addToDo(record);
		}
		return ret;
	}
	
	/**
	 * 把消息直接加到待处理，不用等定时任务去载入数据
	 * @param logMsg
	 */
	@Async
	public void addToDo(LogMsgPO logMsg){
		String key=null;
		if(logMsg.getMsgType().intValue()==2){
			key="MSG_MAIL";
		}
		else if(logMsg.getMsgType().intValue()==1){
			key="MSG_SMS";
		}
		else if(logMsg.getMsgType().intValue()==5){
			key="MSG_WX";
		}
		
//		if(this.scheduleToDoService!=null)
//			scheduleToDoService.addToDo(key, logMsg);
	}
	
	@LogService(title="logMsg.insertBatch", author="cjh", calls="LogMsgMapper.insertBatch")
	public void insertBatch(List<LogMsgPO> msgList) throws Exception{
		if(true){
			this.mapper.insertList(msgList);
			return;
		}
		int batchSize=800;
		List<LogMsgPO> cacheList=new ArrayList<>();
		RetResult<Long> ret=null;
		for(LogMsgPO msg:msgList){
			msg.initCreate();
			msg.setMailCount(this.getMailCount(msg));
			if(msg.getSendTime()==null){
				msg.setSendTime(new Date());
			}
			ret=this.checkByMsgType(msg);
			if(ret.isSuccess()){
				throw new Exception(ret.getMsgBody());
			}
			cacheList.add(msg);
			if(cacheList.size()>batchSize){
				this.mapper.insertBatch(cacheList);
				cacheList.clear();
			}
		}
		if(cacheList.size()>0){
			this.mapper.insertBatch(cacheList);
		}
		
	}
	
	@LogService(title="logMsg.updateBatch", author="cjh", calls="LogMsgMapper.updateBatch")
	public void updateBatch(List<LogMsgPO> msgList) throws Exception{
		int batchSize=800;
		List<LogMsgPO> cacheList=new ArrayList<>();
		RetResult<Long> ret=null;
		for(LogMsgPO msg:msgList){
			msg.initUpdate();
			ret=this.checkByMsgType(msg);
			if(ret.isSuccess()){
				throw new Exception(ret.getMsgBody());
			}
			cacheList.add(msg);
			if(cacheList.size()>batchSize){
				this.mapper.updateBatch(cacheList);
				cacheList.clear();
			}
		}
		if(cacheList.size()>0){
			this.mapper.updateBatch(cacheList);
		}
		
	}
	
	@LogService(title="logMsg.update", author="cjh", calls="LogMsgMapper.updateByPrimaryKey")
	public RetResult<Long> update(LogMsgPO record){
		logger.info("----update--");
		RetResult<Long> ret = new RetResult<>();
		if(record.getCid()==null||record.getCid().longValue()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+record.getCid()+" is empty", null);
			return ret;
		}
		record.initUpdate();
		record.setIfDel(0);
		this.mapper.updateByPrimaryKey(record);
		ret.setData(record.getCid());
		return ret;
	}
	
	private int getMailCount(String tos){
		int mailCount=0;
		if (tos != null) {
			tos = tos.trim();
			mailCount = tos.split(",").length;
		}
		return mailCount;
	}
	
	private int getMailCount(LogMsgPO logMsg){
		int mailCount = 0;
		mailCount+=this.getMailCount(logMsg.getTos());
		mailCount+=this.getMailCount(logMsg.getCcs());
		mailCount+=this.getMailCount(logMsg.getBccs());
		return mailCount;
	}

}
