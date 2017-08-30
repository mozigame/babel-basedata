package com.babel.basedata.logger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.logger.ILogSqlManager;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.util.CommUtil;
import com.babel.common.core.util.ObjectToMapUtil;
import com.babel.common.web.context.JobContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

/**
 * 用于记录接口对应的作者及标题
 * @author cjh
 *
 */
@Aspect
@Component
public class LogServiceAspect {
	private static final Logger logger = Logger.getLogger(LogServiceAspect.class);
	@Autowired
	private ILogSqlManager logSqlManager;
	

//	private static Map<String, LogSql> logSqlMap=new HashMap<String, LogSql>();

	// @Pointcut("execution(* com.babel..*.*(..))")
	/**
	 * 对有@Traced标记的方法,记录其执行参数及返回结果.
	 */
	@Pointcut("@annotation(com.babel.basedata.logger.LogService)")
	public void log() {
		System.out.println("我是一个切入点");
	}

	/**
	 * 在所有标注@Log的地方切入
	 * 
	 * @param joinPoint
	 */
	@Before("log()")
	public void beforeExec(JoinPoint joinPoint) {
//		time.set(System.currentTimeMillis());
//		tag.set(UUID.randomUUID().toString());
//		logger.info("-----tag="+tag.get());
		
//		MethodSignature ms = (MethodSignature) joinPoint.getSignature();
//		Method method = ms.getMethod();
//		LogSql logSql = method.getAnnotation(LogSql.class);
//		
//		String target=joinPoint.getTarget().toString();
//		int find=target.indexOf("@");
//		String className=target;
//		if(find>0){
//			className=target.substring(0,  find);
//		}
//		logSqlMap.put(className+"."+method.getName(), logSql);
//		logger.info("-------beforeExec----"+className+" logSqlMap="+logSqlMap);
	}
	
	
	@Around("log()")
	public Object aroundExec(ProceedingJoinPoint pjp) throws Throwable {
		final long time = System.currentTimeMillis();
		Date startTime= new Date();
		MethodSignature ms = (MethodSignature) pjp.getSignature();
		Method method = ms.getMethod();
		HashMap<String, Object> pointMap = getPointMap(pjp);
		LogService logService = method.getAnnotation(LogService.class);
		
		
		Object returnValue = null;
		String methodCode=null;
		try {
			methodCode=(String)pointMap.get("method");
			
//			pointMap.put("logService", logService);
			pointMap.put("logService.title", logService.title());
			pointMap.put("logService.author", logService.author());
			pointMap.put("logService.descs", logService.descs());
			pointMap.put("logService.calls", logService.calls());
			pointMap.put("threadId",String.valueOf(Thread.currentThread().getId()));
			
			String userName= (String)JobContext.getContext().getAttachment("userName");
			Long userId= (Long)JobContext.getContext().getAttachment("userId");
			String uuid= (String)JobContext.getContext().getAttachment("uuid");
			String remoteIp=(String)JobContext.getContext().getAttachment("remoteIp");
			if(StringUtils.isEmpty(remoteIp)){
				remoteIp= (String)JobContext.getContext().getAttachment("remoteHost");
			}
//			System.out.println("---- methodCode="+methodCode+" mapKey=trd_"+Thread.currentThread().getId()+" uuid="+uuid+" userId="+userId);
			pointMap.put("userName", userName);
			pointMap.put("userId", userId);
			pointMap.put("remoteHost", remoteIp);
			pointMap.put("uuid", uuid);
		
			returnValue = pjp.proceed();
			
			long runTime=System.currentTimeMillis() - time;
			//取得数据行数
			Integer rows=this.getReturnRows(returnValue);
			pointMap.put("rows", rows);
			
			if(returnValue!=null && !LogService.noRet.equals(logService.descs())){
				Object dataValue = removeDatas(logService.descs(), returnValue);//descs为noDatas的处理
				pointMap.put("returnValue", CommUtil.getStringLimit(gson.toJson(dataValue), 3800));
			}
			if(LogService.noParams.equals(logService.descs())){
				pointMap.remove("args");
			}
//			long runTime2=System.currentTimeMillis() - time;
//			System.out.println("----service.runTime="+runTime+"/"+runTime2);
//			logger.info("------methodCode="+methodCode+"	pointMap1="+pointMap);
			this.logSqlManager.addLogServiceAsync(startTime,  methodCode,  runTime, null, pointMap);
		}
		catch (Exception e) {
			long runTime=System.currentTimeMillis() - time;
//			logger.info("------methodCode="+methodCode+" pointMap2="+pointMap);
			this.logSqlManager.addLogServiceAsync(startTime,  methodCode, runTime, e, pointMap);
			throw e;
		}
		return returnValue;
	}
	
	@AfterThrowing(pointcut = "log()", throwing = "e")  
    public  void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
//		Date startTime= new Date();
//		long time = System.currentTimeMillis();
//		MethodSignature ms = (MethodSignature) joinPoint.getSignature();
//		Method method = ms.getMethod();
//		LogService logService = method.getAnnotation(LogService.class);
//		HashMap<String, Object> pointMap = getPointMap(joinPoint);
//		pointMap.put("logService", logService);
//		pointMap.put("threadId",String.valueOf(Thread.currentThread().getId()));
//
//		String userName= RpcContext.getContext().getAttachment("userName");
//		String userId= RpcContext.getContext().getAttachment("userId");
//		String uuid= RpcContext.getContext().getAttachment("uuid");
//		pointMap.put("userName", userName);
//		pointMap.put("userId", userId);
//		pointMap.put("remoteHost", RpcContext.getContext().getRemoteHost());
//		pointMap.put("uuid", uuid);
//		if(!StringUtils.isEmpty(uuid)){
//			RpcContext.getContext().setAttachment("uuid",uuid);
//			RpcContext.getContext().setAttachment("userName",userName);
//			RpcContext.getContext().setAttachment("userId",userId);
//		}
//
//		long runTime=System.currentTimeMillis() - time;
//	
//		this.logSqlManager.addLogAsync(startTime, runTime, e, pointMap);
	}

	/**
	 * 获取返回的数据行数
	 * @param returnValue
	 * @return
	 */
	private Integer getReturnRows(final Object returnValue){
		if(returnValue==null){
			return 0;
		}
		if(returnValue instanceof RetResult){
			RetResult ret=(RetResult)returnValue;
			return ret.getSize();
		}
		else if(returnValue instanceof PageVO){
			PageVO page=(PageVO)returnValue;
			if(page.getRows()!=null)
				return page.getRows().size();
		}
		else if(returnValue instanceof List){
			return ((List)returnValue).size();
		}
//		应该不需要数组，因为一般情况下只会返回list
//		else if(returnValue.getClass().isArray()){
//			return ((Object[])returnValue).length;
//		}
		return 0;
		
	}

	/**
	 * descs为noDatas的处理，如果返回对象是PageVO,或RetResult则去掉datas以减少日志量
	 * @param descs
	 * @param returnValue
	 * @return
	 */
	private Object removeDatas(final String descs, final Object returnValue) {
		if(!LogService.noDatas.equals(descs)){
			return returnValue;
		}
		if(returnValue instanceof RetResult){
			RetResult ret=(RetResult)returnValue;
			RetResult ret2=new RetResult(ret);
			ret2.setDataList(new ArrayList());
			ret2.setMsgBody("size:"+ret.getSize());
			return ret2;
		}
		else if(returnValue instanceof PageVO){
			PageVO page=(PageVO)returnValue;
			Map<String, Object> pageMap=new HashMap<String, Object>();
			pageMap.put("total", page.getTotal());
			pageMap.put("pageSize", page.getPageSize());
			pageMap.put("pageLimit", page.getPageLimit());
			pageMap.put("totalPage", page.getTotalPage());
			pageMap.put("pageBegin", page.getPageBegin());
			pageMap.put("pageEnd", page.getPageEnd());
			return pageMap;
		}
		return returnValue;
	}


	private HashMap<String, Object> getPointMap(JoinPoint joinPoint) {
		HashMap<String, Object> infoMap = new HashMap<>();
		infoMap.put("kind", joinPoint.getKind());
		infoMap.put("target", joinPoint.getTarget().toString());
		infoMap.put("args", this.getArgs(joinPoint.getArgs()));
		infoMap.put("signature", ""+joinPoint.getSignature());
		infoMap.put("method", ""+joinPoint.getSignature().getName());
		infoMap.put("sourceLocation", ""+joinPoint.getSourceLocation());
		infoMap.put("staticPart", ""+joinPoint.getStaticPart());
		return infoMap;
	}
	private static Gson gson = new Gson();
	private Map<String, Object> getArgs(Object[] args) {
		Map<String, Object> map=new HashMap<String, Object>();
		Object arg=null;
		for (int i = 0; i < args.length; i++) {
			arg=args[i];
			if(arg!=null){
				if(arg instanceof Enum){
					arg=arg.toString();
				}
				else if(!(arg instanceof String || arg instanceof Number || arg instanceof Date)){
					try {
						arg=ObjectToMapUtil.objectToMap(arg);
					} catch (Exception e) {
						logger.warn("----getArgs--"+arg+","+e.getMessage());
						arg=gson.toJson(arg);
					}
				}
				
			}
			map.put(""+i, arg);
		}
		
		return map;
	}

}
