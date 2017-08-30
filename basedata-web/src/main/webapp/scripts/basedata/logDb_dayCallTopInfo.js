Array.prototype.contains = function (obj) {  
    var i = this.length;  
    while (i--) {  
        if (this[i] === obj) {  
            return true;  
        }  
    }  
    return false;  
} 

function getUuidMaxDepth(jsonParam){
	load();
	var url='/basedata/logdb/staticUuidMaxDepth';
	//var obj={id:1, code:'test'};
	$.ajax({			
		url: url,
		type : "get",
		cache:false,
		async:false,
		dataType : "json",
		data:jsonParam,
		error : function() {
			alert("Error loading "+url);
		},
		success : function(ret) {
			//eval("var ret="+text);
			//console.log(ret);
			if(ret.flag!=0){
				alert(ret.msgCode+':'+ret.msgBody);
				return;
			}
			var dataList=ret.dataList;
			addInfoUUID(dataList);
			disLoad();
		}
	});	
}

function getIntfCodeMaxRunTime(jsonParam){
	load();
	var url='/basedata/logdb/staticIntfCodeMaxRunTime';
	//var obj={id:1, code:'test'};
	$.ajax({			
		url: url,
		type : "get",
		cache:false,
		async:false,
		dataType : "json",
		data:jsonParam,
		error : function() {
			alert("Error loading "+url);
		},
		success : function(ret) {
			//eval("var ret="+text);
			//console.log(ret);
			if(ret.flag!=0){
				alert(ret.msgCode+':'+ret.msgBody);
				return;
			}
			var dataList=ret.dataList;
			
			addInfoRunTime(dataList);
			disLoad();
		}
	});	
	
}
//弹出加载层
 function load() {  
     $("<div class=\"datagrid-mask\"></div>").css({ display: "block", width: "100%", height: $(window).height() }).appendTo("body");  
     $("<div class=\"datagrid-mask-msg\"></div>").html("正在加载，请稍候。。。").appendTo("body").css({ display: "block", left: ($(document.body).outerWidth(true) - 190) / 2, top: ($(window).height() - 45) / 2 });  
 }
 
 //取消加载层  
 function disLoad() {  
     $(".datagrid-mask").remove();  
     $(".datagrid-mask-msg").remove();  
 }
 


function doSearch(){
	var jsonParam = $('#search-form').serializeJson();
	getUuidMaxDepth(jsonParam);
	getIntfCodeMaxRunTime(jsonParam);
}

function nvl(v1, v2){
	if(v1){
		return v1;
	}
	return v2;
}
function addInfoUUID(dataList){
	var data;
	var str='<p class="lead">Top max depth by uuid</p>';
	str+='<table class="table table-bordered" style="table-layout:fixed"><tr><th width="200px">UUID</th><th width="200px">A intf code</th><th width="80px"><font color="red">Depth</font></th><th width="100px"><font color="red">Max runTime</font></th><th width="20%">Max date</th><th>Call info</th></tr>';
	for(var i=0; i<dataList.length; i++){
		data=dataList[i];
		str+='<tr>'
		str+='<td data-toggle="tooltip" title="'+data.uuid+'">'+data.uuid+'</td><td data-toggle="tooltip" title="'+data.oneIntfCode+'">'+data.oneIntfCode+'</td><td>'+data.count+'</td><td>'+data.runTime+'</td><td>'+Common.DateTimeFormatter(data.maxDate)+'</td><td data-toggle="tooltip" title="'+data.allIntfCode+'">'+data.allIntfCode+'</td>';
		str+='</tr>'
	}
	str+='</table>';
	$("#containerReport").html(str);
}

function addInfoRunTime(dataList){
	var data;
	var str='<p class="lead">Top max avg runTime</p>';
	str+='<table class="table table-bordered" style="table-layout:fixed"><tr><th width="15%">Intf code</th><th width="10%">Title</th><th width="10%">Author</th><th width="15%">Calls</th><th width="15%">Descs</th><th width="80px">Count</th><th width="100px"><font color="red">Avg runTime</font></th><th width="100px">Max runTime</th><th>Max date</th></tr>';
	for(var i=0; i<dataList.length; i++){
		data=dataList[i];
		str+='<tr>'
		str+='<td data-toggle="tooltip" title="'+data.intfCode+'">'+data.intfCode+'</td><td data-toggle="tooltip" title="'+data.title+'">'+nvl(data.title,'')+'</td><td>'+nvl(data.author,'')+'</td><td data-toggle="tooltip" title="'+data.calls+'">'+nvl(data.calls,'')+'</td><td>'+nvl(data.descs,'')+'</td><td>'+data.count+'</td><td>'+data.avgTime+'</td><td>'+data.maxRunTime+'</td><td>'+Common.DateTimeFormatter(data.maxDate)+'</td>';
		str+='</tr>'
	}
	str+='</table>';
	//console.log('runTime--'+str);
	
	var html=$("#containerReport").html();
	html+='<br/><br/>'
	$("#containerReport").html(html+str);
	$("table").colResizable();
}

$(function () {
	var recentlyDate=new Date(new Date().getTime()-7*24*3600*1000);//7天内
	$("#query_startDate").val(Common.DateFormatter(recentlyDate));
	
	
	//日期时间选择器
	$("#query_startDate").datetimepicker({
		format: "yyyy-mm-dd",
		autoclose: true,
		minView: "month",
		maxView: "decade",
		endDate:new Date(),
		//todayBtn: true,
		pickerPosition: "bottom-left"
	}).on("click",function(ev){
		//$("#startTime").datetimepicker("setEndDate", 1470393604852);
	});
	$("#query_endDate").datetimepicker({
		format: "yyyy-mm-dd",
		autoclose: true,
		minView: "month",
		maxView: "decade",
		endDate:new Date(),
		todayBtn: true,
		pickerPosition: "bottom-left"
	}).on('changeDate',function(ev){
		//console.log(ev.currentTarget.value)
		$("#query_startDate").datetimepicker("setEndDate", ev.currentTarget.value);
	});

	 $('#search-btn').click(function(){
 		console.log('----search-btn-----')
    	doSearch();
	});
$('#reset-btn').click(function(){
		$('#search-form')[0].reset();
	});
	
	doSearch();
	
});