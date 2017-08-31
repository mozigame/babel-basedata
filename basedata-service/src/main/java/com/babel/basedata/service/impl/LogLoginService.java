package com.babel.basedata.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.babel.basedata.entity.LogInfoVO;
import com.babel.basedata.mapper.LogLoginMapper;
import com.babel.basedata.model.LogLoginPO;
import com.babel.basedata.model.UserPO;
import com.babel.basedata.service.ILogLoginService;
import com.babel.basedata.util.Sysconfigs;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.impl.BaseService;
import com.babel.common.core.util.RedisListUtil;
import com.babel.common.core.util.RedisUtil;
import com.babel.common.core.util.SpringContextUtil;
import com.babel.common.core.util.TaskExecutorUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

@Service("logLoginService")
public class LogLoginService extends BaseService<LogLoginPO> implements ILogLoginService {
	 private static final Log logger = LogFactory.getLog(LogLoginService.class);
	//logLevel:1 debug, 2 info, 3 warn, 4 error, 5 fatal.

	 @Autowired
	 private LogLoginMapper mapper;
	 
//	 @Resource(name = "taskExecutorLogin")
//	private TaskExecutor taskExecutor;	 
	 
	@Override
	public LogLoginMapper getMapper() {
		return mapper;
	}
	
	
	
	@Override
	public PageVO<LogLoginPO> findPageByLogLogin(LogLoginPO logLogin, PageVO<LogLoginPO> page) {
		logger.info("----findPageByLogLogin--");
		Example example = new Example(LogLoginPO.class);
		Example.Criteria criteria = example.createCriteria();
		
		if (StringUtil.isNotEmpty(logLogin.getIp())) {
            criteria.andLike("ip", "%" + logLogin.getIp() + "%");
        }
		if (StringUtil.isNotEmpty(logLogin.getUserName())) {
            criteria.andLike("userName", "%" + logLogin.getUserName() + "%");
        }

 
        if (logLogin.getUserId() != null) {
            criteria.andEqualTo("userId", logLogin.getUserId());
        }
   
        
        if (logLogin.getLoginType() != null) {
            criteria.andEqualTo("loginType", logLogin.getLoginType());
        }
        if (logLogin.getLoginDate() != null) {
            criteria.andGreaterThanOrEqualTo("loginDate", logLogin.getLoginDate());
        }

        //order default
        if(StringUtils.isEmpty(page.getSort())){
        	page.setSort("loginDate");
        }
        if(StringUtils.isEmpty(page.getOrder())){
        	page.setOrder("desc");
        }
        example.setOrderByClause(page.getOrderClause());
		
		// 分页查询
		PageHelper.startPage(page.getCurrentPage(), page.getPageSize());
		List<LogLoginPO> list = selectByExample(example);
		PageInfo<LogLoginPO> pageInfo = new PageInfo<LogLoginPO>(list);
		PageVO<LogLoginPO> pageRet = new PageVO<LogLoginPO>(pageInfo);
		return pageRet;
	}



	@Override
	public LogLoginPO findLogLoginById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RetResult<Long> create(LogLoginPO record,RetResult<UserPO> loginRet){
		logger.info("----create--");
		long time=System.currentTimeMillis();
		RetResult<Long> ret = new RetResult<>();
		if(StringUtils.isEmpty(StringUtils.isEmpty(record.getIp()))){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "ip="+record.getIp()+" is empty", null);
			logger.warn("create="+ret.getMsgBody());
			return ret;
		}
		record.initCreate();
		this.getMapper().insert(record);
		ret.setData(record.getCid());
		return ret;
	}
	
	private static Properties propertiesLog=null;
	private  static Properties getPropertiesLog(Class clazz) {
		if(propertiesLog==null){
			String logType=null;
			 String key=logType;
			 Properties properties=TaskExecutorUtils.getPoolInfo(1, 50, 1000);
			propertiesLog=properties;
		}
		return propertiesLog;
	}

	@Override
	public void createLogLoginAsync(LogLoginPO record, RetResult<UserPO> loginRet){
		int timeLimit=0;
		final Class clazz=this.getClass();
		final String key="createLogLoginAsync";
		final Properties properties=getPropertiesLog(clazz);
		properties.put("limitTime", timeLimit);
		TaskExecutor taskExecutor=TaskExecutorUtils.getTaskExecutorInstance(Sysconfigs.getAppType(), clazz, key, properties);
		final RedisTemplate redisTemplate=RedisUtil.getRedisTemplate();
		this.saveLogLoginBatch(redisTemplate);
		taskExecutor.execute(new Runnable() {
			public void run() {
				try {
					record.initCreate();
					if(loginRet!=null){
						UserPO user=loginRet.getFirstData();
						if(user!=null){
							record.setUserId(user.getCid());
						}
						record.setOther(loginRet.toJson());
					}
					
					if(redisTemplate==null){
						getMapper().insert(record);
					}
					else{
						redisTemplate.boundListOps(REDIS_LOG_LOGIN).rightPush(record);
					}
				} catch (Exception e) {
					logger.error("----createLogLoginAsync, error:"+e.getMessage(), e);
				}
			}
		});
	}
	
	public final static String REDIS_LOG_LOGIN="logLogins";
	/**
	 * 异步批量保存，频率不用太高
	 * @param redisTemplate
	 */
	private synchronized void saveLogLoginBatch(final RedisTemplate redisTemplate){
		int timeLimit=3;
		final String redisKey=REDIS_LOG_LOGIN;
		if(RedisUtil.isRunLimit(this.getClass(), timeLimit, redisKey)){//间隔低于限制时间
			return;
		}
		if(!RedisUtil.tryLock(redisKey)){
			return;
		}
		final int sizeGet=200;
		TaskExecutor taskExecutor=TaskExecutorUtils.getExecutorSingle(Sysconfigs.getAppType(), this.getClass(), "saveLogLoginBatch", timeLimit);
		taskExecutor.execute(new Runnable() {
			public void run() {
				List<LogLoginPO> logLoginList=null;
//				if(SpringContextUtil.containsBean("redisClusterConfiguration")){
//					logLoginList=RedisListUtil.getListWithRemove(redisTemplate, redisKey, sizeGet);
//				}
//				else{
//					logLoginList=RedisListUtil.getListWithPop(redisTemplate, redisKey, sizeGet);
//				}
				
				Long size=redisTemplate.boundListOps(redisKey).size();
				if(size>sizeGet){
					size=sizeGet+0l;
				}
				logLoginList=new ArrayList<>();
				LogInfoVO logInfoVO=null;
				for(int i=0; i<size; i++){
					logInfoVO=(LogInfoVO)redisTemplate.boundListOps(redisKey).leftPop();
					if(logInfoVO!=null){
						logList.add(logInfoVO);
					}
				}
				
				if(logLoginList.size()>0){
					logger.info("------saveLogLoginBatch--sizeGet="+sizeGet+" listSize="+logLoginList.size());
					mapper.insertList(logLoginList);
				}
				RedisUtil.unLock(redisKey);
			}
		});
	}
}
