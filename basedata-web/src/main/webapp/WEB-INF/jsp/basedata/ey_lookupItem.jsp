<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html manifest="${env['app_manifest']}" >
<head>
	<meta charset="UTF-8">
    <title>数据字典</title>
    <%@ include file="/system/_script.jsp"%>
	
	<style type="text/css">
      #fm{
          margin:0;
          padding:10px 30px;
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
          width:80px;
      }
      .input_query{
      	width: 100px;
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
		<div data-options="region:'north', title:'数据字典-条件'"
			style="height: 80px; padding: 5px 80px;">
			<form id="search-form" >
				<table class="search-table">
					<input type="hidden" name="lookupId" id="query_lookupId">
					<tr>
						<td>
							编码:
							<input type="text" class="input_query" name="itemCode" id="query_code"/>
						</td>
						
						<td>
							名称:
							<input type="text" class="input_query" name="itemName" id="query_itemName"/>
						</td>
						<td>
							语言:
							<input type="text" class="input_query" name="language" id="query_language"/>
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
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="onAdd()">New</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="onEdit()">Edit</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="onDestroy()">Remove</a>
		    </div>
		    <table id="dg" class="easyui-datagrid" data-options="onDblClickRow:onDblClick"
		            url="/basedata/lookupItem/list" 
		            toolbar="#toolbar" pagination="true"
		            rownumbers="true" fitColumns="true" singleSelect="true">
		        <thead>
		            <tr >
		            	<th field="cid" width="50" hidden="true">Id</th>
		            	<th field="itemCode" width="50" sortable="true">子项编码</th>
		                <th field="itemName" width="50" sortable="true">子项名称</th>
		                <th field="language" width="50" sortable="true">语言</th>
		                <th field="orderCount" width="50">顺序号</th>
		                <th field="color" width="50">颜色</th>
		                <th field="attr1" width="50">备用属性1</th>
		                <th field="attr2" width="150"  hidden="true">备用属性2</th>
		                <th field="attr3" width="150"  hidden="true">备用属性3</th>
		                <th field="attr4" width="150"  hidden="true">备用属性4</th>
		                <th field="status" width="50" sortable="true" formatter="renderStatus">状态</th>
		                
		                <th field="create_disp" width="100" sortable="true"  hidden="true">Create user</th>
		                <th field="createDate" width="120" sortable="true"  hidden="true" formatter="Common.DateTimeFormatter">Create date</th>
		                <th field="modify_disp" width="100" sortable="true"  hidden="true">Modify user</th>
		                <th field="modifyDate" width="120" sortable="true"  hidden="true" formatter="Common.DateTimeFormatter">Modify date</th>
		               <%/* */ %>
		                <th field="remark" width="150"  hidden="true">备注</th>
		               
		            </tr>
		        </thead>
		    </table>
		 </div>
	</div>
    
    <div id="dlg" class="easyui-dialog" style="width:510px;height:340px;padding:10px 20px;display:none"
            closed="true" buttons="#dlg-buttons">
        <fieldset>
			<legend>
				数据字典
			</legend>   
	        <form id="fm" method="post" novalidate>
	        	<input type="hidden" name="cid">
	        	<input type="hidden" name="lookupId" id="lookupItem_lookupId">
	            <div class="fitem">
	                <label>子项编码:</label>
	                <input name="itemCode" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>子项名称:</label>
	                <input name="itemName" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>语言:</label>
	                <input name="language" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>顺序号:</label>
	                <input name="orderCount" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>颜色:</label>
	                <input name="color" class="easyui-validatebox" >
	            </div>
	            <div class="fitem">
	                <label>备用属性1:</label>
	                <input name="attr1" class="easyui-validatebox" >
	            </div>
	            <div class="fitem">
	                <label>备用属性2:</label>
	                <input name="attr2" class="easyui-validatebox" >
	            </div>
	            <div class="fitem">
	                <label>备用属性3:</label>
	                <input name="attr3" class="easyui-validatebox" >
	            </div>
	            <div class="fitem">
	                <label>备用属性4:</label>
	                <input name="attr4" class="easyui-validatebox" >
	            </div>
	            <div class="fitem">
	                <label>自定义json:</label>
	                 <textarea name="attrJson" id="lookupItem_attrJson" style="height:60px;width:220px"></textarea>
	            </div>
	            
	           
	            <div class="fitem">
	                <label>状态:</label>
	                <input class="easyui-combobox" name="status" id="lookupItem_status" required="true" data-options="
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_status" />
	            </div>
	            <div class="fitem">
	                <label>备注:</label>
	                <textarea name="remark" id="lookupItem_remark" style="height:60px;width:220px"></textarea>
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
    	var lookupId;
    	var canModify;
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
			$('#dg').datagrid('load', jsonParam);
        }
		
		
		
		function doSearchReload(){
        	var jsonParam = $('#search-form').serializeJson();
        	//status为all时处理
        	value_all2empty(jsonParam);
			$('#dg').datagrid('reload', jsonParam);
        }
		
		function onDblClick(rowIndex, rowData){
        	//alert('---onDblClick--rowIndex='+rowIndex+' row.cid='+rowData.cid);
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
            $('#dlg').dialog('open').dialog('setTitle','New LookupItem');
            $('#fm').form('clear');
            $('#lookupItem_status').combobox('select', '1');
            $('#lookupItem_lookupId').val(lookupId);
            //$("#lookupItem_remark").val('test');
        }
        
        function iframeCenterSearch(options) {
        	console.log('----iframeCenterSearch--'+options);
        	lookupId = options.lookupId;
        	canModify=options.canModify;
        	$('#query_lookupId').val(lookupId);
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
            	var entityName='lookupItem';
            	var obj = getRowData(entityName, row);
            	//alert(obj.'lookupItem.empCode');
                $('#dlg').dialog('open').dialog('setTitle','Edit LookupItem');
                $('#fm').form('clear');
                $('#fm').form('load',obj);
            }
           
        }
        function onSave(){
        	if(canModify==0){
        		$.messager.show({
                    title: 'Error',
                    msg: '不可修改，禁止操作'
                });
        		return;
        	}
            $('#fm').form('submit',{
                url: '/basedata/lookupItem/save',
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
                        $.post('/basedata/lookupItem/delete',{id:row.cid},function(result){
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