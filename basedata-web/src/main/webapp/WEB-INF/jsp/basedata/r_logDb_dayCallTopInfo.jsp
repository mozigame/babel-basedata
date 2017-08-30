<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE HTML>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Interface call count</title>
		<%--<%@ include file="/system/_script.jsp"%>--%>
		<style type="text/css">
			${demo.css}
		</style>
		<script type="text/javascript" src="/scripts/lib/jquery/jquery-2.0.1.min.js"></script>
		<script type="text/javascript" src="/scripts/lib/comm.render.js"></script>
		<script type="text/javascript" src="/scripts/basedata/logDb_dayCallTopInfo.js"></script>
		<script type="text/javascript" src="/scripts/lib/Common.js"></script>
		<script type="text/javascript" src="/scripts/lib/colResizable-1.5.min.js"></script>
		<link href="/scripts/basedata/datetimepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet" />
		<script type="text/javascript" src="/scripts/basedata/datetimepicker/js/bootstrap-datetimepicker.js"></script>
	</head>
	<body>
	<link href="/static/ace/css/bootstrap.css" rel="stylesheet">
	<style>
		#search-form{ margin-bottom: 40px;}
		#search-form label{ width: 100px;}
		#search-form .form-group{ margin-bottom: 10px;}
		td {overflow:hidden;white-space:nowrap;text-overflow:ellipsis;}
	</style>

	<div style="padding: 15px">
		<form id="search-form"  role="form" class="form-inline" >
			<div class="row">
				

				<div class="form-group col-md-3">
					<label for="query_userName">开始时间>=:</label>
					<input name="startDate" id="query_startDate" class="form-control" formatter="Common.DateTimeFormatter">
				</div>
				<div class="form-group col-md-3">
					<label for="query_userName">结束时间<=:</label>
					<input name="endDate" id="query_endDate" class="form-control" formatter="Common.DateTimeFormatter">
				</div>
				<div class="form-group col-md-3">
					<label for="query_userName">User name:</label>
					<input type="text" class="form-control" name="userName" id="query_userName"/>
				</div>
				<div class="form-group col-md-3">
					<label for="query_ip">IP:</label>
					<input type="text" class="form-control" name="ip" id="query_ip"/>
				</div>

				<div class="form-group col-md-3">
					<label for="query_packageName">包名:</label>
					<input type="text" class="form-control" name="packageName" id="query_packageName"/>
				</div>
				<div class="form-group col-md-3">
					<label for="query_className">Class name:</label>
					<input type="text" class="form-control" name="className" id="query_className"/>
				</div>
				<div class="form-group col-md-3">
					<label for="query_userName">Method name:</label>
					<input type="text" class="form-control" name="methodName" id="query_methodName"/>
				</div>
				<div class="form-group col-md-3">
					<label for="query_runTime">Run time>=:</label>
					<input type="text" class="form-control" name="runTime" id="query_runTime"/>
				</div>
				<div class="form-group col-md-3">
					<label for="query_rows">Return rows>=:</label>
					<input type="text" class="form-control" name="rows" id="query_rows"/>
				</div>
				<div class="form-group col-md-3">
					<label for="query_title">标题:</label>
					<input type="text" class="form-control" name="title" id="query_title"/>
				</div>
				<div class="form-group col-md-3">
					<label for="query_logLevel">Log level:</label>
					<select class="form-control" name="logLevel" id="query_logLevel">
					  <option value="1" >DEBUG</option>
					  <option value="2" class="btn-info">INFO</option>
					  <option value="3" class="btn btn-warning">WARN</option>
					  <option value="4" class="btn btn-danger">ERROR</option>
					  <option value="5" class="btn btn-danger">FATAL</option>
					</select>
				</div>
				
				<div class="form-group col-md-3">
					<label for="query_userName">TopN:</label>
					<input name="topN" id="query_topN" class="form-control" value="10">
				</div>
			</div>
			<div class="row text-center">
				<a id="search-btn" class="btn btn-primary">查询</a>
				<a id="reset-btn" class="btn btn-success">重置</a>
			</div>
		</form>
	</div>

	<div id="containerReport" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
	</body>
</html>
<%@ include file="/system/_foot.jsp"%>
