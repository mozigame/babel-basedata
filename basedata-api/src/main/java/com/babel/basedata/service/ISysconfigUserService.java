package com.babel.basedata.service;

import java.util.List;
import java.util.Map;

import com.babel.basedata.model.SysconfigUserPO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;

public interface ISysconfigUserService  extends IBaseService<SysconfigUserPO> {
	public List<SysconfigUserPO> selectBySysconfigUser(SysconfigUserPO item, int page, int rows);
	
	public RetResult<SysconfigUserPO> findSysconfigUserById(Long id);
	
	/**
	 * 查询用户已配置的用户数据信息
	 * @param userId 用户id
	 * @param sysconfigIds 系统参数id
	 * @return
	 */
	public List<SysconfigUserPO> findSysconfigUserListByUsers(List<Long> userIdList, List<Long> sysconfigIds);
	
	public List<SysconfigUserPO> findSysconfigUserByUser(Long userId,Long sysconfigId);
	
	public PageVO<SysconfigUserPO> findPageBySysconfigUser(SysconfigUserPO search, PageVO<SysconfigUserPO> page);
	
	public RetResult<Long> update(SysconfigUserPO record);
	
	public RetResult<Long> create(SysconfigUserPO record);
	
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid);
	
	public RetResult<Integer> deleteVirtualByUser(Long userId, Long sysconfigId);
	
	public RetResult<SysconfigUserPO> findSysconfigUserBySysconfigCode(Long userId,String sysconfigCode);
	
	public void findSysconfigUserBySysconfigCode_flushCache(Long userId, String sysconfigCode);
	
	/**
	 * 载入用户系统参数默认信息
	 * @return
	 */
	public Map<String, Object> initSysconfigBaseUserMap() ;

	/**
	 * 按用户查询系统参数设置
	 * @param userId
	 * @return
	 */
	public Map<String, Object> initSysconfigUserMap(Long userId);
	
	/**
	 * 载入用户修改过的系统参数设置
	 * @param userIdList 为空表示全部
	 * @return
	 */
	public Map<Long, Map<String, Object>> initSysconfigUserMapAll(List<Long> userIdList);
}
