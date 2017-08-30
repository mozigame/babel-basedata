<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <title>模板消息</title>
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
		<div data-options="region:'north', title:'模板消息-条件'"
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
			                <input type="text" id="query_msgType" name="msgType" class="easyui-validatebox">
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
							模板TplId:
			                <input type="text" id="query_tplId" name="tplId" class="easyui-validatebox">
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
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="onImport()">导入模板</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="onSend()">发送消息</a>
		    </div>
		    <table id="dg" class="easyui-datagrid" data-options="onDblClickRow:onDblClick, onClickRow:onClick, onLoadSuccess:function(){$('.datagrid-btable').find('div.datagrid-cell').css('text-align','left');}"
		            url="/basedata/template/list"
		            toolbar="#toolbar" pagination="true"
		            rownumbers="true" fitColumns="true" singleSelect="true" style="display:none;">
		        <thead>
		            <tr >
						<th align="center" field="sysType" width="50" sortable="true">系统类型</th>
						<th align="center" field="msgType" width="50" formatter="renderMsgType" sortable="true">消息类型</th>
						<th align="center" field="code" width="50" sortable="true">编号</th>
						<th align="center" field="name" width="50" sortable="true">功能名称</th>
						<th align="center" field="msgCode" width="50" sortable="true">消息编码</th>
						<th align="center" field="tplId" width="50" sortable="true">模板TplId</th>
						<th align="center" field="title" width="50" sortable="true">标题</th>
						<th align="center" field="primaryIndustry" width="200" sortable="true" hidden="true">主行业</th>
						<th align="center" field="deputyIndustry" width="50" sortable="true">副行业</th>
						<th align="center" field="tplData" width="200" sortable="true" hidden="true">模板数据</th>
						<th align="center" field="example" width="200" sortable="true" hidden="true">模板样例</th>
						<th align="center" field="paramName" width="200" sortable="true" hidden="true">详情参数名</th>
						<th align="center" field="url" width="200" sortable="true" hidden="true">详情url</th>
						
						
						<th align="center" field="tplExample" width="50" sortable="true">模板样例json</th>
						
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
				模板消息
			</legend>   
	        <form id="fm" method="post" novalidate>
	        	<input type="hidden" name="cid">
				<div class="fitem">
	                <label>系统类型:</label>
	                <input class="easyui-combobox" name="sysType" id="template_sysType"  data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_sys_type"/>
	            </div>
				<div class="fitem">
	                <label>消息类型:</label>
	                <input class="easyui-combobox" name="msgType" id="template_msgType" data-options="
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_msg_type" />
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
	                <label>消息编码:</label>
	                <input name="msgCode" class="easyui-validatebox" required="true">
	            </div>	
	            <div class="fitem">
	                <label>标题:</label>
	                <input name="title" class="easyui-validatebox" required="true">
	            </div>	
	            <div class="fitem">
	                <label>主行业:</label>
	                <input name="primaryIndustry" class="easyui-validatebox" required="true">
	            </div>	
	            <div class="fitem">
	                <label>副行业:</label>
	                <input name="deputyIndustry" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>模板TplId:</label>
	                <input name="tplId" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>模板数据:</label>
	                <textarea name="tplData" id="template_tplData" style="height:60px;width:200px"></textarea>
	            </div>	
	            <div class="fitem">
	                <label>模板样例:</label>
	                <textarea name="example" id="template_example" style="height:60px;width:200px"></textarea>
	            </div>	
				<div class="fitem">
	                <label>模板样例json:</label>
	                <textarea name="tplExample" id="template_tplExample" style="height:60px;width:200px"></textarea>
	            </div>	
	            <div class="fitem">
	                <label>详请参数名:</label>
	                <input name="paramName" class="easyui-validatebox" >
	            </div>	
				<div class="fitem">
	                <label>详情url:</label>
	                <input name="url" class="easyui-validatebox" >
	            </div>	
	            <div class="fitem">
	                <label>状态:</label>
	                <input class="easyui-combobox" name="status" id="template_status" required="true" data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_status" />
	            </div>
	            <div class="fitem">
	                <label>备注:</label>
	                <textarea name="remark" id="template_remark" style="height:60px;width:200px"></textarea>
	            </div>
	        </form>
        </fieldset>
        <div id="dlg-buttons">
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="onSave()">Save</a>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">Cancel</a>
	    </div>
    </div>
    
    <div id="dlgSend" class="easyui-dialog" style="width:500px;height:500px;padding:5px 10px;display:none;"
            closed="true" buttons="#dlg-buttonsSend">
        <fieldset>
			<legend>
				发送消息
			</legend>   
	        <form id="fmSend" method="post" novalidate>
	        	<input type="hidden" name="cid">
	        	<input type="hidden" name="name">
	        	<input type="hidden" name="code">
	        	<input type="hidden" name="msgType">
	        	<input type="hidden" name="tplId">
	        	<input type="hidden" name="msgCode">
	        	<input type="hidden" name="url">
				<div class="fitem">
	                <label>系统类型:</label>
	                <input class="easyui-combobox" name="sysType" id="send_sysType" readOnly="true" data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_sys_type" />
	            </div>
	            <div class="fitem">
	                <label>消息内容:</label>
	                <textarea name="tplExample" id="send_tplExample" style="height:200px;width:350px"></textarea>
	            </div>
	            <div class="fitem">
	                <label>接收人:</label>
	                <textarea name="tos" id="send_tos" style="height:60px;width:350px"></textarea>
	            </div>
	        </form>
        </fieldset>
        <div id="dlg-buttonsSend">
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="onSaveSend()">发送消息</a>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgSend').dialog('close')">Cancel</a>
	    </div>
    </div>
    
     <div id="dlgImport" class="easyui-dialog" style="width:300px;height:200px;padding:5px 10px;display:none;"
            closed="true" buttons="#dlg-buttonsImport">
        <fieldset>
			<legend>
				导入微信模板
			</legend>   
	        <form id="fmImport" method="post" novalidate>
				<div class="fitem">
	                <label>系统类型:</label>
	                <input class="easyui-combobox" name="sysType" id="send_sysType" data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_sys_type" />
	            </div>
	        </form>
        </fieldset>
        <div id="dlg-buttonsImport">
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="onSaveImport()">导入</a>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgImport').dialog('close')">Cancel</a>
	    </div>
    </div>
    
    <script type="text/javascript">
    	var send_tos='';
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
		
		function onImport(){
			$('#dlgImport').dialog('open').dialog('setTitle','导入微信模板');
		}
		
		function onSaveImport(){
			$('#fmImport').form('submit',{
                url: '/basedata/template/importWxTpl',
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
                        $('#dlgImport').dialog('close');        // close the dialog
                        $.messager.show({
                            title: result.msgCode,
                            msg: result.msgBody
                        });
                        doSearchReload();    // reload the user data
                    }
                }
            });
		}
        function onSend(){
        	var row = $('#dg').datagrid('getSelected');
            if (row){
	        	 $('#dlgSend').dialog('open').dialog('setTitle','发送消息');
	        	 $('#fmSend').form('clear');
	        	 
	        	 var entityName='template';
	            	var obj = getRowData(entityName, row);
	                $('#fmSend').form('load',obj);
	                $('#send_tos').val(send_tos);
	                
	        	 //$('#send_tplExample').val(row.tplExample);
	        	 //$('#send_sysType').combobox('select', row.sysType);
            }
        }
        
        function onSaveSend(){
        	send_tos=$('#send_tos').val();
        	console.log('-----onSaveSend--'+send_tos);
            $('#fmSend').form('submit',{
                url: '/basedata/template/sendMsg',
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
                        //$('#dlgSend').dialog('close');        // close the dialog
                        $.messager.show({
                            title: result.msgCode,
                            msg: result.msgBody
                        });
                        //doSearchReload();    // reload the user data
                    }
                }
            });
        }
	
        var url;
        function onAdd(){
            $('#dlg').dialog('open').dialog('setTitle','New Template');
            $('#fm').form('clear');
            $('#template_status').combobox('select', '1');
            //$("#template_remark").val('test');
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
            	$('#fm').form('clear');
            	var entityName='template';
            	var obj = getRowData(entityName, row);
                $('#dlg').dialog('open').dialog('setTitle','Edit Template');
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
                url: '/basedata/template/save',
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
                        $.post('/basedata/template/delete',{id:row.cid},function(result){
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