<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html manifest="${env['app_manifest']}" >
<head>
	<meta charset="UTF-8">
    <title>数据字典</title>
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
 		<div region="west" split="true" title="Navigator" style="width:350px;">
 			<iframe  id="iframeLeft" width="100%" height="100%" frameborder="0" src="/basedata/lookup">
			</iframe>
 		</div>
 		<div id="content" region="center" title="Language" style="padding:5px;">
 			<iframe id="iframeCenter" width="100%" height="100%" frameborder="0" src="/basedata/lookupItem/index">
			</iframe>
		</div>
 	</div>
 	<%@ include file="/system/_foot.jsp"%>
</body>
</html>