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
							标题:
			                <input type="text" id="query_title" name="title" class="easyui-validatebox">
						</td>
						<td>
							系统类型:
			                <input class="easyui-combobox" name="sysType" id="query_sysType" data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: getDictData(dict_tf_sys_type,'all')" style="width:90px" />
						</td>
						<td>
							二维码类型:
			                <input class="easyui-combobox" name="qrType" id="query_qrType" data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: getDictData(dict_tf_qr_type,'all')" style="width:80px"/>
						</td>
						<td>
							时效类型:
			                <input class="easyui-combobox" name="agingType" id="query_agingType"  data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: getDictData(dict_tf_aging_type,'all')" style="width:80px"/>
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
	    		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="onAddQrWx()">微信二维码生成</a>
	    		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="onAddQr()">普通二维码生成</a>
	    		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showQr()">查看二维码</a>
	    		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="onReload()">刷新token</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="onAdd()">新增</a>
		        
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="onEdit()">修改</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="onDestroy()">删除</a>
		    </div>
		    <table id="dg" class="easyui-datagrid" data-options="onDblClickRow:onDblClick, onClickRow:onClick, onLoadSuccess:function(){$('.datagrid-btable').find('div.datagrid-cell').css('text-align','left');}"
		            url="/basedata/qrCode/list"
		            toolbar="#toolbar" pagination="true"
		            rownumbers="true" fitColumns="true" singleSelect="true" style="display:none;">
		        <thead>
		            <tr >
		            	<%/* 
		            	<th align="center" field="cid" width="50" sortable="true">编号</th>
		            	*/ %>
						<th align="center" field="sysType" width="50" sortable="true">系统类型</th>
						<th align="center" field="qrType" width="50" sortable="true" formatter="renderQrType">二维码类型</th>
						<th align="center" field="agingType" width="50" sortable="true" formatter="renderAgingType">时效类型</th>
						<th align="center" field="title" width="50" sortable="true">标题</th>
						<th align="center" field="data" width="50" sortable="true" hidden="true">二维码数据</th>
						<th align="center" field="partnerName" width="50" sortable="true">聚道</th>
						<th align="center" field="ticket" width="50" sortable="true">ticket</th>
						<th align="center" field="url" width="50" sortable="true">url地址</th>
		                <%/**/ %>
		                <th align="center" field="status" width="50" sortable="true" formatter="renderStatus">状态</th>
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
				
			</legend>   
	        <form id="fm" method="post" novalidate>
	        	<input type="hidden" name="cid">
				<div class="fitem">
	                <label>系统类型:</label>
	                <input class="easyui-combobox" name="sysType" id="qrCode_sysType" required="true" data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_sys_type"/>
	            </div>	
				<div class="fitem">
	                <label>二维码类型:</label>
	                <input class="easyui-combobox" name="qrType" id="qrCode_qrType" required="true" data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_qr_type"/>
	            </div>	
				<div class="fitem">
	                <label>时效类型:</label>
	                 <input class="easyui-combobox" name="agingType" id="qrCode_agingType" required="true" data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_aging_type" />
	            </div>	
				<div class="fitem">
	                <label>标题:</label>
	                <input name="title" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>二维码数据:</label>
	                <input name="data" class="easyui-validatebox" >
	            </div>	
				<div class="fitem">
	                <label>聚道id:</label>
	                <input name="partnerId" class="easyui-validatebox">
	            </div>	
				<div class="fitem">
	                <label>ticket:</label>
	                <input name="ticket" class="easyui-validatebox">
	            </div>	
				<div class="fitem">
	                <label>url地址:</label>
	                <input name="url" class="easyui-validatebox" required="true">
	            </div>	
				
	            <%/**/ %>
	            <div class="fitem">
	                <label>状态:</label>
	                <input class="easyui-combobox" name="status" id="qrCode_status" required="true" data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_status" />
	            </div>
	            <div class="fitem">
	                <label>备注:</label>
	                <textarea name="remark" id="qrCode_remark" style="height:60px;width:200px"></textarea>
	            </div>
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
				普通二维码生成
			</legend>   
	        <form id="fmGen" method="post" novalidate>
	        	<input type="hidden" name="cid">
	        	<input type="hidden" name="qrType" value="2">
				<div class="fitem">
	                <label>系统类型:</label>
	                <input class="easyui-combobox" name="sysType" id="gen_qrCode_sysType"  data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_sys_type"/>
	            </div>	
				<div class="fitem">
	                <label>时效类型:</label>
	                 <input class="easyui-combobox" name="agingType" id="gen_qrCode_agingType"  data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_aging_type" />
	            </div>
	            <div class="fitem">
	                <label>聚道id:</label>
	                <input name="partnerId" class="easyui-validatebox">
	            </div>	
				<div class="fitem">
	                <label>标题:</label>
	                <input name="title" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>url地址:</label>
	                <textarea name="url" style="height:60px;width:200px"></textarea>
	            </div>	
				
	            <%/**/ %>
	            <div class="fitem">
	                <label>状态:</label>
	                <input class="easyui-combobox" name="status" id="gen_qrCode_status" required="true" data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_status" />
	            </div>
	            <div class="fitem">
	                <label>备注:</label>
	                <textarea name="remark" id="gen_qrCode_remark" style="height:60px;width:200px"></textarea>
	            </div>
	        </form>
        </fieldset>
        <div id="dlg-buttonsGen">
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="onSaveQr()">普通二维码生成</a>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgGen').dialog('close')">Cancel</a>
	    </div>
    </div>
    
    <div id="dlgGenWx" class="easyui-dialog" style="width:400px;height:500px;padding:5px 10px;display:none;"
            closed="true" buttons="#dlg-buttonsGenWx">
        <fieldset>
			<legend>
				微信二维码生成
			</legend>   
	        <form id="fmGenWx" method="post" novalidate>
	        	<input type="hidden" name="cid">
	        	<input type="hidden" name="qrType" value="1">
				<div class="fitem">
	                <label>系统类型:</label>
	                <input class="easyui-combobox" name="sysType" id="genWx_qrCode_sysType" required="true" data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_sys_type" required="true"/>
	            </div>	
				<div class="fitem">
	                <label>时效类型:</label>
	                 <input class="easyui-combobox" name="agingType" id="genWx_qrCode_agingType" required="true" data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_aging_type" required="true"/>
	            </div>	
	            <div class="fitem">
	                <label>聚道:</label>
	                <input name="partnerId" class="easyui-validatebox">
	            </div>	
				<div class="fitem">
	                <label>标题:</label>
	                <input name="title" class="easyui-validatebox" required="true">
	            </div>	
				
				
				
	            <div class="fitem">
	                <label>状态:</label>
	                <input class="easyui-combobox" name="status" id="genWx_qrCode_status" required="true" data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_status" />
	            </div>
	            <div class="fitem">
	                <label>备注:</label>
	                <textarea name="remark" id="genWx_qrCode_remark" style="height:60px;width:200px"></textarea>
	            </div>
	        </form>
        </fieldset>
        <div id="dlg-buttonsGenWx">
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="onSaveQrWx()">生成二维码</a>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgGenWx').dialog('close')">Cancel</a>
	    </div>
    </div>
    
    <div id="dlgReload" class="easyui-dialog" style="width:350px;height:200px;padding:5px 10px;display:none;"
            closed="true" buttons="#dlg-buttonsReload">
        <fieldset>
			<legend>
				刷新token
			</legend>   
	        <form id="fmReload" method="post" novalidate>
				<div class="fitem">
	                <label>系统类型:</label>
	                <input class="easyui-combobox" name="sysType" id="reload_sysType"  data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_sys_type" />
	            </div>
	        </form>
        </fieldset>
        <div id="dlg-buttonsReload">
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="onSaveReload()">刷新token</a>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgReload').dialog('close')">Cancel</a>
	    </div>
    </div>
    
    <div id="dlgQr" class="easyui-dialog" style="width:700px;height:600px;padding:5px 10px;display:none;"
            closed="true" >
            <div>URL：</div>
            <div id='qrUrl'></div>
            <div>&nbsp;</div>
            <div>二维码：</div>
        	<iframe id="qrMain" width="100%" height="100%" frameborder="0">
        	</iframe>
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
            $('#dlg').dialog('open').dialog('setTitle','New QrCode');
            $('#fm').form('clear');
            $('#qrCode_status').combobox('select', '1');
            //$("#qrCode_remark").val('test');
        }
        
        function onAddQr(){
        	 $('#dlgGen').dialog('open').dialog('setTitle','普通二维码生成');
             $('#fmGen').form('clear');
             
             $('#gen_qrCode_agingType').combobox('select', '1');
             $('#gen_qrCode_status').combobox('select', '1');
             //$("#qrCode_remark").val('test');
        }
        
        function onAddQrWx(){
       	 $('#dlgGenWx').dialog('open').dialog('setTitle','微信二维码生成');
            $('#fmGenWx').form('clear');
            
            $('#genWx_qrCode_agingType').combobox('select', '1');
            $('#genWx_qrCode_status').combobox('select', '1');
            //$("#qrCode_remark").val('test');
       }
        
        function onReload(){
			$('#dlgReload').dialog('open').dialog('setTitle','刷新token');
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
        
        var qrApiUrl='https://mp.weixin.qq.com/cgi-bin/showqrcode';
        function onEdit(){
            var row = $('#dg').datagrid('getSelected');
            if (row){
            	var entityName='qrCode';
            	var obj = getRowData(entityName, row);
                $('#dlg').dialog('open').dialog('setTitle','Edit QrCode');
                $('#fm').form('clear');
                $('#fm').form('load',obj);
                if(obj.ticket){
                	$('#code').text(qrApiUrl+'?ticket='+obj.ticket);
                }
            }
        }
        
        function showQr(){
          	 var row = $('#dg').datagrid('getSelected');
               if (row){
            	   var url='';
            	   if(row.qrType==1){
            		   url=qrApiUrl+'?ticket='+row.ticket;
            	   }
            	   else{
            		   url='/code/qrcode?sysType='+row.sysType+'&url='+row.url;
            	   }
              	 $('#qrUrl').html(row.url+'<br/><br/>'+url); 
              	 $('#qrMain').attr('src',url);
              	 $('#dlgQr').dialog('open').dialog('setTitle','显示二维码');
               }
          }
        
        function onSaveReload(){
        	var sysType=$('#reload_sysType').combobox('getValue');
        	if(!sysType){
        		$.messager.show({
                    title: '刷新token',
                    msg: '请选择查询框的系统类型'
                });
        		return;
        	}
        	$.messager.confirm('Confirm','Are you sure you want to reload wxToken?',function(r){
        		if(r){
        			$.get('/basedata/qrCode/reloadWxToken',{sysType:sysType},function(result){
	   	                 if (result.success){
	   	                	 $.messager.show({    // show error message
	   	                         title: 'Success',
	   	                         msg: '操作成功'
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
        function onSave(){
        	var row = $('#dg').datagrid('getSelected');
            $('#fm').form('submit',{
                url: '/basedata/qrCode/save',
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
        
        function onSaveQrWx(){
        	console.log('-----onSaveQrWx--');
            $('#fmGenWx').form('submit',{
                url: '/basedata/qrCode/genQrCodeWx',
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
                        $('#dlgGenWx').dialog('close');        // close the dialog
                        $.messager.show({
                            title: result.msgCode,
                            msg: result.msgBody
                        });
                        doSearchReload();    // reload the user data
                    }
                }
            });
        }
        function onSaveQr(){
            $('#fmGen').form('submit',{
                url: '/basedata/qrCode/genQrCode',
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
                        $('#dlgGen').dialog('close');        // close the dialog
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
                        $.post('/basedata/qrCode/delete',{id:row.cid},function(result){
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