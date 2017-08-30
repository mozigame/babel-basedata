package com.babel.basedata.controller;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.babel.basedata.entity.MailVO;
import com.babel.basedata.entity.Menu;
import com.babel.basedata.entity.SmsVO;
import com.babel.basedata.model.LogLoginPO;
import com.babel.basedata.model.ModulePO;
import com.babel.basedata.model.RetryRuleDetailPO;
import com.babel.basedata.model.SysconfigPO;
import com.babel.basedata.model.UserPO;
import com.babel.basedata.model.UserRolePO;
import com.babel.basedata.service.ILogLoginService;
import com.babel.basedata.service.ILoginService;
import com.babel.basedata.service.IModuleService;
import com.babel.basedata.service.IUserRoleService;
import com.babel.basedata.service.IUserService;
import com.babel.basedata.util.RetryRuleUtils;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.entity.UserPermitVO;
import com.babel.common.core.logger.ILogMsgManager;
import com.babel.common.core.logger.LogAudit;
import com.babel.common.core.service.IRetryRuleCheckService;
import com.babel.common.core.util.CommUtil;
import com.babel.common.core.util.SpringContextUtil;
import com.babel.common.web.context.AppContext;
import com.babel.common.web.controller.WebBaseController;

/**
 * @author liuzh_3nofxnp
 * @since 2015-09-19 17:15
 */
@Controller
public class LoginController extends WebBaseController{
	private static final Log logger = LogFactory.getLog(LoginController.class);

    @Autowired
    private ILoginService loginService;
    
    @Autowired
	private ILogLoginService logLoginService;
    
    @Autowired
    private ILogMsgManager logMsgManager;
    
    @Autowired
    private IUserService userService;
    
    @Autowired
	private IUserRoleService userRoleService;
    
    @Autowired
    private IModuleService moduleService;
    
    /**
     * retryRuleCheckRedisService
     * retryRuleCheckEhcacheService
     */
   
//    @Resource(name = "retryRuleCheckEhcacheService")
    private static IRetryRuleCheckService retryRuleCheckService;
    
	private static String RETRY_FUNC_CODE_LOGIN="f_login";
    private void initService() {
		retryRuleCheckService=RetryRuleUtils.getCheckServiceByFuncRuleCode(RETRY_FUNC_CODE_LOGIN);
	}
    
    private static Map<String, Date> moduleLoadDate=new ConcurrentHashMap<>();
    private List<Menu> getModuleMenuByParentId(Long parentId){
    	if(parentId==null){
    		parentId=0l;
    	}
    	List<Menu> menuList = new ArrayList<Menu>();
    	List<Long> idList=CommUtil.newList(parentId);
    	
//    	List<ModulePO> list=this.moduleService.findModuleByParentIds(idList);
    	
    	HttpServletRequest request=this.getRequest();
    	
    	String sessionId=request.getRequestedSessionId();
		UserPermitVO userInfo=AppContext.getUserInfo(sessionId);
		if(userInfo==null){
			logger.info("-----getModuleMenuByParentId--sessionId="+sessionId+" userInfo=null");
			return menuList;
		}
    	
		List<Long> roleIdList=userInfo.getRoleIdList();
		String roleIdStr=userInfo.getRoleIdStr();
		
		Set<String> userModules=AppContext.getRoleModule(roleIdStr);
    	if(userModules==null){
    		userModules=new HashSet<>();
    	}
    	
    	String key=roleIdStr+"_"+parentId;
    	Date date=moduleLoadDate.get(key);
    	boolean isLoad=false;
    	List<ModulePO> list=(List<ModulePO>)AppContext.getRoleModuleList(roleIdStr, ""+parentId);
    	if(list==null|| date==null|| date.before(DateUtils.addSeconds(new Date(), -30))){
//    		根据用户的角色菜单权限获取菜单
    		list=this.moduleService.findRolesModuleByParentIds(roleIdList, idList);
    		AppContext.putRoleModuleList(roleIdStr, ""+parentId, list);
    		moduleLoadDate.put(roleIdStr+"_"+parentId, new Date());
    		isLoad=true;
    	}
    	
//    	boolean hasChild=false;
//    	String status=null;
//    	idList.clear();
//    	for(ModulePO m:list){
//    		idList.add(m.getCid());
//    	}
//    	list=this.moduleService.findModuleByParentIds(idList);
    	
    	Menu menu=null;
    	for(ModulePO module:list){
    		if(module.getShowType()==null||module.getShowType().intValue()!=1){
    			continue;
    		}
    		menu=new Menu(module);
    		userModules.add(module.getUrl());
//	    	//检查是否有子节点
//			hasChild=false;
//			for(ModulePO m:mList){
//				if(m.getParentId().longValue()==module.getCid().longValue()){
//					hasChild=true;
//					break;
//				}
//			}
//			if(hasChild){
//				
//				menu.setMENU_STATE("1");
//			}
			menu.setMENU_STATE("1");
			menu.setHasMenu(true);
			menu.setTarget("treeFrame");
			menuList.add(menu);
    	}
    	if(isLoad){
    		AppContext.putRoleModule(roleIdStr, userModules);
    	}
    	return menuList;
    	
    }
    
    private void addMenuToModel(ModelAndView mv){
    	List<Menu> menuList = this.getModuleMenuByParentId(2l);
		List<Menu> subMenuList=null;
		for(Menu menu:menuList){
			subMenuList = this.getModuleMenuByParentId(Long.valueOf(menu.getMENU_ID()));
			menu.setSubMenu(subMenuList);
			
		}
		mv.addObject("menuList", menuList);
    }
    
    @RequestMapping(value = "/index")
	public ModelAndView voteSave(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("system/index/main");
		
		this.addMenuToModel(mv);
		return mv;
	}
    @RequestMapping(value="/login_toLogin")
    @LogAudit(author="cjh", title="test", descs=LogAudit.noRet)
    public ModelAndView index(SysconfigPO sysconfig){
    	ModelAndView result = new ModelAndView("login");
    	result.addObject("env", super.getEnvMap());
    	return result;
    }
    @RequestMapping(value={ "/dashboard/login", "/dashboard/login.html", "/dashboard" })
    @LogAudit(author="caco", title="test", descs=LogAudit.noRet)
    public ModelAndView dashboardIndex(SysconfigPO sysconfig){
    	ModelAndView result = new ModelAndView("ins/dashboard/login");
    	result.addObject("env", super.getEnvMap());
    	return result;
    }
    
    private Map getRequestMap(HttpServletRequest request){
		Map properties = request.getParameterMap();
		Map returnMap = new HashMap(); 
		Iterator entries = properties.entrySet().iterator(); 
		Map.Entry entry; 
		String name = "";  
		String value = "";  
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next(); 
			name = (String) entry.getKey(); 
			Object valueObj = entry.getValue(); 
			if(null == valueObj){ 
				value = ""; 
			}else if(valueObj instanceof String[]){ 
				String[] values = (String[])valueObj;
				for(int i=0;i<values.length;i++){ 
					 value = values[i] + ",";
				}
				value = value.substring(0, value.length()-1); 
			}else{
				value = valueObj.toString(); 
			}
			returnMap.put(name, value); 
		}
		return returnMap;
    }
    
    public static void main(String[] args) {
		System.out.println(createRandomPasswd(8));
	}
    
    /**
     * 一个简单的包含1个符号的密码
     * @return
     */
    private static String createRandomPasswd(int length) {
    	String[] letters = {"a","b","c","d","e","f","g","h","i","g","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
   		String[] symbols = {"~","@","#","$","%","&","*","(",")","_","+","[","]"};
   		//String[] numbers = {"1","2","3","4","5","6","7","8","9","0"};
   		StringBuffer passwd = new StringBuffer("");
   		Random r = new Random();
   		for (int i = 0; i < length; i++) {
   			if(r.nextInt(2)==0) {
   				passwd.append(letters[r.nextInt(letters.length)]);
   			} else {
   				passwd.append(letters[r.nextInt(letters.length)].toUpperCase());
   			}
		}
   		String symbol = symbols[r.nextInt(symbols.length)];
   		int symbolsIndex = r.nextInt(length);
   		
   		passwd.replace(symbolsIndex, symbolsIndex+1, symbol);
    	return passwd.toString();
    }
    
    /**请求登录，验证用户
   	 * @return
   	 * @throws Exception
   	 */
   	@RequestMapping(value="/forget" ,produces="application/json;charset=UTF-8")
   	@ResponseBody
   	@LogAudit(title="login.forget", calls="selectByUser,changePasswordByManage,saveMail", descs="forget username or password")
   	public RetResult<Integer> forget()throws Exception{
   		RetResult<Integer> ret=new RetResult<Integer>();
   		//生成随机密码
   		String randomPasswd = createRandomPasswd(8);
   		//查找用户信息
   		UserPO user = new UserPO();
   		Map reqMap=this.getRequestMap(this.getRequest());
   		Object mailObj = reqMap.get("mail");
   		Object phoneObj = reqMap.get("phone");
   		Object codeObj = reqMap.get("code");
   		if(StringUtils.isEmpty((String)mailObj)) {
   			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "邮箱地址为空", null);
			return ret;
   		}
   		if(StringUtils.isEmpty((String)codeObj)) {
   			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "验证码为空", null);
			return ret;
   		}
   		Session session = SecurityUtils.getSubject().getSession();
   		String sessionCode = (String)session.getAttribute(AppContext.SESSION_SECURITY_CODE);		//获取session中的验证码
   		if(!(sessionCode!=null && sessionCode.equalsIgnoreCase(codeObj.toString()))){		//判断登录验证码
   			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "验证码错误", null);
			return ret;
		}
   		
   		
   		//按邮箱查询用户信息
   		user.setEmail(mailObj.toString());
   		List<UserPO> users = userService.selectByUser(user);
   		if(users == null || users.size() == 0) {
   			ret.initError(RetResult.msg_codes.ERR_DATA_UNEXISTED, "邮箱不存在", null);
			return ret;
   		}
   		user = users.get(0);
   		if(phoneObj != null) {
   			if(!phoneObj.toString().equals(user.getMobile())) {
   				ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "手机号码与邮箱对应用户不符", null);
   				return ret;
   			}
   		}
   		
   		//修改用户密码
   		String ip=AppContext.getRemoteIp(this.getRequest());
   		loginService.changePasswordByManage(ip, user.getCid(),randomPasswd);
   		
   		String content = "你好，你的账号为:"+user.getUserName()+"，新随机密码为：" + randomPasswd+"，请妥善保管！";
   		try{
	   		//通知用户，发邮件
	   		MailVO mail = new MailVO();
	   		mail.setTos(user.getEmail());
	   		mail.setTitle("重置密码邮件");
	   		mail.setContent(content);
	   		
	   		logMsgManager.saveMail("SEND_PASSWD_MAIL", mail, 1, true);
	   		//如果有手机号码则发短信通知
	   		if(phoneObj != null) {
	   			//验证手机格式，因为后端不信任前端，这里要验证
	   			//todo
	   			SmsVO sms = new SmsVO();
	   			sms.setSenderId(1l);
	   			sms.setTos(phoneObj.toString());
	   			sms.setContent(content);
	   			logMsgManager.saveSms("SEND_PASSWD_SMS", sms, true);
	   		}
   		} catch (Exception e) {
			System.out.println("邮件和短信发送还没OK，后面测试");
		}
   		return ret;
   	}
    
   	/**踢除用户 
     * @param username 
     */  
    public void kickOutUser(String username){  
    	 logger.info("-----kickOutUser--"+username);
    	 if(!SpringContextUtil.containsBean("redisShiroSessionDAO")){
    		 return;
    	 }
    	SessionDAO sessionDao=(SessionDAO)SpringContextUtil.getBean("redisShiroSessionDAO");
    	Collection<Session> sessions=sessionDao.getActiveSessions();
    	Session userSession=null;
    	for(Session session : sessions){  
            if(null != session && StringUtils.equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)), username)){  
            	userSession=session;
            	break;
            }  
        }  
        
        if(null != userSession && !StringUtils.equals(String.valueOf(userSession.getId()), this.getRequest().getRequestedSessionId())){  
        	userSession.setTimeout(0);//设置session立即失效，即将其踢出系统
        	userSession.setAttribute("kickout", true);
        	sessionDao.update(userSession);
            logger.info("-----kickOutUser success kick out user="+username+" date="+new Date()+" startTime="+userSession.getStartTimestamp()+" "+userSession.getTimeout());  
        }  
    }  
   	
    /**请求登录，验证用户
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/login_login" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	@LogAudit(title="login", descs="login system")
	public RetResult<UserPO> login()throws Exception{
		RetResult<UserPO> loginRet=new RetResult<UserPO>();
		Map reqMap=this.getRequestMap(this.getRequest());
		String reqData=(String)reqMap.get("KEYDATA");
		String KEYDATA[] = reqData.replaceAll("qq313596790fh", "").replaceAll("QQ978336446fh", "").split(",fh,");
		if(null != KEYDATA && KEYDATA.length == 3){
			Session session = SecurityUtils.getSubject().getSession();
			String sessionCode = (String)session.getAttribute(AppContext.SESSION_SECURITY_CODE);		//获取session中的验证码
			session.removeAttribute(AppContext.SESSION_SECURITY_CODE);//验证码用过不能再用
			String userName = KEYDATA[0];	//登录过来的用户名
			String passwd  = KEYDATA[1];	//登录过来的密码
			String code = KEYDATA[2];
			
			String ip=AppContext.getIpAddr(this.getRequest());
			LogLoginPO logLogin=new LogLoginPO();
			logLogin.setUserName(userName);
			logLogin.setIp(ip);
			logLogin.setReferInfo(this.getRequest().getHeader("referer"));
			logLogin.setLoginDate(new Date());
			logLogin.setLoginType(1);
			session.setAttribute(AppContext.SESSION_USERNAME, userName);
			this.getRequest().getSession().setAttribute(AppContext.SESSION_USERNAME, userName);
			
			
			if(retryRuleCheckService==null){
				this.initService();
			}
			if(retryRuleCheckService!=null){
				RetResult<String> retCheck=retryRuleCheckService.checkLoginRetryCount(RETRY_FUNC_CODE_LOGIN, userName, ip);
				if(!retCheck.isSuccess()){
					loginRet=new RetResult<UserPO>(retCheck);
					logLoginService.createLogLoginAsync(logLogin, loginRet);
					return loginRet;
				}
			}
			
			
			if(null == code || "".equals(code)){//判断效验码
				//效验码为空
				loginRet.initError(RetResult.msg_codes.ERR_DATA_INPUT, "Valid code empty", null);
//				logLoginService.createLogLoginAsync(logLogin, loginRet);
				return loginRet;
			}
			
			
			Object ifCloseValidCode=getEnvMap().get("dev.no_validCode");//是否关闭验证码
			if(!"true".equals(ifCloseValidCode) && !(sessionCode!=null && sessionCode.equalsIgnoreCase(code))){//判断登录验证码
				loginRet.initError(RetResult.msg_codes.ERR_DATA_INPUT, "Valid code invalid", null);
				logLoginService.createLogLoginAsync(logLogin, loginRet);
				return loginRet;
			}
			
			

//			String passwd = new SimpleHash("SHA-1", USERNAME, PASSWORD).toString();	//密码加密
			RetResult<UserPO> ret=loginService.login(ip, userName, passwd);//登入验证
			if(!ret.isSuccess()){//用户名或密码有误
				logLoginService.createLogLoginAsync(logLogin, loginRet);
				return ret;
			}

			
			/**
			 * 登入成功，清除重试次数缓存
			 */
			if(retryRuleCheckService!=null){//如果redisTemplate未找到则整个retry check不起作用
				retryRuleCheckService.cleanRetry(RETRY_FUNC_CODE_LOGIN, userName, ip);
			}
			
//			if("true".equals(this.getEnvMap().get("app.user.login.autoKick"))
//					&& "one".equals(this.getUserEnvMap().get("user.login.type"))){
//				kickOutUser(userName);
//			}
			
			UserPO user = ret.getFirstData();
			logLogin.setUserId(user.getCid());
			//用户角色权限处理
			UserPermitVO userPermit=getUserPermitInfo(session, logLogin);
			if(userPermit.getRoleIdList().size()==0){
				loginRet.initError(RetResult.msg_codes.ERR_PERMISSION_DENIED, "user not role permit", null);
				logLoginService.createLogLoginAsync(logLogin, loginRet);
				return loginRet;
			}
			
//			String sessionId=this.getRequest().getSession(true).getId();
			AppContext.putUserInfo(session.getId().toString(), userPermit);
			user.setPasswd(null);
			session.setAttribute(AppContext.SESSION_USER, user);			//把用户信息放session中
			session.setAttribute(AppContext.SESSION_USERID, user.getCid());
			session.setAttribute(AppContext.SESSION_USERIP, ip);
			session.removeAttribute(AppContext.SESSION_SECURITY_CODE);	//清除登录验证码的session
			//shiro加入身份验证
			Subject subject = SecurityUtils.getSubject(); 
		    UsernamePasswordToken token = new UsernamePasswordToken(userName, passwd); 
		    try { 
		        subject.login(token);
		        logLoginService.createLogLoginAsync(logLogin, ret);
		    } catch (AuthenticationException e) {
		    	//身份验证失败！
		    	loginRet.initError(RetResult.msg_codes.ERR_PERMISSION_DENIED, "Valid code invalid", e);
		    	logLoginService.createLogLoginAsync(logLogin, loginRet);
				return loginRet;
		    }
		    
			//成功由后面返回
		}else{
			loginRet.initError(RetResult.msg_codes.ERR_DATA_INPUT, "param missing", null);
		}
		return loginRet;
	}
	
	private String getRetryKey(RetryRuleDetailPO retry, String code){
		return "_retry_"+code+"_"+retry.getCode();
	}

	private UserPermitVO getUserPermitInfo(Session session, LogLoginPO logLogin) {
		UserPermitVO userPermit=new UserPermitVO();
		userPermit.setUserId(logLogin.getUserId());
		
		List<UserRolePO> urList=this.userRoleService.findUserRoleByUserId(logLogin.getUserId());
		
		
		List<Long> roleIdList=new ArrayList<>();
		for(UserRolePO ur:urList){
			if(ur.getIsDefault()!=null && ur.getIsDefault().intValue()==1){
				userPermit.setDefaultRoleId(ur.getRoleId());
			}
			roleIdList.add(ur.getRoleId());
		}
		logger.info("-----getUserPermitInfo--userId="+logLogin.getUserId()+" userRole="+urList.size()+" roleIdList="+roleIdList);
		Collections.sort(roleIdList);
		
		String roleIdStr="";
		for(int i=0; i<roleIdList.size(); i++){
			if(i==0){
				roleIdStr+=roleIdList.get(i);
			}
			else{
				roleIdStr+=","+roleIdList.get(i);
			}
		}
		userPermit.setRoleIdList(roleIdList);
		userPermit.setRoleIdStr(roleIdStr);
		userPermit.setLoginDate(logLogin.getLoginDate());
		userPermit.setLoginType(logLogin.getLoginType());
		return userPermit;
	}
	
	/**访问系统首页
	 * @param changeMenu：切换菜单参数
	 * @return
	 */
	@RequestMapping(value="/main/{changeMenu}")
	@SuppressWarnings("unchecked")
	public ModelAndView login_index(@PathVariable("changeMenu") String changeMenu){
		ModelAndView mv = new ModelAndView();
//		PageData pd = new PageData();
//		pd = this.getPageData();
		try{
			Session session = SecurityUtils.getSubject().getSession();
			UserPO user = (UserPO)session.getAttribute(AppContext.SESSION_USER);				//读取session中的用户信息(单独用户信息)
			if (user != null) {
				UserPO userr = (UserPO)session.getAttribute(AppContext.SESSION_USERROL);		//读取session中的用户信息(含角色信息)
				if(null == userr){
//					user = userService.getUserAndRoleById(user.getUSER_ID());		//通过用户ID读取用户信息和角色信息
					session.setAttribute(AppContext.SESSION_USERROL, user);				//存入session	
				}else{
					user = userr;
				}
				String USERNAME = user.getUserName();
//				RolePO role = user.getRole();											//获取用户角色
//				String roleRights = role!=null ? role.getRIGHTS() : "";				//角色权限(菜单权限)
//				session.setAttribute(USERNAME + Const.SESSION_ROLE_RIGHTS, roleRights); //将角色权限存入session
				session.setAttribute(AppContext.SESSION_USERNAME, USERNAME);				//放入用户名到session
//				List<Menu> allmenuList = new ArrayList<Menu>();
//				if(null == session.getAttribute(USERNAME + Const.SESSION_allmenuList)){	
//					allmenuList = menuService.listAllMenuQx("0");					//获取所有菜单
//					if(Tools.notEmpty(roleRights)){
//						allmenuList = this.readMenu(allmenuList, roleRights);		//根据角色权限获取本权限的菜单列表
//					}
//					session.setAttribute(USERNAME + Const.SESSION_allmenuList, allmenuList);//菜单权限放入session中
//				}else{
//					allmenuList = (List<Menu>)session.getAttribute(USERNAME + Const.SESSION_allmenuList);
//				}
//				//切换菜单处理=====start
//				List<Menu> menuList = new ArrayList<Menu>();
//				if(null == session.getAttribute(USERNAME + Const.SESSION_menuList) || ("yes".equals(changeMenu))){
//					List<Menu> menuList1 = new ArrayList<Menu>();
//					List<Menu> menuList2 = new ArrayList<Menu>();
//					//拆分菜单
//					for(int i=0;i<allmenuList.size();i++){
//						Menu menu = allmenuList.get(i);
//						if("1".equals(menu.getMENU_TYPE())){
//							menuList1.add(menu);
//						}else{
//							menuList2.add(menu);
//						}
//					}
//					session.removeAttribute(USERNAME + Const.SESSION_menuList);
//					if("2".equals(session.getAttribute("changeMenu"))){
//						session.setAttribute(USERNAME + Const.SESSION_menuList, menuList1);
//						session.removeAttribute("changeMenu");
//						session.setAttribute("changeMenu", "1");
//						menuList = menuList1;
//					}else{
//						session.setAttribute(USERNAME + Const.SESSION_menuList, menuList2);
//						session.removeAttribute("changeMenu");
//						session.setAttribute("changeMenu", "2");
//						menuList = menuList2;
//					}
//				}else{
//					menuList = (List<Menu>)session.getAttribute(USERNAME + Const.SESSION_menuList);
//				}
//				//切换菜单处理=====end
//				if(null == session.getAttribute(USERNAME + Const.SESSION_QX)){
//					session.setAttribute(USERNAME + Const.SESSION_QX, this.getUQX(USERNAME));	//按钮权限放到session中
//				}
				AppContext.getRemoteIp(this.getRequest());	//更新登录IP
				mv.setViewName("system/index/main");
				mv.addObject("user", user);
//				mv.addObject("menuList", menuList);
			}else {
				mv.setViewName("system/index/login");//session失效后跳转登录页面
			}
		} catch(Exception e){
			mv.setViewName("system/index/login");
			logger.error(e.getMessage(), e);
		}
//		pd.put("SYSNAME", Tools.readTxtFile(Const.SYSNAME)); //读取系统名称
//		mv.addObject("pd",pd);
		return mv;
	}
	
	
	/**
	 * 进入首页后的默认页面
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/login_default")
	@LogAudit(title="login_default", descs="login system")
	public ModelAndView defaultPage() throws Exception{
		ModelAndView mv = new ModelAndView();
		Map map=new HashMap();
		map.put("userCount", 1);
		map.put("appUserCount", 1);
		mv.addObject("pd",map);
		HttpServletRequest request=this.getRequest();
		HttpServletResponse response=null;
//		this.voteSave(request, response);
		this.addMenuToModel(mv);
		mv.setViewName("system/index/main");
		return mv;
	}
	
	/**
	 * 进入首页后的默认页面
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/login_dashboard")
	@LogAudit(title="login_default", descs="login system")
	public ModelAndView dashboardPage() throws Exception{
		ModelAndView mv = defaultPage();
		mv.setViewName("ins/dashboard/index");
		return mv;
	}
	
	/**
     * save or update
     * @param user
     * @return
     */
    @RequestMapping(value = "/passwd/update", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="login.updatePasswd",author="csh",calls="changePasswordByManage")
    public RetResult<Integer> updatePasswd(UserPO user) {
    	RetResult<Integer> ret=new RetResult<Integer>();
    	String ip=AppContext.getRemoteIp(this.getRequest());
    	ret = loginService.changePasswordByManage(ip, user.getCid(),user.getPasswd());
        return ret;
    }
	/**
	 * 用户注销
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/logout")
	public ModelAndView logout(){
//		String USERNAME = AppContext.getUsername();	//当前登录的用户名
		String ip=AppContext.getIpAddr(this.getRequest());
		
		
		ModelAndView mv = new ModelAndView();
		Session session = SecurityUtils.getSubject().getSession();	//以下清除session缓存
		
		LogLoginPO logLogin=new LogLoginPO();
		logLogin.setUserName((String)session.getAttribute(AppContext.SESSION_USERNAME));
		logLogin.setIp(ip);
		logLogin.setUserId((Long)session.getAttribute(AppContext.SESSION_USERID));
		logLogin.setReferInfo("logout");
		logLogin.setLoginDate(new Date());
		logLogin.setLoginType(1);
		logLoginService.createLogLoginAsync(logLogin, null);
		
		
		session.removeAttribute(AppContext.SESSION_USER);
//		session.removeAttribute(USERNAME + Const.SESSION_ROLE_RIGHTS);
//		session.removeAttribute(USERNAME + Const.SESSION_allmenuList);
//		session.removeAttribute(USERNAME + Const.SESSION_menuList);
//		session.removeAttribute(USERNAME + Const.SESSION_QX);
//		session.removeAttribute(Const.SESSION_userpds);
//		session.removeAttribute(Const.SESSION_USERNAME);
//		session.removeAttribute(Const.SESSION_USERROL);
		session.removeAttribute("changeMenu");
		//shiro销毁登录
		Subject subject = SecurityUtils.getSubject(); 
		subject.logout();
//		pd = this.getPageData();
//		pd.put("msg", pd.getString("msg"));
//		pd.put("SYSNAME", Tools.readTxtFile(Const.SYSNAME)); //读取系统名称
		mv.setViewName("redirect:/login");
//		mv.addObject("pd",pd);
		return mv;
	}
	
	/**
	 * 进入tab标签
	 * @return
	 */
	@RequestMapping(value="/tab")
	public String tab(){
		return "system/index/tab";
	}
	
	/**
	 * 进入首页后的默认页面
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/head/getList.do")
	public String defaultPage1() throws Exception{
		String test = "{\"oladress\":\"192.168.3.11:8889\",\"fhsmsCount\":\"0\",\"list\":[{\"NUMBER\":\"001\",\"RIGHTS\":\"1133671055321055258374707980945218933803269864762743594642571294\",\"IP\":\"192.168.3.11\",\"PHONE\":\"13242939298\",\"USER_ID\":\"1\",\"LAST_LOGIN\":\"2016-06-27 11:33:34\",\"EMAIL\":\"shihui.cai@163.com\",\"NAME\":\"系统管理员\",\"STATUS\":\"0\",\"PASSWORD\":\"de41b7fb99201d8334c23c014db35ecd92df81bc\",\"BZ\":\"最高统治者\",\"USERNAME\":\"admin\",\"ROLE_ID\":\"1\",\"SKIN\":\"default\"}],\"FHsmsSound\":\"2\",\"wimadress\":\"192.168.3.11:8887\"}\"";
		return test;
	}
	

	public static void logBefore(Logger logger, String interfaceName){
		logger.info("");
		logger.info("start");
		logger.info(interfaceName);
	}
	
	public static void logAfter(Logger logger){
		logger.info("end");
		logger.info("");
	}
}
