package com.babel.basedata.filter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.springframework.http.HttpStatus;

import com.babel.basedata.model.FuncRetryPO;
import com.babel.basedata.util.RetryRuleUtils;
import com.babel.basedata.util.WhiteListUtils;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.entity.UserPermitVO;
import com.babel.common.core.service.IRetryRuleCheckService;
import com.babel.common.core.service.IWhiteListCheckService;
import com.babel.common.core.util.ServerUtil;
import com.babel.common.web.context.AppContext;

/**
 *  
 * 用于控制用户访问频率，从而禁止同一用户同样的请求执行执行频率过高
 * 这个功能对并发性能影响较多，所以尽量使用java缓存
 *
 */
public class LoginFilter implements Filter {
	private static final Log logger = LogFactory.getLog(LoginFilter.class);

	/**
	 * 初始化
	 */
	 public void init(FilterConfig fc) throws ServletException {
		 //FileUtil.createDir("d:/FH/topic/");
	 }

	
	public void destroy() {

	}

//	private static RedisTemplate redisTemplate = null;
	/**
	 * retryRuleCheckEhcacheService
	 * retryRuleCheckRedisService
	 */
	private static IRetryRuleCheckService retryRuleCheckService = null;
	private static IWhiteListCheckService whiteListCheckService = null;
	private static String SHIRO_SESSION_ID="org.apache.shiro.web.servlet.ShiroHttpServletRequest_REQUESTED_SESSION_ID";
	
	private static final String[] FILTER_FILE_TYPE = new String[] { ".js",
			".css", ".jpg", ".png", ".gif", ".woff" };

	

	private void initService() {
		retryRuleCheckService=RetryRuleUtils.getCheckServiceByFuncRuleCode(RetryRuleUtils.RETRY_FUNC_CODE_LOGINFILTER);
		whiteListCheckService=WhiteListUtils.getCheckServiceByTypeCode();
	}

	private boolean isMatchUrl(String anonUrl, String servletInfo) {
		boolean isMatch = false;
		if (anonUrl.equals(servletInfo)) {
			isMatch = true;
		} else if (anonUrl.endsWith("**")
				&& servletInfo.startsWith(anonUrl.substring(0,
						anonUrl.length() - 2))) {
			isMatch = true;
		}
		return isMatch;
	}

	/**
	 * 过滤shiro不进行权限控制的资源
	 * @param servletInfo
	 * @return
	 */
	private boolean isAnon(String servletInfo) {
		if (ServerUtil.filterChainMap == null
				|| ServerUtil.filterChainMap.isEmpty()) {
			return true;
		}
		boolean isAnon = false;
		Set<Map.Entry<String, String>> entrys = ServerUtil.filterChainMap
				.entrySet();
		// System.out.println("---------"+ServerUtil.filterChainMap);

		for (Map.Entry<String, String> entry : entrys) {
			// String anonUrl=entry.getKey();
			// System.out.println(anonUrl.substring(0, anonUrl.length()-2));
			if ("anon".equals(entry.getValue())
					&& this.isMatchUrl(entry.getKey(), servletInfo)) {
				isAnon = true;
				break;
			}
		}
		// System.out.println("----isanon="+isAnon+" size="+ServerUtil.filterChainMap.size()+" servletInfo="+servletInfo);
		return isAnon;
	}


	
	
	
	/**
	 * 用于检查url是否是funcRetry配置的url
	 * @param funcRetry
	 * @param servletInfo
	 * @return
	 */
	private boolean checkRetryUrl(FuncRetryPO funcRetry, String servletInfo){
		boolean isCheckUrl=false;
		if(funcRetry==null){
			return isCheckUrl;
		}
		Map<String, String> paramMap =RetryRuleUtils.getFuncRetryParam(funcRetry.getCid());
		if(paramMap!=null){
			String checkPaths=paramMap.get("checkPaths");
			if(checkPaths!=null){
				String[] paths=checkPaths.split(",");
				for(String path:paths){
					if(servletInfo.equals(path)){
						isCheckUrl=true;
						break;
					}
				}
			}
		}
		
		return isCheckUrl;
	}
	
	/**
	 * 基于reqUri的权限检查
	 * @param reqUri
	 * @param allModuleUrl
	 * @param roleModuleUrl
	 * @return
	 */
	private boolean checkPermit(String reqUri, Set<String> allModuleUrl, Set<String> roleModuleUrl){
		//如果reqUri未在模块菜单中配过，则不控制，直接通过
		if(!allModuleUrl.contains(reqUri)){
			return true;
		}
		//如果reqUri在模块菜单中配过，则需要用户角色的权限中也有这项才通过
		if(roleModuleUrl.contains(reqUri)){
			return true;
		}
		//否则不通过
		return false;
	}
	
	/**
	 * 过滤静态资源，如.jpg,js等
	 * @param reqUri
	 * @return
	 */
	private boolean isFilter(String reqUri){
		boolean isFilter=false;
		if ("/".equals(reqUri) || "/code.do".equals(reqUri)||"/logout".equals(reqUri)||"/login".equals(reqUri)) {
			isFilter = true;
			return isFilter;
		}
		for(String fType:FILTER_FILE_TYPE){
			if(reqUri.endsWith(fType)){
				isFilter=true;
				break;
			}
		}
		return isFilter;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String reqUri=request.getRequestURI();
		String ip=AppContext.getIpAddr(request);
		String sessionId=request.getRequestedSessionId();
		if(sessionId==null){
			sessionId=(String)req.getAttribute(SHIRO_SESSION_ID);
		}
		
		String userName = ServerUtil.getUserNameBySession(sessionId);
		
		boolean isFilter = this.isFilter(reqUri);

//		if (!isFilter && this.isAnon(servletInfo)) {
//			isFilter = true;
//		}

		if(!isFilter){
			if(retryRuleCheckService==null){
				this.initService();
			}
			if(whiteListCheckService!=null){
				int dataType=1;//数据类型：1IP，2邮箱，3手机号
				RetResult<Boolean> passRet=whiteListCheckService.check(dataType, ip);
				if(!passRet.getFirstData()){
					logger.warn("---filter-"+reqUri+" ip="+ip+" msgCode="+passRet.getMsgBody());
					response.sendError(HttpStatus.DESTINATION_LOCKED.value(), "permission deny");
					return;
				}
				else{
//					logger.info("---filter-"+reqUri+" ip="+ip+" msgCode="+passRet.getMsgBody());
				}
			}
			/**
			 * 用户菜单权限检查，看是否有权限使用
			 */
			UserPermitVO userInfo=AppContext.getUserInfo(sessionId);
			if(userInfo==null){
				logger.warn("----userInfo=null uri="+reqUri+" userName="+userName+" sessionId="+sessionId);
			}
			if(userInfo!=null){
				Set<String> allModuleUrl=AppContext.getRoleModule(AppContext.ROLE_ALL_MODULE_URL);
				Set<String> roleModuleUrl=AppContext.getRoleModule(userInfo.getRoleIdStr());
				boolean isPermit=this.checkPermit(reqUri, allModuleUrl, roleModuleUrl);
//				logger.info("---filter-"+reqUri+" sessionId="+sessionId+" userId="+userInfo.getUserId()+" roleId="+userInfo.getRoleIdStr()+" isPermit="+isPermit);
				userInfo.setVisitCount(userInfo.getVisitCount()+1);
				if(!isPermit){
					logger.warn("---filter-"+reqUri+" userId="+userInfo.getUserId()+" userName="+userName+" roleId="+userInfo.getRoleIdStr()+" isPermit="+isPermit);
					userInfo.setFilterCount(userInfo.getFilterCount()+1);
					response.sendError(HttpStatus.DESTINATION_LOCKED.value(), "permission deny");
					return;
				}
			}
		
		}
		
		if(!isFilter && this.retryRuleCheckService!=null){
			FuncRetryPO funcRetry=RetryRuleUtils.getFuncRetry(RetryRuleUtils.RETRY_FUNC_CODE_LOGINFILTER);
			boolean isCheckUrl=this.checkRetryUrl(funcRetry, reqUri);//是否进行访问频率控制
//			logger.info("----reqUri="+reqUri+" isCheckUrl="+isCheckUrl);
			if(isCheckUrl){
				RetResult<String> ret=retryRuleCheckService.checkLoginRetryCount(RetryRuleUtils.RETRY_FUNC_CODE_LOGINFILTER, userName, ip);
				if(!ret.isSuccess()){
//					res.setCharacterEncoding("UTF-8");
					response.sendError(HttpStatus.DESTINATION_LOCKED.value(),  
	                        ret.getMsgBody());
	                return;
				}
			}
		}
		
		if("localhost".equals(ip)){
			ip="127.0.0.1";
		}
		MDC.put("ip", ip.substring(ip.indexOf(".")+1));
		
		if(userName!=null){
			MDC.put("user", userName);
		}
		chain.doFilter(req, res); // 调用下一过滤器
		if(userName!=null){
			MDC.remove("user");
		}
		MDC.remove("ip");
	}
}
