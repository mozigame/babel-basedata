package com.babel.basedata.service.impl;

import java.util.Collection;
import java.util.List;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.impl.BaseService;
import com.babel.basedata.logger.LogService;
import com.babel.basedata.mapper.UserMapper;
import com.babel.basedata.model.UserPO;
import com.babel.basedata.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;

@Service("userService")
public class UserServiceImpl extends BaseService<UserPO> implements IUserService{
	 private static final Log logger = LogFactory.getLog(UserServiceImpl.class);


	@Autowired
	private UserMapper userMapper;
	
	 @Override
		public UserMapper getMapper() {
			return userMapper;
		}
	
	@Override
	public UserPO findUserById(Long id) {
		logger.info("----findUserById--id="+id);
		return userMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public List<UserPO> findUserByIds(List<Long> ids) {
		Example example = new Example(UserPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("cid", ids);
		return userMapper.selectByExample(example);
	}
	
	@Override
	public List<UserPO> findUserByUserNames(Collection<String> userNames) {
		Example example = new Example(UserPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("userName", userNames);
		return userMapper.selectByExample(example);
	}
	
	@Override
    public List<UserPO> selectByUser(UserPO user) {
		logger.info("----selectByUser--user="+user);
//        Example example = new Example(UserPO.class);
//        example.selectProperties("nameCn","nameEn","cid");
        /*Example.Criteria criteria = example.createCriteria();
        if (StringUtil.isNotEmpty(user.getXxx())) {
            criteria.andLike("xxx", "%" + user.getXxx() + "%");
        }*/
//        selectByUser(user);
        return this.userMapper.select(user);
        //分页查询
        //PageHelper.startPage(page, rows);
//        return selectByExample(example);
    }
	
	public RetResult<UserPO> findUserByIdList(List<Long> idList){
		 RetResult<UserPO> ret = new RetResult<UserPO>();
		 if(idList==null||idList.size()==0){
			 ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "idList is empty", null);
			 return ret;
		 }
		 Example example = new Example(UserPO.class);
	     example.selectProperties("userName","name","cid");
	     Example.Criteria criteria = example.createCriteria();
	     criteria.andEqualTo("ifDel", 0);
	     criteria.andIn("cid", idList);
	     ret.setDataList(this.selectByExample(example)); 
	     return ret;
	 }
	
	public PageVO<UserPO> findPageByUser(UserPO search, PageVO<UserPO> page) {
		logger.info("----findUserListByPage--");

		
		List<UserPO> list=null;
		int totalSize=0;
		try {
			totalSize = userMapper.findUserListByPageCount(search);
			list = userMapper.findUserListByPage(search, page);
		} catch (Exception e) {
			logger.error("-----findUserListByPage--", e);
		}
		
		
		PageVO<UserPO> pageRet=new PageVO<UserPO>(list, totalSize, page.getPageSize(), page.getCurrentPage());
		return pageRet;
		
	}
	
	@LogService(title="user.create",author="cjh",calls="insert")
	public RetResult<Long> create(UserPO record){
		logger.info("----create--");
		RetResult<Long> ret = new RetResult<Long>();
//		if(StringUtils.isEmpty(record.getXxx())){
//			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "xxx is empty", null);
//			logger.warn("create="+ret.getMsgBody());
//			return ret;
//		}
		record.initCreate();
		userMapper.insert(record);
		ret.setData(record.getCid());
		return ret;
	}
	
	@LogService(title="user.update",author="cjh",calls="updateByPrimaryKey", descs="cid=#{0.cid},userName=#{0.userName}")
	public RetResult<Long> update(UserPO record){
		logger.info("----update--");
		RetResult<Long> ret = new RetResult<Long>();
		if(record.getCid()==null||record.getCid().longValue()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+record.getCid()+" is empty", null);
			return ret;
		}
		record.initUpdate();
		record.setIfDel(0);
		userMapper.updateByPrimaryKey(record);
		ret.setData(record.getCid());
		return ret;
	}
	
	@LogService(title="user.deleteVirtual",author="cjh",calls="updateByPrimaryKeySelective", descs="cid=#{1}")
	public RetResult<Integer> deleteVirtual(Long operId, Long cid){
		logger.info("----update--");
		RetResult<Integer> ret = new RetResult<Integer>();
		if(cid==null||cid==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+cid+" is empty", null);
			return ret;
		}
		UserPO user = new UserPO();
		user.setCid(cid);
		user.setModifyUser(operId);
		int v=0;
		try {
			v = this.deleteVirtual(user);
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "cid="+cid+",error="+e.getMessage(), e);
		}
		ret.setData(v);
		return ret;
	}

}
