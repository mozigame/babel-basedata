package com.babel.fwork.schedule.task;

import com.babel.common.core.data.RetResult;
import com.babel.fwork.schedule.task.ScheduleManager;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;

/**
 * 
 * date 2016-07-01
 * @author cjh
 *
 */
@Service("scheduleToDoService")
public class ScheduleToDoService implements IScheduleToDoService{
	private static final Log logger = LogFactory.getLog(ScheduleToDoService.class);
	/**
	 * 把消息直接加到待处理，不用等定时任务去载入数据
	 */
	public RetResult<String> addToDo(String key, Object obj) {
		RetResult<String> ret = new RetResult<>();
		if(ScheduleManager.TASK_TYPES.isExist(key)){
			ScheduleManager.addData(key, obj);
		}
		else{
			logger.warn("-----taskType of key:"+key+" is invalid");
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "key:"+key+" is invalid", null);
		}
		return ret;
	}

}
