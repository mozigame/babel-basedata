package com.babel.basedata.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.impl.BaseService;
import com.babel.basedata.logger.LogService;
import com.babel.basedata.mapper.RoleMapper;
import com.babel.basedata.model.RolePO;
import com.babel.basedata.model.UserPO;
import com.babel.basedata.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.github.pagehelper.PageHelper;

@Service("roleService")
public class RoleServiceImpl extends BaseService<RolePO> implements IRoleService{
	 private static final Log logger = LogFactory.getLog(RoleServiceImpl.class);
	 //@Autowired
	 //private ILogDbService logDbService;
	
	@Autowired
	private RoleMapper roleMapper;
	
	 @Override
		public RoleMapper getMapper() {
			return roleMapper;
		}
	
	@Override
	public RolePO findRoleById(Long id) {
		logger.info("----findRoleById--id="+id);
		return roleMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public List<RolePO> findRoleByIds(List<Long> ids) {
		if(ids==null||ids.size()==0){
			logger.warn("----findRoleByIds--ids="+ids+" empty");
			return new ArrayList<>();
		}
		Example example = new Example(UserPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("cid", ids);
		return this.roleMapper.selectByExample(example);
	}
	
	@Override
    public List<RolePO> selectByRole(RolePO role, int page, int rows) {
		logger.info("----selectByRole--role="+role);
		
        Example example = new Example(RolePO.class);
        example.selectProperties("nameCn","nameEn","code","cid");
        /*Example.Criteria criteria = example.createCriteria();
        if (StringUtil.isNotEmpty(role.getXxx())) {
            criteria.andLike("xxx", "%" + role.getXxx() + "%");
        }*/

        //分页查询
        PageHelper.startPage(page, rows);
        return selectByExample(example);
    }
	
	public PageVO<RolePO> findPageByRole(RolePO search, PageVO<RolePO> page) {
		logger.info("----findRoleListByPage--");

		
		List<RolePO> list=null;
		int totalSize=0;
		try {
			totalSize = roleMapper.findRoleListByPageCount(search);
			list = roleMapper.findRoleListByPage(search, page);
		} catch (Exception e) {
			logger.error("-----findRoleListByPage--", e);
		}
		
		
		PageVO<RolePO> pageRet=new PageVO<RolePO>(list, totalSize, page.getPageSize(), page.getCurrentPage());
		return pageRet;
		
	}
	
	@LogService(title="role.create",author="cjh",calls="insert", descs="cid=#{0.cid},code=#{0.code}")
	public RetResult<Long> create(RolePO record){
		logger.info("----create--");
//		this.logDbService.info(record, "create", "start", 0l);
		RetResult<Long> ret = new RetResult<Long>();
//		if(StringUtils.isEmpty(record.getXxx())){
//			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "xxx is empty", null);
//			logger.warn("create="+ret.getMsgBody());
//			return ret;
//		}
		record.initCreate();
		roleMapper.insert(record);
		ret.setData(record.getCid());
		return ret;
	}
	
	@LogService(title="role.update",author="cjh",calls="updateByPrimaryKey", descs="cid=#{0.cid},code=#{0.code}")
	public RetResult<Long> update(RolePO record){
		logger.info("----update--");
		RetResult<Long> ret = new RetResult<Long>();
		if(record.getCid()==null||record.getCid().longValue()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+record.getCid()+" is empty", null);
			return ret;
		}
		record.initUpdate();
		record.setIfDel(0);
		roleMapper.updateByPrimaryKey(record);
		ret.setData(record.getCid());
		return ret;
	}
	
	@LogService(title="role.deleteVirtual",author="cjh",calls="updateByPrimaryKeySelective", descs="cid=#{1}")
	public RetResult<Integer> deleteVirtual(Long operId, Long cid){
		logger.info("----update--");
//		this.logDbService.info(null, "update", "start", 0l);
		RetResult<Integer> ret = new RetResult<Integer>();
		if(cid==null||cid==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+cid+" is empty", null);
			return ret;
		}
		RolePO role = new RolePO();
		role.setCid(cid);
		role.setModifyUser(operId);
		int v=0;
		try {
			v = this.deleteVirtual(role);
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "cid="+cid+",error="+e.getMessage(), e);
		}
		ret.setData(v);
		return ret;
	}

}
