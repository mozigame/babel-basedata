<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html manifest="${env['app_manifest']}" >
<head>
	<meta charset="UTF-8">
    <title>角色表</title>
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
		<div data-options="region:'north', title:'角色表-条件'"
			style="height: 80px; padding: 5px 80px;">
			<form id="search-form" >
				<table class="search-table">
					<input type="hidden" name="lookupId" id="query_lookupId">
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
							<a class="easyui-linkbutton"
								data-options="iconCls:'icon-search'" id="search-btn">查询</a>
							<a class="easyui-linkbutton"
								data-options="iconCls:'icon-reload'" id="reset-btn">重置</a>
						</td>

					</tr>
				</table>
			</form>
		</div>
		
		<div data-options="region:'east',split:true" title="East" style="width:500px;">
	    	
		    <div class="easyui-panel" style="padding:5px" width="100%">
				<ul id="menuTree" class="easyui-tree" data-options="url:'/basedata/module/findRoleModuleByParentId'
					,animate:true,dnd:true,lines:true,onBeforeLoad:onBeforeLoad,onBeforeExpand:onBeforeExpand,onLoadSuccess:onLoadSuccess
					,onExpandAll:onExpandAll,onCollapseAll:onCollapseAll
					,checkbox:true"></ul>
			</div>
		 </div>
		
		<!-- 查询结果 center -->
	    <div data-options="region:'center', title:'查询结果'">
	    	<div id="toolbar">
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="onAdd()">New</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="onEdit()">Edit</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="onDestroy()">Remove</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="onExpandAll()">Expand all</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="onCollapseAll()">Collapse all</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="onSaveRoleModule()">Save role module</a>
		    </div>
		    <table id="dg" class="easyui-datagrid" data-options="onDblClickRow:onDblClick, onClickRow:onClick"
		            url="/basedata/role/list"
		            toolbar="#toolbar" pagination="true"
		            rownumbers="true" fitColumns="true" singleSelect="true">
		        <thead>
		            <tr >
		            	<%/* 
		            	<th field="cid" width="50" sortable="true">编号</th>
		            	*/ %>
						<th field="code" width="50" sortable="true">编码</th>
						<th field="name" width="50" sortable="true">名称</th>
		                <%/* */ %>
		                <th field="status" width="50" sortable="true" formatter="renderStatus"  hidden="true">状态</th>
		                <th field="create_disp" width="100" sortable="true"  hidden="true">Create user</th>
		                <th field="createDate" width="120" sortable="true"  hidden="true">Create date</th>
		                <th field="modify_disp" width="100" sortable="true"  hidden="true">Modify user</th>
		                <th field="modifyDate" width="120" sortable="true"  hidden="true">Modify date</th>
		                <th field="remark" width="50"  hidden="true">备注</th>
		               
		            </tr>
		        </thead>
		    </table>
		 </div>
	</div>
    
    <div id="dlg" class="easyui-dialog" style="width:400px;height:500px;padding:5px 10px;display:none"
            closed="true" buttons="#dlg-buttons">
        <fieldset>
			<legend>
				角色表
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
	                <label>状态:</label>
	                <input class="easyui-combobox" name="status" id="role_status" required="true" data-options="
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_status" />
	            </div>
	            <div class="fitem">
	                <label>备注:</label>
	                <textarea name="remark" id="role_remark" style="height:60px;width:200px"></textarea>
	            </div>
	        </form>
        </fieldset>
        <div id="dlg-buttons">
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="onSave()">Save</a>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">Cancel</a>
	    </div>
    </div>
    
    <script type="text/javascript">
    	var role_cid;
    	function onBeforeLoad(node, param){
    		param.roleId=role_cid;
    		//console.log(param);
    	}
    	function onBeforeExpand(node, param){
			$('#menuTree').tree('options').url = "/basedata/module/findRoleModuleByParentId?moduleId="+node.id+'&roleId='+role_cid;     
		}
    	 function onSelectRole(){
	        	var row = $('#dg').datagrid('getSelected');
	            if (row){
	            	var roleId=row.cid;
	            	role_cid=roleId;
	            	console.log('----role_cid='+role_cid);
	            	$('#menuTree').tree({url:'/basedata/module/findRoleModuleByParentId?roleId='+roleId});
	            	//openRootDelay();
	            	//$('#menuTree').tree('reload'); 
	            }
	        	
	        }
    	 function onExpandAll(){
 			$('#menuTree').tree('expandAll');
 		}
 		
 		function onCollapseAll(){
 			$('#menuTree').tree('collapseAll');
 		}
    	 function onLoadSuccess(){
    		 openRoot()
    	 }
    	function openRootDelay(){
   	    	//setTimeout("openRoot()", 100);
   	    	//setTimeout("openRoot()", 300);
   	    }
   	    function openRoot(){
   			 var rootNode = $("#menuTree").tree('getRoot');
   			// console.log(rootNode);
   			//$('#menuTree').tree('expand', rootNode);
   			$('#menuTree').tree('expandAll');
   		}
   	    
   	    function onSaveRoleModule(){
   	    	var nodes = $('#menuTree').tree('getChecked');
   	    	var moduleIds='';
   	    	for(i in nodes){
   	    		moduleIds+=nodes[i].id+',';
   	    	}
   	    	console.log(moduleIds);
	   	     $.messager.confirm('Confirm','Are you sure you want to save role module?',function(r){
	             if (r){
	                 $.post('/basedata/module/saveRoleModules',{roleId:role_cid,moduleIds:moduleIds},function(result){
	                     if (result.success){
	                     	//doSearchReload();    // reload the user data
	                    	 $.messager.show({    // show error message
	                             title: 'OK',
	                             msg: 'Operate success'
	                         });
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
			console.log('----onClick--');
			onSelectRole();
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
            $('#dlg').dialog('open').dialog('setTitle','New Role');
            $('#fm').form('clear');
            $('#role_status').combobox('select', '1');
            //$("#role_remark").val('test');
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
            	var entityName='role';
            	var obj = getRowData(entityName, row);
                $('#dlg').dialog('open').dialog('setTitle','Edit Role');
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
                url: '/basedata/role/save',
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
                        $.post('/basedata/role/delete',{id:row.cid},function(result){
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