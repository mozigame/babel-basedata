package com.babel.basedata.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.babel.basedata.model.UserPO;
import com.babel.basedata.service.IUserService;
import com.babel.common.core.data.IUser;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.service.IUserInfoService;

@Service("userBaseService")
public class UserBaseServiceImpl implements IUserInfoService{
	@Autowired
	private IUserService userService;

	@Override
	public RetResult<IUser> findUserByIdList(List<Long> idList) {
		RetResult<UserPO> userRet= userService.findUserByIdList(idList);
		RetResult<IUser> retResult=new RetResult<IUser>();
		retResult.setMsgBody(userRet.getMsgBody());
		retResult.setMsgCode(userRet.getMsgCode());
		retResult.setFlag(userRet.getFlag());
		List<IUser> userList=new ArrayList<>();
		userList.addAll(userRet.getDataList());
		retResult.setDataList(userList);
		return retResult;
	}

}
