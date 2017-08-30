var lang=getCookie('locale');

var LANG_CN="zh_CN";
var LANG_EN="en_US";

var lang='cn';
/**
 * easyui支持
 */
if(typeof jQuery != 'undefined'){
(function(){
if (!window.WebUtil)
			WebUtil = {};
	})();
	WebUtil.form = {
		serializeJson : function(frm){
			var json = {};
	        frm = frm || $('body');
	        if (!frm) {
	            return json;
	        }
	        var inputs = frm.find('input[type!=button][type!=reset][type!=submit][type!=image],select,textarea');
	        if (!inputs) {
	        	return json;
	        }
	        for (var index = 0; index < inputs.length; index++) {
	        	var input = $(inputs[index]);
	        	var name = input.attr('name');
	        	var value = input.val();
	        	if (name != null && $.trim(name) != '' && value != null && $.trim(value) != '') {
	        		json[name] = value;
	        	}
	        }
	        return json;
		}
	};
	(function($){
		$.fn.serializeJson = function(){
			return WebUtil.form.serializeJson($(this));
		};
	}(jQuery));
}

function formatDateTime(value){
	if(value){
		try{
			value=value.replace('T', ' ');
		}catch(e){
			console.log(e);
		}
	}
	return value;
}

function clone(b) {
	if ("object" != typeof b || null == b)
		return b;
	var d = {}, a;
	for (a in b)
		d[a] = clone(b[a]);
	return d
}

function getDictClone(b) {
	for (var d = [], a, c = 0; c < b.length; c++)
		a = b[c], d.push(clone(a));
	return d
}

function getDictData(b, d) {
	var a = [], c;
	if(!b){
		console.log('----getDictData--dict='+b);
		return a;
	}
	if(d){
		a.push({
				id : "-1",
				name : d
			});
	}
	for (var e = 0; e < b.length; e++)
		c = b[e], a.push(c);
	return a
}

//status为all时处理，即把-1改成空
function value_all2empty(obj){
	if(obj){
		if(obj.status==-1){
			obj.status='';
		}
		if(obj.logLevel==-1){
			obj.logLevel='';
		}
	}
}

/**
 * end easyui支持
 */

/**
 * 取字典值
 * @param dictValue
 * @returns
 */
function getDictValue(dictValue){
	var index;	
	if(typeof(dictValue)=='string'){
		index = dictValue.indexOf("</");		
		if(index>=0){
			dictValue = dictValue.substring(0, index);
		}
		index = dictValue.indexOf(">");
		if(dictValue.indexOf(">")>=0){
			dictValue = dictValue.substring(index+1);
		}	
	}	
	return dictValue;
}

function getDictName(dict, id){
	var name;
	var obj=getDict(dict, id);
	if(obj){
		name=obj.name;
	}
	return name;
}

function getDict(dict, id){
	var name;
	var obj;
	for(var i=0; i<dict.length; i++){
		obj = dict[i];
		if(obj.id==id){
			return obj;
		}
	}
	return null;
}

/**
 * 字体颜色
 * @param {} value
 * @param {} color
 * @return {}
 */
function getValueColor(value, color){
	if(!color||color==''){
		color='black';
	}
	return "<font color='"+color+"'>"+value+"</font>"
	
}

/**
 * 字典转成数组
 * @param objs
 * @returns {Array} 如[['','全部'],['1','是'],['0','否']]
 */
function getDataArray(objs, type){
	var dicts=[];
	var cur=[];
	if(type=='all'){
		cur.push('');
		if(lang==LANG_EN){
			cur.push('All');
		}
		else{
			cur.push('全部');
		}
		dicts.push(cur);
	}
	var obj;
	for(var i=0; i<objs.length; i++){
		obj = objs[i];
		cur=[];
		cur.push(''+obj.id);//数字转字符，在asura2.1时必须以字符处理
		cur.push(obj.name);
		dicts.push(cur);
	}
	return dicts;
}

/**
 * 字典转成数组,id不转字符串
 * @param {} objs
 * @param {} type
 * @return {}
 */
function getDataArrayValue(objs, type){
	var dicts=[];
	var cur=[];
	if(type=='all'){
		cur.push('');
		if(lang==LANG_EN){
			cur.push('All');
		}
		else{
			cur.push('全部');
		}
		dicts.push(cur);
	}
	var obj;
	for(var i=0; i<objs.length; i++){
		obj = objs[i];
		cur=[];
		cur.push(obj.id);//数字转字符，在asura2.1时必须以字符处理
		cur.push(obj.name);
		dicts.push(cur);
	}
	return dicts;
}

/**
 * 数组转成store类型
 * @param {} dict
 * @param {} type
 * @return {}
 */
function getDataStore(dict, type){
	return new Ext.data.SimpleStore( {
				fields : ['key','value'],
				data : getDataArray(dict, type)
			});	
}

function renderDict(dict, value, noColor){
	if(!dict){
		console.log('----renderDict--dict='+dict+' value='+value);
		return value;
	}
	var obj;
	for(var temp in dict){
		obj = dict[temp];
		if(value==obj.id){
			if(noColor==true ){
				return obj.name;
			}
			else{
				return getValueColor(obj.name, obj.color);
			}
		}
	}    
	return value;
}

function formatterCombobox(dict, value){
	//console.log(value);
	var color='black';
	var data=getDict(dict, value.id);
	if(data){
		color=data.color;
	}
	return '<font color="'+color+'">'+value.name+'</font>';
}

/**
 *是否
 * @param value
 * @param noColor noColor=true时不显示颜色
 */
function renderIf(value, noColor){
	return renderDict(dict_tf_if, value, noColor);
}

/**
 * 是否删除
 * @param value
 * @param noColor noColor=true时不显示颜色
 * @returns
 */
function renderIfDel(value, noColor){ 
	return renderDict(dict_tf_if_del, value, noColor);
}

/**
 * 是否有效
 * @param value
 * @param noColor noColor=true时不显示颜色
 * @returns
 */
function renderStatus(value, noColor) {
	return renderDict(dict_tf_status, value, noColor);
}

function formatterStatus(value){
	return formatterCombobox(dict_tf_status, value);
}

/**
 * 是否有效
 * @param value
 * @param noColor noColor=true时不显示颜色
 * @returns
 */
function renderLogLevel(value, noColor){
	return renderDict(dict_tf_log_level, value, noColor);
}
function formatterLogLevel(value){
	return formatterCombobox(dict_tf_log_level, value);
}

/**
 * 排序方式
 * @param {} value
 * @param noColor noColor=true时不显示颜色
 * @return {}
 */
function renderOrderType(value, noColor){
	return renderDict(dict_order_type, value, noColor);
}

/**
 * 最近输入限制类型
 * @param {} value
 * @param noColor noColor=true时不显示颜色
 * @return {}
 */
function renderRecentlyLimit(value, noColor){
	return renderDict(dict_recently_limit, value, noColor);
}

/**
 * 最近输入清除类型
 * @param {} value
 * @param noColor noColor=true时不显示颜色
 * @return {}
 */
function renderRecentlyClearType(value, noColor){ 
	return renderDict(dict_recently_clear_type, value, noColor);
}


/**
 * 最近输入显示类型
 * @param {} value
 * @param noColor noColor=true时不显示颜色
 * @return {}
 */
function renderRecentlyShowType(value, noColor){ 
	return renderDict(dict_recently_show_type, value, noColor);
}

/**
 * 是否男女
 * @param value
 * @param noColor noColor=true时不显示颜色
 * @returns
 */
function renderSex(value, noColor){
	return renderDict(dict_tf_sex, value, noColor);
}

/**
 * 合作机构类型
 * @param value
 * @param noColor noColor=true时不显示颜色
 * @returns
 */
function renderOrganizationType(value, noColor){
	return renderDict(dict_yc_organization_type, value, noColor);
}

/**
 * 产品类型
 * @param value
 * @param noColor noColor=true时不显示颜色
 * @returns
 */
function renderProductType(value, noColor){
	return renderDict(dict_yc_product_type, value, noColor);
}

/**
 * 账户交易类型
 * @param value
 * @param noColor noColor=true时不显示颜色
 * @returns
 */
function renderTransactionType(value, noColor){
	return renderDict(dict_yc_transaction_type, value, noColor);
}

/**
 * 证件类型
 * @param value
 * @param noColor noColor=true时不显示颜色
 * @returns
 */
function renderCardType(value, noColor){
	return renderDict(dict_tf_card_type, value, noColor);
}

/**
 * 时间标签类型
 * @param value
 * @param noColor noColor=true时不显示颜色
 * @returns
 */
function renderTimeMarkerType(value, noColor){
	return renderDict(dict_yc_time_marker_type, value, noColor);
}

/**
 * logLevel
 * @param value
 * @param noColor noColor=true时不显示颜色
 * @returns
 */
function renderLogLevel(value, noColor){
	return renderDict(dict_tf_log_level, value, noColor);
}

/**
 * return type
 * @param value
 * @param noColor noColor=true时不显示颜色
 * @returns
 */
function renderRetFlag(value, noColor){
	return renderDict(dict_tf_ret_flag, value, noColor);
}

function renderSendFlag(value, noColor){
	return renderDict(dict_tf_send_flag, value, noColor);
}

function renderSendType(value, noColor){
	return renderDict(dict_tf_send_type, value, noColor);
}

function renderMsgType(value, noColor){
	return renderDict(dict_tf_msg_type, value, noColor);
}

function renderReqType(value, noColor){
	return renderDict(dict_tg_intf_req_type, value, noColor);
}

function renderWeight(value, noColor){
	return renderDict(dict_tg_weight, value, noColor);
}

function renderJobType(value, noColor){
	return renderDict(dict_lh_job_type, value, noColor);
}

function renderWhiteType(value, noColor){
	return renderDict(dict_tf_white_type, value, noColor);
}

function renderWhiteDataType(value, noColor){
	return renderDict(dict_tf_white_data_type, value, noColor);
}

function renderQrType(value, noColor){
	return renderDict(dict_tf_qr_type, value, noColor);
}

function renderAgingType(value, noColor){
	return renderDict(dict_tf_aging_type, value, noColor);
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
