<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html manifest="${env['app_manifest']}" >
<head>
	<meta charset="UTF-8">
    <title>重试规则</title>
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
		<div data-options="region:'north', title:'重试规则-条件'"
			style="height: 160px; padding: 5px 40px;">
			<form id="search-form" >
				<div class="fitem">
	                <label>编码:</label>
	                <input name="code" class="input_query" id="query_code">
	            </div>
	            <div class="fitem">
	                <label>名称:</label>
	                <input name="name" class="input_query" id="query_name">
	            </div>
	            <div class="fitem">
	                <label>状态:</label>
	                <input class="easyui-combobox" class="input_query" name="status" id="query_status" data-options="
								valueField: 'id',
								textField: 'value',
								data: getDictData(dict_tf_status,'all')" style="width:100px"/>
	            </div>
				<div class="fitem">
					<a class="easyui-linkbutton"
								data-options="iconCls:'icon-search'" id="search-btn">查询</a>
							
				</div>
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
		            url="/basedata/retryRule/list" 
		            toolbar="#toolbar" pagination="true"
		            rownumbers="true" fitColumns="true" singleSelect="true">
		        <thead>
		            <tr >
		            	<%/* 
		            	<th field="id" width="50" sortable="true">Id</th>
		            	*/ %>
		            	<th field="code" width="50" sortable="true">编码</th>
		                <th field="name" width="50" sortable="true">名称</th>
		                <th field="ruleType" width="50">Rule type</th>
		                <th field="serviceCode" width="50" hidden="true">spring接口服务名</th>
		                <th field="intfTaskId" width="50" hidden="true">接口任务id</th>
		                <th field="status" width="50" sortable="true" formatter="renderStatus">状态</th>
		               
		                <th field="canModify" width="50" sortable="true" formatter="renderIf" hidden="true">是否可修改</th>
		                 <%/*  */ %>
		                <th field="create_disp" width="100" sortable="true" hidden="true">Create user</th>
		                <th field="createDate" width="120" sortable="true" hidden="true">Create date</th>
		                <th field="modify_disp" width="100" sortable="true" hidden="true">Modify user</th>
		                <th field="modifyDate" width="120" sortable="true" hidden="true">Modify date</th>
		                <th field="remark" width="150" hidden="true">备注</th>
		               
		            </tr>
		        </thead>
		    </table>
		 </div>
	</div>
    
    <div id="dlg" class="easyui-dialog" style="width:300px;height:300px;padding:5px 10px;display:none"
            closed="true" buttons="#dlg-buttons">
        <fieldset>
			<legend>
				重试规则
			</legend>   
	        <form id="fm" method="post" novalidate>
	        	<input type="hidden" name="cid">
	            <div class="fitem">
	                <label>编码:</label>
	                <input name="code" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>名称:</label>
	                <input name="name" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>spring接口服务名:</label>
	                <input name="serviceCode" class="easyui-validatebox">
	            </div>
	            <div class="fitem">
	                <label>接口任务id:</label>
	                <input name="intfTaskId" class="easyui-validatebox">
	            </div>
	            <div class="fitem">
	                <label>Rule type:</label>
	                <input name="ruleType" class="easyui-validatebox">
	            </div>
	           
	            <div class="fitem">
	                <label>状态:</label>
	                <input class="easyui-combobox" name="status" id="retryRule_status" required="true" data-options="
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_status" />
	            </div>
	            <div class="fitem">
	                <label>是否可修改:</label>
	                <input class="easyui-combobox" name="canModify" id="retryRule_canModify" required="true" data-options="
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_if" style="width:100px"/>
	            </div>
	            <div class="fitem">
	                <label>备注:</label>
	                <textarea name="remark" id="retryRule_remark" style="height:60px;width:200px"></textarea>
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
        		parent.iframeCenterSearch({retryRuleId:rowData.cid, canModify:rowData.canModify});
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
            $('#dlg').dialog('open').dialog('setTitle','New RetryRule');
            $('#fm').form('clear');
            $('#retryRule_status').combobox('select', '1');
            //$("#retryRule_remark").val('test');
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
            	var entityName='retryRule';
            	var obj = getRowData(entityName, row);
            	//alert(obj.'retryRule.empCode');
                $('#dlg').dialog('open').dialog('setTitle','Edit RetryRule');
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
                url: '/basedata/retryRule/save',
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
                        $.post('/basedata/retryRule/delete',{id:row.cid},function(result){
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
        
        $(function(){
        	if(parent)
        		parent.iframeCenterSearch({retryRuleId:0});//刷新重置关联查询id
        });
    </script>
    <%@ include file="/system/_foot.jsp"%>
</body>
</html>