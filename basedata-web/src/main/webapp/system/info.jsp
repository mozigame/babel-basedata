<%//@ page session="false"%>
<%
	out.println(session.getAttribute("_login"));
%>
<script type="text/javascript" src="/scripts/lib/jquery/jquery-2.0.1.min.js"></script>
<a href="/serverInfo?getType=requestInfo">requestInfo</a><br/>
<a href="/serverInfo?getType=memoryInfo">memoryInfo</a><br/>
<a href="/serverInfo?getType=mbean">mbean</a><br/>
<a href="/serverInfo?getType=envInfo">envInfo</a><br/>
<a href="/serverInfo?getType=userEnvInfo">userEnvInfo</a><br/>
<a href="/serverInfo?getType=threadPool">threadPool</a><br/>
<a href="/serverInfo?getType=scheduleInfo">scheduleInfo</a><br/>
<a href="/serverInfo?getType=redisListSize">redisListSize</a><br/>
<a href="/serverInfo?getType=runCount">runCount</a><br/>
<a href="/serverInfo?getType=retryRule">retryRule</a><br/>
<a href="/serverInfo?getType=dataSource">dataSource</a><br/>
<a href="/serverInfo?getType=onlineUser">Online user</a><br/>
<a href="/serverInfo?getType=cleanOldSession">cleanOldSession</a><br/>


<%@ include file="_foot.jsp"%>