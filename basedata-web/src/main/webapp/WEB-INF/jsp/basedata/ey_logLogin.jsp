<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html manifest="${env['app_manifest']}" >
<head>
	<meta charset="UTF-8">
    <title>操作日志</title>
   	<%@ include file="/system/_script.jsp"%>
	
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
    
 	<div id="mainWindow" class="easyui-layout" data-options="fit:true" style="display:none">
    	<!-- 查询条件  -->
		<div data-options="region:'north', title:'操作日志-条件'"
			style="height: 80px; padding: 5px 80px;">
			<form id="search-form" >
				<table class="search-table">
					<tr>
						<td>
							用户名:
							<input type="text" class="input_query" name="userName" id="query_code"/>
						</td>
						<td>
							角色:
							<input type="text" class="input_query" name="roles" id="query_roles"/>
						</td>
						
						<td>
							IP:
							<input type="text" class="input_query" name="ip" id="query_ip"/>
						</td>
						<td>
							登入类型:
							<input class="easyui-combobox"  style="width:100px" name="loginType" id="query_loginType" data-options="
								valueField: 'id',
								textField: 'name',
								data: getDictData(dict_tf_login_type,'all')" />
						</td>
						<td>
							登入时间>=:
							<input name="loginDate" id="query_loginDate" class="easyui-datetimebox" style="width:150px" formatter="Common.DateTimeFormatter">
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
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="onAdd()">New</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="onEdit()">Edit</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="onDestroy()">Remove</a>
		    </div>
		    <table id="dg" class="easyui-datagrid" data-options="onDblClickRow:onDblClick"
		            toolbar="#toolbar" pagination="true"
		            rownumbers="true" fitColumns="true" singleSelect="true">
		        <thead>
		            <tr >
		            	<th field="serverId" width="50" sortable="true"  hidden="true">Server id</th>
		            	<th field="loginType" width="50" sortable="true">Login type</th>
		            	<th field="userName" width="50" sortable="true">UserName</th>
		            	<th field="userId" width="50" sortable="true">UserId</th>
		            	<th field="roles" width="50" sortable="true">Roles</th>
		            	<th field="ip" width="50" sortable="true">Ip address</th>
		            	<th field="address" width="150" sortable="true"  hidden="true">Address</th>
		            	<th field="loginDate" width="100" sortable="true"  formatter="Common.DateTimeFormatter">登入时间</th>
		                <th field="referInfo" width="150" sortable="true"  hidden="true">Refer info</th>
		                <th field="other" width="150" sortable="true"  hidden="true">描述信息</th>		                
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
	                <label>Server id:</label>
	                <input name="serverId" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>Login type:</label>
	                <input name="loginType" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>User name:</label>
	                <input name="userName" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>User id:</label>
	                <input name="userId" class="easyui-validatebox" required="true">
	            </div>
	            
	            <div class="fitem">
	                <label>Roles:</label>
	                <input name="roles" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>IP address</label>
	                <input name="ip" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>Address</label>
	                <input name="address" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>Login type:</label>
	                <input name="loginType" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>Login date:</label>
	                <input name="loginDate" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>Refer info</label>
	                <input name="referInfo" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>other:</label>
	                <textarea name="other" id="logMsg_jsonParam" style="height:60px;width:200px"></textarea>
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
		
		function doSearch(){
        	var jsonParam = $('#search-form').serializeJson();
        	//status为all时处理
        	value_all2empty(jsonParam);
        	$('#dg').datagrid({  
        		url:"/basedata/loglogin/list"
                ,queryParams:jsonParam
            });
			//$('#dg').datagrid('load', jsonParam);
        }
		
		
		$(function () {
			var recentlyDate=new Date(new Date().getTime()-7*24*3600*1000);//7天内
			$('#query_loginDate').datetimebox('setValue', Common.DateFormatter(recentlyDate));
			doSearch();
		});
		
		
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
		$('#query_code').keydown(listenerName);
		$('#query_name').keydown(listenerName);
	
        var url;
        function onAdd(){
            $('#dlg').dialog('open').dialog('setTitle','New LogMsg');
            $('#fm').form('clear');
            $('#logMsg_status').combobox('select', '1');
            //$("#logMsg_remark").val('test');
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
            	var entityName='logMsg';
            	var obj = getRowData(entityName, row);
            	//alert(obj.'logMsg.empCode');
                $('#dlg').dialog('open').dialog('setTitle','Edit LogMsg');
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
                url: '/basedata/loglogin/save',
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
                        $.post('/basedata/loglogin/delete',{id:row.cid},function(result){
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