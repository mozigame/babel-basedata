<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html manifest="${env['app_manifest']}" >
<head>
	<meta charset="UTF-8">
    <title>用户角色关系管理</title>
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
	width: 160px;
	border: 1px solid #ccc;
	padding: 2px;
}
</style>
</head>

<body>
    
 	<div id="mainWindow" class="easyui-layout" data-options="fit:true" style="display:none">
    	<!-- 查询条件  -->
		<div data-options="region:'north', title:'查询-条件'"
			style="height: 80px; padding: 5px 80px;">
			<form id="search-form" >
				<table class="search-table">
					<tr>
						<td>
							 角色
							<input class="easyui-combobox" name="roleId" 
	                			data-options="valueField:'cid',textField:'name',url:'/basedata/role/name/list',panelHeight:'auto'" />
						</td>
						<td>
							 工作类型
							<input class="easyui-combobox" class="input_query" name="jobType" id="query_jobType" data-options="
								valueField: 'id',
								textField: 'name',
								data: getDictData(dict_lh_job_type,'all')" />
						</td>
						<td>
							 是否默认
							<input class="easyui-combobox" class="input_query" name="isDefault" id="query_isDefault" data-options="
								valueField: 'id',
								textField: 'name',
								data: getDictData(dict_tf_if,'all')" style="width:100px" />
						</td>
						<td>
							 状态
							<input class="easyui-combobox" class="input_query" name="status" id="query_status" data-options="
								valueField: 'id',
								textField: 'name',
								data: getDictData(dict_tf_status,'all')" style="width:100px" />
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
		    <table id="dg" class="easyui-datagrid" data-options="onDblClickRow:onDblClick, onClickRow:onClick"
		            url="/basedata/userRole/list" 
		            toolbar="#toolbar" pagination="true"
		            rownumbers="true" fitColumns="true" singleSelect="true">
		        <thead>
		            <tr >
		            	<%/* 
		            	<th field="id" width="50" sortable="true">Id</th>
		            	*/ %>
		            	<th field="userName" width="50" sortable="true">用户</th>
		                <th field="roleName" width="50" sortable="true">角色</th>
		                <th field="jobType" width="50" sortable="true" formatter="renderJobType">工作类型</th>
		                
		                <th field="isDefault" width="50" sortable="true" formatter="renderIf">是否默认</th>
		                <th field="startDate" width="50" sortable="true">开始时间</th>
		                <th field="endDate" width="50" sortable="true">结束时间</th>
		                <th field="status" width="50" sortable="true" formatter="renderStatus">状态</th>
		                <%/**/ %> 
		                
		                <th field="create_disp" width="100" sortable="true" hidden="true">Create user</th>
		                <th field="createDate" width="120" sortable="true" hidden="true">Create date</th>
		                <th field="modify_disp" width="100" sortable="true" hidden="true">Modify user</th>
		                <th field="modifyDate" width="120" sortable="true"  hidden="true">Modify date</th>
		                <th field="remark" width="150">备注</th>
		                
		            </tr>
		        </thead>
		    </table>
		 </div>
	</div>
    
    <div id="dlg" class="easyui-dialog" style="width:510px;height:500px;padding:5px 10px;display:none"
            closed="true" buttons="#dlg-buttons">
        <fieldset>
			<legend>
				用户角色关系管理
			</legend>   
	        <form id="fm" method="post" novalidate>
	        	<input type="hidden" name="cid">
	            <div class="fitem">
	                <label>用户:</label>
	               <input class="easyui-combobox" name="userId" required="true" 
	                	data-options="valueField:'cid',textField:'name',url:'/basedata/user/name/list',panelHeight:'auto'" />
	            </div>
	            <div class="fitem">
	                <label>角色:</label>
	                <input class="easyui-combobox" name="roleId" required="true" 
	                	data-options="valueField:'cid',textField:'name',url:'/basedata/role/name/list',panelHeight:'auto'" />
	            </div>
	            
	           <div class="fitem">
	                <label>是否默认:</label>
	                <input class="easyui-combobox" name="isDefault" id="userRole_isDefault" required="true" data-options="
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_if" />
	            </div>
	            <div class="fitem">
	                <label>工作类型:</label>
	                <input class="easyui-combobox" name="jobType" id="userRole_jobType" required="true" data-options="
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_lh_job_type" />
	            </div>
	            <div class="fitem">
	                <label>开始时间:</label>
	                <input name="startDate" class="easyui-datetimebox" id="userRole_startDate" required="true">
	            </div>
	            <div class="fitem">
	                <label>结束时间:</label>
	                <input name="endDate"   class="easyui-datetimebox" id="userRole_endDate" >
	            </div>
	            <div class="fitem">
	                <label>状态:</label>
	                <input class="easyui-combobox" name="status" id="userRole_status" required="true" data-options="
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_status" />
	            </div>
	            <div class="fitem">
	                <label>备注:</label>
	                <textarea name="remark" id="userRole_remark" style="height:60px;width:200px"></textarea>
	            </div>
	        </form>
        </fieldset>
        <div id="dlg-buttons">
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="onSave()">Save</a>
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
			$('#dg').datagrid('load', jsonParam);
        }
		
		
		
		function doSearchReload(){
        	var jsonParam = $('#search-form').serializeJson();
			$('#dg').datagrid('reload', jsonParam);
        }
		
		function onDblClick(rowIndex, rowData){
        	//alert('---onDblClick--rowIndex='+rowIndex+' row.id='+rowData.id);
        	onEdit();
        }
		
		function onClick(rowIndex, rowData){
        	if(parent){
        		parent.parent_data.userRoleId=rowData.cid;
        		parent.parent_data.canModify=rowData.canModify;
			}
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
            $('#dlg').dialog('open').dialog('setTitle','New UserRole');
            $('#fm').form('clear');
            $('#userRole_status').combobox('select', '1');
            $('#userRole_isDefault').combobox('select', '0');
           // $('#userRole_startDate').datetimebox('setValue', new Date());
            //$("#userRole_remark").val('test');
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
            	var entityName='userRole';
            	var obj = getRowData(entityName, row);
            	//alert(obj.'userRole.empCode');
                $('#dlg').dialog('open').dialog('setTitle','Edit UserRole');
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
                url: '/basedata/userRole/save',
                onSubmit: function(){
                    return $(this).form('validate');
                },
                success: function(result){
                    var result = eval('('+result+')');
                    if (!result.success){
                        $.messager.show({
                            title: result.msgCode,
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
                        $.post('/basedata/userRole/delete',{id:row.cid},function(result){
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