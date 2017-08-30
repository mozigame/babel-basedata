<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <title>线程池管理</title>
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
		<div data-options="region:'north', title:'线程池管理-条件'"
			style="height: 80px; padding: 5px 80px;display:none;">
			<form id="search-form" >
				<table class="search-table">
					<tr>
						<td>
							系统类型:
			                <input type="text" id="query_sysType" name="sysType" class="easyui-validatebox">
						</td>
						<td>
							线程类型:
			                <input type="text" id="query_threadType" name="threadType" class="easyui-validatebox">
						</td>
						<td>
							编码:
			                <input type="text" id="query_code" name="code" class="easyui-validatebox">
						</td>
						<td>
							名称:
			                <input type="text" id="query_name" name="name" class="easyui-validatebox">
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
		            url="/basedata/threadPool/list"
		            toolbar="#toolbar" pagination="true"
		            rownumbers="true" fitColumns="true" singleSelect="true" style="display:none;">
		        <thead>
		            <tr >
		            	<%/* 
		            	<th align="center" field="cid" width="50" sortable="true">编号</th>
		            	*/ %>
						<th align="center" field="sysType" width="50" sortable="true">系统类型</th>
						<th align="center" field="threadType" width="50" sortable="true">线程类型</th>
						<th align="center" field="code" width="50" sortable="true">编码</th>
						<th align="center" field="name" width="50" sortable="true">名称</th>
						<th align="center" field="corePoolSize" width="50" sortable="true">维护线程的最少数量</th>
						<th align="center" field="maxPoolSize" width="50" sortable="true">维护线程的最大数量</th>
						<th align="center" field="queueCapacity" width="50" sortable="true">所使用的缓冲队列</th>
						<th align="center" field="keepAliveSeconds" width="50" sortable="true" hidden="true">维护线程所允许的空闲时间</th>
						<th align="center" field="allowCoreThreadTimeOut" width="50" sortable="true" formatter="renderIf">是否所有线程可退出</th>
						
						<th align="center" field="lastErrorDate" width="50" sortable="true" hidden="true">最近出错时间</th>
						<th align="center" field="lastErrorCount" width="50" sortable="true" hidden="true">最近的错误次数</th>
						<th align="center" field="lastErrorFirstDate" width="50" sortable="true" hidden="true">最近首次出错时间</th>
						<th align="center" field="lastError" width="50" sortable="true" hidden="true">最近的错误信息</th>
		                <%/*  */ %>
		                <th align="center" field="status" width="50" sortable="true" formatter="renderStatus">状态</th>
		                <th align="center" field="canModify" width="50" sortable="true" hidden="true" >是否可修改</th>
		                <th align="center" field="create_disp" width="100" sortable="true" hidden="true">Create user</th>
		                <th align="center" field="createDate" width="120" sortable="true" hidden="true">Create date</th>
		                <th align="center" field="modify_disp" width="100" sortable="true" hidden="true">Modify user</th>
		                <th align="center" field="modifyDate" width="120" sortable="true" hidden="true">Modify date</th>
		                <th align="center" field="remark" width="50" hidden="true">备注</th>
		               
		            </tr>
		        </thead>
		    </table>
		 </div>
	</div>
    
    <div id="dlg" class="easyui-dialog" style="width:400px;height:500px;padding:5px 10px;display:none;"
            closed="true" buttons="#dlg-buttons">
        <fieldset>
			<legend>
				线程池管理
			</legend>   
	        <form id="fm" method="post" novalidate>
	        	<input type="hidden" name="cid">
				<div class="fitem">
	                <label>系统类型:</label>
	                <input name="sysType" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>线程类型:</label>
	                <input name="threadType" class="easyui-validatebox" required="true">
	            </div>	
	            <div class="fitem">
	                <label>编码:</label>
	                <input name="code" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>名称:</label>
	                <input name="name" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>维护线程的最少数量:</label>
	                <input name="corePoolSize" id="threadPool_corePoolSize" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>维护线程的最大数量:</label>
	                <input name="maxPoolSize" id="threadPool_maxPoolSize" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>所使用的缓冲队列:</label>
	                <input name="queueCapacity" id="threadPool_queueCapacity" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>维护线程所允许的空闲时间:</label>
	                <input name="keepAliveSeconds" id="threadPool_keepAliveSeconds" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>是否所有线程可退出:</label>
	                <input name="allowCoreThreadTimeOut" id="threadPool_allowCoreThreadTimeOut"  class="easyui-validatebox" >
	            </div>	
					
				<div class="fitem">
	                <label>最近出错时间:</label>
	                <input name="lastErrorDate" class="easyui-datetimebox" >
	            </div>	
				<div class="fitem">
	                <label>最近的错误次数:</label>
	                <input name="lastErrorCount" class="easyui-validatebox" >
	            </div>	
				<div class="fitem">
	                <label>最近首次出错时间:</label>
	                <input name="lastErrorFirstDate" class="easyui-datetimebox" >
	            </div>
	            <div class="fitem">
	                <label>最近的错误信息:</label>
	                <textarea name="lastError" id="threadPool_lastError" style="height:60px;width:200px"></textarea>
	            </div>
	            <%/*
	            */ %>
	            <div class="fitem">
	                <label>是否可修改:</label>
	                <input class="easyui-combobox" name="canModify" id="threadPool_canModify" required="true" data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_if" />
	            </div>
	            <div class="fitem">
	                <label>状态:</label>
	                <input class="easyui-combobox" name="status" id="threadPool_status" required="true" data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_status" />
	            </div>
	            <div class="fitem">
	                <label>备注:</label>
	                <textarea name="remark" id="threadPool_remark" style="height:60px;width:200px"></textarea>
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
            $('#dlg').dialog('open').dialog('setTitle','New ThreadPool');
            $('#fm').form('clear');
            $('#threadPool_status').combobox('select', '1');
            
            //$("#threadPool_remark").val('test');
            $("#threadPool_corePoolSize").val('1');
            $("#threadPool_maxPoolSize").val(100);
            $("#threadPool_queueCapacity").val(100);
            $("#threadPool_keepAliveSeconds").val(300);
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
            	var entityName='threadPool';
            	var obj = getRowData(entityName, row);
                $('#dlg').dialog('open').dialog('setTitle','Edit ThreadPool');
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
                url: '/basedata/threadPool/save',
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
                        $.post('/basedata/threadPool/delete',{id:row.cid},function(result){
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