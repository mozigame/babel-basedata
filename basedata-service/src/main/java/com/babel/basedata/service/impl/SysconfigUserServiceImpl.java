package com.babel.basedata.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.ISelfInject;
import com.babel.common.core.service.impl.BaseService;
import com.babel.common.core.util.CommUtil;
import com.babel.basedata.logger.LogService;
import com.babel.basedata.mapper.SysconfigUserMapper;
import com.babel.basedata.model.SysconfigPO;
import com.babel.basedata.model.SysconfigUserPO;
import com.babel.basedata.service.ISysconfigService;
import com.babel.basedata.service.ISysconfigUserService;
import com.babel.basedata.util.SysconfigUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("sysconfigUserService")
public class SysconfigUserServiceImpl extends BaseService<SysconfigUserPO> implements ISysconfigUserService, ISelfInject{
	 private static final Log logger = LogFactory.getLog(SysconfigUserServiceImpl.class);
	 @Autowired
	 private SysconfigUserMapper mapper;
	 
	 @Autowired
	 private ISysconfigService sysconfigService;
	 
	 private ISysconfigUserService _self;
	 public void setSelf(Object proxyBean) { //通过InjectBeanSelfProcessor注入自己（目标对象）的AOP代理对象  
	        this._self = (ISysconfigUserService) proxyBean;  
	    }  
	 
	@Override
	public SysconfigUserMapper getMapper() {
		return mapper;
	}

	 public List<SysconfigUserPO> selectBySysconfigUser(SysconfigUserPO item, int page, int rows){
		 Example example = new Example(SysconfigUserPO.class);
	        
       example.selectProperties("color","itemCode","itemName","orderCount","itemValue","sysconfigId","attr1","attr2","cid");
       example.setOrderByClause("sysconfig_Id,order_count");
       Example.Criteria criteria = example.createCriteria();
       criteria.andEqualTo("ifDel", 0);
       criteria.andEqualTo("status", 1);
       if (item.getCid() != null) {
           criteria.andEqualTo("cid", item.getCid());
       }
       if (item.getSysconfigId() != null) {
           criteria.andEqualTo("sysconfigId", item.getSysconfigId());
       }
       //分页查询
       PageHelper.startPage(page, rows);
       return selectByExample(example); 
	 }
	@Override
	public RetResult<SysconfigUserPO> findSysconfigUserById(Long id) {
		RetResult<SysconfigUserPO> ret = new RetResult<>();
		if(id==null||id.longValue()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "invalid id:"+id, null);
			return ret;
		}
		ret.setData(mapper.selectByPrimaryKey(id));
		return ret;
	}
	
	public List<SysconfigUserPO> findSysconfigUserListByUsers(List<Long> userIdList, List<Long> sysconfigIds){
		Example example = new Example(SysconfigUserPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		if(userIdList!=null &&userIdList.size()>0){
			criteria.andIn("userId", userIdList);
		}
		criteria.andIn("sysconfigId", sysconfigIds);
		return mapper.selectByExample(example);
	}
	
	public List<SysconfigUserPO> findSysconfigUserByUser(Long userId,Long sysconfigId){
		Example example = new Example(SysconfigUserPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("sysconfigId", sysconfigId);
		return mapper.selectByExample(example);
	}
	
	public List<SysconfigUserPO> findSysconfigUserBySysconfigId(Long sysconfigId) {
		logger.info("----findSysconfigUserBySysconfigId--sysconfigId="+sysconfigId);
		
		Example example = new Example(SysconfigUserPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("sysconfigId", sysconfigId);
		return mapper.selectByExample(example);
	}
	
	@CacheEvict(value = "basedata", key = "'getSysconfigUserByCode'+#userId+'__'+#sysconfigCode" )
//	@CacheEvict("sysconfigUsers")
	public void findSysconfigUserBySysconfigCode_flushCache(Long userId, String sysconfigCode){
		logger.info("----flushCache--lsysconfigCode="+sysconfigCode);
	}
	
	@Cacheable(value = "basedata", key = "'getSysconfigUserByCode'+#userId+'__'+#sysconfigCode" )
//	@Cacheable("sysconfigUsers")
	public RetResult<SysconfigUserPO> findSysconfigUserBySysconfigCode(Long userId, String sysconfigCode) {
		logger.info("----findSysconfigUserBySysconfigCode--sysconfigCode="+sysconfigCode);
		RetResult<SysconfigUserPO> ret = new RetResult<>();
		
	
		if(sysconfigCode==null){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "invalid sysconfigCode:"+sysconfigCode, null);
			return ret;
		}
		else if(sysconfigCode.length()>200){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "sysconfigCode:"+sysconfigCode+" out of length:"+200, null);
			return ret;
		}
		
		String[] codes=sysconfigCode.split(",");
		List<String> codeList=new ArrayList<String>();
		for(String code:codes){
			codeList.add(code.trim());
		}
		
		try {
			List<SysconfigUserPO> list= this.getMapper().findSysconfigUserListByCode(userId, codeList);
			ret.setDataList(list);
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "sysconfigCode:"+sysconfigCode, e);
			return ret;
		}
		return ret;
	}
	
	private void findSysconfigUserBySysconfigCode_flushCache(Long userId,Long sysconfigId){
		SysconfigPO sysconfig=this.sysconfigService.findSysconfigById(sysconfigId);
		if(sysconfig!=null){
			List<SysconfigUserPO> sysUserList=this.findSysconfigUserByUser(userId, sysconfigId);
			Object value=CommUtil.nvl(sysconfig.getValue(), sysconfig.getValueDefault());
			if(sysUserList.size()>0){
				SysconfigUserPO sysUser=sysUserList.get(0);
				value=sysUser.getValue();
			}
			Map<String, Object> configMap=SysconfigUsers.getUserMap(userId);
			if(configMap!=null){
				configMap.put(sysconfig.getCode(),  value);
			}
			this._self.findSysconfigUserBySysconfigCode_flushCache(userId, sysconfig.getCode());
		}
	}
	
	@Override
	public PageVO<SysconfigUserPO> findPageBySysconfigUser(SysconfigUserPO sysconfigUser, PageVO<SysconfigUserPO> page) {
		logger.info("----findPageBySysconfigUser--sysconfigId="+sysconfigUser.getSysconfigId());
		Example example = new Example(SysconfigUserPO.class);
//		  example.selectProperties("nameCn","nameEn","code","cid");
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		
        if (sysconfigUser.getSysconfigId() != null) {
            criteria.andEqualTo("sysconfigId", sysconfigUser.getSysconfigId());
        }
		
		// 分页查询
		PageHelper.startPage(page.getCurrentPage(), page.getPageSize());
		List<SysconfigUserPO> list = selectByExample(example);
		PageInfo<SysconfigUserPO> pageInfo = new PageInfo<SysconfigUserPO>(list);
		PageVO<SysconfigUserPO> pageRet = new PageVO<SysconfigUserPO>(pageInfo);
		return pageRet;
	}
	
	/**
	 * 加载默认设置
	 */
	public Map<String, Object> initSysconfigBaseUserMap() {
		Integer confType=4;
		Map<String, Object> configMap = new HashMap<>();
		RetResult<SysconfigPO> ret = this.sysconfigService.findSysconfigAll(confType, null);

		if (ret.isSuccess()) {
			Collection<SysconfigPO> sysList=ret.getDataList();
			for(SysconfigPO sys:sysList){
				Object value=CommUtil.nvl(sys.getValue(), sys.getValueDefault());
				configMap.put(sys.getCode(), value);
			}
		}

		return configMap;
	}
	
	/**
	 * 加载用户设置
	 */
	public Map<String, Object> initSysconfigUserMap(Long userId) {
		Integer confType=4;
		Map<String, Object> configMap = new HashMap<>();
		RetResult<SysconfigPO> ret = this.sysconfigService.findSysconfigAll(confType, null);

		if (ret.isSuccess()) {
			List<Long> cidList = new ArrayList<>();
			Collection<SysconfigPO> sysList=ret.getDataList();
			for (SysconfigPO sys : sysList) {
				cidList.add(sys.getCid());
			}
			
			if(cidList.isEmpty()){
				return configMap;
			}
			List<SysconfigUserPO> sysUserList=this.findSysconfigUserListByUsers(CommUtil.newList(userId), cidList);
			for(SysconfigPO sys:sysList){
				Object value=CommUtil.nvl(sys.getValue(), sys.getValueDefault());
				for(SysconfigUserPO sysUser:sysUserList){
					if(sysUser.getSysconfigId().longValue()==sys.getCid().longValue()){
						value=sysUser.getValue();
						break;
					}
				}
				configMap.put(sys.getCode(), value);
			}
		}

		return configMap;
	}
	
	/**
	 * 查询所有用户的系统参数设置
	 * @param userIdList 为空表示全部
	 * @return
	 */
	public Map<Long, Map<String, Object>> initSysconfigUserMapAll(List<Long> userIdList){
		if(userIdList==null){
			userIdList=new ArrayList();
		}
		Integer confType=4;
		Map<Long, Map<String, Object>> userParamMap=new HashMap<>();
		RetResult<SysconfigPO> ret = this.sysconfigService.findSysconfigAll(confType, null);
		
	

		if (ret.isSuccess()) {
			List<Long> cidList = new ArrayList<>();
			Collection<SysconfigPO> sysList=ret.getDataList();
			for (SysconfigPO sys : sysList) {
				cidList.add(sys.getCid());
			}
			
			if(cidList.isEmpty()){
				return userParamMap;
			}
			List<SysconfigUserPO> sysUserList=this.findSysconfigUserListByUsers(userIdList, cidList);
			Set<Long> userIdSet=new HashSet<>();
			for(SysconfigUserPO sysUser:sysUserList){
				userIdSet.add(sysUser.getUserId());
			}
			
			
			for(Long userId:userIdSet){
				Map<String, Object> configMap=new HashMap<>();
				for(SysconfigPO sys:sysList){
					Object value=CommUtil.nvl(sys.getValue(), sys.getValueDefault());
					for(SysconfigUserPO sysUser:sysUserList){
						if(sysUser.getSysconfigId().longValue()==sys.getCid().longValue()
								&& sysUser.getUserId().longValue()==userId.longValue()){
							value=sysUser.getValue();
							configMap.put(sys.getCode(), value);
							break;
						}
					}
					
				}
				userParamMap.put(userId, configMap);
			}
		}
		
		return userParamMap;
	}
	
	public Map<String, Object> initSysconfigUserMapAll() {
		Integer confType=4;
		Map<String, Object> configMap = new HashMap<>();
		RetResult<SysconfigPO> ret = this.sysconfigService.findSysconfigAll(confType, null);

		if (ret.isSuccess()) {
			List<Long> cidList = new ArrayList<>();
			for (SysconfigPO sys : ret.getDataList()) {
				cidList.add(sys.getCid());
			}
			
			if(cidList.isEmpty()){
				return configMap;
			}
			List<Long> userIdList=null;
			List<SysconfigUserPO> sysUserList=this.findSysconfigUserListByUsers(userIdList, cidList);
			for(SysconfigPO sys:ret.getDataList()){
				Object value=CommUtil.nvl(sys.getValue(), sys.getValueDefault());
				for(SysconfigUserPO sysUser:sysUserList){
					if(sysUser.getSysconfigId().longValue()==sys.getCid().longValue()){
						value=sysUser.getValue();
						break;
					}
				}
				configMap.put(sys.getCode(), value);
			}
		}

		return configMap;
	}
	
	@LogService(title="sysconfigUser.create",author="cjh",calls="insert", descs="sysconfigId=#{0.sysconfigId}")
	public RetResult<Long> create(SysconfigUserPO record){
		logger.info("----create--");
		RetResult<Long> ret = new RetResult<>();
		record.initCreate();
		this.getMapper().insert(record);
		this.findSysconfigUserBySysconfigCode_flushCache(record.getUserId(), record.getSysconfigId());
		ret.setData(record.getCid());
		return ret;
	}
	
	@LogService(title="sysconfigUser.update",author="cjh",calls="updateByPrimaryKeySelective", descs="sysconfigId=#{0.sysconfigId}")
	public RetResult<Long> update(SysconfigUserPO record){
		logger.info("----update--");
		RetResult<Long> ret = new RetResult<>();
		if(record.getCid()==null||record.getCid().longValue()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+record.getCid()+" is empty", null);
			return ret;
		}
		record.initUpdate();
		this.mapper.updateByPrimaryKeySelective(record);
		this.findSysconfigUserBySysconfigCode_flushCache(record.getUserId(), record.getSysconfigId());
		ret.setData(record.getCid());
		
		return ret;
	}
	
	@LogService(title="sysconfigUser.deleteVirtual",author="cjh",calls="updateByPrimaryKeySelective")
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid){
		logger.info("----deleteVirtual--");
		RetResult<Integer> ret = new RetResult<>();
		if(cid==null||cid==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+cid+" is empty", null);
			return ret;
		}
		SysconfigUserPO sysconfigUser = new SysconfigUserPO();
		sysconfigUser.setCid(cid);
		sysconfigUser.setModifyUser(operUserId);
		int v=0;
		try {
			v = this.deleteVirtual(sysconfigUser);
			this.findSysconfigUserBySysconfigCode_flushCache(sysconfigUser.getUserId(), sysconfigUser.getSysconfigId());
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "cid="+cid+",error="+e.getMessage(), e);
		}
		ret.setData(v);
		return ret;
	}
	
	@LogService(title="sysconfigUser.deleteVirtualByUser",author="cjh",calls="updateByPrimaryKeySelective")
	public RetResult<Integer> deleteVirtualByUser(Long userId, Long sysconfigId){
		logger.info("----deleteVirtual--");
		RetResult<Integer> ret = new RetResult<>();
		if(sysconfigId==null||sysconfigId==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "sysconfigId="+sysconfigId+" is empty", null);
			return ret;
		}
		
		List<SysconfigUserPO> sysUserList=this.findSysconfigUserByUser(userId, sysconfigId);
		if(sysUserList.size()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_NOT_FOUND, "userId="+userId+" sysconfigId="+sysconfigId+" not found", null);
			return ret;
		}
		
		SysconfigUserPO sysconfigUser = sysUserList.get(0);
		int v=0;
		try {
			v = this.deleteVirtual(sysconfigUser);
			this.findSysconfigUserBySysconfigCode_flushCache(sysconfigUser.getUserId(), sysconfigUser.getSysconfigId());
		} catch (Exception e) {
			e.printStackTrace();
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "userId="+userId+",sysconfigId="+sysconfigId+",error="+e.getMessage(), e);
		}
		ret.setData(v);
		return ret;
	}

}
