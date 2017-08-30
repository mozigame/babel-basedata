<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html manifest="${env['dev.app_manifest']}" >
<head>
	<meta charset="UTF-8">
    <title>模块菜单</title>
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
		<div data-options="region:'north', title:'系统参数-条件'"
			style="height: 80px; padding: 5px 80px;">
			<form id="search-form" >
				<table class="search-table">
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
	    	<%/* 
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="onAdd()">New</a>
		        */ %>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openTree()">New</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="onEdit()">Edit</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="onDestroy()">Remove</a>
		    </div>
		    <table id="dg" class="easyui-datagrid" data-options="onDblClickRow:onDblClick"
		            url="/basedata/module/list" 
		            toolbar="#toolbar" pagination="true"
		            rownumbers="true" fitColumns="true" singleSelect="true">
		        <thead>
		            <tr >
		            	<%/* 
		            	<th field="cid" width="50" sortable="true">Id</th>
		            	<th field="parentId" width="50" sortable="true">Parent id</th>
		            	*/ %>
		            	<th field="code" width="50" sortable="true">编码</th>
		                <th field="name" width="50" sortable="true">名称</th>
		                <th field="parentName" width="50" sortable="true">父节点名称</th>
		                <th field="appType" width="50" sortable="true">应用类型</th>
		                <th field="showType" width="50" sortable="true" formatter="renderIf">是否显示</th>
		                <th field="mtype" width="50" sortable="true" hidden="true">菜单类型</th>
		                <th field="url" width="100" sortable="true" hidden="true">url</th>
		                <th field="icon" width="50" sortable="true" hidden="true">小图标</th>
		                <th field="color" width="50" sortable="true" >颜色</th>
		                <th field="css" width="50" sortable="true">样式</th>
		                <th field="jsonValue" width="50" sortable="true" hidden="true">自定义json</th>
		                <th field="orderCount" width="50" sortable="true">顺序号</th>
		                <th field="status" width="50" sortable="true" formatter="renderStatus">状态</th>
		                <th field="create_disp" width="100" sortable="true" hidden="true">Create user</th>
		                <th field="createDate" width="120" sortable="true" hidden="true" formatter="Common.DateTimeFormatter">Create date</th>
		                <th field="modify_disp" width="100" sortable="true" hidden="true">Modify user</th>
		                <th field="modifyDate" width="120" sortable="true" hidden="true" formatter="Common.DateTimeFormatter">Modify date</th>
		                <th field="remark" width="150" hidden="true">备注</th>
		                
		            </tr>
		        </thead>
		    </table>
		 </div>
	</div>
    
    <div id="dlg" class="easyui-dialog" style="width:510px;height:500px;padding:5px 10px;display:none"
            closed="true" buttons="#dlg-buttons">
        <fieldset>
			<legend>
				系统参数
			</legend>   
	        <form id="fm" method="post" novalidate>
	        	<input type="hidden" name="cid">
	        	<input type="hidden" name="parentId">
	        	<div class="fitem">
	                <label>Parent name:</label>
	                <label><div id="parentName"></div></label>
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
	                <label>应用类型:</label>
	                <input class="easyui-combobox" name="appType" id="module_appType" required="true" data-options="
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_app_type" />
	            </div>
	            <div class="fitem">
	                <label>模块类型:</label>
	                <input class="easyui-combobox" name="mtype" id="module_mtype" required="true" data-options="
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_mtype" />
	            </div>
	            <div class="fitem">
	                <label>是否显示:</label>
	                <input class="easyui-combobox" name="showType" id="module_showType" required="false" data-options="
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_if" />
	            </div>
	            <div class="fitem">
	                <label>URL:</label>
	                <input name="url" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>小图标:</label>
	                <input name="icon" class="easyui-validatebox">
	            </div>
	            <div class="fitem">
	                <label>CSS样式:</label>
	                <input name="css" class="easyui-validatebox">
	            </div>
	            <div class="fitem">
	                <label>Color:</label>
	                <input name="color" class="easyui-validatebox">
	            </div>
	            <div class="fitem">
	                <label>Json参数定义:</label>
	                <textarea name="jsonValue"  style="height:60px;width:220px"></textarea>
	            </div>
	            <div class="fitem">
	                <label>顺序号:</label>
	                <input name="orderCount" class="easyui-validatebox" required="true">
	            </div>
	            
	           
	            <div class="fitem">
	                <label>状态:</label>
	                <input class="easyui-combobox" name="status" id="module_status" required="true" data-options="
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_status" />
	            </div>
	            <div class="fitem">
	                <label>备注:</label>
	                <textarea name="remark" id="module_remark" style="height:60px;width:280px"></textarea>
	            </div>
	        </form>
        </fieldset>
        <div id="dlg-buttons">
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="onSave()">Save</a>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">Cancel</a>
	    </div>
    </div>
    
    <div id="treeWin" class="easyui-dialog" style="width:600px;height:500px;padding:5px 10px;display:none"
            closed="true">
            <iframe src='/basedata/module/tree' frameborder="0" style="width:100%;height:100%;">
            </iframe>
    </div>
    
    <script type="text/javascript" src="/scripts/basedata/module.js"></script>
    <script type="text/javascript">
    	
    </script>
    <%@ include file="/system/_foot.jsp"%>
</body>
</html>    