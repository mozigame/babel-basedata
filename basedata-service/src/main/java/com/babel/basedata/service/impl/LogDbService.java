package com.babel.basedata.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.babel.basedata.logger.LogService;
import com.babel.basedata.mapper.LogDbMapper;
import com.babel.basedata.model.LogDbPO;
import com.babel.basedata.model.ModelPO;
import com.babel.basedata.service.ILogDbService;
import com.babel.basedata.service.IModelService;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.entity.BaseEntity;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.impl.BaseService;
import com.babel.common.core.util.CommUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

@Service("logDbService")
public class LogDbService extends BaseService<LogDbPO> implements ILogDbService {
	 private static final Log logger = LogFactory.getLog(LogDbService.class);
	//logLevel:1 debug, 2 info, 3 warn, 4 error, 5 fatal.

	 @Autowired
	 private LogDbMapper mapper;
	 
	 @Autowired
	 private IModelService modelService;
	 
	 
	@Override
	public LogDbMapper getMapper() {
		return mapper;
	}
	
	public RetResult<Map<String, Object>> staticIntfCallCount(Date startDate, Date endDate, Map<String, Object> paramMap){
		RetResult<Map<String, Object>> ret = new RetResult<Map<String, Object>>();
		if(startDate==null){
			startDate=DateUtils.addDays(new Date(), -30);
		}
		
		int day=0;
		if(endDate!=null){
			day=com.babel.common.core.util.DateUtils.getReduce(endDate, startDate, Calendar.DATE);
		}
		else{
			day=com.babel.common.core.util.DateUtils.getReduce(new Date(), startDate, Calendar.DATE);
		}
		if(day>31){
			return ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "date search "+day+" days out of limit day:31", null);
		}
		ret.setDataList(this.getMapper().staticIntfCallCount(startDate, endDate, paramMap));
		return ret;
	}
	
	public RetResult<Map<String, Object>> staticIntfCodeTopCount(Date startDate, Date endDate, Integer topN, Map<String, Object> paramMap){
		RetResult<Map<String, Object>> ret = new RetResult<Map<String, Object>>();
		if(startDate==null){
			startDate=DateUtils.addDays(new Date(), -30);
		}
		if(topN==null){
			topN=20;
		}
		if(topN>100){
			return ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "topN:"+topN+" out of limit 100", null);
		}
		int day=0;
		if(endDate!=null){
			day=com.babel.common.core.util.DateUtils.getReduce(endDate, startDate, Calendar.DATE);
		}
		else{
			day=com.babel.common.core.util.DateUtils.getReduce(new Date(), startDate, Calendar.DATE);
		}
		if(day>31){
			return ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "date search "+day+" days out of limit day:31", null);
		}
		ret.setDataList(this.getMapper().staticIntfCodeTopCount(startDate, endDate, topN, paramMap));
		return ret;
	}
	
	public RetResult<Map<String, Object>> staticUuidMaxDepth(Date startDate, Date endDate, Integer topN, Map<String, Object> paramMap){
		RetResult<Map<String, Object>> ret = new RetResult<Map<String, Object>>();
		if(startDate==null){
			startDate=DateUtils.addDays(new Date(), -30);
		}
		if(topN==null){
			topN=20;
		}
		if(topN>100){
			return ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "topN:"+topN+" out of limit 100", null);
		}
		int day=0;
		if(endDate!=null){
			day=com.babel.common.core.util.DateUtils.getReduce(endDate, startDate, Calendar.DATE);
		}
		else{
			day=com.babel.common.core.util.DateUtils.getReduce(new Date(), startDate, Calendar.DATE);
		}
		if(day>31){
			return ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "date search "+day+" days out of limit day:31", null);
		}
		ret.setDataList(this.getMapper().staticUuidMaxDepth(startDate, endDate, topN, paramMap));
		return ret;
	}
	
	public RetResult<Map<String, Object>> staticIntfCodeMaxRunTime(Date startDate, Date endDate, Integer topN, Map<String, Object> paramMap){
		RetResult<Map<String, Object>> ret = new RetResult<Map<String, Object>>();
		if(startDate==null){
			startDate=DateUtils.addDays(new Date(), -30);
		}
		if(topN==null){
			topN=20;
		}
		if(topN>100){
			return ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "topN:"+topN+" out of limit 100", null);
		}
		int day=0;
		if(endDate!=null){
			day=com.babel.common.core.util.DateUtils.getReduce(endDate, startDate, Calendar.DATE);
		}
		else{
			day=com.babel.common.core.util.DateUtils.getReduce(new Date(), startDate, Calendar.DATE);
		}
		if(day>31){
			return ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "date search "+day+" days out of limit day:31", null);
		}
		ret.setDataList(this.getMapper().staticIntfCodeMaxRunTime(startDate, endDate, topN, paramMap));
		return ret;
	}
	
	
	/**
	 * 统计时段内发生的所有每个接口的调用次数，最新调用时间，最大执行时间等
	 * @param startDate
	 * @param endDate
	 * @param topN
	 * @param paramMap
	 * @return
	 */
	public RetResult<Map<String, Object>> stataicIntfCodeShowAll(Date startDate, Date endDate, Integer topN, Map<String, Object> paramMap){
		RetResult<Map<String, Object>> ret = new RetResult<Map<String, Object>>();
		if(startDate==null){
			startDate=DateUtils.addDays(new Date(), -30);
		}
		if(topN==null){
			topN=20;
		}
		if(topN>100){
			return ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "topN:"+topN+" out of limit 100", null);
		}
		int day=0;
		if(endDate!=null){
			day=com.babel.common.core.util.DateUtils.getReduce(endDate, startDate, Calendar.DATE);
		}
		else{
			day=com.babel.common.core.util.DateUtils.getReduce(new Date(), startDate, Calendar.DATE);
		}
		if(day>31){
			return ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "date search "+day+" days out of limit day:31", null);
		}
		ret.setDataList(this.getMapper().stataicIntfCodeShowAll(startDate, endDate, topN, paramMap));
		return ret;
	}
	
	private void log(int logLevel, Object obj, String title, String descs, Long runTime, Exception e, RetResult ret){
		String userName="";
		String remark=null;
		if(e!=null){
			descs=e.getMessage()+"\n"+descs;
			remark=ExceptionUtils.getFullStackTrace(e);
			if(remark.length()>2000){
				remark=remark.substring(0, 2000);
			}
		}
		LogDbPO logDb = new LogDbPO();
		if(logDb.getCreateDate()==null){
			logDb.initCreate();
		}
		
		Gson gson = new Gson();
		if(obj!=null){
			if(obj instanceof BaseEntity){
				BaseEntity entity=(BaseEntity)obj;
				logDb.setUserId(entity.getModifyUser());
				try {
					String json=gson.toJson(obj);//JSONObject.fromObject(obj).toString();
					json=CommUtil.getStringLimit(json, 2000);
					logDb.setJsonParam(json);
				} catch (Exception e1) {
					logger.warn("json convert", e1);
				}
			}
			else{
				logDb.setJsonParam(""+obj);
			}
		}
		
		if(ret!=null){
			try {
				String json=gson.toJson(ret);//JSONObject.fromObject(ret).toString();
				json=CommUtil.getStringLimit(json, 2000);
				logDb.setJsonParam(json);
			} catch (Exception e1) {
				logger.warn("json convert", e1);
			}
			logDb.setFlag(""+ret.getFlag());
		}
		if(logDb.getFlag()==null){
			logDb.setFlag("0");
		}
		logDb.setUserName(userName);
		logDb.setTitle(title);
		logDb.setDescs(descs);
		logDb.setRemark(remark);
		logDb.setRunTime(runTime);
		logDb.setLogLevel(logLevel);
		this.getMapper().insert(logDb);
	}
	
	@Override
	public void log(LogDbPO logDb){
		this.initLogDb(logDb);
		this.mapper.insert(logDb);
	}
	
	private void initLogDb(LogDbPO logDb){
		if(logDb.getCreateDate()==null){
			logDb.initCreate();
		}
		if(logDb.getFlag()==null){
			logDb.setFlag("0");
		}
		String json=CommUtil.getStringLimit(logDb.getJsonRet(), 2000);
		logDb.setJsonRet(json);
		
		json=CommUtil.getStringLimit(logDb.getJsonParam(), 2000);
		logDb.setJsonParam(json);
	}
	
	public void logList(List<LogDbPO> logDbList){
		long time=System.currentTimeMillis();
		if(logDbList.size()==0){
			logger.warn("----logList--logDbList="+logDbList.size());
			return;
		}
		for(LogDbPO logDb:logDbList){
			this.initLogDb(logDb);
		}
		try {
			this.mapper.insertList(logDbList);
		} catch (Exception e) {
//			e.printStackTrace();
			
			Gson gson = new Gson();
			logger.warn("-----logList--time="+(System.currentTimeMillis()-time)+" size="+logDbList.size()+"\n"+gson.toJson(logDbList), e);
		}
	}

	@Override
	public LogDbPO findLogDbById(Long id) {
		logger.info("----findLogDbById--id="+id);
		return this.getMapper().selectByPrimaryKey(id);
	}
	
	@Override
	@LogService(title="findPageByLogDb", descs="logLevel:#logDb.logLevel")
	public PageVO<LogDbPO> findPageByLogDb(LogDbPO logDb, PageVO<LogDbPO> page) {
		logger.info("----findPageByLogDb--"+logDb.getLogLevel());
		Example example = new Example(LogDbPO.class);
//		  example.selectProperties("nameCn","nameEn","code","cid");
		Example.Criteria criteria = example.createCriteria();
//		criteria.andEqualTo("ifDel", 0);
		if (StringUtil.isNotEmpty(logDb.getTitle())) {
            criteria.andLike("title", "%" + logDb.getTitle() + "%");
        }
		if (StringUtil.isNotEmpty(logDb.getDescs())) {
            criteria.andLike("descs", "%" + logDb.getDescs() + "%");
        }
//        if (StringUtil.isNotEmpty(logDb.getCode())) {
//            criteria.andLike("itemCode", "%" + logDb.get() + "%");
//        }
        if (logDb.getLogLevel() != null && logDb.getLogLevel()>0) {
            criteria.andEqualTo("logLevel", logDb.getLogLevel());
        }
        
        if (logDb.getUserId() != null) {
            criteria.andEqualTo("userId", logDb.getUserId());
        }
        
        if (logDb.getThreadId() != null) {
            criteria.andEqualTo("threadId", logDb.getThreadId());
        }
        
        if (logDb.getUserName() != null) {
            criteria.andEqualTo("userName", logDb.getUserName());
        }
        
        if (logDb.getIp() != null) {
            criteria.andEqualTo("ip", logDb.getIp());
        }
        if (logDb.getUuid() != null) {
            criteria.andEqualTo("uuid", logDb.getUuid());
        }
  
        List<Long> modelIdList=getModelIds(logDb);
//        System.out.println("-----modelIdList="+modelIdList);
        if(modelIdList.size()>0){
        	 criteria.andIn("modelId", modelIdList);
        }
        if (logDb.getModelId() != null) {
            criteria.andEqualTo("modelId", logDb.getModelId());
        }
        
        if (logDb.getProjectId() != null) {
            criteria.andEqualTo("projectId", logDb.getProjectId());
        }
        
        if (logDb.getCreateDate() != null) {
            criteria.andGreaterThanOrEqualTo("createDate", logDb.getCreateDate());
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
		List<LogDbPO> list = selectByExample(example);
		
		initDispOfModel(list);//显示model信息byModelId
		
		PageInfo<LogDbPO> pageInfo = new PageInfo<LogDbPO>(list);
		PageVO<LogDbPO> pageRet = new PageVO<LogDbPO>(pageInfo);
		
		
		return pageRet;
	}

	/**
	 * @param logDb
	 */
	private List<Long> getModelIds(LogDbPO logDb) {
		List<Long> modelIdList=new ArrayList<Long>();
		if(logDb.getMethodName()==null && logDb.getClassName()==null && logDb.getPackageName()==null){
			return modelIdList;
		}
		PageVO<ModelPO> pageModel=new PageVO(1, 800);
        ModelPO modelSearch=new ModelPO();
        modelSearch.setFuncCode(logDb.getMethodName());
        modelSearch.setClassName(logDb.getClassName());
        modelSearch.setPackageName(logDb.getPackageName());
        pageModel=this.modelService.findPageByModel(modelSearch, pageModel);
        Collection<ModelPO> modelList=pageModel.getRows();
        
        for(ModelPO model:modelList){
        	modelIdList.add(model.getCid());
        }
        return modelIdList;
	}

	
	/**
	 * 根据modelId转换className,methodName
	 * @param list
	 */
	private void initDispOfModel(List<LogDbPO> list) {
		List<Long> modelIdList=new ArrayList<>();
		for(LogDbPO log:list){
			if(log.getModelId()!=null)
				modelIdList.add(log.getModelId());
		}
		if(modelIdList.size()>0){
			List<ModelPO> mList=this.modelService.findModelByIds(modelIdList);
			for(LogDbPO log:list){
				if(log.getModelId()!=null){
					for(ModelPO m:mList){
						if(m.getCid().longValue()==log.getModelId().longValue()){
							log.setClassName(m.getClassName());
							log.setMethodName(m.getFuncCode());
							log.setPackageName(m.getPackageName());
							break;
						}
					}
				}
			}
		}
	}


	
}
