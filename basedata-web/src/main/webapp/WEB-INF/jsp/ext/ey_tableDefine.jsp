<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <title>扩展表定义</title>
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
		<div data-options="region:'north', title:'扩展表定义-条件'"
			style="height: 80px; padding: 5px 80px;display:none;">
			<form id="search-form" >
				<input type="hidden" id="query_tableId" name="tableId">
				<table class="search-table">
					<tr>
						<td>
							字段编码:
			                <input type="text" id="query_columnCode" name="columnCode" class="easyui-validatebox">
						</td>
						
						<td>
							字段类型:
			                <input class="easyui-combobox" class="input_query" name="columnType" id="query_columnType" data-options="
								valueField: 'id',
								textField: 'name',
								data: getDictData(dict_tf_table_column_type,'all')" style="width:100px" />
						</td>
						<td>
							状态:
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
	    	<div id="toolbar" style="display:none;">
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="onAdd()">新增</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="onEdit()">修改</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="onDestroy()">删除</a>
		    </div>
		    <table id="dg" class="easyui-datagrid" data-options="onDblClickRow:onDblClick, onClickRow:onClick, onLoadSuccess:function(){$('.datagrid-btable').find('div.datagrid-cell').css('text-align','left');}"
		            url="/ext/tableDefine/list"
		            toolbar="#toolbar" pagination="true"
		            rownumbers="true" fitColumns="true" singleSelect="true" style="display:none;">
		        <thead>
		            <tr >
		            	<%/* 
		            	<th align="center" field="cid" width="50" sortable="true">编号</th>
		            	<th align="center" field="projectCode" width="50" sortable="true">项目编码</th>
		            	*/ %>
						
						<th align="center" field="tableId" width="50" sortable="true"  hidden="true">扩展表id</th>
						<th align="center" field="orderCount" width="50" sortable="true">顺序号</th>
						
						<th align="center" field="columnCode" width="50" sortable="true">字段编码</th>
						<th align="center" field="columnName" width="50" sortable="true">字段名</th>
						<th align="center" field="columnNameEn" width="50" sortable="true">字段英文名</th>
						<th align="center" field="columnLength" width="50" sortable="true"  hidden="true">字段长度</th>
						<th align="center" field="tipInfo" width="50" sortable="true">输入提示信息</th>
						<th align="center" field="srcColumn" width="50" sortable="true">原表属性名</th>
						
						
						<th align="center" field="columnType" width="50" sortable="true">字段类型</th>
						<th align="center" field="columnCount" width="50" sortable="true">字段类型序号</th>
						<th align="center" field="ifHide" width="50" sortable="true">是否隐藏字段</th>
						<th align="center" field="ifRequire" width="50" sortable="true">是否必填</th>
		                <%/* */ %>
		                <th align="center" field="status" width="50" sortable="true" formatter="renderStatus">状态</th>
		                <th align="center" field="create_disp" width="100" sortable="true"  hidden="true">Create user</th>
		                <th align="center" field="createDate" width="120" sortable="true"  hidden="true">Create date</th>
		                <th align="center" field="modify_disp" width="100" sortable="true"  hidden="true">Modify user</th>
		                <th align="center" field="modifyDate" width="120" sortable="true"  hidden="true">Modify date</th>
		                <th align="center" field="remark" width="150"  hidden="true">备注</th>
		               
		            </tr>
		        </thead>
		    </table>
		 </div>
	</div>
    
    <div id="dlg" class="easyui-dialog" style="width:400px;height:500px;padding:5px 10px;display:none;"
            closed="true" buttons="#dlg-buttons">
        <fieldset>
			<legend>
				扩展表定义：扩属属性定义：date则取data_date，datetime取data_datetime，num则取dat
			</legend>   
	        <form id="fm" method="post" novalidate>
	        	<input type="hidden" name="cid">
	        	<%/* 
				<div class="fitem">
	                <label>项目编码:</label>
	                <input name="projectCode" class="easyui-validatebox" required="true">
	            </div>
	            */ %>
				<div class="fitem">
	                <label>扩展表id:</label>
	                <input name="tableId" id="tableDefine_tableId" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>顺序号:</label>
	                <input name="orderCount" class="easyui-validatebox" required="true">
	            </div>	
	            <div class="fitem">
	                <label>字段编码:</label>
	                <input name="columnCode" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>字段名:</label>
	                <input name="columnName" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>字段英文名:</label>
	                <input name="columnNameEn" class="easyui-validatebox" required="true">
	            </div>	
	            <div class="fitem">
	                <label>输入提示信息:</label>
	                <input name="tipInfo" class="easyui-validatebox" >
	            </div>
	            <div class="fitem">
	                <label>原表属性:</label>
	                <input name="srcColumn" class="easyui-validatebox" >
	            </div>	
	            <div class="fitem">
	                <label>扩展属性序号:</label>
	                <input name="columnCount" class="easyui-validatebox" >
	            </div>	
				
				<div class="fitem">
	                <label>字段长度:</label>
	                <input name="columnLength" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>字段类型:</label>
	                <input class="easyui-combobox" name="columnType" id="tableDefine_columnType" required="true" data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_table_column_type" />
	            </div>	
	            <div class="fitem">
	                <label>数据字典编码:</label>
	                <input name="lookupCode" class="easyui-validatebox">
	            </div>	
				<div class="fitem">
	                <label>是否隐藏字段:</label>
	                <input class="easyui-combobox" name="ifHide" id="tableDefine_ifHide" required="true" data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_if" />
	            </div>	
	            <div class="fitem">
	                <label>是否必填:</label>
	                <input class="easyui-combobox" name="ifRequire" id="tableDefine_ifRequire" required="true" data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_if" />
	            </div>	
				
				
	            <%/**/ %>
	            <div class="fitem">
	                <label>状态:</label>
	                <input class="easyui-combobox" name="status" id="tableDefine_status" required="true" data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_status" />
	            </div>
	            <div class="fitem">
	                <label>备注:</label>
	                <textarea name="remark" id="tableDefine_remark" style="height:60px;width:200px"></textarea>
	            </div>
	            
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
			console.log('-----doSearch--');
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
		$('#query_columnCode').keydown(listenerName);
		$('#query_columnName').keydown(listenerName);
	
        var url;
        function onAdd(){
            $('#dlg').dialog('open').dialog('setTitle','New TableDefine');
            $('#fm').form('clear');
            $('#tableDefine_status').combobox('select', '1');
            $('#tableDefine_tableId').val($('#query_tableId').val());
            //$("#tableDefine_remark").val('test');
        }
        
        function iframeCenterSearch(options) {
        	console.log('----iframeCenterSearch--');
        	var tableId = options.tableId;
        	$('#query_tableId').val(tableId);
        	doSearch();
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
            	var entityName='tableDefine';
            	var obj = getRowData(entityName, row);
                $('#dlg').dialog('open').dialog('setTitle','Edit TableDefine');
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
                url: '/ext/tableDefine/save',
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
                        $.post('/ext/tableDefine/delete',{id:row.cid},function(result){
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