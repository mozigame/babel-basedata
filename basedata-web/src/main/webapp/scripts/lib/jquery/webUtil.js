 // WebUtil 常用工具类
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
	
	// 与easyui相关的工具类
	WebUtil.easyui = {};
	WebUtil.easyui.formatter = {
		// 毫秒数转日期字符串yyyy-MM-dd HH:mm:ss
		millisecond : function(value,row,index){
			if (value) {
				var date = new Date(value);
				var year = date.getFullYear();
				var month = date.getMonth() < 9 ? '0' + parseInt(date.getMonth() + 1) : parseInt(date.getMonth() + 1);
				var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
				var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
				var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
				var seconds = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
				var dateTimeStr = year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
				return dateTimeStr;
			}
		},
		// 毫秒数转日期yyyy-MM-dd
		millisecondToDate : function(value,row,index){
			if (value) {
				var date = new Date(value);
				var year = date.getFullYear();
				var month = date.getMonth() < 9 ? '0' + parseInt(date.getMonth() + 1) : parseInt(date.getMonth() + 1);
				var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
				var dateStr = year + "-" + month + "-" + day;
				return dateStr;
			}
		}		
	
	};
	