<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html manifest="${env['dev.app_manifest']}" >
<head>
	<meta charset="UTF-8">
    <title>用户</title>
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
    
 	<div id="mainWindow" class="easyui-layout" data-options="fit:true" style="display:none;">
    	<!-- 查询条件  -->
		<div data-options="region:'north', title:'用户-条件'"
			style="height: 80px; padding: 5px 80px;display:none;">
			<form id="search-form" >
				<table class="search-table">
					<input type="hidden" name="lookupId" id="query_lookupId">
					<tr>
						<td>
							姓名:
							<input type="text" class="input_query" name="name" id="query_name"/>
						</td>
						<td>
							手机:
							<input type="text" class="input_query" name="mobile" id="query_mobile"/>
						</td>
						<td>
							邮箱地址:
							<input type="text" class="input_query" name="email" id="query_email"/>
						</td>
						<td>
							证件号:
							<input type="text" class="input_query" name="idCard" id="query_idCard"/>
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
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="onPasswdEdit()">修改密码</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="onDestroy()">删除</a>
		    </div>
		    <table id="dg" class="easyui-datagrid" data-options="onDblClickRow:onDblClick, onClickRow:onClick"
		            url="/basedata/user/list"
		            toolbar="#toolbar" pagination="true"
		            rownumbers="true" fitColumns="true" singleSelect="true" style="display:none;">
		        <thead>
		            <tr >
		            	<%/* 
		            	<th field="cid" width="50" sortable="true">编号</th>
		            	*/ %>
						<th field="name" width="50" sortable="true">姓名</th>
						<th field="userName" width="50" sortable="true">账号名</th>
						<th field="countryCode" width="50" sortable="true"  hidden="true">国家码</th>
						<th field="mobile" width="50" sortable="true">手机</th>
						<th field="email" width="50" sortable="true">邮箱地址</th>
						<th field="cardType" width="50" sortable="true" formatter="renderCardType">证件类型</th>
						<th field="idCard" width="100" sortable="true">证件号</th>
						<th field="sex" width="50" sortable="true" formatter="renderSex">性名</th>
						<th field="birthdate" width="100" sortable="true"  hidden="true">生日</th>
						<th field="address" width="200" sortable="true"  hidden="true">地址</th>
		                <%/**/ %>
		                <th field="status" width="50" sortable="true"  hidden="true" formatter="renderStatus">状态</th>
		                <th field="canModify" width="50" sortable="true"  hidden="true" formatter="renderIf">是否可修改</th>
		                <th field="create_disp" width="100" sortable="true"  hidden="true">Create user</th>
		                <th field="createDate" width="120" sortable="true"  hidden="true">Create date</th>
		                <th field="modify_disp" width="100" sortable="true"  hidden="true">Modify user</th>
		                <th field="modifyDate" width="120" sortable="true"  hidden="true">Modify date</th>
		                <th field="remark" width="150"  hidden="true">备注</th>
		                
		            </tr>
		        </thead>
		    </table>
		 </div>
	</div>
    
    <div id="dlg" class="easyui-dialog" style="width:400px;height:500px;padding:5px 10px;display:none;"
            closed="true" buttons="#dlg-buttons">
        <fieldset>
			<legend>
				用户
			</legend>   
	        <form id="fm" method="post" novalidate>
	        	<input type="hidden" name="cid">
				<div class="fitem">
	                <label>姓名:</label>
	                <input name="name" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>账号名:</label>
	                <input name="userName" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>国家码:</label>
	                <input name="countryCode" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>手机:</label>
	                <input name="mobile" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>邮箱地址:</label>
	                <input name="email" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>证件号:</label>
	                <input name="idCard" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>证件类型:</label>
	                <input class="easyui-combobox" name="cardType" id="user_cardType" required="true" data-options="
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: getDictData(dict_tf_card_type,'all')" />
	            </div>	
				<div class="fitem">
	                <label>性名(m男，f女）:</label>
	                <input class="easyui-combobox" name="sex" id="user_sex" required="true" data-options="
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									
									data: dict_tf_sex" />
	            </div>	
				<div class="fitem">
	                <label>生日:</label>
	                <input name="birthdate" class="easyui-datebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>地址:</label>
	                <input name="address" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>备注:</label>
	                <input name="remark" class="easyui-validatebox" required="true">
	            </div>	
	            <%/*
	            <div class="fitem">
	                <label>状态:</label>
	                <input class="easyui-combobox" name="status" id="user_status" required="true" data-options="
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_status" />
	            </div>
	            <div class="fitem">
	                <label>备注:</label>
	                <textarea name="remark" id="user_remark" style="height:60px;width:200px"></textarea>
	            </div>
	            */ %>
	        </form>
        </fieldset>
        <div id="dlg-buttons">
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="onSave()">Save</a>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">Cancel</a>
	    </div>
    </div>
    <!-- 用户密码修改 -->
    <div id="modifyPwdDlg" class="easyui-dialog" style="width:400px;height:220px;padding:5px 10px;display:none;"
            closed="true" buttons="#dlg-buttons-1">
        <fieldset>
			<legend>
				用户密码修改
			</legend>   
	        <form id="fmPwd" method="post" novalidate>
	        	<input type="hidden" name="cid">
				<div class="fitem">
	                <label>姓名:</label>
	                <input name="name" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>账号名:</label>
	                <input name="userName" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>密码:</label>
	                <input type="password" name="passwd" id="m_passwd" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>确认密码:</label>
	                <input type="password" name="confirmPasswd" id="m_confirmPasswd" class="easyui-validatebox" required="true">
	            </div>
	        </form>
        </fieldset>
        <div id="dlg-buttons-1">
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="onPasswdSave()">保存</a>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#modifyPwdDlg').dialog('close')">取消</a>
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
        	//alert('---onDblClick--rowIndex='+rowIndex+' row.id='+rowData.id);
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
            $('#dlg').dialog('open').dialog('setTitle','New User');
            $('#fm').form('clear');
            $('#user_status').combobox('select', '1');
            //$("#user_remark").val('test');
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
            	var entityName='user';
            	var obj = getRowData(entityName, row);
                $('#dlg').dialog('open').dialog('setTitle','修改用户信息');
                $('#fm').form('clear');
                $('#fm').form('load',obj);
            }
           
        }
        function onPasswdEdit(){
            var row = $('#dg').datagrid('getSelected');
            if (row){
            	var entityName='user';
            	var obj = getRowData(entityName, row);
                $('#modifyPwdDlg').dialog('open').dialog('setTitle','修改密码');
                obj.passwd='';
                obj.confirmPasswd='';
                $('#fmPwd').form('load',obj);
            }
           
        }
        function onPasswdSave(){
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
            $('#fmPwd').form('submit',{
                url: '/passwd/update',
                onSubmit: function(){
                	console.info($(this).serialize());
                	if($('#m_confirmPasswd').val()!=$('#m_passwd').val()) {
                		$.messager.show({
                            title: '系统消息',
                            msg: '前后输入密码不一致!'
                        });
                		return false;
                	}
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
                    	$.messager.show({
                            title: '系统消息',
                            msg: '修改成功!'
                        });
                        $('#modifyPwdDlg').dialog('close');        // close the dialog
                        doSearchReload();    // reload the user data
                    }
                }
            });
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
                url: '/basedata/user/save',
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
                        $.post('/basedata/user/delete',{id:row.cid},function(result){
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