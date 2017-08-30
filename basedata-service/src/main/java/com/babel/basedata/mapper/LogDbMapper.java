package com.babel.basedata.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.babel.basedata.model.LogDbPO;

import tk.mybatis.mapper.common.MapperMy;

public interface LogDbMapper extends MapperMy<LogDbPO> {
	/**
	 * 统计每天接口调用总次数
	 * @param startDate
	 * @param endDate
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> staticIntfCallCount(@Param("startDate")Date startDate
			, @Param("endDate")Date endDate
			, @Param("param") Map<String, Object> paramMap);
	
	/**
	 * 统计时段内调用次数最多的前topN个接口的每天调用次数变化情况
	 * @param startDate
	 * @param endDate
	 * @param topN
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> staticIntfCodeTopCount(@Param("startDate")Date startDate
			, @Param("endDate")Date endDate
			, @Param("topN") Integer topN
			, @Param("param") Map<String, Object> paramMap
			);
	
	/**
	 * 统计时段内依据uuid的调用深度最深的前topN个的接口
	 * @param startDate
	 * @param endDate
	 * @param topN
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> staticUuidMaxDepth(@Param("startDate")Date startDate
			, @Param("endDate")Date endDate
			, @Param("topN") Integer topN
			, @Param("param") Map<String, Object> paramMap
			);
	
	/**
	 * 统计时段内接口执行时间最长的前topN个的接口
	 * @param startDate
	 * @param endDate
	 * @param topN
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> staticIntfCodeMaxRunTime(@Param("startDate")Date startDate
			, @Param("endDate")Date endDate
			, @Param("topN") Integer topN
			, @Param("param") Map<String, Object> paramMap
			);
	
	/**
	 * 统计时段内发生的所有每个接口的调用次数，最新调用时间，最大执行时间等
	 * @param startDate
	 * @param endDate
	 * @param topN
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> stataicIntfCodeShowAll(@Param("startDate")Date startDate
			, @Param("endDate")Date endDate
			, @Param("topN") Integer topN
			, @Param("param") Map<String, Object> paramMap
			);
}