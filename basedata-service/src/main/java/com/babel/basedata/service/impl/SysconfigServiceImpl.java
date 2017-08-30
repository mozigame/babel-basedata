package com.babel.basedata.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.babel.basedata.logger.LogService;
import com.babel.basedata.mapper.SysconfigMapper;
import com.babel.basedata.model.SysconfigPO;
import com.babel.basedata.service.ISysconfigService;
import com.babel.basedata.util.Sysconfigs;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.ISelfInject;
import com.babel.common.core.service.impl.BaseService;
import com.babel.common.core.util.CommUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

@Service("sysconfigService")
public class SysconfigServiceImpl extends BaseService<SysconfigPO> implements ISysconfigService, ISelfInject{
	 private static final Log logger = LogFactory.getLog(SysconfigServiceImpl.class);
	 
	@Autowired
	private SysconfigMapper mapper;

	@Override
	public SysconfigMapper getMapper() {
		return mapper;
	}

//	@Autowired
//	private ILogDbService logDbService;
	
//	@Resource(name = "taskExecutor")
//	private TaskExecutor taskExecutor;

	private ISysconfigService _self;

	public void setSelf(Object proxyBean) { // 通过InjectBeanSelfProcessor注入自己（目标对象）的AOP代理对象
		this._self = (ISysconfigService) proxyBean;
	}
	
	@Override
//	@Cacheable(value = "default",key = "'getSysconfig_' + #id")
	public SysconfigPO findSysconfigById(Long id) {
		logger.info("----findSysconfigById--id="+id);
		return mapper.selectByPrimaryKey(id);
	}
	
	public List<Map<String, Object>> findSysconfigByParentIdsCount(Integer confType, 
			List<Long> pidList){
		return mapper.findSysconfigByParentIdsCount(confType, pidList);
	}
	
	public List<SysconfigPO> findSysconfigByParentIds(Integer confType, List<Long> parentIds){
		logger.info("----findSysconfigByParentIds--confType="+confType+" parentIds="+parentIds);
		if(parentIds.size()==0){
			return new ArrayList<>();
		}
		
//		Example example = new Example(SysconfigPO.class);
////		  example.selectProperties("nameCn","nameEn","code","cid");
//		Example.Criteria criteria = example.createCriteria();
//		criteria.andEqualTo("ifDel", 0);
//		criteria.andIn("pid", parentIds);
//		if(confType!=null){
//			criteria.andEqualTo("confType", confType);
//		}
//		example.setOrderByClause("order_count asc");
//		List<SysconfigPO> list= this.selectByExample(example);
		List<SysconfigPO> list = this.getMapper().findSysconfigByParentIds(confType, parentIds);
		
		return list;
	}
	
	public List<SysconfigPO> findSysconfigByIds(List<Long> idList){
		if(idList.size()==0){
			return new ArrayList<>();
		}
		Example example = new Example(SysconfigPO.class);
		example.selectProperties("code","cid","name","pid","orderCount");
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		criteria.andIn("cid", idList);
		return this.selectByExample(example);
	}
	
	@Override
	@LogService(title="lookup.findPageBySysconfig",author="cjh",calls="selectByExample")
	public PageVO<SysconfigPO> findPageBySysconfig(SysconfigPO sysconfig, PageVO<SysconfigPO> page) {
		logger.info("----findSysconfigById--id="+sysconfig.getCid()+" status="+sysconfig.getStatus());
		Example example = new Example(SysconfigPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		if (StringUtil.isNotEmpty(sysconfig.getName())) {
			criteria.andLike("name", "%" + sysconfig.getName() + "%");
		}
		if (StringUtil.isNotEmpty(sysconfig.getCode())) {
			criteria.andLike("code", "%" + sysconfig.getCode() + "%");
		}
		if(sysconfig.getStatus()!=null){
			criteria.andEqualTo("status", sysconfig.getStatus());
		}
		if (sysconfig.getCid() != null && sysconfig.getCid()>=0) {
			criteria.andEqualTo("cid", sysconfig.getCid());
		}
		if (sysconfig.getConfType() != null) {
			criteria.andEqualTo("confType", sysconfig.getConfType());
		}
		if (sysconfig.getIfEnv() != null) {
			criteria.andEqualTo("ifEnv", sysconfig.getIfEnv());
		}
		
		//order default
        if(StringUtils.isEmpty(page.getSort())){
        	example.setOrderByClause("pid, order_count desc");
        }
        else{
        	example.setOrderByClause(page.getOrderClause());
        }
		
		// 分页查询
		PageHelper.startPage(page.getCurrentPage(), page.getPageSize());
		List<SysconfigPO> list = selectByExample(example);
		
		this.setParentName(list);
		
		PageInfo<SysconfigPO> pageInfo = new PageInfo<SysconfigPO>(list);
		PageVO<SysconfigPO> pageRet = new PageVO<SysconfigPO>(pageInfo);
		return pageRet;
	}
	
	/**
	 * @param list
	 */
	private void setParentName(List<SysconfigPO> list) {
		List<Long> idList=new ArrayList();
		for(SysconfigPO m:list){
			if(!idList.contains(m.getPid())){
				idList.add(m.getPid());
			}
		}
		if(idList.size()>0){
			List<SysconfigPO> mList=this.findSysconfigByIds(idList);
			for(SysconfigPO mo:list){
				for(SysconfigPO m:mList){
					if(mo.getPid().longValue()==m.getCid().longValue()){
						mo.setParentName(m.getName());
						break;
					}
				}
			}
		}
	}
	
	@Override
	@LogService(title="lookup.create",author="cjh",calls="insert")
	public RetResult<Long> create(SysconfigPO record){
		logger.info("----create--");
		long time=System.currentTimeMillis();
		RetResult<Long> ret = new RetResult<>();
		if(StringUtils.isEmpty(record.getCode())||StringUtils.isEmpty(record.getName())){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "code="+record.getCode()+" or name="+record.getName()+" is empty", null);
			logger.warn("create="+ret.getMsgBody());
			return ret;
		}
		record.initCreate();
		try {
			this.getMapper().insertSysconfig(record);
			ret.setData(record.getCid());
			this.findSysconfigAll_cacheFlush(record.getConfType(), record.getPid());
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "同一节点下的编码不及名称不能重复:"+e.getMessage(), e);
		}
//		this.logDbService.info(record, "create", "cid="+record.getCid(), System.currentTimeMillis()-time);
		return ret;
	}
	
	public RetResult<Long> updateRel(Long operId, Long cid, Long oldParentId, Long newParentId){
		logger.info("----update--");
		long time=System.currentTimeMillis();
		RetResult<Long> ret = new RetResult<>();
		if(cid==null||cid.longValue()==0){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid is empty", null);
    		return ret;
    	}
    	if(oldParentId==null||oldParentId.longValue()==0){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "oldParentId is empty", null);
    		return ret;
    	}
    	if(newParentId==null||newParentId.longValue()==0){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "newParentId is empty", null);
    		return ret;
    	}
    	SysconfigPO sysconfig=new SysconfigPO();
//    	sysconfig.setCid(cid);
    	sysconfig.initUpdate();
    	sysconfig.setModifyUser(operId);
    	sysconfig.setPid(newParentId);
    	
    	Example example = new Example(SysconfigPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("pid", oldParentId);
		criteria.andEqualTo("cid", cid);
		try {
			this.mapper.updateByExampleSelective(sysconfig, example);
			ret.setData(cid);
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "同一节点下的编码不及名称不能重复:"+e.getMessage(), e);
		}
		return ret;
	}
	
	@Override
	@LogService(title="lookup.update",author="cjh",calls="updateByPrimaryKey")
	public RetResult<Long> update(SysconfigPO record){
		logger.info("----update--");
//		long time=System.currentTimeMillis();
		RetResult<Long> ret = new RetResult<>();
		if(record.getCid()==null||record.getCid().longValue()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+record.getCid()+" is empty", null);
			logger.warn("update="+ret.getMsgBody());
			return ret;
		}
		record.initUpdate();
		record.setIfDel(0);
		this.mapper.updateByPrimaryKey(record);
		ret.setData(record.getCid());
		this.findSysconfigAll_cacheFlush(record.getConfType(), record.getPid());
//		this.logDbService.info(record, "update", "cid="+record.getCid(), System.currentTimeMillis()-time);
		return ret;
	}
	
	@Override
	public RetResult<SysconfigPO> getSysconfigByCode(Integer confType, String code) {
		RetResult<SysconfigPO> ret = new RetResult<SysconfigPO>();
		Example example = new Example(SysconfigPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		criteria.andEqualTo("status", 1);
		criteria.andEqualTo("code", code);
		if(confType!=null){
			criteria.andEqualTo("confType", confType);
		}
		ret.setDataList(this.selectByExample(example));
		return ret;
	}
	
	@Cacheable(value = "basedata", key = "'getSysconfigByCodeCache_'+#confType+'_'+#code" )
	public SysconfigPO getSysconfigByCodeCache(Integer confType, String code){
		RetResult<SysconfigPO> ret=this.getSysconfigByCode(confType, code);
		if(ret.isSuccess()){
			return ret.getFirstData();
		}
		return null;
	}
	
	@CacheEvict(value = "basedata", key = "'getSysconfigByCodeCache_'+#confType+'_'+#code" )
	public void getSysconfigByCodeCache_cacheFlush(Integer confType, String code){
		logger.info("---findSysconfigAll_cacheFlush--code="+code);
	}
	
	public RetResult<SysconfigPO> getSysconfigByName(Integer confType, String name){
		RetResult<SysconfigPO> ret = new RetResult<SysconfigPO>();
    	if(StringUtils.isEmpty(name)){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "name is empty", null);
    		return ret;
    	}
    	
    	Example example = new Example(SysconfigPO.class);
//		  example.selectProperties("nameCn","nameEn","code","cid");
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		criteria.andLike("name", name+"%");
		if(confType!=null){
			criteria.andEqualTo("confType", confType);
		}
        List<SysconfigPO> list= this.selectByExample(example);
        ret.setDataList(list);
        return ret;
	}
	
	

	
	private static final String[] configEnvStarts={"dev.","app.","sql.","service."};
//	private static Map<String, Object> envMap=new HashMap<>();
	private static Date findSysconfig1MaxDate=null;
	private static Date findSysconfig1LoadDate=null;
	
	
	private boolean isStartWith(String code, String[] sysTypes){
		String[] starts=concat(configEnvStarts, sysTypes);
		for(String start:starts){
			if(code.startsWith(start)){
				return true;
			}
		}
		return false;
	}
	
	
	
	private String[] concat(String[] a, String[] b) {  
	   String[] c= new String[a.length+b.length];  
	   System.arraycopy(a, 0, c, 0, a.length);  
	   System.arraycopy(b, 0, c, a.length, b.length);  
	   return c;  
	}
	
	/**
	 * sysType用于支持多个子系统的方式
	 * 如果是平台则sysType为空或者不配
	 * 如果是子系统则sysType=子系统编码+"."，对应为子系统code的开头部份，如btl则以btl.开头的都算
	 */
	public  Map<String, Object> initSysconfigMap(Integer confType, String sysType){
		Map<String, Object> configMap=new HashMap<>();
		RetResult<SysconfigPO> ret=this.findSysconfigAll(confType, null);
		if(ret.isSuccess()){
			logger.info("-----initSysconfigMap--confType="+confType+" size="+ret.getSize());
			Collection<SysconfigPO> sysList=ret.getDataList();
			boolean isEmptySysType=StringUtils.isEmpty(sysType);
			if(sysType==null){
				sysType="";
			}
			String[] sysTypes=sysType.split(",");
			for(SysconfigPO sys:sysList){
				if(isEmptySysType || isStartWith(sys.getCode(), sysTypes)){//code.startsWith("dev.")
					if(sys.getIfEnv()!=null && sys.getIfEnv().longValue()==1){
						Object value=CommUtil.nvl(sys.getValue(), sys.getValueDefault());
						Sysconfigs.putMapSetByKey(configMap, sys.getCode(), value);
					}
				}
			}
			//如果是生产模式，则把dev.开头的所有参数，全部置为false，即从内存上关闭所有开发状态
			if("product".equals(configMap.get("app.runType"))){
				Set<String> keys=configMap.keySet();
				for(String key:keys){
					if(key.startsWith("dev.")){//key.startsWith("dev.")
						configMap.put(key, "false");
					}
				}
			}
			String menifestInfo="/easyui.appcache";
			if("true".equals(configMap.get("dev.no_appcache"))){
				menifestInfo="";
			}
			configMap.put("app_manifest", menifestInfo);
			
		}
		else{
			logger.warn("-----initSysconfigMap--confType="+confType+" error:"+ret.getMsgBody());
		}
		return configMap;
	}

	
	
	private void findSysconfigAll_cacheFlush(Integer confType, Long parentId){
		if(parentId==null){
			this._self.findSysconfigAll_cacheFlush(confType, null);
			return;
		}
		resetMaxDate(confType);
		SysconfigPO sysconfig=this.findSysconfigById(parentId);
		if(sysconfig!=null){
			Object value=CommUtil.nvl(sysconfig.getValue(), sysconfig.getValueDefault());
			Sysconfigs.getEnvMap().put(sysconfig.getCode(), value);
			this._self.getSysconfigByCodeCache_cacheFlush(confType, sysconfig.getCode());
			this._self.findSysconfigAll_cacheFlush(confType, sysconfig.getCode());
		}
		else{
			this._self.getSysconfigByCodeCache_cacheFlush(confType, null);
			this._self.findSysconfigAll_cacheFlush(confType, null);
		}
	}
	
	@Cacheable(value = "basedata", key = "'findSysconfigAllCache_'+#confType+'_'+#parentCode" )
	public RetResult<SysconfigPO> findSysconfigAllCache(Integer confType, String parentCode){
		return this.findSysconfigAll(confType, parentCode);
	}
	
	@CacheEvict(value = "basedata", key = "'findSysconfigAllCache_'+#confType+'_'+#parentCode" )
	public void findSysconfigAll_cacheFlush(Integer confType, String parentCode){
		logger.info("---findSysconfigAll_cacheFlush--parentCode="+parentCode);
	}
	
	public RetResult<SysconfigPO> findSysconfigAll(Integer confType, String parentCode){
		logger.debug("-----findSysconfigAll--parentCode="+parentCode);
		RetResult<SysconfigPO> ret = new RetResult<SysconfigPO>();
		
//		Example example =this.findSysconfigAllExample(confType, parentCode);
		
//		ret.setDataList(this.selectByExample(example));
		ret.setDataList(this.mapper.findSysconfigAll(confType, parentCode));
		//如果dataList为空，取消该缓存
		if(ret.getDataList()==null||ret.getDataList().size()==0){
			this._self.findSysconfigAll_cacheFlush(confType, parentCode);
		}
		return ret;
	}
	
	private Integer getIntValue(Object obj){
		if(obj ==null ){
			return null;
		}
		if(obj instanceof Integer){
			return (Integer)obj;
		}
		else{
			return Integer.valueOf(""+obj);
		}
		
	}
	
	public Date findSysconfigAllMaxModifyDate(Integer confType, String parentCode){
		Date maxDate=null;
		Integer envLoadInteval=this.getIntValue(Sysconfigs.getEnvMap().get("app.env.load.interval"));
		if(envLoadInteval==null||envLoadInteval.intValue()==0){
			envLoadInteval=180;
		}
		if(findSysconfig1MaxDate==null || findSysconfig1LoadDate.before(DateUtils.addSeconds(new Date(), -180))){//超过3分钟就重新检查一下数据时间
			Example example =this.findSysconfigAllExample(confType, parentCode);
			maxDate= this.mapper.selectMaxModifyDateByExample(example);
//			System.out.println("-----findSysconfig1LoadDate="+findSysconfig1LoadDate+" newDate="+DateUtils.addSeconds(new Date(), -180));
			findSysconfig1MaxDate=maxDate;
			findSysconfig1LoadDate=new Date();
		}
		else{
			maxDate=findSysconfig1MaxDate;
		}
		return maxDate;
	}
	
	private Example findSysconfigAllExample(Integer confType, String parentCode){
		Example example = new Example(SysconfigPO.class);
		example.selectProperties("code", "name", "cid", "value", "valueDefault", "valueJson","value1", "value2","confType", "ifEnv","modifyDate");
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		criteria.andEqualTo("status", 1);
		if(confType!=null){
			criteria.andEqualTo("confType", confType);
		}
		
		if(!StringUtils.isEmpty(parentCode)){
			RetResult<SysconfigPO> parentRet = this.getSysconfigByCode(confType, parentCode);
			Long pid = null;
			if (parentRet.isSuccess()) {
				SysconfigPO parent = parentRet.getFirstData();
				if (parent != null) {
					pid = parent.getCid();
				}
			}
			criteria.andEqualTo("pid", pid);
		}
		return example;
	}
	
	private void resetMaxDate(Integer confType){
		if(confType.intValue()==1){
			findSysconfig1MaxDate=new Date();
			findSysconfig1LoadDate=new Date();
		}
	}
	
	@Override
	@LogService(title="lookup.deleteVirtual",author="cjh",calls="findSysconfigById,updateByPrimaryKeySelective")
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid){
		logger.info("----update--");
//		long time=System.currentTimeMillis();
		RetResult<Integer> ret = new RetResult<>();
		if(cid==null||cid==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+cid+" is empty", null);
			logger.warn("deleteVirtual="+ret.getMsgBody());
			return ret;
		}
		
		
		SysconfigPO sysconfig = new SysconfigPO();
		sysconfig.setCid(cid);
		sysconfig.setModifyUser(operUserId);
		
		try {
			int v = this.deleteVirtual(sysconfig);
			ret.setData(v);
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "cid="+cid+",error="+e.getMessage(), e);
		}
		
		
		SysconfigPO sys=this.findSysconfigById(cid);
		if(sys!=null){
			Sysconfigs.getEnvMap().remove(sysconfig.getCode());
			this.findSysconfigAll_cacheFlush(sys.getConfType(), sys.getPid());
		}
//		this.logDbService.info(cid, "deleteVirtual", "cid="+cid, System.currentTimeMillis()-time);
		return ret;
	}
	
	@Override
	public SysconfigPO findByCode(String code) {
		SysconfigPO sysconfigPO = new SysconfigPO();
		sysconfigPO.setCode(code);
		List<SysconfigPO> list = mapper.select(sysconfigPO);
		if(list != null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

}
