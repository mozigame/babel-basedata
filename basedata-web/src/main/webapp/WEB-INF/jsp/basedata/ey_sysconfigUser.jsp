<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html manifest="${env['dev.app_manifest']}" >
<head>
	<meta charset="UTF-8">
    <title>系统参数</title>
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
		<div data-options="region:'north', title:'系统参数-条件'"
			style="height: 80px; padding: 5px 80px;">
			<form id="search-form" >
				<input type="hidden" name="confType" id="query_confType">
				<table class="search-table">
					<tr>
						<td>
							编码:
							<input type="text" class="input_query" name="code" id="query_code"/>
						</td>
						<td>
							名称:
							<input type="text" class="input_query" name="name" id="query_name"/>
						</td>
						<td>
							 状态
							<input class="easyui-combobox" class="input_query" name="status" id="query_status" data-options="
								valueField: 'id',
								textField: 'name',
								data: getDictData(dict_tf_status,'all')" />
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
	    		<%/* 
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="onAdd()">New</a>
		        */ %>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openTree()">New</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="onEdit()">Edit</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="onDestroy()">Remove</a>
		    </div>
		    <table id="dg" class="easyui-datagrid" data-options="onDblClickRow:onDblClick"
		            url="/basedata/sysconfigUser/userlist" 
		            toolbar="#toolbar" pagination="true"
		            rownumbers="true" fitColumns="true" singleSelect="true">
		        <thead>
		            <tr >
		            	<%/* 
		            	<th field="cid" width="50" sortable="true">Id</th>
		            	*/ %>
		            	<th field="code" width="50" sortable="true">编码</th>
		                <th field="name" width="50" sortable="true">名称</th>
		                <th field="value" width="50" sortable="true">值</th>
		                <th field="valueDefault" width="50" sortable="true">默认值</th>
		                <th field="orderCount" width="50" sortable="true">顺序号</th>
		                <th field="ifEnv" width="50" sortable="true" formatter="renderIf">是否环境参数</th>
		                <th field="canModify" width="50" sortable="true"  hidden="true" formatter="renderIf">是否可修改</th>
		                <th field="create_disp" width="100" sortable="true"  hidden="true">Create user</th>
		                <th field="createDate" width="120" sortable="true"  hidden="true" formatter="Common.DateTimeFormatter">Create date</th>
		                <th field="modify_disp" width="100" sortable="true"  hidden="true">Modify user</th>
		                <th field="modifyDate" width="120" sortable="true"  hidden="true" formatter="Common.DateTimeFormatter">Modify date</th>
		                <th field="remark" width="150"  hidden="true">备注</th>
		                
		            </tr>
		        </thead>
		    </table>
		 </div>
	</div>
    
    <div id="dlg" class="easyui-dialog" style="width:510px;height:500px;padding:5px 10px;display:none"
            closed="true" buttons="#dlg-buttons">
        <fieldset>
			<legend>
				系统参数
			</legend>   
	        <form id="fm" method="post" novalidate>
	        	<input type="hidden" name="cid">
	        	<input type="hidden" name="pid">
	        	<input type="hidden" name="sysconfigId" id="sysconfig_sysconfigId">
	        	<input type="hidden" name="confType" id="sysconfig_confType">
	        	<div class="fitem">
	                <label>Parent:</label>
	                <label><div id="parentName" style="width:300px"></div></label>
	            </div>
	            <div class="fitem">
	                <label>编码:</label>
	                <label><div id="v_code" name="code" style="width:300px"></div></label>           
	            </div>
	            <div class="fitem">
	                <label>名称:</label>
	                <label><div id="v_name" name="name" style="width:300px"></div></label>
	            </div>
	            <div class="fitem">
	                <label>值:</label>
	                <textarea name="value" id="sysconfig_value" style="height:60px;width:280px" required="true"></textarea>
	            </div>
	            <div class="fitem">
	                <label>默认值:</label>
	                <textarea name="valueDefault" id="sysconfig_valueDefault" style="height:60px;width:280px" required="true"></textarea>
	            </div>
	            <div class="fitem">
	                <label>json值:</label>
	                <textarea name="valueJson" id="sysconfig_valueJson" style="height:60px;width:280px"></textarea>
	            </div>
	            <div class="fitem">
	                <label>顺序号:</label>
	                <input name="orderCount" class="easyui-validatebox" required="true">
	            </div>
	            
	           
	           
	            <div class="fitem">
	                <label>备注:</label>
	                <textarea name="remark" id="sysconfig_remark" style="height:60px;width:280px"></textarea>
	            </div>
	        </form>
        </fieldset>
        <div id="dlg-buttons">
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="onSave()">Save</a>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">Cancel</a>
	    </div>
    </div>
    
    <div id="treeWin" class="easyui-dialog" style="width:600px;height:500px;padding:5px 10px;display:none"
            closed="true">
            <iframe src='/basedata/sysconfigUser/tree' frameborder="0" style="width:100%;height:100%;">
            </iframe>
    </div>
    
    <script type="text/javascript">
    	$('#mainWindow').css('display','block');//用于避免chrome下页面刚打开时，画面会有一小会的格式不好的问题
	    $('#search-btn').click(function(){
	    	doSearch();
		});
		$('#reset-btn').click(function(){
			$('#search-form')[0].reset();
		});
		
		function openTree(){
			 $('#treeWin').dialog('open').dialog('setTitle','Sysconfig tree');
		}
		
		function doSearch(){
        	var jsonParam = $('#search-form').serializeJson();
        	//status为all时处理
        	value_all2empty(jsonParam);
        	
			$('#dg').datagrid('load', jsonParam);
        }
		
		
		
		function doSearchReload(){
        	var jsonParam = $('#search-form').serializeJson();
        	value_all2empty(jsonParam);
			$('#dg').datagrid('reload', jsonParam);
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
            $('#dlg').dialog('open').dialog('setTitle','New Sysconfig');
            $('#fm').form('clear');
            $('#sysconfig_status').combobox('select', '1');
            //$("#sysconfig_remark").val('test');
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
            	var entityName='sysconfig';
            	var obj = getRowData(entityName, row);
            	//alert(obj.'sysconfig.empCode');
                $('#dlg').dialog('open').dialog('setTitle','Edit Sysconfig');
                $('#fm').form('clear');
                $('#fm').form('load',obj);
                $('#v_code').html(obj.code);
                $('#v_name').html(obj.name);
                $('#parentName').html(obj.parentName);
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
            $('#sysconfig_sysconfigId').val(row.cid);
            $('#fm').form('submit',{
                url: '/basedata/sysconfigUser/save',
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
                        $.post('/basedata/sysconfigUser/deleteByUser',{sysconfigId:row.cid},function(result){
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