function operWidthData(oper){
			var jsonParam={cid:selectNode.id};
			var entityName='sysconfig';
			$.ajax({			
				url: '/basedata/sysconfigUser/view',
				type : "get",
				cache:false,
				async:false,
				dataType : "json",
				data:jsonParam,
				error : function() {
					alert("Error loading "+url);
				},
				success : function(ret) {
					//console.log(ret);
					if(ret.flag!=0){
						alert(ret.msgCode+':'+ret.msgBody);
						return;
					}
					var dataList=ret.dataList;
					var data=dataList[0];
					
	                if(oper=='edit'){
	                	var obj = getRowData(entityName, data);
		                $('#fm').form('load',obj);
		                $('#v_code').html(obj.code);
		                $('#v_name').html(obj.name);
	                	$('#dlg').dialog('open').dialog('setTitle','Edit Sysconfig');
	                	
	                }
	                else if(oper=='add'){
	                	var obj = getRowData(entityName, data);
	                	obj.name='';
	                	obj.code='';
	                	obj.pid=obj.cid;
	                	obj.cid='';
		                $('#fm').form('load',obj);
	                	$('#dlg').dialog('open').dialog('setTitle','Add Sysconfig');
	                }
				}
			});
		}

function onSave(){
	
	var jsonParam = $('#fm').serializeJson();
	$('#sysconfig_sysconfigId').val(jsonParam.cid);
    $('#fm').form('submit',{
        url: '/basedata/sysconfigUser/save',
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
            	jsonParam.cid=result.dataList[0];
            	//console.log(result);
            	//console.log(jsonParam);
            	//console.log(jsonParam.cid);
            	updateNode(selectNode, jsonParam);//更新节点
                $('#dlg').dialog('close');        // close the dialog
            }
        }
    });
}

function onDestroy(){
    if (selectNode){
        $.messager.confirm('Confirm','Are you sure you want to destroy this data?',function(r){
            if (r){
                $.post('/basedata/sysconfigUser/deleteByUser',{sysconfigId:selectNode.id},function(result){
                    if (result.success){
                    	$('#menuTree').tree('remove', selectNode.target);
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