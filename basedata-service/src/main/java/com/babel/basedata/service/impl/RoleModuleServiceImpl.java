package com.babel.basedata.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.impl.BaseService;
import com.babel.basedata.logger.LogService;
import com.babel.basedata.mapper.RoleModuleMapper;
import com.babel.basedata.model.RoleModulePO;
import com.babel.basedata.service.IRoleModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("roleModuleService")
public class RoleModuleServiceImpl extends BaseService<RoleModulePO> implements IRoleModuleService{
	 private static final Log logger = LogFactory.getLog(RoleModuleServiceImpl.class);
	 
	 @Autowired
	 private RoleModuleMapper mapper;
	 
	@Override
	public RoleModuleMapper getMapper() {
		return mapper;
	}
	
	
	
	@Override
	public RoleModulePO findRoleModuleById(Long id) {
		logger.info("----findRoleModuleById--id="+id);
		return mapper.selectByPrimaryKey(id);
	}
	
	@Override
	public List<RoleModulePO> findRoleModuleByRoleId(Long roleId) {
		logger.info("----findRoleModuleByRoleId--roleId="+roleId);
		Example example = new Example(RoleModulePO.class);
//		  example.selectProperties("nameCn","nameEn","code","cid");
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		criteria.andEqualTo("roleId", roleId);
		return mapper.selectByExample(example);
	}
	
	public List<RoleModulePO> findRoleModuleByRoleIds(List<Long> roleIdList) {
		logger.info("----findRoleModuleByRoleIds--roleIdList="+roleIdList);
		if(roleIdList==null||roleIdList.size()==0){
			logger.warn("----findRoleModuleByRoleIds--roleIdList="+roleIdList);
			return new ArrayList<>();
		}
		Example example = new Example(RoleModulePO.class);
//		  example.selectProperties("nameCn","nameEn","code","cid");
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		criteria.andIn("roleId", roleIdList);
		return mapper.selectByExample(example);
	}
	
	public List<Map<String, Object>> getRoleModuleCountByParentId(List<Long> roleIdList, List<Long> parentIdList){
		if(parentIdList.size()>0 && roleIdList.size()>0){
			return mapper.getRoleModuleCountByParentId(roleIdList, parentIdList);
		}
		else{
			logger.warn("----getRoleModuleCountByParentId--roleIdList="+roleIdList+" parentIdList="+parentIdList);
		}
		return new ArrayList<>();
	}
	
	@Override
	public PageVO<RoleModulePO> findPageByRoleModule(RoleModulePO roleModule, PageVO<RoleModulePO> page) {
		logger.info("----findRoleModuleById--id="+roleModule.getCid());
		Example example = new Example(RoleModulePO.class);
//		  example.selectProperties("nameCn","nameEn","code","cid");
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		if (StringUtil.isNotEmpty(roleModule.getName())) {
			criteria.andLike("name", "%" + roleModule.getName() + "%");
		}
		if (StringUtil.isNotEmpty(roleModule.getCode())) {
			criteria.andLike("code", "%" + roleModule.getCode() + "%");
		}
		if (roleModule.getCid() != null) {
			criteria.andEqualTo("cid", roleModule.getCid());
		}
		
		// 分页查询
		PageHelper.startPage(page.getCurrentPage(), page.getPageSize());
		List<RoleModulePO> list = selectByExample(example);
		PageInfo<RoleModulePO> pageInfo = new PageInfo<RoleModulePO>(list);
		PageVO<RoleModulePO> pageRet = new PageVO<RoleModulePO>(pageInfo);
		return pageRet;
	}
	
	private List<Long> getLongFromStr(String ids){
		String[] m_ids=ids.split(",");
		List<Long> idList=new ArrayList();
		for(String m_id:m_ids){
			if(!StringUtils.isEmpty(m_id) && !"null".equals(m_id)){
				idList.add(Long.valueOf(m_id));
			}
		}
		return idList;
	}
	
	@LogService(title="roleModule.saveRoleModules",author="cjh",calls="insertList,updateListByIdSelective")
	public RetResult<String> saveRoleModules(Long roleId, String moduleIds, Long operUserId){
		RetResult<String> ret = new RetResult<String>();
		List<RoleModulePO> rmList=this.findRoleModuleByRoleId(roleId);
		List<Long> moduleIdList=this.getLongFromStr(moduleIds);
		
		List<RoleModulePO> changeList=new ArrayList<>();
		RoleModulePO roleModule=null;
		boolean isExist=false;
		for(RoleModulePO rm:rmList){
			isExist=false;
			for(Long mId:moduleIdList){
				if(mId.longValue()==rm.getModuleId().longValue()){
					isExist=true;
					break;
				}
			}
			if(!isExist){
				rm.setIfDel(1);
				rm.initUpdate();
				rm.setModifyUser(operUserId);
				this.updateNotNull(rm);
				changeList.add(rm);
			}
		}
		
//		if(changeList.size()>0)
//			this.updateNotNullBatch(changeList);
		logger.info("------saveRoleModules--roleId="+roleId+" moduleIds="+moduleIds+" delSize="+changeList.size());
		changeList.clear();
		
		for(Long mId:moduleIdList){
			isExist=false;
			for(RoleModulePO rm:rmList){
				if(mId.longValue()==rm.getModuleId().longValue()){
					isExist=true;
					break;
				}
			}
			if(!isExist){
				roleModule=new RoleModulePO();
				roleModule.setRoleId(roleId);
				roleModule.setModuleId(mId);
				roleModule.setStatus(1);
				roleModule.initCreate();
				roleModule.setCreateUser(operUserId);
				roleModule.setModifyUser(operUserId);
				changeList.add(roleModule);
			}
		}
		if(changeList.size()>0)
			this.saveBatch(changeList);
		logger.info("------saveRoleModules--roleId="+roleId+" addSize="+changeList.size());
		
		return ret;
	}
	
	@Override
	@LogService(title="roleModule.create",author="cjh",calls="insert")
	public RetResult<Long> create(RoleModulePO record){
		logger.info("----create--");
		RetResult<Long> ret = new RetResult<>();
		if(StringUtils.isEmpty(record.getCode())||StringUtils.isEmpty(record.getName())){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "code="+record.getCode()+" or name="+record.getName()+" is empty", null);
			logger.warn("create="+ret.getMsgBody());
			return ret;
		}
		record.initCreate();
		this.getMapper().insert(record);
		ret.setData(record.getCid());
		return ret;
	}
	
	@Override
	@LogService(title="roleModule.update",author="cjh",calls="updateByPrimaryKey")
	public RetResult<Long> update(RoleModulePO record){
		logger.info("----update--");
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
		return ret;
	}
	
	@Override
	@LogService(title="roleModule.deleteVirtual",author="cjh",calls="updateByPrimaryKeySelective")
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid){
		long time=System.currentTimeMillis();
		RetResult<Integer> ret = new RetResult<>();
		if(cid==null||cid==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+cid+" is empty", null);
			logger.warn("deleteVirtual="+ret.getMsgBody());
			return ret;
		}
		RoleModulePO roleModule = new RoleModulePO();
		roleModule.setCid(cid);
		roleModule.setModifyUser(operUserId);
		int v=0;
		try {
			v = this.deleteVirtual(roleModule);
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "cid="+cid+",error="+e.getMessage(), e);
			return ret;
		}
		ret.setData(v);
		return ret;
	}

}
