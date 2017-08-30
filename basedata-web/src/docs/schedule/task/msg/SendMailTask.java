package com.babel.fwork.schedule.task.msg;


import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.util.CommAutoSum;
import com.babel.common.core.util.CommUtil;
import com.babel.fwork.basedata.model.LogMsgDetailPO;
import com.babel.fwork.basedata.model.LogMsgPO;
import com.babel.fwork.basedata.service.ILogMsgDetailService;
import com.babel.fwork.basedata.service.ILogMsgService;
import com.babel.fwork.schedule.task.IScheduleTask;
import com.babel.fwork.schedule.task.ScheduleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;

public class SendMailTask implements IScheduleTask<LogMsgPO> {
	private static final Log logger = LogFactory.getLog(SendMailTask.class);
	@Autowired
	private ILogMsgService logMsgService;
	
	@Autowired
	private ILogMsgDetailService logMsgDetailService;
	
	@Resource(name = "mailSender")
	private MailSender sendMail;
	
	private String fromMail="ycstest@126.com";
	private String isLogMailDetail="true";//是否记录邮件发送明细，默认不记录

	@Override
	public String getTaskType() {
		// TODO Auto-generated method stub
		return ScheduleManager.TASK_TYPES.MSG_MAIL.getKey();
	}

	@Override
	public Collection loadData() {
		if(sendMail instanceof JavaMailSenderImpl){
			JavaMailSenderImpl senderImpl=(JavaMailSenderImpl)sendMail;
			fromMail=senderImpl.getUsername();
		}
		
		Date date = new Date();
		date=DateUtils.addHours(date, -1);
		Integer sendFlag=0;//未發送
		Integer msgType=2;//消息类型：1短信，2邮件，默认邮件
		RetResult<LogMsgPO> ret=this.logMsgService.findLogMsgList(date, msgType, sendFlag);
		return ret.getDataList();
	}

	@Override
	public Object execute(LogMsgPO logMsg) {
//		logger.info("-----execute--cid="+logMsg.getCid()+" sendType="+logMsg.getSendType());
		// TODO Auto-generated method stub
		RetResult<String> ret=null;
		if(logMsg.getSendType()!=null && logMsg.getSendType().intValue()==1){
			ret=this.sendMail(logMsg);
		}
		else if(logMsg.getSendType()!=null && logMsg.getSendType().intValue()==2){
			ret=this.sendMailSplit(logMsg);
		}
		if(ret!=null){
			logger.info("-----execute--cid="+logMsg.getCid()+" sendType="+logMsg.getSendType()+" "+ret.getFirstData());
		}
		return ret;
	}
	
	

	public RetResult<String> sendMail(final LogMsgPO logMsg) {
		RetResult<String> ret=new RetResult<String>();
//		logger.info("-----sendMail--cid="+logMsg.getCid());
		long time=System.currentTimeMillis();
		try {
			//处理前，再次检查状态
			LogMsgPO logMsgCheck=logMsgService.selectByKey(logMsg.getCid());
			if(logMsgCheck==null || logMsgCheck.getSendFlag().intValue()!=0){
				ret.initError(RetResult.msg_codes.ERR_DATA_INVALID, "cid="+logMsg.getCid()+"数据状态不是0待发送,取消", null);
				logger.warn(ret.getMsgBody());
//				runningDataMap.remove(logMsg.getCid());//处理过，或状态无效，则移除不用处理
				return ret;
			}
			SimpleMailMessage mail = new SimpleMailMessage(); // <span style="color:
			// #ff0000;">注意SimpleMailMessage只能用来发送text格式的邮件</span>
			mail.setTo(CommUtil.split(logMsg.getTos(), ","));// 接受者
			mail.setCc(CommUtil.split(logMsg.getCcs(), ","));
			mail.setBcc(CommUtil.split(logMsg.getBccs(), ","));
			mail.setFrom(fromMail);// 发送者,这里还可以另起Email别名，不用和xml里的username一致
			mail.setSubject(logMsg.getTitle());// 主题
			mail.setText(logMsg.getContent());// 邮件内容
			sendMail.send(mail);
			logMsg.setEndTime(new Date());
			logMsg.setSendFlag(1);//發送完成標志
			logMsg.setSendCount(1);
			
		} catch (Exception e) {
			logMsg.setSendFlag(3);
			logMsg.setExceptions(e.getMessage());
			logMsg.setEndTime(new Date());
			logger.error("----saveMailAsync title="+logMsg.getTitle(), e);
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "cid="+logMsg.getCid(), e);
		}
		logMsg.setRunTime(System.currentTimeMillis()-time);//执行时间
		RetResult<Long> retUpdate=logMsgService.update(logMsg);
//				runningMap.put(key, runningDataMap);
//		logger.info("----sendMsg--cid="+logMsg.getCid()+" tos="+logMsg.getTos()+" isSuccess="+ret.isSuccess());
		return ret;
	}
	
	
	private Set<String> getReceiveSet(String tos){
		Set<String> receiveSet = new HashSet<String>();
		String[] strs=null;
		if (tos != null) {
			tos = tos.trim();
			strs = tos.split(",");
			for(String str:strs){
				str=str.trim();
				receiveSet.add(str);
			}
		}
		return receiveSet;
	}
	
	private Set<String> getMsgReceiveSet(final LogMsgPO logMsg) {
		Set<String> receiveSet = new HashSet<String>();
		Set<String> sets=null;
		
		sets=this.getReceiveSet(logMsg.getTos());
		receiveSet.addAll(sets);
		sets=this.getReceiveSet(logMsg.getCcs());
		receiveSet.addAll(sets);
		sets=this.getReceiveSet(logMsg.getBccs());
		receiveSet.addAll(sets);
		return receiveSet;
	}
	
	private RetResult<String> sendMailSplit(final LogMsgPO logMsg) {
		RetResult<String> ret=new RetResult<String>();
		LogMsgPO logMsgCheck=logMsgService.selectByKey(logMsg.getCid());
		if(logMsgCheck==null || logMsgCheck.getSendFlag().intValue()!=0){
//			logger.warn("-----cid="+logMsg.getCid()+" 数据状态不是0待发送,取消---");
			ret.initError(RetResult.msg_codes.ERR_DATA_INVALID, "cid="+logMsg.getCid()+" 数据状态不是0待发送,取消", null);
			logger.warn(ret.getMsgBody());
			return ret;
		}
	
		long timeStart=System.currentTimeMillis();//用于计算整体任务执行时间
		Set<String> receiveSet = getMsgReceiveSet(logMsg);
		logMsg.setSendCount(receiveSet.size());
		logMsg.setSendFlag(2);//开始发送，在检查全部发送完成后，会重新改回1
		RetResult<Long> retUpdate=logMsgService.update(logMsg);
		
		
		for(String receive:receiveSet){
			RetResult<String> retMail=this.sendMailSplitDetail(logMsg, receive, receiveSet.size(), timeStart);
			if(!retMail.isSuccess()){
				ret.setFlag(1);
				ret.setData(retMail.getFirstData());
			}
		}
		logMsg.setSendFlag(1);//发送完成
		logMsg.setRunTime(System.currentTimeMillis()-timeStart);//计算所有邮件发送完成后的时间
		logMsgService.update(logMsg);
		return ret;
	}

	//动态累加器,用于输出邮件拆分后邮件发送的进度
	private static CommAutoSum autoSum=new CommAutoSum();
	
	private RetResult<String> sendMailSplitDetail(final LogMsgPO logMsg, final String to, final int totalCount, final long timeStart) {
		long time=System.currentTimeMillis();
		RetResult<String> ret=new RetResult<>();
		LogMsgPO logMsgCheck=logMsgService.selectByKey(logMsg.getCid());
		if(logMsgCheck==null || logMsgCheck.getSendFlag().intValue()!=2){//拆分
			logger.warn("-----sendMailSplitDetail--cid="+logMsg.getCid()+" 数据状态不是2，拆分后待发送---");
			return ret.initError(RetResult.msg_codes.ERR_DATA_INVALID, "cid="+logMsg.getCid()+" 数据状态不是2，拆分后待发送", null);
		}
		
		
		LogMsgDetailPO msgDetail=new LogMsgDetailPO();
		msgDetail.setLogMsgId(logMsg.getCid());
		msgDetail.setTos(to);
		msgDetail.setSendDate(new Date());
		try {
			SimpleMailMessage mail = new SimpleMailMessage(); // <span style="color:
			// #ff0000;">注意SimpleMailMessage只能用来发送text格式的邮件</span>

			mail.setTo(to);// 接受者
			mail.setFrom(fromMail);// 发送者,这里还可以另起Email别名，不用和xml里的username一致
			mail.setSubject(logMsg.getTitle());// 主题
			mail.setText(logMsg.getContent());// 邮件内容
			sendMail.send(mail);
		} catch (Exception e) {
			msgDetail.setRemark(e.getMessage());
			logger.error("----saveMailAsync to="+to, e);
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "cid="+logMsg.getCid()+",to="+to+",发送异常:"+e.getMessage(), e);
		}
		autoSum.count(logMsg.getCid());
		msgDetail.setRunTime(System.currentTimeMillis()-time);//执行时间
		RetResult<Long> retCreate=logMsgDetailService.create(msgDetail);
		logger.info("----sendMsgDetail--cid="+logMsg.getCid()+" sending="+autoSum.getValue(logMsg.getCid()).intValue()+"/"+totalCount+" tos="+logMsg.getTos()+" isSuccess="+ret.isSuccess());
		
//		if(totalCount==autoSum.getValue(logMsg.getCid()).intValue()){//全部发送完成
//			logMsg.setSendFlag(1);
//			logMsg.setRunTime(System.currentTimeMillis()-timeStart);//计算所有邮件发送完成后的时间
//			logMsgService.update(logMsg);
//			//處理完,從正在處理的列表中移除
////				runningDataMap.remove(logMsg.getCid());
////				logger.info("----sendMsgSplit--cid="+logMsg.getCid()+" tos="+logMsg.getTos()+" isSuccess="+ret.isSuccess()+" runningSize="+runningDataMap.size());
//		}
		return ret;
	}

	public void setFromMail(String fromMail) {
		this.fromMail = fromMail;
	}

	public void setIsLogMailDetail(String isLogMailDetail) {
		this.isLogMailDetail = isLogMailDetail;
	}

	public void setSendMail(MailSender sendMail) {
		this.sendMail = sendMail;
	}

	
		
}
