package com.babel.basedata.service;

import com.babel.basedata.model.UserPO;
import com.babel.common.core.data.RetResult;

public interface ILoginService {
	public RetResult<UserPO> login(String loginIp, String username, String password);
	
	public RetResult<Integer> changePassword(String loginIp, Long userId, String oldPassword, String newPassword);
	
	public RetResult<Integer> changePasswordByManage(String loginIp, Long userId, String newPassword);
}
