<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html manifest="${env['app_manifest']}" >
<head>
	<meta charset="UTF-8">
    <title>消息日志</title>
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
	width: 260px;
	border: 1px solid #ccc;
	padding: 2px;
}
</style>
</head>

<body>
    
 	<div id="mainWindow" class="easyui-layout" data-options="fit:true" style="display:none">
    	<!-- 查询条件  -->
		<div data-options="region:'north', title:'操作日志-条件'"
			style="height: 80px; padding: 5px 80px;">
			<form id="search-form" >
				<table class="search-table">
					<tr>
						<td>
							Msg code:
							<input type="text" class="input_query" name="msgCode" id="query_code"/>
						</td>
						<td>
							编码:
							<input type="text" class="input_query" name="code" id="query_code"/>
						</td>
						<td>
							中文名:
							<input type="text" class="input_query" name="nameCn" id="query_nameCn"/>
						</td>
						<td>
							创建时间>=:
							<input name="createDate" id="query_createDate" class="easyui-datetimebox" style="width:150px" formatter="Common.DateTimeFormatter">
						</td>
						<td>
							 状态
							<input class="easyui-combobox" class="input_query"  style="width:100px" name="status" id="query_status" data-options="
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
		            toolbar="#toolbar" pagination="true"
		            rownumbers="true" fitColumns="true" singleSelect="true">
		        <thead>
		            <tr >
		            	<th field="msgCode" width="50" sortable="true">Msg code</th>
		            	<th field="msgType" width="50" formatter="renderMsgType" sortable="true">Msg type</th>
		            	<%/* 
		            	<th field="serviceId" width="50" sortable="true">Service Id</th>
		            	*/ %>
		            	<th field="userId" width="50" sortable="true">User Id</th>
		            	<th field="tos" width="50" sortable="true">Tos</th>
		            	<th field="ccs" width="150" sortable="true"  hidden="true">Copy tos</th>
		            	<%/* */%>
		            	<th field="bccs" width="150" sortable="true"  hidden="true">Blind copy tos</th>
		            	<th field="failMails" width="150" sortable="true"  hidden="true">Fail mails</th>
		            	
		                <th field="title" width="50" sortable="true">标题</th>
		                <th field="dataName" width="50" sortable="true">数据名称</th>
		                <th field="dataId" width="50" sortable="true">数据Id</th>
		                <th field="relUrl" width="50" sortable="true">关联url</th>
		                <th field="content" width="150" sortable="false"  hidden="true">内容</th>
		                <th field="exceptions" width="150" sortable="true" hidden="true">异常信息</th>
		                		                
		                <th field="sendType" width="50" sortable="true" formatter="renderSendType">发送类型</th>
		                <th field="sendFlag" width="50" sortable="true" formatter="renderSendFlag">发送状态</th>
		                <th field="template" width="50"  hidden="true" >Template</th>
		                <th field="createDate" width="50" sortable="true" formatter="Common.DateTimeFormatter">创建时间</th>
		                <th field="sendTime" width="50" sortable="true" formatter="Common.DateTimeFormatter">发送时间</th>
		                <th field="endTime" width="50" sortable="true" formatter="Common.DateTimeFormatter">结束时间</th>
		                <th field="runTime" width="50" sortable="true">运行时间</th>
		                <th field="mailCount" width="50" sortable="true"  hidden="true">邮件个数</th>
		                <th field="sendCount" width="50" sortable="true"  hidden="true">发送成功次数</th>
		                <th field="ifRead" width="50" sortable="true" formatter="renderIf">是否已阅</th>
		                <th field="readTime" width="120" sortable="true" formatter="Common.DateTimeFormatter"  hidden="true">阅读时间</th>
		            </tr>
		        </thead>
		    </table>
		 </div>
	</div>
    
    <div id="dlg" class="easyui-dialog" style="width:500px;height:600px;padding:5px 10px;display:none"
            closed="true" buttons="#dlg-buttons">
        <fieldset>
			<legend>
				操作日志
			</legend>   
	        <form id="fm" method="post" novalidate>
	        	<input type="hidden" name="cid">
	        	<div class="fitem">
	                <label>Msg code:</label>
	                <input name="msgCode" class="easyui-validatebox">
	            </div>
	            <div class="fitem">
	                <label>User name:</label>
	                <input name="userName" class="easyui-validatebox">
	            </div>
	            <div class="fitem">
	                <label>发送类型:</label>
	                <input class="easyui-combobox" name="msgType" id="logMsg_msgType" data-options="
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_msg_type" />
	            </div>
	            <div class="fitem">
	                <label>服务:</label>
	                <input name="serviceId" class="easyui-validatebox">
	            </div>
	            <div class="fitem">
	                <label>收件人:</label>
	                <input name="tos" class="easyui-validatebox">
	            </div>
	            <div class="fitem">
	                <label>抄送人:</label>
	                <input name="ccs" class="easyui-validatebox">
	            </div>
	            <div class="fitem">
	                <label>密送人:</label>
	                <input name="bccs" class="easyui-validatebox">
	            </div>
	            <div class="fitem">
	                <label>标题:</label>
	                <input name="title" class="easyui-validatebox">
	            </div>
	             <div class="fitem">
	                <label>数据名称:</label>
	                <input name="dataName" class="easyui-validatebox">
	            </div>
	            <div class="fitem">
	                <label>数据Id:</label>
	                <input name="dataId" class="easyui-validatebox">
	            </div>
	            <div class="fitem">
	                <label>RelUrl:</label>
	                <input name="relUrl" class="easyui-validatebox">
	            </div>
	            <div class="fitem">
	                <label>内容:</label>
	                <textarea name="content" id="logMsg_content" style="height:100px;width:260px"></textarea>
	            </div>
	            <div class="fitem">
	                <label>模板id:</label>
	                <input name="template" class="easyui-validatebox">
	            </div>
	            <div class="fitem">
	                <label>异常:</label>
	                <textarea name="exceptions" id="logMsg_exceptions" style="height:60px;width:220px"></textarea>
	            </div>
	            
	           
	            <div class="fitem">
	                <label>发送类型:</label>
	                <input class="easyui-combobox" name="sendType"  style="width:100px" id="logMsg_sendType" data-options="
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_send_type" />
	            </div>
	            <div class="fitem">
	                <label>发送状态:</label>
	                <input class="easyui-combobox" name="sendFlag"  style="width:100px" id="logMsg_sendFlag" data-options="
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_send_flag" />
	            </div>
	            <div class="fitem">
	                <label>邮件个数:</label>
	                <input name="mailCount" class="easyui-validatebox">
	            </div>
	            <div class="fitem">
	                <label>发送成功个数:</label>
	                <input name="sendCount" class="easyui-validatebox">
	            </div>
	            <div class="fitem">
	                <label>发送时间:</label>
	                <input name="sendTime" class="easyui-validatebox">
	            </div>
	            <div class="fitem">
	                <label>结束时间:</label>
	                <input name="endTime" class="easyui-validatebox">
	            </div>
	            <div class="fitem">
	                <label>运行时间:</label>
	                <input name="runTime" class="easyui-validatebox">
	            </div>
	            <div class="fitem">
	                <label>备注:</label>
	                <textarea name="remark" id="logMsg_remark" style="height:60px;width:200px"></textarea>
	            </div>
	        </form>
        </fieldset>
        <div id="dlg-buttons">
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">Cancel</a>
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
		
		
		$(function () {
			var recentlyDate=new Date(new Date().getTime()-7*24*3600*1000);//7天内
			$('#query_createDate').datetimebox('setValue', Common.DateFormatter(recentlyDate));
			doSearch();
		});
		function doSearch(){
        	var jsonParam = $('#search-form').serializeJson();
        	//status为all时处理
        	value_all2empty(jsonParam);
      		 $('#dg').datagrid({  
                   url:"/basedata/logmsg/list"
                   ,queryParams:jsonParam
               });
			//$('#dg').datagrid('load', jsonParam);
        }
		
		
		
		function doSearchReload(){
			doSearch();
        }
		
		function onDblClick(rowIndex, rowData){
        	//alert('---onDblClick--rowIndex='+rowIndex+' row.id='+rowData.id);
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
            $('#dlg').dialog('open').dialog('setTitle','New LogMsg');
            $('#fm').form('clear');
            $('#logMsg_status').combobox('select', '1');
            //$("#logMsg_remark").val('test');
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
            	var entityName='logMsg';
            	var obj = getRowData(entityName, row);
            	//alert(obj.'logMsg.empCode');
            	obj.sendTime=Common.DateTimeFormatter(obj.sendTime);
            	obj.endTime=Common.DateTimeFormatter(obj.endTime);
                $('#dlg').dialog('open').dialog('setTitle','Edit LogMsg');
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
                url: '/basedata/logmsg/save',
                onSubmit: function(){
                    return $(this).form('validate');
                },
                success: function(result){
                    var result = eval('('+result+')');
                    if (result.msgBody){
                        $.messager.show({
                            title: 'Error',
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
                        $.post('/basedata/logmsg/delete',{id:row.cid},function(result){
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