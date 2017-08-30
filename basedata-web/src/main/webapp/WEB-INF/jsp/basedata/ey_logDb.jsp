<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html manifest="${env['app_manifest']}" >
<head>
	<meta charset="UTF-8">
    <title>操作日志</title>
    
	<%@ include file="/system/_script.jsp"%>

	<script type="text/javascript">
		console.log("dev.appcache=${env['dev.appcache']} ${env['dev.app_manifest']}");
	</script>
	
	<style type="text/css">
      #fm{
          margin:0;
          padding:10px 20px;
      }
      .ftitle{
          font-size:14px;
          font-weight:bold;
          padding:5px 0;
          margin-bottom:10px;
          border-bottom:1px solid #ccc;
      }
      .fitem{
          margin-bottom:5px;
      }
      .fitem label{
          display:inline-block;
          width:60px;
      }
      .input_query{
      	width: 80px;
      }
      input,textarea {
	width: 260px;
	border: 1px solid #ccc;
	padding: 2px;
}
</style>
</head>

<body>
    
 	<div id='mainWindow' class="easyui-layout" data-options="fit:true" style="display:none">
    	<!-- 查询条件  -->
		<div data-options="region:'north', title:'操作日志-条件'"
			style="height: 80px; padding: 5px 80px; width:200px">
			<form id="search-form" >
				<table class="search-table">
					<tr>
						<td>
							UUID:
							<input type="text" class="input_query" name="uuid" id="query_uuid"/>
						</td>
						<td>
							User name:
							<input type="text" class="input_query" name="userName" id="query_userName"/>
						</td>
						<td>
							IP:
							<input type="text" class="input_query" name="ip" id="query_ip"/>
						</td>
						<td>
							包名:
							<input type="text" class="input_query" name="packageName" id="query_packageName"/>
						</td>
						<td>
							类名:
							<input type="text" class="input_query" name="className" id="query_className"/>
						</td>
						<td>
							方法名:
							<input type="text" class="input_query" name="methodName" id="query_methodName"/>
						</td>
						<td>
							标题:
							<input type="text" class="input_query" name="title" id="query_title"/>
						</td>
						<td>
							 日志级别
							<input class="easyui-combobox" class="input_query" name="logLevel" id="query_status" data-options="
								valueField: 'id',
								textField: 'name',
								data: getDictData(dict_tf_log_level,'all')" style="width:100px"/>
						</td>
						<td>
							操作时间>=:
							<input name="createDate" id="query_createDate" class="easyui-datetimebox" style="width:150px" formatter="Common.DateTimeFormatter">
						</td>
						<td>
							<a class="easyui-linkbutton"
								data-options="iconCls:'icon-search'" id="search-btn">查询</a>
							<a class="easyui-linkbutton"
								data-options="iconCls:'icon-reload'" id="reset-btn">重置</a>
						</td>

					</tr>
				</table>
			</form>
		</div>
		
		<!-- 查询结果 center -->
	    <div data-options="region:'center', title:'查询结果'">
	    	<div id="toolbar">
		        
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="onEdit()">View</a>
		        <!-- 
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="onAdd()">New</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="onDestroy()">Remove</a>
		         -->
		    </div>
		    <!-- userId用于控制以最小性能的情况下查询空数据，以便于进行重新查询 -->
		    <table id="dg" class="easyui-datagrid" data-options="onDblClickRow:onDblClick"
		            toolbar="#toolbar" pagination="true"
		            rownumbers="true" fitColumns="true" singleSelect="true">
		        <thead>
		            <tr >
		            	<th field="userName" width="30" sortable="true">UserName</th>
		            	
		            	<th field="ip" width="30" sortable="true">IP地址</th>
		            	<th field="threadId" width="20" sortable="true">Thread id</th>
		            	<th field="operType" width="40" sortable="true">Oper type</th>
		            	<th field="createDate" width="55" sortable="true" formatter="Common.DateTimeFormatter">操作时间</th>
		                <th field="title" width="55" sortable="true">标题</th>
		                <th field="className" width="40" sortable="false">类名</th>
		                <th field="methodName" width="40" sortable="false">方法名</th>
		                <th field="calls" width="50" sortable="true">调用接口</th>
		                <th field="uuid" width="50" sortable="true">uuid</th>
		                <th field="descs" width="50" sortable="true"  hidden="true">描述信息</th>
		                <th field="logLevel" width="20" sortable="true" formatter="renderLogLevel">日志级别</th>
		                <th field="flag" width="20" sortable="true" formatter="renderRetFlag">执行状态</th>
		                <th field="author" width="30" sortable="true">Author</th>
		                <th field="rows" width="25" sortable="true">行数</th>
		                <th field="runTime" width="30" sortable="true">运行时间</th>
		                <th field="jsonParam" width="90"  hidden="true">Json param</th>
		                <th field="jsonRet" width="90"  hidden="true">Json return</th>
		                <th field="userId" width="50" sortable="true"  hidden="true">UserId</th>
		              	<th field="serviceId" width="50"  hidden="true">Service Id</th>
		            	<th field="modelId" width="50"  hidden="true">Model Id</th>
		                <th field="packageName" width="200" hidden="true">包名</th>
		            </tr>
		        </thead>
		    </table>
		 </div>
	</div>
    
    <div id="dlg" class="easyui-dialog" style="width:500px;height:600px;padding:5px 10px;display:none"
            closed="true" buttons="#dlg-buttons">
        <fieldset>
			<legend>
				操作日志
			</legend>   
	        <form id="fm" method="post" novalidate>
	        	<input type="hidden" name="cid">
	            <div class="fitem">
	                <label>User name:</label>
	                <input name="userName" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>Header:</label>
	                <input name="headerInfo" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>IP:</label>
	                <input name="ip" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>Thread ID:</label>
	                <input name="threadId" class="easyui-validatebox" required="true">
	            </div>
	          
	            <div class="fitem">
	                <label>服务:</label>
	                <input name="serviceId" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>标题:</label>
	                <input name="title" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>包名:</label>
	                <input name="packageName" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>名类:</label>
	                <input name="className" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>方法名:</label>
	                <input name="methodName" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>Calls:</label>
	                <input name="calls" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>UUID:</label>
	                <input name="uuid" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>描述:</label>
	                <input name="descs" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>数据报文:</label>
	                <textarea name="jsonParam" id="logDb_jsonParam" style="height:60px;width:260px"></textarea>
	            </div>
	            <div class="fitem">
	                <label>数据返回报文:</label>
	                <textarea name="jsonRet" id="logDb_jsonRet" style="height:60px;width:260px"></textarea>
	            </div>
	           
	            <div class="fitem">
	                <label>日志级别:</label>
	                <input class="easyui-combobox" name="logLevel" id="logDb_logLevel" required="true" data-options="
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_log_level" />
	            </div>
	            <div class="fitem">
	                <label>执行状态:</label>
	                <input class="easyui-combobox" name="retFlag" id="logDb_canModify" required="true" data-options="
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_ret_flag" />
	            </div>
	            <div class="fitem">
	                <label>Author:</label>
	                <input name="author" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>Rows:</label>
	                <input name="rows" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>Run Time:</label>
	                <input name="runTime" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>备注:</label>
	                <textarea name="remark" id="logDb_remark" style="height:60px;width:260px"></textarea>
	            </div>
	        </form>
        </fieldset>
        <div id="dlg-buttons">
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">Cancel</a>
	    </div>
    </div>
    
   
    <script type="text/javascript">
		$('#mainWindow').css('display','block');//用于避免chrome下页面刚打开时，画面会有一小会的格式不好的问题
	    $('#search-btn').click(function(){
	    	doSearch();
		});
		$('#reset-btn').click(function(){
			$('#search-form')[0].reset();
		});
		$(function () {
			var recentlyDate=new Date(new Date().getTime()-7*24*3600*1000);//7天内
			$('#query_createDate').datetimebox('setValue', Common.DateFormatter(recentlyDate));
			doSearch();
		});
		function doSearch(){
        	var jsonParam = $('#search-form').serializeJson();
        	$('#dg').datagrid({  
                url:"/basedata/logdb/list"
                ,queryParams:jsonParam
            });
        }
		
		
		
		function doSearchReload(){
			doSearch();
        }
		
		function onDblClick(rowIndex, rowData){
        	//alert('---onDblClick--rowIndex='+rowIndex+' row.id='+rowData.id);
        	onEdit();
        }
		
		function listenerName(ex) {
	        if (ex.keyCode == 13) {                
	        	doSearch();
	        }
	    }
		
		
		$('#query_title').keydown(listenerName);
		$('#query_ip').keydown(listenerName);
		$('#query_userName').keydown(listenerName);
		$('#query_uuid').keydown(listenerName);
		$('#query_packageName').keydown(listenerName);
		$('#query_className').keydown(listenerName);
		$('#query_methodName').keydown(listenerName);
	
        var url;
        function onAdd(){
            $('#dlg').dialog('open').dialog('setTitle','New LogDb');
            $('#fm').form('clear');
            $('#logDb_status').combobox('select', '1');
            //$("#logDb_remark").val('test');
        }
        
        function getRowData(entityName, row){
        	var tmp;
        	var obj={}
        	obj[entityName]={};
        	for(i in row){
        		tmp = row[i];
        		obj[i]=tmp;
        	}
        	return obj;
        }
        function onEdit(){
            var row = $('#dg').datagrid('getSelected');
            if (row){
            	var entityName='logDb';
            	var obj = getRowData(entityName, row);
            	//alert(obj.'logDb.empCode');
                $('#dlg').dialog('open').dialog('setTitle','Edit LogDb');
                $('#fm').form('clear');
                $('#fm').form('load',obj);
            }
           
        }
        function onSave(){
        	var row = $('#dg').datagrid('getSelected');
            if (row){
            	if(row.canModify==0){
            		$.messager.show({
                        title: 'Error',
                        msg: '不可修改，禁止操作'
                    });
            		return;
            	}
            }
            $('#fm').form('submit',{
                url: '/basedata/logdb/save',
                onSubmit: function(){
                    return $(this).form('validate');
                },
                success: function(result){
                    var result = eval('('+result+')');
                    if (result.msgBody){
                        $.messager.show({
                            title: 'Error',
                            msg: result.msgBody
                        });
                    } else {
                        $('#dlg').dialog('close');        // close the dialog
                        doSearchReload();    // reload the user data
                    }
                }
            });
        }
        function onDestroy(){
            var row = $('#dg').datagrid('getSelected');
            if (row){
                $.messager.confirm('Confirm','Are you sure you want to destroy this data?',function(r){
                    if (r){
                        $.post('/basedata/logdb/delete',{id:row.cid},function(result){
                            if (result.success){
                            	doSearchReload();    // reload the user data
                            } else {
                                $.messager.show({    // show error message
                                    title: 'Error',
                                    msg: result.msgBody
                                });
                            }
                        },'json');
                    }
                });
            }
        }
    </script>
    <%@ include file="/system/_foot.jsp"%>
</body>
</html>