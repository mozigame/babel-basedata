package com.babel.basedata.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import com.babel.common.core.data.IMailVO;
import com.babel.common.core.data.ISmsVO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.util.TaskExecutorUtils;
import com.babel.basedata.model.LogMsgPO;
import com.babel.basedata.service.ILogMsgService;
import com.babel.basedata.util.Sysconfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

/**
 * 消息异步处理
 * @author cjh
 *
 */
@Service("logMsgManagerAsyncService")
public class LogMsgManagerAsyncService  {
	private static final Logger logger = Logger.getLogger(LogMsgManagerAsyncService.class);

	@Autowired
	private ILogMsgService logMsgService;

//	@Resource(name = "taskExecutor")
//	private TaskExecutor taskExecutor;

//	public void setTaskExecutor(TaskExecutor taskExecutor) {
//		this.taskExecutor = taskExecutor;
//	}


	public void saveMail(final String msgCode, final IMailVO mail, final int sendType) {
		TaskExecutor taskExecutor=TaskExecutorUtils.getExecutorSingle(Sysconfigs.getAppType(), this.getClass(), "saveMail", null);
		taskExecutor.execute(new Runnable() {
			public void run() {
				try {
					LogMsgPO msg = new LogMsgPO();
					msg.load(mail);
					msg.setSendType(sendType);
					msg.setMsgCode(msgCode);
					RetResult<Long> ret=logMsgService.create(msg);
					if(!ret.isSuccess()){
						logger.warn(ret.getMsgCode()+","+ret.getMsgBody());
					}
				} catch (Exception e) {
					logger.error("----saveMailAsync title="+mail.getTitle(), e);
				}
			}
		});
	}


	public void saveMailBatch(final String msgCode, final List<IMailVO> mailList, final int sendType) {
		TaskExecutor taskExecutor=TaskExecutorUtils.getExecutorSingle(Sysconfigs.getAppType(), this.getClass(), "saveMailBatch", null);
		taskExecutor.execute(new Runnable() {
			public void run() {
				try {
					List<LogMsgPO> list=new ArrayList<LogMsgPO>();
					LogMsgPO msg =null;
					for(IMailVO mail:mailList){
						msg = new LogMsgPO();
						msg.load(mail);
						msg.setMsgCode(msgCode);
						msg.setSendType(sendType);
						list.add(msg);
					}
					logMsgService.insertBatch(list);
				} catch (Exception e) {
					logger.error("----saveMailBatch, error:"+e.getMessage(), e);
				}
			}
		});
		
	}


	public void saveSms(final String msgCode, ISmsVO sms) {
		TaskExecutor taskExecutor=TaskExecutorUtils.getExecutorSingle(Sysconfigs.getAppType(), this.getClass(), "saveSms", null);
		taskExecutor.execute(new Runnable() {
			public void run() {
				try {
					LogMsgPO msg = new LogMsgPO();
					msg.load(sms);
					msg.setMsgCode(msgCode);
					RetResult<Long> ret=logMsgService.create(msg);
					if(!ret.isSuccess()){
						logger.warn(ret.getMsgCode()+","+ret.getMsgBody());
					}
				} catch (Exception e) {
					logger.error("----saveSms tos="+sms.getTos(), e);
				}
			}
		});
		
	}


	public void saveSmsBatch(final String msgCode, List<ISmsVO> smsList) {
		TaskExecutor taskExecutor=TaskExecutorUtils.getExecutorSingle(Sysconfigs.getAppType(), this.getClass(), "saveSmsBatch", null);
		taskExecutor.execute(new Runnable() {
			public void run() {
				try {
					List<LogMsgPO> list=new ArrayList<LogMsgPO>();
					LogMsgPO msg =null;
					for(ISmsVO sms:smsList){
						msg = new LogMsgPO();
						msg.setMsgCode(msgCode);
						msg.load(sms);
						list.add(msg);
					}
					logMsgService.insertBatch(list);
				} catch (Exception e) {
					logger.error("----saveSmsBatch, error:"+e.getMessage(), e);
				}
			}
		});
		
	}
}