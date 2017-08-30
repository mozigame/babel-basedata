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
							Package:
							<input type="text" class="input_query" name="packageName" id="query_packageName"/>
						</td>
						<td>
							Class name:
							<input type="text" class="input_query" name="className" id="query_className"/>
						</td>
						<td>
							Method code:
							<input type="text" class="input_query" name="funcCode" id="query_funcCode"/>
						</td>
						<td>
							Title:
							<input type="text" class="input_query" name="title" id="query_title"/>
						</td>
						<td>
							 接口类型
							<input class="easyui-combobox" class="input_query" name="intfType" id="query_intfType" data-options="
								valueField: 'id',
								textField: 'name',
								data: getDictData(dict_tf_service_type,'all')" style="width:100px" />
						</td>
						<td>
							 接口日志类型
							<input class="easyui-combobox" class="input_query" name="openType" id="query_openType" data-options="
								valueField: 'id',
								textField: 'name',
								data: getDictData(dict_tf_intf_log_type,'all')" style="width:100px" />
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
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="onEdit()">Edit</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="onDestroy()">Remove</a>
		    </div>
		    <table id="dg" class="easyui-datagrid" data-options="onDblClickRow:onDblClick"
		            url="/basedata/model/list" 
		            toolbar="#toolbar" pagination="true"
		            rownumbers="true" fitColumns="true" singleSelect="true">
		        <thead>
		            <tr >
		            	<%/* 
		            	<th field="cid" width="50" sortable="true">Id</th>
		            	<th field="parentId" width="50" sortable="true">Parent id</th>
		            	*/ %>
		            	<th field="packageName" width="50" sortable="true">packageName</th>
		                <th field="className" width="50" sortable="true">className</th>
		                <th field="funcCode" width="50" sortable="true">funcCode</th>
		                <th field="intfType" width="50" sortable="true">Intf type</th>
		                <th field="openType" width="50" sortable="true" formatter="renderIf">Intf log type</th>
		                <th field="title" width="50" sortable="true">Title</th>
		                <th field="author" width="50" sortable="true">Author</th>
		                <th field="calls" width="50" sortable="true">Calls</th>
		                <th field="descs" width="50" sortable="true"  hidden="true">Descs</th>
		                <th field="orderCount" width="100" sortable="true">顺序号</th>
		                <th field="createDate" width="120" sortable="true"  hidden="true" formatter="Common.DateTimeFormatter">Create date</th>
		                <th field="modify_disp" width="100" sortable="true"  hidden="true">Modify user</th>
		                <th field="modifyDate" width="120" sortable="true"  hidden="true" formatter="Common.DateTimeFormatter">Modify date</th>
		                <th field="remark" width="150"  hidden="true">备注</th>
		                
		            </tr>
		        </thead>
		    </table>
		 </div>
	</div>
    
    <div id="dlg" class="easyui-dialog" style="width:510px;height:500px;padding:5px 10px;display:none"
            closed="true" buttons="#dlg-buttons">
        <legend>
			接口日志管理
		</legend>   
        <form id="fm" method="post" novalidate>
        	<input type="hidden" name="cid">
        	<input type="hidden" name="intfType">
        	<fieldset>
				<legend>接口信息(ReadOnly)</legend>  
	        	<div class="fitem">
	                <label>Parent:</label>
	                <label><div id="parentName"></div></label>
	            </div>
	            <div class="fitem">
	                <label>packageName:</label>
	                <input name="packageName" class="easyui-validatebox" required="true" readOnly>
	            </div>
	            <div class="fitem">
	                <label>className:</label>
	                <input name="className" class="easyui-validatebox" required="true" readOnly>
	            </div>
	            <div class="fitem">
	                <label>funcCode:</label>
	                <input name="funcCode" class="easyui-validatebox" required="true" readOnly>
	            </div>
	            <div class="fitem">
	                <label>params:</label>
	                <input name="params" class="easyui-validatebox"  readOnly>
	            </div>
	        </fieldset>
	        <fieldset>
	        	<legend>Log注解</legend>  
	        	<div class="fitem">
	                <label>title:</label>
	                <input name="title" class="easyui-validatebox" >
	            </div>
	            <div class="fitem">
	                <label>author:</label>
	                <input name="author" class="easyui-validatebox" >
	            </div>
	            <div class="fitem">
	                <label>calls:</label>
	                <input name="calls" class="easyui-validatebox" >
	            </div>
	            <div class="fitem">
	                <label>descs:</label>
	                <input name="descs" class="easyui-validatebox" >
	            </div>
	        </fieldset>
	        <fieldset>
	        	<legend>Log配置</legend>  
	            <div class="fitem">
	                <label>接口日志类型:</label>
	                <input class="easyui-combobox" name="openType"  data-options="
									valueField: 'id',
									textField: 'name',
									data: dict_tf_intf_log_type" />
	            </div>
	            
	            <div class="fitem">
	                <label>Order count:</label>
	                <input name="orderCount" class="easyui-validatebox" >
	            </div>
	            <div class="fitem">
	                <label>备注:</label>
	                <textarea name="remark" id="lookupItem_remark" style="height:60px;width:220px"></textarea>
	            </div>
            </fieldset>
       </form>
        <div id="dlg-buttons">
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="onSave()">Save</a>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">Cancel</a>
	    </div>
    </div>
    
    <script type="text/javascript" src="/scripts/basedata/model.js"></script>
    <script type="text/javascript">
    	
    </script>
    <%@ include file="/system/_foot.jsp"%>
</body>
</html>    