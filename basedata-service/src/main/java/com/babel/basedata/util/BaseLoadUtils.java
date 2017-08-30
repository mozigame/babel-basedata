package com.babel.basedata.util;

import java.util.List;

import org.apache.log4j.Logger;

import com.babel.basedata.model.UserPO;
import com.babel.basedata.service.IUserService;
import com.babel.common.core.data.RetResult;


public class BaseLoadUtils {
	private static Logger logger = Logger.getLogger(BaseLoadUtils.class);
	


	private static IUserService userService;
	
	public void setUserService(IUserService userService) {
		BaseLoadUtils.userService = userService;
	}


	
	public static RetResult<UserPO> findUserByIdList(List<Long> idList){
		if(userService==null){
			logger.warn("----disabled initDisp by BaseLoadUtils.userService is null");
			return null;
		}
		return userService.findUserByIdList(idList);
	}
	
}
