package com.babel.basedata.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.impl.BaseService;
import com.babel.basedata.logger.LogService;
import com.babel.basedata.mapper.UserRoleMapper;
import com.babel.basedata.model.RolePO;
import com.babel.basedata.model.UserPO;
import com.babel.basedata.model.UserRolePO;
import com.babel.basedata.service.IRoleService;
import com.babel.basedata.service.IUserRoleService;
import com.babel.basedata.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("userRoleService")
public class UserRoleServiceImpl extends BaseService<UserRolePO> implements IUserRoleService{
	 private static final Log logger = LogFactory.getLog(UserRoleServiceImpl.class);


	@Autowired
	private UserRoleMapper userRoleMapper;
	@Autowired
	private IUserService userService;
	@Autowired
	
	private IRoleService roleService;
	 @Override
		public UserRoleMapper getMapper() {
			return userRoleMapper;
		}
	
	@Override
	public UserRolePO findUserRoleById(Long id) {
		logger.info("----findUserRoleById--id="+id);
		return userRoleMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public List<UserRolePO> findUserRoleByJobTypes(List<Integer> jobTypes, Date dataDate, Date searchDate) {
		if(jobTypes!=null && jobTypes.isEmpty()){
			jobTypes=null;
		}
		
		return this.userRoleMapper.findUserRoleByJobTypes(jobTypes, dataDate, searchDate);
	}
	
	@Override
	public List<UserRolePO> findUserRoleByIds(List<Long> ids) {
		Example example = new Example(UserRolePO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("cid", ids);
		return userRoleMapper.selectByExample(example);
	}
	
	/**
	 * 用户有效的角色关系表，即如果时间过期则不算
	 */
	@Override
	public List<UserRolePO> findUserRoleByUserId(Long userId) {
		logger.info("------findUserRoleByUserId--userId="+userId);
		Example example = new Example(UserRolePO.class);
		
		Date now=new Date();
		Example.Criteria criteria = example.createCriteria();
		Example.Criteria criteria2 = example.createCriteria();
		criteria.andEqualTo("ifDel", 0).andEqualTo("userId", userId);
		criteria.andIsNull("endDate").andLessThanOrEqualTo("startDate", now);
		
		criteria2.andEqualTo("ifDel", 0).andEqualTo("userId", userId);
		criteria2.andIsNotNull("endDate");
		//startDate<=now and endDate>now
		criteria2.andLessThanOrEqualTo("startDate", now).andGreaterThan("endDate", now);
		
		example.or(criteria2);
		return userRoleMapper.selectByExample(example);
	}
	
	@Override
	public List<UserRolePO> findUserRoleByUserRoleNames(Collection<String> userRoleNames) {
		Example example = new Example(UserRolePO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		criteria.andIn("userRoleName", userRoleNames);
		return userRoleMapper.selectByExample(example);
	}
	
	@Override
    public List<UserRolePO> selectByUserRole(UserRolePO userRole) {
		logger.info("----selectByUserRole--userRole="+userRole);
//        Example example = new Example(UserRolePO.class);
//        example.selectProperties("nameCn","nameEn","cid");
        /*Example.Criteria criteria = example.createCriteria();
        if (StringUtil.isNotEmpty(userRole.getXxx())) {
            criteria.andLike("xxx", "%" + userRole.getXxx() + "%");
        }*/
//        selectByUserRole(userRole);
        return this.userRoleMapper.select(userRole);
        //分页查询
        //PageHelper.startPage(page, rows);
//        return selectByExample(example);
    }
	
	public RetResult<UserRolePO> findUserRoleByIdList(List<Long> idList){
		 RetResult<UserRolePO> ret = new RetResult<UserRolePO>();
		 if(idList==null||idList.size()==0){
			 ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "idList is empty", null);
			 return ret;
		 }
		 Example example = new Example(UserRolePO.class);
	     example.selectProperties("userRoleName","name","cid");
	     Example.Criteria criteria = example.createCriteria();
	     criteria.andEqualTo("ifDel", 0);
	     criteria.andIn("cid", idList);
	     ret.setDataList(this.selectByExample(example)); 
	     return ret;
	 }
	
	public PageVO<UserRolePO> findPageByUserRole(UserRolePO search, PageVO<UserRolePO> page) {
		logger.info("----findUserRoleListByPage--");

		
		Example example = new Example(UserRolePO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		if(search.getRoleId()!=null){
			criteria.andEqualTo("roleId", search.getRoleId());
		}
		if(search.getUserId()!=null){
			criteria.andEqualTo("userId", search.getUserId());
		}
		if(search.getIsDefault()!=null && search.getIsDefault()>=0){
			criteria.andEqualTo("isDefault", search.getIsDefault());
		}
		if(search.getJobType()!=null && search.getJobType()>0){
			criteria.andEqualTo("jobType", search.getJobType());
		}
		if(search.getStatus()!=null && search.getStatus()>=0){
			criteria.andEqualTo("status", search.getStatus());
		}
		// 分页查询
		PageHelper.startPage(page.getCurrentPage(), page.getPageSize());
		List<UserRolePO> list = selectByExample(example);
		
		try {
			dispName(list);
		} catch (Exception e) {
			logger.warn("----findPageByUserRole dispName "+e.getMessage(), e);
		}
		
		PageInfo<UserRolePO> pageInfo = new PageInfo<UserRolePO>(list);
		PageVO<UserRolePO> pageRet = new PageVO<UserRolePO>(pageInfo);
		return pageRet;
		
	}

	private void dispName(List<UserRolePO> list) {
		List<Long> userIdList=new ArrayList<>();
		List<Long> roleIdList=new ArrayList<>();
		for(UserRolePO uRole:list){
			userIdList.add(uRole.getUserId());
			roleIdList.add(uRole.getRoleId());
		}
		
		RetResult<UserPO> retUser=this.userService.findUserByIdList(userIdList);
		Collection<UserPO> userList=new ArrayList<>();
		if(retUser.isSuccess()){
			userList=retUser.getDataList();
		}
		List<RolePO> roleList=this.roleService.findRoleByIds(roleIdList);
		
		for(UserRolePO uRole:list){
			for(UserPO u:userList){
				if(uRole.getUserId().longValue()==u.getCid().longValue()){
					uRole.setUserName(u.getName());
					uRole.setUserUnionId(u.getWxUnionId());
					break;
				}
			}
			for(RolePO r:roleList){
				if(uRole.getRoleId().longValue()==r.getCid().longValue()){
					uRole.setRoleName(r.getName());
					break;
				}
			}
		}
	}
	
	@LogService(title="userRole.create",author="cjh",calls="insert")
	public RetResult<Long> create(UserRolePO record){
		logger.info("----create--");
		RetResult<Long> ret = new RetResult<Long>();
//		if(StringUtils.isEmpty(record.getXxx())){
//			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "xxx is empty", null);
//			logger.warn("create="+ret.getMsgBody());
//			return ret;
//		}
		record.initCreate();
		userRoleMapper.insert(record);
		ret.setData(record.getCid());
		return ret;
	}
	
	@LogService(title="userRole.update",author="cjh",calls="updateByPrimaryKey", descs="cid=#{0.cid},userRoleName=#{0.userRoleName}")
	public RetResult<Long> update(UserRolePO record){
		logger.info("----update--");
		RetResult<Long> ret = new RetResult<Long>();
		if(record.getCid()==null||record.getCid().longValue()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+record.getCid()+" is empty", null);
			return ret;
		}
		record.initUpdate();
		record.setIfDel(0);
		userRoleMapper.updateByPrimaryKey(record);
		ret.setData(record.getCid());
		return ret;
	}
	
	@LogService(title="userRole.deleteVirtual",author="cjh",calls="updateByPrimaryKeySelective", descs="cid=#{1}")
	public RetResult<Integer> deleteVirtual(Long operId, Long cid){
		logger.info("----update--");
		RetResult<Integer> ret = new RetResult<Integer>();
		if(cid==null||cid==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+cid+" is empty", null);
			return ret;
		}
		UserRolePO userRole = new UserRolePO();
		userRole.setCid(cid);
		userRole.setModifyUser(operId);
		int v=0;
		try {
			v = this.deleteVirtual(userRole);
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "cid="+cid+",error="+e.getMessage(), e);
		}
		ret.setData(v);
		return ret;
	}

}
