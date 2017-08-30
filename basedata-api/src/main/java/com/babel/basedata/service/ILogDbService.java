package com.babel.basedata.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.babel.basedata.model.LogDbPO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;

public interface ILogDbService extends IBaseService<LogDbPO> {
	/**
	 * 统计每天接口调用总次数
	 * @param startDate
	 * @param endDate
	 * @param paramMap
	 * @return
	 */
	public RetResult<Map<String, Object>> staticIntfCallCount(Date startDate, Date endDate, Map<String, Object> paramMap);
	/**
	 * 统计时段内调用次数最多的前topN个接口的每天调用次数变化情况
	 * @param startDate
	 * @param endDate
	 * @param topN
	 * @param paramMap
	 * @return
	 */
	public RetResult<Map<String, Object>> staticIntfCodeTopCount(Date startDate, Date endDate, Integer topN, Map<String, Object> paramMap);
	/**
	 * 统计时段内依据uuid的调用深度最深的前topN个的接口
	 * @param startDate
	 * @param endDate
	 * @param topN
	 * @param paramMap
	 * @return
	 */
	public RetResult<Map<String, Object>> staticUuidMaxDepth(Date startDate, Date endDate, Integer topN, Map<String, Object> paramMap);
	/**
	 * 统计时段内接口执行时间最长的前topN个的接口
	 * @param startDate
	 * @param endDate
	 * @param topN
	 * @param paramMap
	 * @return
	 */
	public RetResult<Map<String, Object>> staticIntfCodeMaxRunTime(Date startDate, Date endDate, Integer topN, Map<String, Object> paramMap);
	/**
	 * 统计时段内发生的所有每个接口的调用次数，最新调用时间，最大执行时间等
	 * @param startDate
	 * @param endDate
	 * @param topN
	 * @param paramMap
	 * @return
	 */
	public RetResult<Map<String, Object>> stataicIntfCodeShowAll(Date startDate, Date endDate, Integer topN, Map<String, Object> paramMap);

//	public void info(Object obj, String title, String descs, Long runTime);
	

	
	public LogDbPO findLogDbById(Long id);
	
	public PageVO<LogDbPO> findPageByLogDb(LogDbPO logDb, PageVO<LogDbPO> page);
	
	public void log(LogDbPO logDb);
	
	public void logList(List<LogDbPO> logDbList);
}
