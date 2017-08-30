<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <title>被动消息回复</title>
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
		<div data-options="region:'north', title:'被动消息回复-条件'"
			style="height: 80px; padding: 5px 80px;display:none;">
			<form id="search-form" >
				<table class="search-table">
					<tr>
						<td>
							系统类型:
			                <input class="easyui-combobox" name="sysType" id="query_sysType" data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: getDictData(dict_tf_sys_type,'all')" style="width:90px" />
						</td>
						<td>
							消息类型:
			                <input class="easyui-combobox" name="msgType" id="query_msgType" data-options="
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: getDictData(dict_tf_msg_type,'all')" style="width:100px" />
						</td>
						<td>
							编码:
			                <input type="text" id="query_code" name="code" class="easyui-validatebox">
						</td>
						<td>
							标题:
			                <input type="text" id="query_title" name="title" class="easyui-validatebox">
						</td>
						<td>
							状态:
			                <input class="easyui-combobox" name="status" id="query_status" data-options="panelHeight:'auto',
											valueField: 'id',
											textField: 'name',
											//multiple:true, //多选增加此项
											data: getDictData(dict_tf_status,'all')"  style="width:100px" />
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
		            url="/basedata/passiveReply/list"
		            toolbar="#toolbar" pagination="true"
		            rownumbers="true" fitColumns="true" singleSelect="true" style="display:none;">
		        <thead>
		            <tr >
		            	<%/* 
		            	<th align="center" field="cid" width="50" sortable="true">编号</th>
		            	*/ %>
						<th align="center" field="sysType" width="50" sortable="true">系统类型</th>
						<th align="center" field="msgType" width="50"  formatter="renderMsgType"  sortable="true">消息类型</th>
						<th align="center" field="title" width="50" sortable="true">标题</th>
						<th align="center" field="description" width="50" sortable="true">内容描述</th>
						<th align="center" field="picUrl" width="50" sortable="true">图片url</th>
						<th align="center" field="url" width="50" sortable="true">详情url</th>
		                <th align="center" field="status" width="50" sortable="true" formatter="renderStatus">状态</th>
						<th align="center" field="create_disp" width="100" sortable="true" hidden="true">Create user</th>
		                <th align="center" field="createDate" width="130" sortable="true" hidden="true">Create date</th>
		                <th align="center" field="modify_disp" width="100" sortable="true" hidden="true">Modify user</th>
		                <th align="center" field="modifyDate" width="130" sortable="true" hidden="true">Modify date</th>
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
				被动消息回复
			</legend>   
	        <form id="fm" method="post" novalidate>
	        	<input type="hidden" name="cid">
				<div class="fitem">
	                <label>系统类型:</label>
	                <input class="easyui-combobox" name="sysType" id="passiveReply_sysType"  data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_sys_type"/>
	            </div>	
				<div class="fitem">
	                <label>消息类型:</label>
	                <input class="easyui-combobox" name="msgType" id="passiveReply_msgType" data-options="
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_msg_type" />
	            </div>
				<div class="fitem">
	                <label>标题:</label>
	                <input name="title" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>内容描述:</label>
	                <input name="description" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>图片url:</label>
	                <input name="picUrl" class="easyui-validatebox">
	            </div>	
				<div class="fitem">
	                <label>详情url:</label>
	                <input name="url" class="easyui-validatebox">
	            </div>	
	            <div class="fitem">
	                <label>状态:</label>
	                <input class="easyui-combobox" name="status" id="passiveReply_status" required="true" data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_status" />
	            </div>
	            <div class="fitem">
	                <label>备注:</label>
	                <textarea name="remark" id="passiveReply_remark" style="height:60px;width:200px"></textarea>
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
            $('#dlg').dialog('open').dialog('setTitle','New PassiveReply');
            $('#fm').form('clear');
            $('#passiveReply_status').combobox('select', '1');
            //$("#passiveReply_remark").val('test');
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
            	var entityName='passiveReply';
            	var obj = getRowData(entityName, row);
                $('#dlg').dialog('open').dialog('setTitle','Edit PassiveReply');
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
                url: '/basedata/passiveReply/save',
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
                        $.post('/basedata/passiveReply/delete',{id:row.cid},function(result){
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