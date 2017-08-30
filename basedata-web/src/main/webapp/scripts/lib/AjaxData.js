
function AjaxData(){
	this.userName='';
	this.userNo='';
	this.userId=0;
	
	this.paras='';//编码，解码使用
	this.action='';//权限检查使用
	this.permit=false;//权限检查使用
	
	this.success=false;
	this.value='';
	this.dataList=[];
}

/**
 * jquery调用:
  	var ajaxData = new AjaxData(false);
 	var userInfo=ajaxData.loadUserInfo();
  	alert(userInfo.userId+'---'+userInfo.userName+'---'+userInfo.userNo);
 
 * ext调用
  	var ajaxData = new AjaxData(false);
  	ajaxData.loadUserInfo();
 * 可以延时后调用
 	timeoutId = setTimeout((function() {
				return function() {
					alert(ajaxData.userId+'---'+ajaxData.userName+'---'+ajaxData.userNo);
				};
			})(this), 1000);
 * @param {} action
 */
AjaxData.prototype.loadUserInfo=function(){
	var _op_this=this;
	var url = "../loginmgmt/findCurrentUserInfo.action";
	
	$.ajax({			
		url: url,
		type : "get",
		cache:false,
		async:false,
		dataType : "text",
		error : function() {
			alert("Error loading loginmgmt/findCurrentUserInfo");
		},
		success : function(text) {
			eval("var resp="+text);
			_op_this.userId=resp.userId;
			_op_this.userName=resp.userName;	
			_op_this.userNo=resp.userNo;

		}
	});	
	
	return _op_this;
}

/**
 * 参数编码
 * @param {} paras
 */
AjaxData.prototype.paraEncode=function(paras){
	var _op_this=this;
	var paraCode=null;
	var url = "../loginmgmt/paraEncode.action?paras="+paras;
	
	$.ajax({			
		url: url,
		type : "get",
		cache:false,
		async:false,
		dataType : "text",
		error : function() {
			alert("Error loading loginmgmt/paraEncode");
		},
		success : function(text) {
			eval("var resp="+text);
			_op_this.paras=resp.paraCode;

		}
	});	
	
	return _op_this.paras;
}

/**
 * 参数解码
 * @param {} paras
 */
AjaxData.prototype.paraDecode=function(paras){
	var _op_this=this;
	var url = "../loginmgmt/paraDecode.action?paras="+paras;
	
	$.ajax({			
		url: url,
		type : "get",
		cache:false,
		async:false,
		dataType : "text",
		error : function() {
			alert("Error loading loginmgmt/paraDecode.action");
		},
		success : function(text) {
			eval("var resp="+text);
			_op_this.paras=resp.paraValue;

		}
	});	
	
	return _op_this.paras;
}

/**
 * userEncodeStr
 * @param {} paras
 * @return {}
 */
AjaxData.prototype.getUserEncodeStr=function(paras){
	var _op_this=this;
	var url = "../loginmgmt/userEncodeStr.action?paras="+paras;
	
	$.ajax({			
		url: url,
		type : "get",
		cache:false,
		async:false,
		dataType : "text",
		error : function() {
			alert("Error loading loginmgmt/userEncodeStr.action");
		},
		success : function(text) {
			eval("var resp="+text);
			_op_this.paras=resp.paraValue;
		}
	});	
	
	return _op_this.paras;
}

/**
 * 检查操作是否有权限
 * @param {} action(userId, action, permit)
 */
AjaxData.prototype.isPermit=function(action){
	var _op_this=this;
	var url = "../loginmgmt/checkPermit.action?action="+action;
	
	$.ajax({			
		url: url,
		type : "get",
		cache:false,
		async:false,
		dataType : "text",
		error : function() {
			alert("Error loading loginmgmt/checkPermit.action");
		},
		success : function(text) {
			eval("var resp="+text);
			_op_this.userId=resp.userId;
			_op_this.action=resp.action;
			_op_this.permit=resp.permit;
		}
	});	
	
	return _op_this.permit;
}


/**
 * jquery调用:
 	var ajaxData = new AjaxData(false);
 	alert(ajaxData.getUserParameterValue(parameterCode));
  
 * ext调用
 	var ajaxData = new AjaxData();
 	ajaxData.getUserParameterValue(parameterCode);
 * 可以延时后调用
 	alert(ajaxData.value);
 * 
 * @param {} action
 */
AjaxData.prototype.getSysconfigValue=function(sysconfigCode){
	var _op_this=this;
	var url = "/basedata/findSysconfigValue.action?sysconfigCode="+sysconfigCode;
	//alert(this.ajaxExtjs);
	
	$.ajax({			
		url: url,
		type : "get",
		cache:false,
		async:false,
		dataType : "text",
		error : function() {
			alert("Error loading basedata/findSysconfigValue.action");
		},
		success : function(text) {
			eval("var resp="+text);
			_op_this.value=resp.sysconfigValue;			
		}
	});	
	
	return _op_this.value;
}

/**
 * jquery调用:
 	var ajaxData = new AjaxData(false);
 	alert(ajaxData.getUserParameterValue(parameterCode));
  
 * ext调用
 	var ajaxData = new AjaxData();
 	ajaxData.getUserParameterValue(parameterCode);
 * 可以延时后调用
 	alert(ajaxData.value);
 * 
 * @param {} action
 */
AjaxData.prototype.getLookupItemByCode=function(code){
	var _op_this=this;
	var url = "/basedata/lookupItem/findByLookupCode?lookupCode="+code;
	
	$.ajax({			
		url: url,
		type : "get",
		cache:false,
		async:false,
		dataType : "text",
		error : function() {
			alert("Error loading "+url);
		},
		success : function(text) {
			eval("var resp="+text);
			_op_this.dataList=resp.dataList;	
		}
	});	
	
	return _op_this.value;
}

/**
 * jquery调用:
 	var ajaxData = new AjaxData(false);
 	alert(ajaxData.getUserParameterValue(parameterCode));
  
 * ext调用
 	var ajaxData = new AjaxData();
 	ajaxData.getUserParameterValue(parameterCode);
 * 可以延时后调用
 	alert(ajaxData.value);
 * 
 * @param {} action
 */
AjaxData.prototype.getUserParameterValue=function(parameterCode){
	var _op_this=this;
	var url = "../basedata/findUserParameterByCode.action?parameterCode="+parameterCode;
	
	$.ajax({			
		url: url,
		type : "get",
		cache:false,
		async:false,
		dataType : "text",
		error : function() {
			alert("Error loading basedata/findUserParameterByCode.action");
		},
		success : function(text) {
			eval("var resp="+text);
			_op_this.userId=resp.userId;
			_op_this.value=resp.value;			
		}
	});	
	
	return _op_this.value;
}


/**
 * 用户自定义参数保存
 * jquery调用:
 	var ajaxData = new AjaxData(false);
 	ajaxData.saveUserParameterValue(parameterCode, parameterValue);
 	
 * ext调用
 	var ajaxData = new AjaxData();
 	ajaxData.saveUserParameterValue(parameterCode, parameterValue);
 * @param {} paras
 */
AjaxData.prototype.saveUserParameterValue=function(parameterCode, parameterValue){
	var _op_this=this;	
	var url = "../basedata/saveUserParameterByCode.action?parameterCode="+parameterCode
		+"&parameterValue="+parameterValue;
		
	$.ajax({			
		url: url,
		type : "post",
		cache:false,
		async:false,
		dataType : "text",
		error : function() {
			alert("Error save basedata/saveUserParameterByCode.action");
		},
		success : function(text) {
			eval("var resp="+text);
			_op_this.success=resp.success;
			if(resp.success==true){
				alert('保存成功');
			}
			else{
				alert(resp.status);
			}
		}
	});	
	
	return _op_this.success;
}

/**
 * 用户自定义参数保存
 * jquery调用:
 	var ajaxData = new AjaxData(false);
 	ajaxData.saveInput(moduleId, propName, input);
 	
 * ext调用
 	var ajaxData = new AjaxData();
 	ajaxData.saveInput(moduleId, propName, input);
 * @param {} paras
 */
AjaxData.prototype.saveRecentlyInput=function(moduleId, propName, input){
	var _op_this=this;	
	var url = "../basedata/saveInput.action?module_id="+moduleId
		+"&prop_name="+propName+"&input="+input;
		
	$.ajax({			
		url: url,
		type : "post",
		cache:false,
		async:true,
		dataType : "text",
		error : function() {
			alert("Error save basedata/saveInput.action");
		},
		success : function(text) {
			eval("var resp="+text);
			_op_this.success=resp.success;
			if(resp.success==true){
				alert('保存成功');
			}
			else{
				alert(resp.status);
			}
		}
	});	
	
	return _op_this.success;
}


/**
 * 清除功能缓存
 * @param {} mmKey
 */
AjaxData.prototype.clearModuleCache=function(mmKey){
	var _op_this=this;
	var url = "../loginmgmt/clearModuleCache.action?mmKey="+mmKey;
	
	$.ajax({			
		url: url,
		type : "get",
		cache:false,
		async:false,
		dataType : "text",
		error : function() {
			alert("Error loading loginmgmt/clearModuleCache.action");
		},
		success : function(text) {
			eval("var resp="+text);
			_op_this.success=resp.success;
			if(_op_this.success){
				//alert('成功');
				alert(resp.paras);
			}

		}
	});	
	
	return _op_this.success;
}



function getCookie(name) {
	var strcookie = document.cookie;
	var arrcookie = strcookie.split("; ");
	for (var i = 0; i < arrcookie.length; i++) {
		var arr = arrcookie[i].split("=");
		if (arr[0] == name)
			return arr[1];
	}
	return "";
}