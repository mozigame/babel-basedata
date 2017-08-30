<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html manifest="${env['app_manifest']}" >
<head>
	<meta charset="UTF-8">
    <title>重试规则</title>
    <%@ include file="/system/_script.jsp"%>
	
	<script type="text/javascript">
	function iframeCenterSearch(options){
		if(document.getElementById("iframeCenter")) {
			try{
				document.getElementById("iframeCenter").contentWindow.iframeCenterSearch(options);
			} catch (e) {
				console.log(e);
			}
		}
	}
	</script>
</head>

<body>
    
 	<div class="easyui-layout" data-options="fit:true">
 		<div region="west" split="true" title="白名单类型" style="width:350px;">
 			<iframe width="100%" height="100%" frameborder="0" src="/basedata/whiteType">
			</iframe>
 		</div>
 		<div id="content" region="center" title="白名单" style="padding:5px;">
 			<iframe id="iframeCenter" width="100%" height="100%" frameborder="0" src="/basedata/whiteList/index">
			</iframe>
		</div>
 	</div>
 	<%@ include file="/system/_foot.jsp"%>
</body>
</html>