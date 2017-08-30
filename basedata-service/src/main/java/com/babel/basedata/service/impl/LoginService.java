package com.babel.basedata.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.babel.basedata.logger.LogService;
import com.babel.basedata.mapper.UserMapper;
import com.babel.basedata.model.UserPO;
import com.babel.basedata.service.ILoginService;
import com.babel.basedata.util.Sysconfigs;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.security.util.Auth;
import com.babel.common.core.service.impl.BaseService;
import com.babel.common.core.util.CommUtil;

import tk.mybatis.mapper.entity.Example;

@Service("loginService")
public class LoginService extends BaseService<UserPO> implements ILoginService {
	 private static final Log logger = LogFactory.getLog(LoginService.class);
	 
	
	 @Autowired
	 private UserMapper mapper;

	@Override
	public UserMapper getMapper() {
		return mapper;
	}

	public RetResult<UserPO> findUserByNo(String username) {
		RetResult<UserPO> ret = new RetResult<>();
		
		if(username==null){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "userName=" + username, null);
			logger.info(ret.getMsgBody());
			return ret;
		}
		
		username = username.toLowerCase(); // 忽略大写小
		
		Example example = new Example(UserPO.class);
		example.selectProperties("userName", "countryCode", "passwd", "mobile", "status", "phone", "cid");
		Example.Criteria criteria = example.createCriteria();

		criteria.andEqualTo("userName", username);
		criteria.andEqualTo("ifDel", 0);

		List<UserPO> userList = (List<UserPO>) this.getMapper().selectByExample(example);
		UserPO user = null;
		if (userList.size() == 0) {
			ret.initError(RetResult.msg_codes.ERR_DATA_NOT_FOUND, "userName=" + username, null);
			logger.warn(ret.getMsgBody());
		} else if (userList.size() > 1) {
			ret.initError(RetResult.msg_codes.ERR_DATA_REPEAT, "userName=" + username, null);
			logger.warn(ret.getMsgBody());
		} else {
			ret.setData(userList.get(0));
		}

		return ret;
	}
//	@RetryRule(code="login", key="'login'+#loginIp+#username")
	@LogService(title="login.login",author="cjh",calls="findUserByNo,updateByPrimaryKeySelective", descs=LogService.noParams)
	public RetResult<UserPO> login(String loginIp, String username, String password){
		long time=System.currentTimeMillis();
		RetResult<UserPO> ret = new RetResult<>();

		ret = this.findUserByNo(username);
		if(!ret.isSuccess()){
//			this.logDbService.info(username, "login", "userName="+username, System.currentTimeMillis()-time, ret);
			return ret;
		}
		
		if (null == password){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "password is null", null);
			logger.info(ret.getMsgBody());
			return ret;
		}
		
		UserPO user=ret.getFirstData();
		// 用户处于无效状态不能登录
		if (user.getStatus()==null || user.getStatus().intValue()!=1){
			ret.initError(RetResult.msg_codes.ERR_PERMISSION_DENIED, "user status invalid", null);
			logger.warn(ret.getMsgBody());
			return ret;
		}
		

		if(!checkPassword(user, password)){
			ret.initError(RetResult.msg_codes.ERR_LOGIN_INVALID_PASSWORD, "user passwd invalid", null);
			logger.warn(ret.getMsgBody());
			return ret;
		}
		
		user.setPasswd(null);//清空对象密码，以免密码安全问题

		return ret;
	}
	
	private boolean checkPassword(UserPO user, String password) {
		boolean isLogin=false;
		Auth auth =new Auth();
		
//		log.info("-----checkPassword--"+user.getEmpCode()+" password="+password);
		if(!"true".equals(Sysconfigs.getEnvMap().get("sys.login.domain"))){
			password = auth.encrypt(user.getUserName(), password);
//			log.info("-----checkPassword--"+user.getEmpCode()+" password="+password+" userPwd="+user.getPassword());
			if (!(user.getPasswd() == null || !auth.checkPassword(user.getPasswd(), password))) {
				isLogin=true;
			}
		}
		else{
			isLogin=auth.authUser(user.getUserName(), password);
		}
		
		return isLogin;
		
	}
	
	@Override
	@LogService(title="login.changePasswordByManage",author="cjh",calls="selectByKey,updateByPrimaryKeySelective")
	public RetResult<Integer> changePasswordByManage(String loginIp, Long userId, String newPassword){
		long time=System.currentTimeMillis();
		RetResult<Integer> ret = new RetResult<>();
		// 用户不能为空
		if (null == userId || userId.equals(0L)){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "userId empty", null);
			return ret;
		}
		// 密码不能为空并且必需大于六个字符
		if (null == newPassword || newPassword.length() < 8){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "New password can not empty and length must more than 8", null);
			return ret;
		}
		ret =changePwd(loginIp, userId, null, newPassword, time);
		return ret;
	}
	
	/**
	 * 修改密码
	 *
	 * @param loginIp
	 * @param userId
	 * @param oldPassword
	 * @param newPassword
	 * @throws AuthorizeException
	 */
	@Override
	@LogService(title="login.changePassword",author="cjh",calls="selectByKey,updateByPrimaryKeySelective")
	public RetResult<Integer> changePassword(String loginIp, Long userId, String oldPassword, String newPassword){
		long time=System.currentTimeMillis();
		RetResult<Integer> ret = new RetResult<>();
		// 用户不能为空
		if (null == userId || userId.equals(0L)){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "userId empty", null);
			return ret;
		}
		// 密码不能为空并且必需大于六个字符
		if (null == oldPassword || oldPassword.length() < 8){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "Old password can not empty and length must more than 8", null);
			return ret;
		}
		if (null == newPassword || newPassword.length() < 8){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "New password can not empty and length must more than 8", null);
			return ret;
		}
			
		ret =changePwd(loginIp, userId, oldPassword, newPassword, time);
		return ret;
		
	}
	
	private RetResult<Integer> changePwd(String loginIp, Long userId, String oldPassword, String newPassword, long time) {
		RetResult<Integer> ret = new RetResult<>();
		UserPO u = this.selectByKey(userId);
		// 用户名不能为空
		if (null == u){
			ret.initError(RetResult.msg_codes.ERR_DATA_UNEXISTED, "User not found,userId="+userId, null);
			return ret;
		}
		
		Auth auth =new Auth();
		String passwordEncrypt=null;
		if(oldPassword!=null){
			passwordEncrypt=auth.encrypt(u.getUserName(), oldPassword);
			if ( !passwordEncrypt.equalsIgnoreCase(
					u.getPasswd())){
				ret.initError(RetResult.msg_codes.ERR_DATA_INVALID, "Old password error", null);
				return ret;
			}
		}
			

		try {
			passwordEncrypt=auth.encrypt(u.getUserName(), newPassword);
			UserPO updateUser=new UserPO();
			updateUser.setCid(u.getCid());
			updateUser.setPasswd(passwordEncrypt);
			updateUser.initUpdate();
			if(loginIp!=null){
				String remark=u.getRemark();
				remark=(String)CommUtil.nvl(remark, "loginIp:"+loginIp);
				updateUser.setRemark(remark);
			}
			this.mapper.updateByPrimaryKeySelective(updateUser);
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "Change password error", e);
			return ret;
		}
		return ret;
	}
	
}
