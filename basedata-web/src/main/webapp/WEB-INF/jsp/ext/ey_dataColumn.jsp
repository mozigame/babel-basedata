<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <title>列形扩展表数据</title>
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
    
 	<div class="easyui-layout" data-options="fit:true">
    	<!-- 查询条件  -->
		<div data-options="region:'north', title:'列形扩展表数据-条件'"
			style="height: 80px; padding: 5px 80px;display:none;">
			<form id="search-form" >
				<table class="search-table">
					<tr>
						<td>
							扩展表定义id:
			                <input type="text" id="query_tableDefineId" name="tableDefineId" class="easyui-validatebox">
						</td>
						<td>
							表数据id:
			                <input type="text" id="query_refDataId" name="refDataId" class="easyui-validatebox">
						</td>
						<td>
							关联数据类型，可以不用:
			                <input type="text" id="query_refDataType" name="refDataType" class="easyui-validatebox">
						</td>
						<td>
							状态(if_active)0无效，1有效:
			                <input type="text" id="query_status" name="status" class="easyui-validatebox">
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
	    	<div id="toolbar" style="display:none;">
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="onAdd()">新增</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="onEdit()">修改</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="onDestroy()">删除</a>
		    </div>
		    <table id="dg" class="easyui-datagrid" data-options="onDblClickRow:onDblClick, onClickRow:onClick, onLoadSuccess:function(){$('.datagrid-btable').find('div.datagrid-cell').css('text-align','left');}"
		            url="/ext/dataColumn/list"
		            toolbar="#toolbar" pagination="true"
		            rownumbers="true" fitColumns="true" singleSelect="true" style="display:none;">
		        <thead>
		            <tr >
		            	<%/* 
		            	<th align="center" field="cid" width="50" sortable="true">编号</th>
		            	*/ %>
						<th align="center" field="projectId" width="50" sortable="true" hidden="true">产品id</th>
						<th align="center" field="tableDefineId" width="50" sortable="true">扩展表定义id</th>
						<th align="center" field="refDataId" width="50" sortable="true">表数据id</th>
						<th align="center" field="refDataType" width="50" sortable="true">关联数据类型，可以不用</th>
						<th align="center" field="col1" width="50" sortable="true">扩展属性1</th>
						<th align="center" field="col2" width="50" sortable="true" hidden="true">扩展属性2</th>
						<th align="center" field="col3" width="50" sortable="true" hidden="true"></th>
						<th align="center" field="colDatetime1" width="50" sortable="true"></th>
						<th align="center" field="colDatetime2" width="50" sortable="true" hidden="true"></th>
						<th align="center" field="colDatetime3" width="50" sortable="true" hidden="true"></th>
						<th align="center" field="colInt1" width="50" sortable="true"></th>
						<th align="center" field="colInt2" width="50" sortable="true" hidden="true"></th>
						<th align="center" field="colInt3" width="50" sortable="true" hidden="true"></th>
		                <%/* */%>
		                <th align="center" field="status" width="50" sortable="true" formatter="renderStatus">状态</th>
		                <th align="center" field="create_disp" width="100" sortable="true" hidden="true">Create user</th>
		                <th align="center" field="createDate" width="120" sortable="true" hidden="true">Create date</th>
		                <th align="center" field="modify_disp" width="100" sortable="true" hidden="true">Modify user</th>
		                <th align="center" field="modifyDate" width="120" sortable="true" hidden="true">Modify date</th>
		                <th align="center" field="remark" width="150" hidden="true">备注</th>
		                
		            </tr>
		        </thead>
		    </table>
		 </div>
	</div>
    
    <div id="dlg" class="easyui-dialog" style="width:400px;height:500px;padding:5px 10px;display:none;"
            closed="true" buttons="#dlg-buttons">
        <fieldset>
			<legend>
				列形扩展表数据
			</legend>   
	        <form id="fm" method="post" novalidate>
	        	<input type="hidden" name="cid">
				<div class="fitem">
	                <label>产品id:</label>
	                <input name="projectId" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>扩展表定义id:</label>
	                <input name="tableDefineId" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>表数据id:</label>
	                <input name="refDataId" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>关联数据类型，可以不用:</label>
	                <input name="refDataType" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>备注:</label>
	                <input name="remark" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>扩展属性1:</label>
	                <input name="col1" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>扩展属性2:</label>
	                <input name="col2" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>:</label>
	                <input name="col3" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>:</label>
	                <input name="colDatetime1" class="easyui-datebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>:</label>
	                <input name="colDatetime2" class="easyui-datebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>:</label>
	                <input name="colDatetime3" class="easyui-datebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>:</label>
	                <input name="colInt1" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>:</label>
	                <input name="colInt2" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>:</label>
	                <input name="colInt3" class="easyui-validatebox" required="true">
	            </div>	
	            <%/*
	            <div class="fitem">
	                <label>状态:</label>
	                <input class="easyui-combobox" name="status" id="dataColumn_status" required="true" data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_status" />
	            </div>
	            <div class="fitem">
	                <label>备注:</label>
	                <textarea name="remark" id="dataColumn_remark" style="height:60px;width:200px"></textarea>
	            </div>
	            */ %>
	        </form>
        </fieldset>
        <div id="dlg-buttons">
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="onSave()">Save</a>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">Cancel</a>
	    </div>
    </div>
    
    <script type="text/javascript">
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
        	//alert('---onDblClick--rowIndex='+rowIndex+' row.cid='+rowData.cid);
        	onEdit();
        }
		
		function onClick(rowIndex, rowData){
        	
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
            $('#dlg').dialog('open').dialog('setTitle','New DataColumn');
            $('#fm').form('clear');
            $('#dataColumn_status').combobox('select', '1');
            //$("#dataColumn_remark").val('test');
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
            	var entityName='dataColumn';
            	var obj = getRowData(entityName, row);
                $('#dlg').dialog('open').dialog('setTitle','Edit DataColumn');
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
                url: '/ext/dataColumn/save',
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
                        $.messager.show({
                            title: result.msgCode,
                            msg: result.msgBody
                        });
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
                        $.post('/ext/dataColumn/delete',{id:row.cid},function(result){
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