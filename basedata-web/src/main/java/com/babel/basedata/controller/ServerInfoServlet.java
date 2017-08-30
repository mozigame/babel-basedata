package com.babel.basedata.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeansException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.babel.basedata.model.RetryRuleDetailPO;
import com.babel.basedata.util.RetryRuleUtils;
import com.babel.basedata.util.SysconfigUsers;
import com.babel.basedata.util.Sysconfigs;
import com.babel.common.core.entity.RunCountVO;
import com.babel.common.core.entity.UserPermitVO;
import com.babel.common.core.performance.MBeanUtil;
import com.babel.common.core.performance.MonitorInfoBean;
import com.babel.common.core.performance.MonitorUtil;
import com.babel.common.core.service.IThreadPoolInfoService;
import com.babel.common.core.util.ConfigUtils;
import com.babel.common.core.util.RedisUtil;
import com.babel.common.core.util.ServerUtil;
import com.babel.common.core.util.TaskExecutorUtils;
import com.babel.common.web.context.AppContext;
import com.google.gson.Gson;



public class ServerInfoServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(ServerInfoServlet.class);

	/** 
     *  
     */  
    private static final long serialVersionUID = 1L;
    private WebApplicationContext ctx;
    @Override
    public void init() throws ServletException {
    	super.init();
    	ServletContext servletContext = this.getServletContext();
    	ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
    }
    
    private IThreadPoolInfoService getThreadPoolInfoService(String serviceName){
    	try {
    		if(ctx.containsBean(serviceName)){
    			return (IThreadPoolInfoService)ctx.getBean(serviceName);
    		}
    		else{
    			logger.warn("serviceName="+serviceName+" not found");
    			return null;
    		}
		} catch (BeansException e) {
			logger.warn("serviceName="+serviceName+" error="+e.getMessage());
			return null;
		}
    }
    
    /**
     * 获取其他应用的线程池信息
     * @return
     */
    private Map<String, Map<String, Object>> getOtherThreadPoolMap(){
    	Map<String, Map<String, Object>> poolInfoMap=new HashMap<>();
    	IThreadPoolInfoService threadPoolInfoService=null;
    	
    	//basedata
    	
    	threadPoolInfoService=this.getThreadPoolInfoService("basedataThreadPoolInfoService");
    	if(threadPoolInfoService!=null){
    		poolInfoMap.putAll(threadPoolInfoService.getPoolInfoMap());
    	}
    	
    	//schedule
    	threadPoolInfoService=this.getThreadPoolInfoService("scheduleThreadPoolInfoService");
    	if(threadPoolInfoService!=null){
    		poolInfoMap.putAll(threadPoolInfoService.getPoolInfoMap());
    	}
    	
    	//taskExecutorUtils
    	poolInfoMap.putAll(TaskExecutorUtils.getThreadPoolMap());
    	return poolInfoMap;
    }
    
    private static List<String> whiteipList=new ArrayList<String>();
    private List<String> getWhiteList(){
    	List<String> ipList=new ArrayList<>();
//    	whiteipList.clear();
    	if(whiteipList.isEmpty()){
    		synchronized (whiteipList) {
    			whiteipList.add("192.168.");
    	        whiteipList.add("localhost");
    	        whiteipList.add("0:0:0:0:0:0:0:1");
    	        whiteipList.add("127.0.0.1");
			}
    	}
    	ipList.addAll(whiteipList);
    	String whiteip=(String)Sysconfigs.getEnvMap().get("app.serverInfo.whiteip");
        if(whiteip!=null){
        	String[] ips=whiteip.split(",");
        	for(String ip:ips){
        		ipList.add(ip);
        	}
        }
        return ipList;
    }
  
    @Override  
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)  
            throws ServletException, IOException {
    	resp.setContentType("text/json");
        PrintWriter out = resp.getWriter();
        String getType = req.getParameter("getType");
        String appRunType=(String)Sysconfigs.getEnvMap().get("app.runType");
        if("product".equals(appRunType)){
	        String remoteIp= AppContext.getRemoteIp(req);
	        List<String> whiteipList=this.getWhiteList();
	        System.out.println("-----remoteIp="+remoteIp+" whiteipList="+whiteipList);
	        boolean isExist=false;
	        for(String ip:whiteipList){
	        	if(remoteIp.startsWith(ip)){
	        		isExist=true;
	        		break;
	        	}
	        }
	        
	        if(!isExist && "product".equals(appRunType)){
	        	out.println("ip:"+remoteIp+" out of limit!");
	        	out.close();
	        	return;
	        }
        }
        Gson gson = new Gson();
        if("requestInfo".equals(getType)){
        	Map<String, Object> reqMap=new TreeMap<>();
        	reqMap.putAll(this.getRequestMap(req));
        	out.print(gson.toJson(reqMap));
        }
        else if("poolInfo".equals(getType)||"threadPool".equals(getType)){
        	Map<String, Map<String, Object>> poolInfoMap=ServerUtil.getThreadPoolInfo();
        	poolInfoMap.putAll(this.getOtherThreadPoolMap());
        	out.print(gson.toJson(poolInfoMap));
        }
        else if("mbean".equals(getType)){
        	Map map=MBeanUtil.getMBeanInfo();
        	out.print(gson.toJson(map));
        }
        else if("runCount".equals(getType)){
        	Map<String, RunCountVO> runCountMap=ServerUtil.runCountMap;
        	out.print(gson.toJson(runCountMap));
        }
        else if("retryRule".equals(getType)){
        	Map<String, List<RetryRuleDetailPO> > retryListMap=RetryRuleUtils.retryRuleListMap;
        	out.print(gson.toJson(retryListMap));
        }
        else if("memoryInfo".equals(getType)){
        	Map<String, Object> map=new TreeMap<>();
        	map.putAll(this.getMemoryMap());
        	out.print(gson.toJson(map));
        }
        else if("envInfo".equals(getType)){
        	Map<String, Object> map=new TreeMap<>();
        	map.putAll(Sysconfigs.getEnvMap());
        	map.put("userEnvCount", SysconfigUsers.getUserEnvMap().size());
        	out.print(gson.toJson(map));
        }
        else if("userEnvInfo".equals(getType)){
        	Map<String, Object> map=new TreeMap<>();
        	Long userId=AppContext.getCurrentUserId();
        	Map<String, Object> userConfigMap=SysconfigUsers.getUserEnvMap(userId);
        	if(userConfigMap!=null){
        		map.putAll(userConfigMap);
        	}
        	out.print(gson.toJson(map));
        }else if("configUtil".equals(getType)){
        	//用于输出一些重要但不保密的配置信息
        	Map<String, Object> map=new TreeMap<>();
        	map.put("sys.runType", ConfigUtils.getConfigValue("sys.runType"));
        	map.put("schedule.loadOnFirstStart", ConfigUtils.getConfigValue("schedule.loadOnFirstStart"));
        	map.put("rest.btl", ConfigUtils.getConfigValue("rest.btl"));
        	map.put("redis.database", ConfigUtils.getConfigValue("redis.database"));
        	map.put("redis.host", ConfigUtils.getConfigValue("redis.host"));
        	out.print(gson.toJson(map));
        }
        else if("userPermit".equals(getType)){
        	UserPermitVO userPermit=AppContext.getUserInfo(req.getRequestedSessionId());
        	if(userPermit!=null){
        		out.println(gson.toJson(userPermit));
//        		out.println("<br/>");
//        		out.println(AppContext.getRoleModule(userPermit.getRoleIdStr()));
        	}
        	else{
        		out.println("userPermit not found");
        	}
        }
        else if("onlineUser".equals(getType)){
        	out.print(gson.toJson(ServerUtil.getOnlineSessions()));
        }
        else if("cleanOldSession".equals(getType)){
        	out.print(gson.toJson(ServerUtil.cleanOldSessions()));
        }
        else if("onlineCount".equals(getType)){
        	out.print(ServerUtil.getOnlineSessionCount());
        }
        else if("scheduleInfo".equals(getType)){
        	Map<String, Object> map=ServerUtil.scheduleMap;
        	out.print(gson.toJson(map));
        }
        else if("dataSource".equals(getType)){
        	Map<String, Map<String, Object>> dataMap=ServerUtil.getDataSourceInfo();
//        	dataMap.putAll(this.getOtherThreadPoolMap());
        	out.print(gson.toJson(dataMap));
        }
        else if("redisListSize".equals(getType)){
        	Map<String, Object> map=ServerUtil.getRedisListSize();
        	out.print(gson.toJson(map));
        }
        else if("redisInfo".equals(getType)){
        	String key=req.getParameter("key");
        	Map<String, Object> map=this.getRedisInfo(key);
        	out.print(gson.toJson(map));
        }
        else{
        	Map<String, Object> map=new HashMap<String, Object>();
        	map.put("error", "invalid getType:"+getType);
        	out.print(gson.toJson(map));
        }
        
        out.close();
        
    }
    
    private Map<String, Object> getMemoryMap(){
    	Map<String, Object> map=new TreeMap<String, Object>();
       
        try {
        	MonitorUtil service = new MonitorUtil();   
			MonitorInfoBean monitorInfo = service.getMonitorInfoBean();
			 
//			map.put("cpuRatio", monitorInfo.getCpuRatio());
			map.put("osName", monitorInfo.getOsName());
			map.put("totalThread", monitorInfo.getTotalThread());
			map.put("availableProcessors", Runtime.getRuntime().availableProcessors());//cpu核数
			
			map.put("physic.totalMemory", monitorInfo.getTotalPhysicalMemorySize()+"kb");
			map.put("physic.freeMemory", monitorInfo.getFreePhysicalMemorySize()+"kb");
			long usedMemory=monitorInfo.getTotalPhysicalMemorySize()-monitorInfo.getFreePhysicalMemorySize();
			map.put("physic.usedMemory",usedMemory+"kb");
			
			
			
			map.put("run.totalMemory", monitorInfo.getTotalMemory()+ "kb");
	    	map.put("run.maxMemory", monitorInfo.getMaxMemory()+ "kb");
	    	map.put("run.freeMemory", monitorInfo.getFreeMemory()+ "kb");
	    	map.put("run.usedMemory",monitorInfo.getUsedMemory()+ "kb");
	    	
		} catch (Exception e) {
			logger.warn("----getMemoryMap--", e);
			map.put("error", e.getMessage());
		}   
        
    	return map;
    }
    
    private Map<String, Object> getRequestMap(HttpServletRequest req){
    	Map<String, Object> reqMap=new TreeMap<String, Object>();
    	reqMap.put("sessionId", req.getSession().getId());
    	reqMap.put("sessionShiroId", SecurityUtils.getSubject().getSession().getId());
    	 String requestedSessionId = req.getRequestedSessionId();
         reqMap.put("requestedSessionId", requestedSessionId);
    	 //获得request 相关信息  
        String contextPath=req.getContextPath();
        reqMap.put("contextPath", contextPath);
        String characterEncoding = req.getCharacterEncoding();
        reqMap.put("characterEncoding", characterEncoding);
        String contentType = req.getContentType();
        reqMap.put("contentType", contentType);
//        String localAddr = req.getLocalAddr(); 
//        reqMap.put("localAddr", localAddr);
//        String localName = req.getLocalName(); 
//        reqMap.put("localName", localName);
        String method = req.getMethod();  
        reqMap.put("method", method);
//        String parameter = req.getParameter("param1");
//        reqMap.put("parameter", parameter);
        String protocol = req.getProtocol();  
        reqMap.put("protocol", protocol);
        String serverName = req.getServerName();
        reqMap.put("serverName", serverName);
//        Cookie[] cookies = req.getCookies();  
//        reqMap.put("cookies", cookies);
        String servletPath = req.getServletPath();
        reqMap.put("servletPath", servletPath);
        reqMap.put("remoteAddr", AppContext.getIpAddr(req));
        String requestURI = req.getRequestURI();
        reqMap.put("requestURI", requestURI);
//        String requestedSessionId = req.getRequestedSessionId();
//        reqMap.put("requestedSessionId", requestedSessionId);
        Map<String, Object> headMap=new TreeMap<>();
        Enumeration<String> headers= req.getHeaderNames();
        String header=null;
        while(headers.hasMoreElements()){
        	header=headers.nextElement();
        	headMap.put(header, req.getHeader(header));
        }
        reqMap.put("header", headMap);
        return reqMap;
        
    }
    
    private Map<String, Object> getRedisInfo(String redisKeys){
    	if(redisKeys==null){
    		redisKeys="*";
    	}
    	Map<String, Object> treeMap=new TreeMap<>();
    	RedisTemplate redisTemplate = RedisUtil.getRedisTemplate();
    	Set<String> keys=(Set<String>)redisTemplate.keys(redisKeys);
    	
    	Map<String, Object> typeMap=null;
    	for(String key:keys){
    		DataType dataType=redisTemplate.type(key);
    		typeMap=(Map<String, Object>)treeMap.get(dataType.code());
    		if(typeMap==null){
    			typeMap=new TreeMap<>();
    		}
    		if("hash".equals(dataType.code())){
    			typeMap.put(key, redisTemplate.boundHashOps(key).size());
    		}
    		else if("list".equals(dataType.code())){
    			typeMap.put(key, redisTemplate.boundListOps(key).size());
    		}
    		else if("string".equals(dataType.code())){
    			try {
					typeMap.put(key, redisTemplate.boundValueOps(key).get());
				} catch (Exception e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
					typeMap.put(key, e.getMessage());
				}
    		}
    		else if("zset".equals(dataType.code())){
//    			BoundZSetOperations<String, Object> zSetOperations = redisTemplate.boundZSetOps(key);
//    			zSetOperations.range(0l, 10l);
				typeMap.put(key, redisTemplate.boundZSetOps(key).size());
    		}
    		else if("set".equals(dataType.code())){
				typeMap.put(key, redisTemplate.boundSetOps(key).size());
    		}
    		treeMap.put(dataType.code(), typeMap);
    	}
    	return treeMap;
    }
    
    

}
