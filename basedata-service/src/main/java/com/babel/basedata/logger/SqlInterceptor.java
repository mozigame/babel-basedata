package com.babel.basedata.logger;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.log4j.Logger;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.logger.ILogSqlManager;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.util.CommUtil;
import com.babel.common.core.util.ConfigUtils;
import com.babel.common.core.util.SpringContextUtil;
import com.babel.common.web.context.JobContext;
import com.google.gson.Gson;

@Intercepts({
		@Signature(type = Executor.class, method = "update", args = {
				MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query", args = {
				MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class })
		})
public class SqlInterceptor implements Interceptor {
	private static final Logger logger = Logger.getLogger(SqlInterceptor.class);
	private ILogSqlManager logSqlManager;
	private Map configMap=new HashMap();
	public static int SQL_LOG_IGNORE_ON_LOAD_LIMIT=60;//单位秒，即对于sql.ignoreSqlIdOnLoad在系统启动后60秒才开始日志
	private Date loadDate;//首次运行时间
	public final static Set<String> ignoreSqlIdSet=CommUtil.newSet("SysconfigMapper.findSysconfigAll"
			,"LogDbMapper.insert","LogDbMapper.insertList", "ModelMapper.selectByExample");
	private String runType;
	private String sqlLogDb;
	public Object intercept(Invocation invocation) throws Throwable {
		final long time = System.currentTimeMillis();
		if(logSqlManager==null && SpringContextUtil.containsBean("logSqlManager")){
			this.logSqlManager=(ILogSqlManager)SpringContextUtil.getBean("logSqlManager");
		}
		Date startTime= new Date();
		if(loadDate==null){
			loadDate=startTime;
		}
		
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		Object parameter = null;
		
		if (invocation.getArgs().length > 1) {
			parameter = invocation.getArgs()[1];
		}

		String sqlId = mappedStatement.getId();
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);
		Configuration configuration = mappedStatement.getConfiguration();
		HashMap<String, Object> pMap=new HashMap<>();
		String sql = getSql(configuration, boundSql, pMap);
		Object returnValue = null;
		HashMap<String, Object> paramMap=new HashMap<>();
		String classMethod=null;
		try {
			paramMap.put("threadId",String.valueOf(Thread.currentThread().getId()));
			paramMap.put("args", gson.toJson(pMap));
			String userName= (String)JobContext.getContext().getAttachment("userName");
			Long userId= (Long)JobContext.getContext().getAttachment("userId");
			String uuid=(String)JobContext.getContext().getAttachment("uuid");
			String remoteIp=(String)JobContext.getContext().getAttachment("remoteIp");
			if(StringUtils.isEmpty(remoteIp)){
				remoteIp= (String)JobContext.getContext().getAttachment("remoteHost");
			}
			paramMap.put("userName", userName);
			paramMap.put("userId", userId);
			paramMap.put("uuid", uuid);
			paramMap.put("remoteHost", remoteIp);
			returnValue = invocation.proceed();
			long runTime = (System.currentTimeMillis() - time);
//			if(returnValue!=null){
//				return returnValue;
//			}
			//取得数据行数
			Integer rows=this.getReturnRows(returnValue);
			String sqlType=this.getOperTypeBySql(sql);
			classMethod=getClassMethod(sqlId);
			paramMap.put("sqlType", sqlType);
			paramMap.put("rows", rows);
			
			//忽略指定的查询类型
			String ignoreSqlType=(String)configMap.get("sql.logSqlIgnore");
			if(ignoreSqlType==null){
				ignoreSqlType=(String)configMap.get("sql.ignoreSqlType");
			}
			if(ignoreSqlType==null){
				ignoreSqlType="null";
			}
			System.out.println("---sql="+sql);
			
			if(isIgnore(sqlType, sql, ignoreSqlType)){
				logger.debug("-----ignoreSqlType sqlId="+sqlId+":"+runTime+"ms"+":"+sql);
				return returnValue;
			}
			
			if(runType==null){
				runType=ConfigUtils.getConfigValue("sys.runType", "prod");
			}
			if(sqlLogDb==null){
				sqlLogDb=ConfigUtils.getConfigValue("sys.sqlLogDb", "false");
				
			}
			if("dev".equals(runType)){
				logger.debug("----runType="+runType+" sqlId="+sqlId+":"+runTime+"ms"+":"+sql);
			}
			
			if("false".equals(sqlLogDb)){
				if(!"dev".equals(runType)){
					logger.debug("----sqlId="+sqlId+":"+runTime+"ms"+":"+sql);
				}
				return returnValue;
			}
			
			//忽略mybatis的mapperId的对应的查询日志
			Set<String> ignoreSqlIdSet=(Set<String>)configMap.get("sql.ignoreSqlId");
			if(this.isIgnoreSqlId(classMethod, ignoreSqlIdSet)){
				logger.debug("-----ignoreSqlId sqlId="+sqlId+":"+runTime+"ms"+":"+sql);
				return returnValue;
			}
			
			Set<String> ignoreSqlIdsOnLoad=(Set<String>)configMap.get("sql.ignoreSqlIdOnLoad");
//			System.out.println("------ignoreSqlIds="+ignoreSqlIds);
			boolean isLoadDelay=loadDate.after(DateUtils.addSeconds(startTime, -SQL_LOG_IGNORE_ON_LOAD_LIMIT));
			if(isLoadDelay//在开机启动的60秒内不起作用
					&& this.isIgnoreSqlId(classMethod, ignoreSqlIdsOnLoad)){
				logger.info("-----ignoreSqlIdOnLoad sqlId="+sqlId+":"+runTime+"ms"+":"+sql);
				return returnValue;
			}
			
			//忽略sql含有指定关键字的查询日志
			Set<String> ignoreSqlKey=(Set<String>)configMap.get("sql.ignoreKey");
			if(this.isContainIgnoreSqlKey(sql, ignoreSqlKey)){
				logger.debug("-----ignoreKey sqlId="+sqlId+":"+runTime+"ms"+":"+sql);
				return returnValue;
			}
			
			
			if(ignoreSqlType.indexOf("sqlLogAll")>=0){
				logger.info("-----ignoreSqlType sqlLogAll sqlId="+sqlId+":"+runTime+"ms"+"\n		sql="+sql);
				return returnValue;
			}
			
//			long runTime2 = (System.currentTimeMillis() - time);
//			System.out.println("----runTime="+runTime+"/"+runTime2);
			if(logSqlManager!=null && "true".equals(sqlLogDb))
				this.logSqlManager.addLogSqlAsync(startTime, sqlId, sql, runTime, null, paramMap);
		}
		catch (Exception e) {
			long runTime = (System.currentTimeMillis() - time);
			Set<String> ignoreSqlIdSet=(Set<String>)configMap.get("sql.ignoreSqlId");
			if(this.isIgnoreSqlId(classMethod, ignoreSqlIdSet)){
				logger.error("----intercept--method="+classMethod+" error:"+e.getMessage(), e);
				throw e;
			}
//			if(logSqlManager!=null && "true".equals(sqlLogDb))
//				this.logSqlManager.addLogSqlAsync(startTime, sqlId, sql, runTime, e, paramMap);
			throw e;
		}
		return returnValue;

	}
	
//	private boolean isIgnoreSqlId(final String sqlId, Set<String> ignoreSqlIdSet){
//		if(ignoreSqlIdSet==null||ignoreSqlIdSet.isEmpty()){
//			return false;
//		}
//		
//		String method=sqlId.substring(sqlId.lastIndexOf(".")+1);
//		String className=sqlId.substring(0, sqlId.lastIndexOf("."));
//		className=className.substring(className.lastIndexOf(".")+1);
//		String classMethod=className+"."+method;
//		if(ignoreSqlIdSet.contains(classMethod)){
//			return true;
//		}
//		
//		return false;
//	}
	
	private boolean isContainIgnoreSqlKey(final String sql, final Set<String> ignoreSqlKeySet){
		if(ignoreSqlKeySet==null){
			return false;
		}
//		System.out.println(ignoreSqlKeySet);
		for(String ignoreSqlKey:ignoreSqlKeySet){
			if(ignoreSqlKey.length()>0 && sql.indexOf(ignoreSqlKey)>=0){
				logger.debug("----ignore:"+ignoreSqlKey+" --sql:"+sql);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取返回的数据行数
	 * @param retValue
	 * @return
	 */
	private Integer getReturnRows(final Object retValue){
		if(retValue==null){
			return 0;
		}
		if(retValue instanceof RetResult){
			RetResult ret=(RetResult)retValue;
			return ret.getSize();
		}
		else if(retValue instanceof PageVO){
			PageVO page=(PageVO)retValue;
			if(page.getRows()!=null)
				return page.getRows().size();
		}
		else if(retValue instanceof List){
			return ((List)retValue).size();
		}
//		应该不需要数组，因为一般情况下只会返回list
//		else if(retValue.getClass().isArray()){
//			return ((Object[])retValue).length;
//		}
		return 0;
	}

	public String getSql(Configuration configuration, BoundSql boundSql, Map<String, Object> paramMap) {

		String sql = showSql(configuration, boundSql, paramMap);
		StringBuilder str = new StringBuilder(100);
//		str.append(sqlId);
//		str.append(":");
		str.append(sql);
//		str.append(":");
//		str.append(time);
//		str.append("ms");
		return str.toString();

	}
	
	/**
	 * 获取数据库操作类型
	 * @param sql
	 * @return
	 */
	private String getOperTypeBySql(final String sql){
		String operType=null;
		String oper=sql;
		if(sql.length()>6){
			oper=sql.substring(0, 6);
		}
		operType=oper.toLowerCase();
		if(operType.equals("select")){
			if(sql.indexOf("count(0)")>0){
				operType+="_count";
			}
			else if(sql.indexOf(" _nextval(")>0){
				operType+="_seq";
			}
		}
		return operType;
	}
	
	/**
	 * 是否忽略
	 * @param operType
	 * @param sql
	 * @param logSqlIgnore
	 * @return
	 */
	private boolean isIgnore(final String operType, final String sql, final String logSqlIgnore){
		if(logSqlIgnore==null){
			return false;
		}
		String[] ignores=logSqlIgnore.split(",");
		for(String ignore:ignores){
			ignore=ignore.trim();
			if(StringUtils.isEmpty(ignore)){
				continue;
			}
			if(operType.equals(ignore)||sql.indexOf(ignore)>=0){
				logger.debug("----ignore:"+ignore+" --sql:"+sql);
				return true;
			}
		}
		return false;
	}
	private static  Gson gson = new Gson();
	
	private String getParameterValue(final Object obj) {
		String value = null;

		if (obj instanceof String) {
			value = "'" + obj.toString() + "'";
		} else if (obj instanceof Date) {
			DateFormat formatter = DateFormat.getDateTimeInstance(
					DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
			value = "'" + formatter.format((Date)obj) + "'";//日期时间问题处理
		} else {
			if (obj != null) {
				value = obj.toString();
			} else {
				value = "null";
			}
		}
		//用于处理要保存的数据中含有$字符，造成replaceFirst异常
//		if(value!=null && value.indexOf("$")>0){
//			value=value.replaceAll("\\$", TMP_$_STR);
//		}
		return value;
	}
	
	private final static String TMP_$_STR="@###";

	public String showSql(Configuration configuration, BoundSql boundSql, Map<String, Object> paramMap) {
		Object parameterObject = boundSql.getParameterObject();
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		String sql = boundSql.getSql().replaceAll("[\\s]+", " ");

		if (parameterMappings.size() > 0 && parameterObject != null) {
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
				sql = sql.replaceFirst("\\?",getParameterValue(parameterObject));
			} else {
				MetaObject metaObject = configuration.newMetaObject(parameterObject);
				try {
					for (ParameterMapping parameterMapping : parameterMappings) {
						String propertyName = parameterMapping.getProperty();
						if (metaObject.hasGetter(propertyName)) {
							Object obj = metaObject.getValue(propertyName);
							sql = sql.replaceFirst("\\?", getParameterValue(obj));
							paramMap.put(propertyName, obj);
						} else if (boundSql.hasAdditionalParameter(propertyName)) {
							Object obj = boundSql.getAdditionalParameter(propertyName);
							sql = sql.replaceFirst("\\?", getParameterValue(obj));
							paramMap.put(propertyName, obj);
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
					logger.warn("---showSql--parameterObject="+parameterObject+ " error:"+e.getMessage());
				}
			}
		}
//		if(sql.indexOf(TMP_$_STR)>0){
//			sql=sql.replaceAll(TMP_$_STR, "$");
//		}
		return sql;

	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}


	public void setLogSqlManager(ILogSqlManager logSqlManager) {
		this.logSqlManager = logSqlManager;
	}
	
	public void setProperties(Properties properties) {
		if(properties==null||properties.isEmpty()){
			return;
		}
		Set<Map.Entry<Object,Object>> sets=properties.entrySet();
		String key=null;
		for(Map.Entry<Object,Object> entry:sets){
			key=(String)entry.getKey();
			if("sql.ignoreSqlId".equals(key)
					||"sql.ignoreSqlIdOnLoad".equals(key)
					||"sql.ignoreKey".equals(key)){
				this.putStringSet(entry);
			}
			else{
				configMap.put(key, entry.getValue());
			}
		}
		logger.info("sqlIntercepter.properties="+configMap);
	}

	/**
	 * ModelMapper:create,insert 转成 ModelMapper.create,ModelMapper.insert
	 * @param lines
	 * @return
	 */
	private Set<String> getString2Set(final String key, final String lines){
		Set<String> sets=new HashSet<String>();
		if(!StringUtils.isEmpty(lines)){
			String[] sqlIds=lines.trim().split("\n");
			for(String sqlId:sqlIds){
				sqlId=sqlId.trim();
				if(!"".equals(sqlId)){
					addAllSqlIds(key, sqlId, sets);
				}
			}
		}
		
		return sets;
	}
	
	private void putStringSet(Map.Entry<Object,Object> entry){
		String v=(String)entry.getValue();
		Set<String> vSet=(Set<String>)configMap.get((String)entry.getKey());
		if(vSet==null){
			vSet=new HashSet<>();
		}
		vSet.addAll(this.getString2Set((String)entry.getKey(), v));
		configMap.put(entry.getKey(), vSet);
	}


	/**
	 * 根据配置的信息生成对应的每个方法的sqlId配置
	 * ModelMapper:create,insert 转成 ModelMapper.create,ModelMapper.insert
	 * @param sets
	 * @param sqlId
	 * @return
	 */
	private void addAllSqlIds(final String key, String sqlId, Set<String> sets) {
		String className="";
		if("sql.ignoreSqlId".equals(key) && sqlId.indexOf(":")>0){
			className=sqlId.substring(0, sqlId.indexOf(":"));
			sqlId=sqlId.substring(sqlId.indexOf(":")+1);
		}
		String[] methods=sqlId.split(",");
		for(String method:methods){
			method=method.trim();
			if(className.isEmpty()){
				sets.add(method);
			}
			else{
				sets.add(className+"."+method);
			}
		}
	}
	
	private String getClassMethod(final String sqlId){
		String tmp=sqlId.substring(0, sqlId.lastIndexOf("."));
		return sqlId.substring(tmp.lastIndexOf(".")+1);
	}
	
	private boolean isIgnoreSqlId(final String classMethod, Set<String> ignoreSqlIdSet){
		if(ignoreSqlIdSet==null||ignoreSqlIdSet.isEmpty()){
			return false;
		}
		
		if(ignoreSqlIdSet.contains(classMethod)){
			return true;
		}
		
		return false;
	}
	
}