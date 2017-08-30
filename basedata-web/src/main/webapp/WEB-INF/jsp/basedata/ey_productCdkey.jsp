<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <title>产品兑换码</title>
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
		<div data-options="region:'north', title:'产品兑换码-条件'"
			style="height: 80px; padding: 5px 80px;display:none;">
			<form id="search-form" >
				<table class="search-table">
					<tr>
						<td>
							产品聚道编码:
			                <input type="text" id="query_appid" name="appid" class="easyui-validatebox">
						</td>
						<td>
							兑换码:
			                <input type="text" id="query_cdkey" name="cdkey" class="easyui-validatebox">
						</td>
						<td>
							状态:
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
		            url="/basedata/productCdkey/list"
		            toolbar="#toolbar" pagination="true"
		            rownumbers="true" fitColumns="true" singleSelect="true" style="display:none;">
		        <thead>
		            <tr >
		            	<%/* 
		            	<th align="center" field="cid" width="50" sortable="true">编号</th>
		            	*/ %>
						<th align="center" field="appid" width="50" sortable="true">appid</th>
						<th align="center" field="orderNo" width="50" sortable="true">订单号</th>
						<th align="center" field="data" width="50" sortable="true">相关数据</th>
						<th align="center" field="price" width="50" sortable="true">价格</th>
						<th align="center" field="cdkey" width="50" sortable="true">兑换码</th>
						<th align="center" field="cdkeyType" width="50" sortable="true">兑换码类型</th>
						<th align="center" field="dueDate" width="50" sortable="true">到期时间</th>
						<th align="center" field="useDate" width="50" sortable="true">使用时间</th>
						<th align="center" field="ifUse" width="50" sortable="true" formatter="renderIf">是否已用</th>
		                <th align="center" field="status" width="50" sortable="true" formatter="renderStatus">状态</th>
		                <th align="center" field="createDate" width="120" sortable="true" hidden="true">Create date</th>
		                <th align="center" field="modifyDate" width="120" sortable="true" hidden="true">Modify date</th>
		            </tr>
		        </thead>
		    </table>
		 </div>
	</div>
    
    <div id="dlg" class="easyui-dialog" style="width:400px;height:500px;padding:5px 10px;display:none;"
            closed="true" buttons="#dlg-buttons">
        <fieldset>
			<legend>
				产品兑换码
			</legend>   
	        <form id="fm" method="post" novalidate>
	        	<input type="hidden" name="cid">
				<div class="fitem">
	                <label>appid:</label>
	                <input name="appid" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>订单号:</label>
	                <input name="orderNo" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>相关数据:</label>
	                <input name="data" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>兑换码:</label>
	                <input name="cdkey" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>兑换码类型:</label>
	                <input name="cdkeyType" class="easyui-validatebox" required="true">
	            </div>	
	            <div class="fitem">
	                <label>价格:</label>
	                <input name="price" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>到期时间:</label>
	                <input name="dueDate" class="easyui-datebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>使用时间:</label>
	                <input name="useDate" class="easyui-datebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>是否已用:</label>
	                <input name="ifUse" class="easyui-validatebox" required="true">
	            </div>	
	            <%/*
	            <div class="fitem">
	                <label>状态:</label>
	                <input class="easyui-combobox" name="status" id="productCdkey_status" required="true" data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_status" />
	            </div>
	            <div class="fitem">
	                <label>备注:</label>
	                <textarea name="remark" id="productCdkey_remark" style="height:60px;width:200px"></textarea>
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
            $('#dlg').dialog('open').dialog('setTitle','New ProductCdkey');
            $('#fm').form('clear');
            $('#productCdkey_status').combobox('select', '1');
            //$("#productCdkey_remark").val('test');
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
            	var entityName='productCdkey';
            	var obj = getRowData(entityName, row);
                $('#dlg').dialog('open').dialog('setTitle','Edit ProductCdkey');
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
                url: '/basedata/productCdkey/save',
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
                        $.post('/basedata/productCdkey/delete',{id:row.cid},function(result){
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