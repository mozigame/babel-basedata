package com.babel.basedata.entity;


import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.util.CommUtil;



public class LogInfoVO implements Serializable {
	private static final long serialVersionUID = 1L;
	public LogInfoVO(){
		
	}
	public LogInfoVO(final Date startTime, final String sqlId, final String sql, final Integer logLevel
			, final long runTime, final Throwable exp, final Map<String, Object> paramMap){
		this.startTime=startTime;
		this.sqlId=sqlId;
		this.sql=sql;
		this.logLevel=logLevel;
		this.runTime=runTime;
		this.exp=expToString(exp);
		this.paramMap=paramMap;
		this.ip=(String)paramMap.get("remoteHost");
	}
	public LogInfoVO(final Date startTime,  String method, final Integer logLevel, final long runTime, final Throwable exp,
			final Map<String, Object> paramMap){
		this.startTime=startTime;
		this.method=method;
		this.logLevel=logLevel;
		this.runTime=runTime;
		this.exp=expToString(exp);
		this.paramMap=paramMap;
		this.ip=(String)paramMap.get("remoteHost");
	}
	
	public static String expToString(Throwable exp){
		if(exp==null){
			return null;
		}
		String expInfo=ExceptionUtils.getFullStackTrace(exp);
		//把caused by的异常信息提到前面去，以免被日志给截掉
		int causedBy_index=expInfo.indexOf("Caused by:");
		if(causedBy_index>=0){
			String causeBy=expInfo.substring(causedBy_index, causedBy_index+200)+"\n";
			expInfo=causeBy+expInfo;
		}
		expInfo=expInfo.replaceAll("\\$", "");
		expInfo= exp.getMessage()+":\n"+expInfo;
		expInfo=CommUtil.getStringLimit(expInfo, 2000);
		return expInfo;
	}
	private Date startTime;
	private Integer logLevel;
	private String sqlId;
	private String method;
	private String sql;
	private long runTime;
	private String exp;
	private String ip;
	private Map<String, Object> paramMap;
	private RetResult<String> retStr;
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public String getSqlId() {
		return sqlId;
	}
	public void setSqlId(String sqlId) {
		this.sqlId = sqlId;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public long getRunTime() {
		return runTime;
	}
	public void setRunTime(long runTime) {
		this.runTime = runTime;
	}
	public String getExp() {
		return exp;
	}
	public void setExp(String exp) {
		this.exp = exp;
	}
	public Map<String, Object> getParamMap() {
		return paramMap;
	}
	public void setParamMap(HashMap<String, Object> paramMap) {
		this.paramMap = paramMap;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Integer getLogLevel() {
		return logLevel;
	}
	public void setLogLevel(Integer logLevel) {
		this.logLevel = logLevel;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public void setRetStr(RetResult<String> retStr) {
		this.retStr = retStr;
	}
	public RetResult<String> getRetStr() {
		return retStr;
	}
	
	
}
