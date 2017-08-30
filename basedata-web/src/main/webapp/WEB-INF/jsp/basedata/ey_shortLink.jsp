<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <title></title>
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
		<div data-options="region:'north', title:'-条件'"
			style="height: 80px; padding: 5px 80px;display:none;">
			<form id="search-form" >
				<table class="search-table">
					<tr>
						<td>
							业务信息类型:
			                <input type="text" id="query_infoType" name="infoType" class="easyui-validatebox">
						</td>
						<td>
							短链接编码:
			                <input type="text" id="query_code" name="code" class="easyui-validatebox">
						</td>
<!-- 						<td> -->
<!-- 							是否删除: -->
<!-- 			                <input type="text" id="query_ifDel" name="ifDel" class="easyui-validatebox"> -->
<!-- 						</td> -->
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
<!-- 		         <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="onGenerate()">生成短链接</a> -->
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="onEdit()">修改</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="onDestroy()">删除</a>
		    </div>
		    <table id="dg" class="easyui-datagrid" data-options="onDblClickRow:onDblClick, onClickRow:onClick, onLoadSuccess:function(){$('.datagrid-btable').find('div.datagrid-cell').css('text-align','left');}"
		            url="/basedata/shortLink/list?shortType=1"
		            toolbar="#toolbar" pagination="true"
		            rownumbers="true" fitColumns="true" singleSelect="true" style="display:none;">
		        <thead>
		            <tr >
		            	<%/* 
		            	<th align="center" field="cid" width="50" sortable="true">编号</th>
		            	*/ %>
<!-- 						<th align="center" field="shortType" width="50" sortable="true">1:url,2数据参数,3json</th> -->
						<th align="center" field="infoType" width="50" sortable="true">业务信息类型</th>
						<th align="center" field="code" width="50" sortable="true">短链接</th>
						<th align="center" field="data" width="50" sortable="true">长链接</th>
		                <%/**/ %>
		                <th align="center" field="status" width="50" sortable="true" formatter="renderStatus" hidden="true">状态</th>
		                <th field="create_disp" width="100" sortable="true"  hidden="true">Create user</th>
		                <th field="createDate" width="120" sortable="true"  hidden="true">Create date</th>
		                <th field="modify_disp" width="100" sortable="true"  hidden="true">Modify user</th>
		                <th field="modifyDate" width="120" sortable="true"  hidden="true">Modify date</th>
		                <th align="center" field="remark" width="50">备注</th>
		                
		            </tr>
		        </thead>
		    </table>
		 </div>
	</div>
    
    <div id="dlg" class="easyui-dialog" style="width:400px;height:500px;padding:5px 10px;display:none;"
            closed="true" buttons="#dlg-buttons">
        <fieldset>
			<legend>
				
			</legend>   
	        <form id="fm" method="post" novalidate>
	        	<input type="hidden" name="cid" value="1">
	        	<input type="hidden" name="code">
	        	 <input type="hidden" id="shortType" name="shortType" value="1">
<!-- 				<div class="fitem"> -->
<!-- 	                <label>1:url,2数据参数,3json:!!!!!</label> -->
<!-- 	                <input name="shortType" class="easyui-validatebox" value="1" required="true"> -->
<!-- 	            </div>	 -->
				<div class="fitem">
	                <label>业务信息类型，如btl-pay,btl-refund:</label>
	                <input name="infoType" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>数据:长链接，参数，json:</label>
	                <textarea name="data" id="shortLink_data" style="height:60px;width:200px"></textarea>
	            </div>	
				<div class="fitem">
	                <label>备注:</label>
	                <textarea name="remark" id="shortLink_remark" style="height:60px;width:200px"></textarea>
	            </div>	
	            <div class="fitem">
	                <label>短链接:</label>
	                <label id="code"></label>
	            </div>	
	            <%/*
	            <div class="fitem">
	                <label>状态:</label>
	                <input class="easyui-combobox" name="status" id="shortLink_status" required="true" data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_status" />
	            </div>
	            <div class="fitem">
	                <label>备注:</label>
	                <textarea name="remark" id="shortLink_remark" style="height:60px;width:200px"></textarea>
	            </div>
	            */ %>
	        </form>
        </fieldset>
        <div id="dlg-buttons">
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="onSave()">Save</a>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">Cancel</a>
	    </div>
    </div>
    
    <div id="dlgGen" class="easyui-dialog" style="width:400px;height:500px;padding:5px 10px;display:none;"
            closed="true" buttons="#dlg-buttonsGen">
        <fieldset>
			<legend>
				
			</legend>   
	        <form id="fmGen" method="post" novalidate>
	        	<input type="hidden" name="cid">
				<div class="fitem">
	                <label>1:url,2数据参数,3json:</label>
	                <input name="shortType" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>业务信息类型，如btl-pay,btl-refund:</label>
	                <input name="infoType" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>数据:长链接，参数，json:</label>
	                <input name="data" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>备注:</label>
	                <input name="remark" class="easyui-validatebox" required="true">
	            </div>	
	            <%/*
	            <div class="fitem">
	                <label>状态:</label>
	                <input class="easyui-combobox" name="status" id="shortLink_status" required="true" data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_status" />
	            </div>
	            <div class="fitem">
	                <label>备注:</label>
	                <textarea name="remark" id="shortLink_remark" style="height:60px;width:200px"></textarea>
	            </div>
	            */ %>
	        </form>
        </fieldset>
        <div id="dlg-buttonsGen">
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
            $('#dlg').dialog('open').dialog('setTitle','New ShortLink');
            $('#fm').form('clear');
            $('#shortType').val('1');
            //$('#shortLink_status').combobox('select', '1');
            //$("#shortLink_remark").val('test');
        }
        
        function onGenerate(){
            $('#dlg').dialog('open').dialog('setTitle','生成短链接');
            $('#fm').form('clear');
            $('#shortLink_status').combobox('select', '1');
            //$("#shortLink_remark").val('test');
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
            	var entityName='shortLink';
            	var obj = getRowData(entityName, row);
                $('#dlg').dialog('open').dialog('setTitle','Edit ShortLink');
                $('#fm').form('load',obj);
                $('#code').text('http://s.yc.ai/'+obj.code);
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
                url: '/basedata/shortLink/generate',
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
                        $.post('/basedata/shortLink/delete',{id:row.cid},function(result){
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