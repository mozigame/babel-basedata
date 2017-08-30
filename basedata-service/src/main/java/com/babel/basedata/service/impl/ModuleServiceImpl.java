package com.babel.basedata.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.impl.BaseService;
import com.babel.basedata.entity.ModuleTreeVO;
import com.babel.basedata.logger.LogService;
import com.babel.basedata.mapper.ModuleMapper;
import com.babel.basedata.model.ModulePO;
import com.babel.basedata.model.RoleModulePO;
import com.babel.basedata.model.UserRolePO;
import com.babel.basedata.service.IModuleService;
import com.babel.basedata.service.IRoleModuleService;
import com.babel.basedata.service.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("moduleService")
public class ModuleServiceImpl extends BaseService<ModulePO> implements IModuleService{
 private static final Log logger = LogFactory.getLog(ModuleServiceImpl.class);
	 
	 @Autowired
	 private ModuleMapper mapper;
	 
	@Override
	public ModuleMapper getMapper() {
		return mapper;
	}
//	
//	 @Autowired
//	 private ILogDbService logDbService;
	@Autowired
	private IUserRoleService userRoleService;
	@Autowired
	private IRoleModuleService roleModuleService;
	
	
	@Override
	public ModulePO findModuleById(Long id) {
		logger.info("----findModuleById--id="+id);
		return mapper.selectByPrimaryKey(id);
	}
	
	public RetResult<ModulePO> getModuleByName(String name) {
		RetResult<ModulePO> ret = new RetResult<ModulePO>();
    	if(StringUtils.isEmpty(name)){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "name is empty", null);
    		return ret;
    	}
    	
    	Example example = new Example(ModulePO.class);
//		  example.selectProperties("nameCn","nameEn","code","cid");
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		criteria.andLike("name", name+"%");
        List<ModulePO> list= this.selectByExample(example);
        ret.setDataList(list);
        return ret;
	}
	
	public List<ModulePO> findModuleByParentIds(List<Long> moduleIds){
		if(moduleIds.size()==0){
			return new ArrayList<>();
		}
		Example example = new Example(ModulePO.class);
//		  example.selectProperties("nameCn","nameEn","code","cid");
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		criteria.andIn("parentId", moduleIds);
		example.setOrderByClause("order_count asc");
		return this.selectByExample(example);
	}
	
	/**
	 * 看parentId模块对应的子模块数量
	 */
	public List<Map<String, Object>> getModuleCountByParentId(@Param("parentIdList") List<Long> parentIdList){
		return this.mapper.getModuleCountByParentId(parentIdList);
	}
	
	public List<ModulePO> findRolesModuleByParentIds(List<Long> roleIdList, List<Long> moduleIds){
		List<ModuleTreeVO> mTreelist= this.findRolesModuleTreeByParentIds(roleIdList, moduleIds);

		List<ModulePO> list=new ArrayList<>();
		
		List<ModuleTreeVO> mTree2List=null;
		ModulePO m=null;
		for(ModuleTreeVO mTree:mTreelist){
			if(mTree.isChecked()){
				list.add(mTree);
			}
			else{
//				如果有子节点，则把当前文件加载出来
//				mTree2List= this.findRolesModuleTreeByParentIds(roleIdList, CommUtil.newList(mTree.getCid()));
//				if(mTree2List.size()>0){
//					list.add(mTree);
//				}
			}
		}
		logger.info("------findUserModuleByParentIds--roleIdList="+roleIdList+" moduleIds="+moduleIds+" list="+list.size());
		return list;
	}
	
	/**
	 * 用户角色模块菜单权限
	 */
	public List<ModulePO> findUserModuleByParentIds(Long userId, List<Long> moduleIds){
		if(moduleIds.size()==0){
			return new ArrayList<>();
		}
		List<UserRolePO> urList=this.userRoleService.findUserRoleByUserId(userId);
		
		List<Long> roleIdList=new ArrayList<>();
		for(UserRolePO ur:urList){
			roleIdList.add(ur.getRoleId());
		}
		
		
		return this.findRolesModuleByParentIds(roleIdList, moduleIds);
	}
	
	/**
	 * 角色对应的模块是否有权限
	 * 支持多角色，即角色权限可合并
	 */
	public List<ModuleTreeVO> findRolesModuleTreeByParentIds(List<Long> roleIdList, List<Long> moduleIds){
		List<RoleModulePO> roleModuleList=this.roleModuleService.findRoleModuleByRoleIds(roleIdList);
		
//		logger.info("-------findRolesModuleTreeByParentIds--roleIdList="+roleIdList+" roleModuleList="+roleModuleList.size());
		List<ModulePO> list=this.findModuleByParentIds(moduleIds);
		List<ModuleTreeVO> treeList=new ArrayList<>();
		ModuleTreeVO tree=null;
		RoleModulePO rm=null;
		List<Long> mIdList=new ArrayList<>();
		for(ModulePO m:list){
			mIdList.add(m.getCid());
		}
		//角色对应的子模块数量处理
		List<Map<String, Object>> mCountMapList=this.roleModuleService.getRoleModuleCountByParentId(roleIdList, mIdList);
		logger.info("------findRolesModuleTreeByParentIds--roleIdList="+roleIdList+" moduleIds="+moduleIds+" mIdList="+mIdList+" mCountMapList="+mCountMapList);
		Long pId=null;
		Long pCount=null;
		for(ModulePO m:list){
			tree=new ModuleTreeVO();
			tree.load(m);
			for(int i=0; i<roleModuleList.size(); i++){
				rm=roleModuleList.get(i);
				if(m.getCid().longValue()==rm.getModuleId().longValue()){
					tree.setChecked(true);
					break;
				}
			}
			if(!tree.isChecked()){
				for(Map<String, Object> mCountMap:mCountMapList){
					pId=getLongValue(mCountMap.get("parentId"));
					pCount=getLongValue(mCountMap.get("cout"));
					if(pId.longValue()==m.getCid().longValue() && pCount.longValue()>0){
						tree.setChecked(true);
					}
				}
			}
			treeList.add(tree);
		}
		
		return treeList;
		
	}
	
	private Long getLongValue(Object obj){
		if(obj==null){
			return 0l;
		}
		else if(obj instanceof BigDecimal){
			return ((BigDecimal)obj).longValue();
		}
		else if(obj instanceof Long){
			return (Long)obj;
		}
		else{
			return Long.valueOf(""+obj);
		}
	}
	
	
	
	public List<ModuleTreeVO> findRoleModuleTreeByParentIds(Long roleId, List<Long> moduleIds){
		List<RoleModulePO> roleModuleList=this.roleModuleService.findRoleModuleByRoleId(roleId);
		
		List<ModulePO> list=this.findModuleByParentIds(moduleIds);
		List<ModuleTreeVO> treeList=new ArrayList<>();
		ModuleTreeVO tree=null;
		RoleModulePO rm=null;
		for(ModulePO m:list){
			tree=new ModuleTreeVO();
			tree.load(m);
			for(int i=0; i<roleModuleList.size(); i++){
				rm=roleModuleList.get(i);
				if(m.getCid().longValue()==rm.getModuleId().longValue()){
					tree.setChecked(true);
					break;
				}
			}
			treeList.add(tree);
		}
		
		return treeList;
		
	}
	
	public List<ModulePO> findModuleByIds(List<Long> idList){
		if(idList.size()==0){
			return new ArrayList<>();
		}
		Example example = new Example(ModulePO.class);
		example.selectProperties("code","cid","name","parentId","orderCount");
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		criteria.andIn("cid", idList);
		return this.selectByExample(example);
	}
	
	
	public PageVO<ModulePO> findPageByModule(ModulePO module, PageVO<ModulePO> page) {
		logger.info("----findModuleById--id="+module.getCid());
		Example example = new Example(ModulePO.class);
//		  example.selectProperties("nameCn","nameEn","code","cid");
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		if (StringUtil.isNotEmpty(module.getName())) {
			criteria.andLike("name", "%" + module.getName() + "%");
		}
		if (StringUtil.isNotEmpty(module.getCode())) {
			criteria.andLike("code", "%" + module.getCode() + "%");
		}
		if (module.getCid() != null) {
			criteria.andEqualTo("cid", module.getCid());
		}
		if (module.getParentId() != null) {
			criteria.andEqualTo("parentId", module.getParentId());
		}
		
		//order default
        if(StringUtils.isEmpty(page.getSort())){
        	example.setOrderByClause("parent_id, order_count desc");
        }
        else{
        	example.setOrderByClause(page.getOrderClause());
        }
        
		
		// 分页查询
		PageHelper.startPage(page.getCurrentPage(), page.getPageSize());
		List<ModulePO> list = selectByExample(example);
		
		setParentName(list);
		
		PageInfo<ModulePO> pageInfo = new PageInfo<ModulePO>(list);
		PageVO<ModulePO> pageRet = new PageVO<ModulePO>(pageInfo);
		return pageRet;
	}


	/**
	 * @param list
	 */
	private void setParentName(List<ModulePO> list) {
		List<Long> idList=new ArrayList();
		for(ModulePO m:list){
			if(!idList.contains(m.getParentId())){
				idList.add(m.getParentId());
			}
		}
		if(idList.size()>0){
			List<ModulePO> mList=this.findModuleByIds(idList);
			for(ModulePO mo:list){
				for(ModulePO m:mList){
					if(mo.getParentId().longValue()==m.getCid().longValue()){
						mo.setParentName(m.getName());
						break;
					}
				}
			}
		}
	}
	
	@LogService(title="module.create",author="cjh",calls="insert")
	public RetResult<Long> create(ModulePO record){
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
//			Long cid=this.getMapper().selectSeqId(record);
//			record.setCid(cid);
//			this.getMapper().insert(record);
			this.save(record);
			ret.setData(record.getCid());
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "同一节点下的编码不及名称不能重复:"+e.getMessage(), e);
		}
		return ret;
	}
	
	@LogService(title="module.update",author="cjh",calls="updateByPrimaryKey")
	public RetResult<Long> update(ModulePO record){
		logger.info("----update--");
		long time=System.currentTimeMillis();
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
    	ModulePO module=new ModulePO();
//    	module.setCid(cid);
    	module.initUpdate();
    	module.setModifyUser(operId);
    	module.setParentId(newParentId);
    	
    	Example example = new Example(ModulePO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("parentId", oldParentId);
		criteria.andEqualTo("cid", cid);
		this.mapper.updateByExampleSelective(module, example);
		ret.setData(cid);
		return ret;
	}
	
	@LogService(title="module.deleteVirtual",author="cjh",calls="updateByPrimaryKeySelective")
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid){
		logger.info("----update--");
		long time=System.currentTimeMillis();
		RetResult<Integer> ret = new RetResult<>();
		if(cid==null||cid==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+cid+" is empty", null);
			logger.warn("deleteVirtual="+ret.getMsgBody());
			return ret;
		}
		ModulePO module = new ModulePO();
		module.setCid(cid);
		module.setModifyUser(operUserId);
		int v=0;
		try {
			v = this.deleteVirtual(module);
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "cid="+cid+",error="+e.getMessage(), e);
		}
		ret.setData(v);
		return ret;
	}

}
