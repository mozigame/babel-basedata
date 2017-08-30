<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <title>扩展属性配置表</title>
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
		<div data-options="region:'north', title:'数据字典-条件'"
			style="height: 160px; padding: 5px 40px;">
			<form id="search-form" >
				<div class="fitem">
	                <label>关联类型编码:</label>
	                <input type="text" id="query_refDataName" name="refDataName" class="easyui-validatebox">
	            </div>
	            <div class="fitem">
	                <label>状态:</label>
	                <input class="easyui-combobox" class="input_query" name="status" id="query_status" data-options="
								valueField: 'id',
								textField: 'name',
								data: getDictData(dict_tf_status,'all')" style="width:100px" />
	            </div>
	           
<!-- 	            <div class="fitem"> -->
<!-- 	                <label>状态:</label> -->
<!-- 	                <input class="easyui-combobox" class="input_query" name="status" id="query_status" data-options=" -->
<!-- 								valueField: 'id', -->
<!-- 								textField: 'value', -->
<!-- 								data: getDictData(dict_tf_status,'all')" style="width:100px"/> -->
<!-- 	            </div> -->
				<div class="fitem">
					<a class="easyui-linkbutton"
								data-options="iconCls:'icon-search'" id="search-btn">查询</a>
							
				</div>
			</form>
		</div>
		<%/* 
    	<!-- 查询条件  -->
		<div data-options="region:'north', title:'扩展属性配置表，使用说明，扩展的数据基于id进行关联，出于性能考虑不支持与非id进行关联-条件'"
			style="height: 80px; padding: 5px 80px;display:none;">
			<form id="search-form" >
				<table class="search-table">
					<tr>
						<td>
							名称:
			                <input type="text" id="query_name" name="name" class="easyui-validatebox">
						</td>
						<td>
							配置类型:
			                <input type="text" id="query_confType" name="confType" class="easyui-validatebox">
						</td>
						<td>
							关联类型编码:
			                <input type="text" id="query_refDataName" name="refDataName" class="easyui-validatebox">
						</td>
						<td>
							对应的扩展表名:
			                <input type="text" id="query_extTable" name="extTable" class="easyui-validatebox">
						</td>
						<td>
							状态:
			                <input class="easyui-combobox" class="input_query" name="status" id="query_status" data-options="
								valueField: 'id',
								textField: 'name',
								data: getDictData(dict_tf_status,'all')" style="width:100px" />
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
		*/ %>
		
		<!-- 查询结果 center -->
	    <div data-options="region:'center', title:'查询结果'">
	    	<div id="toolbar" style="display:none;">
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="onAdd()">新增</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="onEdit()">修改</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="onDestroy()">删除</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="showSql()">查看sql</a>
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="showData()">查看数据</a>
		    </div>
		    <table id="dg" class="easyui-datagrid" data-options="onDblClickRow:onDblClick, onClickRow:onClick, onLoadSuccess:function(){$('.datagrid-btable').find('div.datagrid-cell').css('text-align','left');}"
		            url="/ext/table/list"
		            toolbar="#toolbar" pagination="true"
		            rownumbers="true" fitColumns="true" singleSelect="true" style="display:none;">
		        <thead>
		            <tr >
		            	<%/* 
		            	<th align="center" field="cid" width="50" sortable="true">编号</th>
		            	*/ %>
		            	<th align="center" field="confType" width="50" sortable="true" hidden="true">配置类型</th>
		            	
						<th align="center" field="name" width="50" sortable="true">名称</th>
						<th align="center" field="refTableName" width="50" sortable="true">关联表名</th>
						<th align="center" field="refDataName" width="50" sortable="true">关联类型编码</th>
						<th align="center" field="refDataTypeName" width="50" sortable="true"  hidden="true">关联类型</th>
						<th align="center" field="extType" width="50" sortable="true"  hidden="true">扩展类型:</th>
						
		                <%/* */ %>
		                <th align="center" field="extTable" width="50" sortable="true"  hidden="true">对应的扩展表名</th>
		                <th align="center" field="status" width="50" sortable="true" formatter="renderStatus"  hidden="true">状态</th>
		                <th align="center" field="create_disp" width="100" sortable="true"  hidden="true">Create user</th>
		                <th align="center" field="createDate" width="120" sortable="true"  hidden="true">Create date</th>
		                <th align="center" field="modify_disp" width="100" sortable="true"  hidden="true">Modify user</th>
		                <th align="center" field="modifyDate" width="120" sortable="true"  hidden="true">Modify date</th>
		                <th align="center" field="remark" width="150"  hidden="true">备注</th>
		               
		            </tr>
		        </thead>
		    </table>
		 </div>
	</div>
	
	<div id="dlgSqlInfo" class="easyui-dialog" style="width:300px;height:400px;padding:5px 10px;display:none;"
            closed="true" >
            <div>Sql info：</div>
            <div id='sqlInfo'></div>
    </div>
    
    <div id="dlg" class="easyui-dialog" style="width:400px;height:500px;padding:5px 10px;display:none;"
            closed="true" buttons="#dlg-buttons">
        <fieldset>
			<legend>
				扩展属性配置表，使用说明，扩展的数据基于id进行关联，出于性能考虑不支持与非id进行关联
			</legend>   
	        <form id="fm" method="post" novalidate>
	        	<input type="hidden" name="cid">
				<div class="fitem">
	                <label>名称:</label>
	                <input name="name" class="easyui-validatebox" required="true">
	            </div>	
	            <div class="fitem">
	                <label>关联表名:</label>
	                <input name="refTableName" class="easyui-validatebox" required="true">
	            </div>
	            <div class="fitem">
	                <label>关联类型编码:</label>
	                <input name="refDataName" class="easyui-validatebox" >
	            </div>		
	            <div class="fitem">
	                <label>关联类型:</label>
	                <input class="easyui-combobox" name="refDataType" id="table_refDataType"  
	                	data-options="valueField:'itemCode',textField:'itemName',panelHeight:'auto'" />
	            </div>	
				<div class="fitem">
	                <label>配置类型:</label>
	                <input name="confType" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>扩展类型:</label>
	                <input name="extType" class="easyui-validatebox" required="true">
	            </div>	
				<div class="fitem">
	                <label>对应的扩展表名:</label>
	                <input name="extTable" class="easyui-validatebox">
	            </div>	
				
	            <%/* */ %>
	            <div class="fitem">
	                <label>状态:</label>
	                <input class="easyui-combobox" name="status" id="table_status" required="true" data-options="panelHeight:'auto',
									valueField: 'id',
									textField: 'name',
									//multiple:true, //多选增加此项
									data: dict_tf_status" />
	            </div>
	            <div class="fitem">
	                <label>备注:</label>
	                <textarea name="remark" id="table_remark" style="height:60px;width:200px"></textarea>
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
			if(parent){
				parent.iframeCenterSearch({tableId:rowData.cid});
			}
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
            $('#dlg').dialog('open').dialog('setTitle','New Table');
            $('#fm').form('clear');
            $('#table_status').combobox('select', '1');
            var refDataName=$('#query_refDataName').val();
            //$("#table_remark").val('test');
            if(refDataName){
	            var url='/basedata/lookupItem/lookupItemList?lookupCode='+refDataName;
	            $('#table_refDataType').combobox({
	            	url:url, 
	            	valueField:'itemCode', 
	            	textField:'itemName',
	            	//hasDownArrow:false,
	            	editable:true
	            	});
        	}
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
            	var refDataName=$('#query_refDataName').val();
            	if(refDataName){
    	            var url='/basedata/lookupItem/lookupItemList?lookupCode='+refDataName;
    	            $('#table_refDataType').combobox({
    	            	url:url, 
    	            	valueField:'itemCode', 
    	            	textField:'itemName',
    	            	//hasDownArrow:false,
    	            	editable:true
    	            	});
            	}
            	var entityName='table';
            	var obj = getRowData(entityName, row);
                $('#dlg').dialog('open').dialog('setTitle','Edit Table');
                $('#fm').form('clear');
                $('#fm').form('load',obj);
            }
           
        }
        function showSql(){
        	  var row = $('#dg').datagrid('getSelected');
              if (row){
            	  var entityName='table';
               	 var obj = getRowData(entityName, row);
            	  var url='/ext/tableDefine/showSql?refTableName='+obj.refTableName+'&refDataName='+obj.refDataName+'&refDataType='+obj.refDataType;
            	  $.get(url,{id:row.cid},function(result){
                      if (result.success){
                    	  var strs='';
                    	  var datas=result.dataList;
                    	  if(datas && datas.length){
	                    	  for(var i=0; i<datas.length; i++){
	                    		  strs+=datas[i]+';<br/><br/>';
	                    	  }
                    	  }
                    	  else{
                    		  strs=datas;
                    	  }
                    	  $('#sqlInfo').html(strs);
                      } else {
                          $.messager.show({    // show error message
                              title: 'Error',
                              msg: result.msgBody
                          });
                      }
                  },'json');
             	 $('#dlgSqlInfo').dialog('open').dialog('setTitle','Show sql info');
              }
        }
 		function getObjectKeySorted(obj){
        	var arr=[];
        	for (item in obj){
      			arr.push(item);
      		}
            arr.sort(function(a, b){return (a + '').localeCompare(b + '')});
        	return arr;
        }
        function getTableHead(sortKeys){
        	var str='<tr>'
        	for (item in sortKeys){
      			str+='<th width="100">'+sortKeys[item]+'</th>';
      		}
        	str+='</tr>';
        	return str;
        }
        function getTableRow(data, sortKeys){
        	if(!sortKeys){
        		sortKeys=getObjectKeySorted(data);
        	}
          var row='<tr>';
  		  for(item in sortKeys){
  			  row+='<td>'+data[sortKeys[item]]+'</td>';
  		  }
  		  row+='</tr>\n';
  		  return row;
        }
        function showData(){
      	  var row = $('#dg').datagrid('getSelected');
            if (row){
          	  var entityName='table';
             	 var obj = getRowData(entityName, row);
          	  var url='/ext/dataColumn/dataList?refTableName='+obj.refTableName+'&refDataName='+obj.refDataName+'&refDataType='+obj.refDataType;
          	  $.get(url,{id:row.cid},function(result){
                    if (result.success){
                  	  var strs='<table border="1" cellspacing="0" cellpadding="0">\n';
                  	  var datas=result.dataList;
                  	  if(datas && datas.length){
                  		  var data=datas[0];
                  		  var sortKeys=getObjectKeySorted(data);
                  		  //console.log('----sortKeys='+sortKeys);
                  		  strs+=getTableHead(sortKeys);
                    	  for(var i=0; i<datas.length; i++){
                    		  data=datas[i];
                    		  strs+=getTableRow(data, sortKeys);
                    	  }
                  	  }
                  	  else{
                  		  if(datas){
                  			strs+=getTableHead(sortKeys);
                  			strs+=getTableRow(datas, sortKeys);
                  		  }
                  		  else{
                  		  	strs=datas;
                  		  }
                  	  }
                  	  strs+='</table>';
                  	  $('#sqlInfo').html(strs);
                    } else {
                    	 $('#sqlInfo').html('');
                        $.messager.show({    // show error message
                            title: 'Error',
                            msg: result.msgBody
                        });
                    }
                },'json');
           	 $('#dlgSqlInfo').dialog('open').dialog('setTitle','Show sql info');
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
                url: '/ext/table/save',
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
                        $.post('/ext/table/delete',{id:row.cid},function(result){
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
        
        $(function(){
        	if(parent)
        		parent.iframeCenterSearch({tableId:0});//刷新重置关联查询id
        });
    </script>
    <%@ include file="/system/_foot.jsp"%>
</body>
</html>